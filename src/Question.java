//The abstract class Question
public abstract class Question{
    static int maxCoderScore = 0;
    static int maxGamerScore = 0;
    String questionType;
    String questionText;
    String userAnswer;

    //the abstract method showQuestion
    abstract void showQuestion();

    abstract int[] getScoreDelta(String userAnswer);
}