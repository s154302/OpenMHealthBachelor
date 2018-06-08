package dsu.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import common.PropertiesLoad;
import common.DataTypeIntervals;

import common.User;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import java.time.LocalDateTime;

/**
 * {@inheritDoc}
 */
@Service
public class DsuServiceImpl implements DsuService {

    /**
     * {@inheritDoc}
     * Store data to DSU.
     */
    @Override
    public LocalDateTime storeData(User user, JsonNode data, String dataType, LocalDateTime latestDate) {
        HttpClient client = HttpClients.createDefault();

        // Set to 10 years before today in order to ensure it initialising earlier than any other date
        //LocalDateTime latestDate = LocalDateTime.now().minusYears(10);
        int dataCounter = 0;
        for (JsonNode dataPoint : data) {
            // Create an HttpPost to store the data
            HttpPost post = createPostRequest(user.getAccessToken(), dataPoint);

            // Execute the POST request to store data. Only increment datacounter if it was a success
            if(executeDataStorage(client, post)) {
                dataCounter++;
            }

            // Update the latest date to return to be stored in the User object
            latestDate = DataTypeIntervals.checkLatestDate(dataType, latestDate, dataPoint);

        }
        // only print data has been stored if there was any actual data storage
        if(dataCounter > 0 )
            System.out.println("Synched " + dataCounter + " of datatype " + dataType + " for " + user.getUsername());
        return latestDate;
    }

    /**
     * Executes the POST request to store data.
     */
    private boolean executeDataStorage(HttpClient client, HttpPost post) {
        boolean success = false;
        try {
            HttpResponse httpResponse = client.execute(post);
            //Checks if the creation was a success
            success = httpResponse.getStatusLine().getStatusCode() == 201;
        } catch (IOException e) {
            System.err.println("Unable to store data: " + e.getMessage());
        } finally {
            post.releaseConnection();
        }
        return success;
    }

    /**
     * Creates the POST request in order to store data in the DSU.
     */
    private HttpPost createPostRequest(String accessToken, JsonNode dataPoint) {
        HttpPost post = new HttpPost("http://" +
                PropertiesLoad.getProperty("mainurl") + ":" +
                PropertiesLoad.getProperty("resourcePort") +
                "/v1.0.M1/dataPoints");
        String body = dataPoint.toString();
        try {
            StringEntity entity = new StringEntity(body);
            post.setEntity(entity);
            post.setHeader("Accept", "application/json");
            post.setHeader("Authorization", "Bearer " + accessToken);
            post.setHeader("Content-Type", "application/json");
        } catch (UnsupportedEncodingException e) {
            System.err.println("Unable to create body to post: " + e.getMessage());
        }
        return post;
    }

    /**
     * {@inheritDoc}
     * Get data from the DSU.
     */
    @Override
    public String getData(String accessToken, String dataType) {

        String version = PropertiesLoad.getShim().getVersion(dataType);
        dataType = dataType.replace("_", "-");
        if (version != null) {
            HttpGet get = createGetRequest(accessToken, dataType, version);
            HttpClient httpClient = HttpClients.createDefault();

            ObjectMapper mapper = new ObjectMapper();

            InputStream content = getResponseContent(get, httpClient);

            try {
                JsonNode jsonNode = mapper.readValue(content, JsonNode.class);
                return jsonNode.toString();
            } catch (IOException e) {
                System.err.println("Unable to create JSON object from JsonNode: " + e.getMessage());
            }finally {
                get.releaseConnection();
            }

        }
        return "[]";
    }

    /**
     * Gets the content from the response of the GET request. This will contain the data point as a JSON.
     */
    private InputStream getResponseContent(HttpGet get, HttpClient httpClient) {
        HttpResponse response;
        InputStream content = null;
        try {
            response = httpClient.execute(get);
            HttpEntity responseEntity = response.getEntity();
            content = responseEntity.getContent();
        } catch (IOException e) {
            System.err.println("Unable to get data: " + e.getMessage());
        }
        return content;
    }

    /**
     * Creates the GET request in order to get data from the DSU
     */
    private HttpGet createGetRequest(String accessToken, String dataType, String version) {
        HttpGet get = new HttpGet("http://" +
                PropertiesLoad.getProperty("mainurl") + ":" +
                PropertiesLoad.getProperty("resourcePort") +
                "/v1.0.M1/dataPoints?schema_namespace=omh&schema_name=" + dataType + "&schema_version=" + version);
        get.setHeader("Accept", "application/json");
        get.setHeader("Authorization", "Bearer " + accessToken);
        return get;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsonReader(String accessToken) {
        String writeString = "";

        for (int i = 0; i < supportedVisualization().length; i++) {
            String tmp = getData(accessToken, supportedVisualization()[i]);
            if (!tmp.equals("[]")) {
                tmp += ",";

                writeString += tmp;
            }

        }
        writeString = trimString(writeString);

        return writeString;
    }

    private String[] supportedVisualization() {
        return new String[]{"body-weight", "heart-rate", "blood-pressure", "physical-activity", "step-count", "distance"};
    }

    private String trimString(String writeString) {
        String tmp = writeString;
        tmp = tmp.replaceAll("],\\[", ",");
        tmp = tmp.replaceFirst("],", "]");
        return tmp;

    }
}
