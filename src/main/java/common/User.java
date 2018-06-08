package common;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * User class used to store data on each user. Use to easily access and store data for specific users in the DSU
 */
public class User {
    private String username, accessToken;
    private Map<String, LocalDateTime> latestRetrievalDates;
    private boolean isSynced;

    /**
     * @return map of the latests dates a data type has been updated
     */
    public Map<String, LocalDateTime> getLatestRetrievalDates() {
        return latestRetrievalDates;
    }

    /**
     * Creates a new user. Each user is initiated with a username and an access token.
     * Furthermore, it initiates the map containing the latest update of all data types.
     * @param username the user's username
     * @param accessToken the user's access token
     */
    public User(String username, String accessToken) {
        this.username = username;
        this.accessToken = accessToken;
        isSynced = false;
        initialiseLatestDataPoints();
    }

    /**
     * Initialises the latestRetrievalDates Map with initial date values 10 years ago.
     */
    private void initialiseLatestDataPoints() {
        latestRetrievalDates = new HashMap<>();
        for (String dataType : PropertiesLoad.getConstantUpdate()) {
            latestRetrievalDates.put(dataType, LocalDateTime.now().minusYears(10));
        }
        for (String dataType : PropertiesLoad.getDailyUpdate()) {
            latestRetrievalDates.put(dataType, LocalDateTime.now().minusYears(10));
        }
        for (String dataType : PropertiesLoad.getWeeklyUpdate()) {
            latestRetrievalDates.put(dataType, LocalDateTime.now().minusYears(10));
        }
    }

    /**
     * @param dataType the type of data being added
     * @param time the date the data was added
     */
    public void addLatestRetrievalDate(String dataType, LocalDateTime time) {
        latestRetrievalDates.put(dataType, time);
    }

    /**
     * @return the user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the user's username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the user's access token
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * @param accessToken the user's access token
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * @return whether or not the user has been synced previously
     */
    public boolean getIsSynced(){ return isSynced; }

    /**
     * @param isSynced whether or not the user has been synced previously
     */
    public void setIsSynced(boolean isSynced){
        this.isSynced=isSynced;
    }

    /**
     * @return Username: + the user's username
     */
    public String toString() {
        return "Username: " + getUsername();
    }
}
