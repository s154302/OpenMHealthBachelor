package shimmer.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import common.DataTypeIntervals;
import common.PropertiesLoad;
import common.enums.ShimKey;
import javafx.util.Pair;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import org.springframework.stereotype.Service;

/**
 * {@inheritDoc}
 */
@Service
public class ShimmerServiceImpl implements ShimmerService {

    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<Integer,JsonNode> getShimmerData(ShimKey shimkey, String dataType, String userName,
                                                 boolean normalize, LocalDateTime startDate, LocalDate endDate, boolean filterData) {

        HttpGet get = createGetRequest(shimkey, dataType, userName, normalize, startDate.toLocalDate(), endDate);
        HttpClient httpClient = HttpClients.createDefault();

        HttpResponse response = null;
        JsonNode jsonNode = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            response = httpClient.execute(get);
            HttpEntity responseEntity = response.getEntity();

            InputStream content = responseEntity.getContent();
            jsonNode = mapper.readValue(content, JsonNode.class);
        } catch (IOException e) {
            System.err.println("Unable to retrieve data: " + e.getMessage());
        } finally {
            get.releaseConnection();
        }

        if(filterData) {
            jsonNode = filterData(jsonNode, startDate, dataType);
        }

        assert response != null;
        return new Pair<>(response.getStatusLine().getStatusCode(), jsonNode);
    }

    /**
     * Creates the GET request to retrieve data using Shimmer.
     */
    private HttpGet createGetRequest(ShimKey shimkey, String dataType, String userName,
                                     boolean normalize, LocalDate startDate, LocalDate endDate) {
        return new HttpGet("http://" +
                    PropertiesLoad.getProperty("mainurl") + ":" +
                    PropertiesLoad.getProperty("shimmerPort") +
                    "/data/" + shimkey.getName() + "/" + dataType +
                    "?username=" + userName +
                    "&normalize=" + normalize +
                    "&dateStart=" + startDate.toString() +
                    "&dateEnd=" + endDate.toString());
    }

    /**
     * Filters the data in accordance to the latest date.
     */
    private JsonNode filterData(JsonNode data, LocalDateTime filter, String dataType) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode newHeader = mapper.createObjectNode();
        newHeader.set("shim", data.get("shim"));
        newHeader.set("timeStamp", data.get("timeStamp"));

        List<JsonNode> newData = new ArrayList<>();
        if (data.get("body") != null) {
            for (JsonNode dataPoint : data.get("body")) {
                LocalDateTime latestDate = DataTypeIntervals.checkLatestDate(dataType, filter, dataPoint);
                if (latestDate.isAfter(filter)) {
                    newData.add(dataPoint);
                }
            }
        }
        newHeader.set("body", mapper.convertValue(newData, JsonNode.class));
        return newHeader;
    }

}
