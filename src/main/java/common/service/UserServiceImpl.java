package common.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.PropertiesLoad;
import common.User;
import common.CurrentUsers;
import common.enums.ShimKey;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import shimmer.common.Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@inheritDoc}
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * {@inheritDoc}
     * Adds a user to the DSU
     */
    @Override
    public HttpStatus addUser(String userName, String password) {
        HttpClient client = HttpClients.createDefault();
        HttpPost post = createAddUserPost(userName, password);

        return executeAddUser(userName, password, client, post);
    }

    /**
     * Attempts to add the user to both the DSU as well as to the CurrentUsers Object within the program.
     */
    private HttpStatus executeAddUser(String userName, String password, HttpClient client, HttpPost post) {

        try {
            HttpResponse response = client.execute(post);

            if (response.getStatusLine().getStatusCode() == 201) {
                String accessToken = getAccessToken(userName, password);
                CurrentUsers.addUser(new User(userName, accessToken));
                return HttpStatus.OK;
            }

        } catch (IOException e) {
            System.err.println("Unable to add user to DSU: " + e.getMessage());
        } finally {
            post.releaseConnection();
        }
        return HttpStatus.BAD_GATEWAY;
    }

    /**
     * Creates the POST request to add a user to the DSU.
     */
    private HttpPost createAddUserPost(String userName, String password) {
        HttpPost post = new HttpPost("http://" +
                PropertiesLoad.getProperty("mainurl") + ":" +
                PropertiesLoad.getProperty("authenticationPort") + "/users");

        String body = "{\"username\": \"" + userName + "\", \"password\": \"" + password + "\"}";

        try {
            StringEntity entity = new StringEntity(body);
            post.setEntity(entity);
            post.setHeader("Accept", "application/json");
            post.setHeader("Authorization", "Bearer ");
            post.setHeader("Content-Type", "application/json");
        } catch (UnsupportedEncodingException e) {
            System.err.println("Caught exception: " + e.getMessage());
        }
        return post;
    }

    /**
     * {@inheritDoc}
     * Gets the access token from the DSU
     */
    @Override
    public String getAccessToken(String username, String password) {
        HttpClient client = HttpClients.createDefault();
        HttpPost post = createGetAccessTokenPost(username, password);
        String accessToken = null;

        try {
            accessToken = executeGetAccessToken(client, post);
        } catch (IOException e) {
            System.err.println("Couldn't get access token: " + e.getMessage());
        } finally {
            post.releaseConnection();
        }

        return accessToken;
    }

    /**
     * Executes the POST request to retrieve an access token from the DSU.
     */
    private String executeGetAccessToken(HttpClient client, HttpPost post) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        HttpResponse response = client.execute(post);
        HttpEntity responseEntity = response.getEntity();

        InputStream content = responseEntity.getContent();
        JsonNode jsonNode = mapper.readValue(content, JsonNode.class);
        return jsonNode.get("access_token").textValue();
    }

    /**
     * Creates the POST request to get an access token from the DSU.
     */
    private HttpPost createGetAccessTokenPost(String username, String password) {
        HttpPost post = new HttpPost("http://" +
                PropertiesLoad.getProperty("mainurl") + ":" +
                PropertiesLoad.getProperty("authenticationPort") + "/oauth/token");

        List<NameValuePair> body = new ArrayList<>();

        body.add(new BasicNameValuePair("grant_type", "password"));
        body.add(new BasicNameValuePair("username", username));
        body.add(new BasicNameValuePair("password", password));

        try {
            post.setHeader("Accept", "application/json");
            post.setHeader("Authorization", "Basic " + Client.getEncodedClientInfo());
            post.setEntity(new UrlEncodedFormEntity(body));
        } catch (UnsupportedEncodingException e) {
            System.err.println("Caught exception: " + e.getMessage());
        }
        return post;
    }

    /**
     * {@inheritDoc}
     * Authenticate user
     */
    @Override
    public String shimmerAuth(String username, ShimKey shimKey) {
        HttpClient client = HttpClients.createDefault();
        HttpGet get = createShimmerAuthGet(username, shimKey);
        String authorizationUrl = null;

        HttpResponse response = executeShimmerAuth(client, get);

        try {
            authorizationUrl = retrieveAuthorizationUrl(response);
        } catch (IOException e) {
            System.err.println("Unable to create JSON object for response: " + e.getMessage());
        }

        return authorizationUrl;
    }

    /**
     * Retrieves the redirect url for authorization from the response.
     */
    private String retrieveAuthorizationUrl(HttpResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        HttpEntity responseEntity = response.getEntity();

        InputStream content = responseEntity.getContent();
        JsonNode jsonNode = mapper.readValue(content, JsonNode.class);

        return jsonNode.get("authorizationUrl").textValue();
    }

    /**
     * Executes the GET request in order to begin Shimmer authorization.
     */
    private HttpResponse executeShimmerAuth(HttpClient client, HttpGet get) {
        HttpResponse response = null;
        try {
            response = client.execute(get);
        } catch (IOException e) {
            System.err.println("Unable to get redirect url: " + e.getMessage());
        } finally {
            get.releaseConnection();
        }
        return response;
    }

    /**
     * Creates a GET request to begin Shimmer authorization.
     */
    private HttpGet createShimmerAuthGet(String username, ShimKey shimKey) {
        return new HttpGet("http://" +
                    PropertiesLoad.getProperty("mainurl") + ":" +
                    PropertiesLoad.getProperty("shimmerPort") +
                    "/authorize/" + shimKey.getName() + "?username=" + username);
    }

}
