import java.io.*;
import java.util.*;

public class Project3{

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        // Stores all users
        ArrayList<User> users = new ArrayList<>();

        // Stores all session results
        ArrayList<Results> allResults = new ArrayList<>();

        int sessionNumber = 1;
        User currentUser = null;

        System.out.println("Welcome to Coder or Gamer – Advanced Edition!");
        System.out.println();

        while (true){

            System.out.println("---@ Session " + sessionNumber + " @---");
            System.out.println();

            /*
             * Prompt for username if no current user
             * Supports switching users between sessions
             */
            if (currentUser == null){
                System.out.println("Enter your name:");
                String name = sc.nextLine();
                System.out.println();

                // Check if user already exists
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).userName.equalsIgnoreCase(name)) {
                        currentUser = users.get(i);
                    }
                }

                // Create new user if not found
                if (currentUser == null){
                    currentUser = new User(name);
                    users.add(currentUser);
                }
            }

            // Load questions from file
            ArrayList<Question> list = loadQuestions("src/question.txt");

            // Convert ArrayList to array
            Question[] questions = new Question[list.size()];
            for (int i = 0; i < list.size(); i++)
                questions[i] = list.get(i);

            // Shuffle questions randomly
            shuffle(questions);

            int coderScore = 0;
            int gamerScore = 0;

            // Ask each question
            for (int i = 0; i < questions.length; i++){

                questions[i].showQuestion();
                System.out.println();

                // Handle different question types
                if (questions[i] instanceof mcQuestion){
                    mcQuestion mc = (mcQuestion) questions[i];
                    questions[i].userAnswer = readMCOption(sc, mc);
                } else if (questions[i] instanceof tfQuestion){
                    boolean b = readBoolean(sc);
                    questions[i].userAnswer = String.valueOf(b);
                } else if (questions[i] instanceof numQuestion){
                    int val = readInt(sc);
                    sc.nextLine();
                    questions[i].userAnswer = String.valueOf(val);
                }

                // Update scores
                int[] delta = questions[i].getScoreDelta(questions[i].userAnswer);
                coderScore += delta[0];
                gamerScore += delta[1];
                System.out.println();
            }

            // Ask for confidence level
            System.out.println("Confidence level (0–100):");
            int confidence = readInt(sc);
            sc.nextLine();
            System.out.println();

            /*
             * Calculate probabilities using static max scores
             * from the abstract Question class
             */
            double coderProb =
                    ((double) coderScore / Question.maxCoderScore * 100)
                            * confidence / 100;

            double gamerProb =
                    ((double) gamerScore / Question.maxGamerScore * 100)
                            * confidence / 100;

            // Display results
            System.out.println("Calculating...");
            System.out.println();
            System.out.println("Coder: " + (int) coderProb + "%");
            System.out.println("Gamer: " + (int) gamerProb + "%");
            System.out.println();

            // Store session result
            Results r = new Results();
            r.user = currentUser;
            r.coderProbability = coderProb;
            r.gamerProbability = gamerProb;
            r.confidence = confidence;

            if (coderProb > gamerProb){
                r.conclusion = "Conclusion: You are likely a CODER.";
            } else {
                r.conclusion = "Conclusion: You are likely a GAMER.";
            }

            System.out.println(r.conclusion);
            System.out.println();

            allResults.add(r);

            // Ask whether to continue
            System.out.println("Would you like another session? (Y/N)");
            if (!readYesNo(sc)) break;

            // Ask whether to switch user
            System.out.println("Would you like to switch user? (Y/N)");
            if (readYesNo(sc)){
                currentUser = null;
            }

            sessionNumber++;
        }

        // Save all results to file
        saveResults(allResults, "Results.txt");
        System.out.println("Results saved.");
    }

    /**
     * Reads a valid integer from the user
     */
    public static int readInt(Scanner sc){
        while (!sc.hasNextInt()){
            System.out.print("Invalid input. Enter a number: ");
            sc.next();
        }
        return sc.nextInt();
    }

    /**
     * Reads a valid boolean (true/false)
     */
    public static boolean readBoolean(Scanner sc){
        while (true){
            String s = sc.nextLine().trim().toLowerCase();
            if (s.equals("true") || s.equals("false"))
                return Boolean.parseBoolean(s);
            System.out.print("Please enter true or false: ");
        }
    }

    /**
     * Reads a valid Yes/No response
     */
    public static boolean readYesNo(Scanner sc){
        while (true){
            String s = sc.nextLine().trim().toUpperCase();
            if (s.equals("Y")) return true;
            if (s.equals("N")) return false;
            System.out.print("Enter Y or N: ");
        }
    }

    /**
     * Ensures the user selects a valid multiple-choice option
     */
    public static String readMCOption(Scanner sc, mcQuestion q){
        while (true){
            String input = sc.nextLine().trim();
            for (int i = 0; i < q.options.size(); i++){
                if (q.options.get(i).equalsIgnoreCase(input)){
                    return input;
                }
            }
            System.out.println("Invalid option. Please choose from the given options:");
            q.showQuestion();
            System.out.println();
        }
    }

    /**
     * Shuffles the questions array randomly
     */
    public static void shuffle(Question[] a){
        Random r = new Random();
        for (int i = 0; i < a.length; i++){
            int j = r.nextInt(a.length);
            Question t = a[i];
            a[i] = a[j];
            a[j] = t;
        }
    }

    /**
     * Loads questions from a text file
     */
    public static ArrayList<Question> loadQuestions(String file) throws Exception {
        Scanner in = new Scanner(new File(file));
        ArrayList<Question> list = new ArrayList<>();

        while (in.hasNextLine()){
            String type = in.nextLine().trim();

            if (type.equals("MC")) {
                mcQuestion q = new mcQuestion();
                q.questionType = "MC";
                q.questionText = in.nextLine();
                while (in.hasNextLine()){
                    String line = in.nextLine().trim();
                    if (line.equals("")) break;
                    String[] p = line.split(",");
                    q.addOption(p[0], Integer.parseInt(p[1]), Integer.parseInt(p[2]));
                }
                list.add(q);
            }

            if (type.equals("TF")){
                tfQuestion q = new tfQuestion();
                q.questionType = "TF";
                q.questionText = in.nextLine();
                String[] t = in.nextLine().split(",");
                String[] f = in.nextLine().split(",");
                q.setWeights(
                        Integer.parseInt(t[0]), Integer.parseInt(t[1]),
                        Integer.parseInt(f[0]), Integer.parseInt(f[1])
                );
                in.nextLine();
                list.add(q);
            }

            if (type.equals("NUM")){
                numQuestion q = new numQuestion();
                q.questionType = "NUM";
                q.questionText = in.nextLine();
                while (in.hasNextLine()){
                    String line = in.nextLine().trim();
                    if (line.equals("")) break;
                    String[] p = line.split(",");
                    q.addRange(
                            Integer.parseInt(p[0]), Integer.parseInt(p[1]),
                            Integer.parseInt(p[2]), Integer.parseInt(p[3])
                    );
                }
                list.add(q);
            }
        }
        return list;
    }

    /**
     * Saves all session results to a text file
     */
    public static void saveResults(ArrayList<Results> list, String file) throws Exception {
        PrintWriter out = new PrintWriter(new File(file));
        for (int i = 0; i < list.size(); i++){
            Results r = list.get(i);
            out.println("User: " + r.user.userName);
            out.println("Coder: " + r.coderProbability + "%");
            out.println("Gamer: " + r.gamerProbability + "%");
            out.println("Confidence: " + r.confidence);
            out.println(r.conclusion);
            out.println();
        }
        out.close();
    }
}