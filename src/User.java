public class User{  //The class aggregate the User class along with the results of this session: the final probability, confidence level, and textual conclusion
    static int totalUsers = 0;
    String userName;

    public User(String name){
        userName = name;
        totalUsers++;
    }
}