package dsu.service;


import com.fasterxml.jackson.databind.JsonNode;
import common.User;

import java.time.LocalDateTime;

/**
 * Service class used to handle storing and accessing data to and from the DSU.
 */
public interface DsuService {

    /**
     *
     * @param user the user to store data for
     * @param data the data point to be stored
     * @param dataType the type of data being stored
     * @param latestDate the latest date data has been stored for this data type
     * @return the date the latest data point stored was measured
     */
    LocalDateTime storeData(User user, JsonNode data, String dataType, LocalDateTime latestDate);

    /**
     *
     * @param accessToken the user's access token
     * @param dataType the type of data to be retrieved
     * @return the data point formatted as a JSON
     */
    String getData(String accessToken, String dataType);

    /**
     * Builds a string containing a user's data which can then be used for visualisation.
     * @param accessToken the user's access token
     * @return all data for a single user in a format so it can be read for visualisation
     */
    String jsonReader(String accessToken);
}
