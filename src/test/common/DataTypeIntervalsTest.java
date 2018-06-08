package common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertTrue;

public class DataTypeIntervalsTest {

    @Test
    public void checkLatestDateNewDate() {
        LocalDateTime earlyDate = LocalDateTime.now().minusDays(10);
        LocalDateTime lateDate = LocalDateTime.now().withNano(0);
        ObjectMapper mapper;
        ObjectNode body;

        mapper = new ObjectMapper();
        body = mapper.createObjectNode();
        ObjectNode timeFrame = mapper.createObjectNode();
        ObjectNode dateTime = mapper.createObjectNode();

        body.set(
                "body", mapper.convertValue(timeFrame
                        .set("effective_time_frame", mapper.convertValue(dateTime
                                .put("date_time", lateDate.toString()), JsonNode.class)), JsonNode.class));

        assertTrue(DataTypeIntervals.checkLatestDate("body_height", earlyDate, body).equals(lateDate));
    }

    @Test
    public void checkLatestDateOldDate() {
        LocalDateTime earlyDate = LocalDateTime.now().minusDays(10).withNano(0);
        LocalDateTime lateDate = LocalDateTime.now();
        ObjectMapper mapper;
        ObjectNode body;

        mapper = new ObjectMapper();
        body = mapper.createObjectNode();
        ObjectNode timeFrame = mapper.createObjectNode();
        ObjectNode dateTime = mapper.createObjectNode();

        body.set(
                "body", mapper.convertValue(timeFrame
                        .set("effective_time_frame", mapper.convertValue(dateTime
                                .put("date_time", earlyDate.toString()), JsonNode.class)), JsonNode.class));

        assertTrue(DataTypeIntervals.checkLatestDate("body_height", lateDate, body).equals(lateDate));
    }
}