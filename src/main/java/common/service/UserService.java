package common.service;

import common.enums.ShimKey;
import org.springframework.http.HttpStatus;

import java.io.IOException;

/**
 * Handles any functionality concerning users. This includes adding users to the DSU, getting a user's access token,
 * and authenticating a user to Shimmer using a shim key.
 */
public interface UserService {

    /**
     * Adds a user to the DSU as well as to the CurrentUsers object.
     * @param userName the user's username
     * @param password the user's password
     * @return the status of addUser
     */
    HttpStatus addUser(String userName, String password);

    /**
     * Gets a specific user's access token from the DSU, using username and password
     * @param username the user's username
     * @param password the user's password
     * @return the access token as a string
     */
    String getAccessToken(String username, String password);

    /**
     * Returns the url to go to in order to authorise a user.
     * To complete the authorisation redirect the user to the site this method returns.
     * @param username the user's username
     * @param shimKey the shimkey, used to specify which service to authorise to
     * @return the url to go to to to authorise a user to a specific shimkey
     */
    String shimmerAuth(String username, ShimKey shimKey);

}
