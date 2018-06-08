package common;

import java.util.HashMap;
import java.util.Map;

/**
 * Static class used to store user objects so they can easily be used.
 */
public class CurrentUsers {
    private static CurrentUsers ourInstance = null;
    private static Map<String, User> users;

    /**
     * @return the user base for the program
     */
    public static CurrentUsers getInstance() {
        if(ourInstance == null) {
            ourInstance = new CurrentUsers();
        }
        return ourInstance;
    }

    private CurrentUsers(){
        users = new HashMap<>();
    }

    /**
     * @return a map of all the users
     */
    public static Map<String, User> getUsers(){
        return getInstance().users;
    }

    /**
     * @param user the user to be added to the user base
     */
    public static void addUser(User user){
        getInstance().users.put(user.getUsername(),user);
    }
}
