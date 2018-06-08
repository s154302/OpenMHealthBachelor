package common;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Static class that contains two Sets.
 * One contains which data types use an interval based time.
 * The other contains data types which don't use interval based time.
 */
public class DataTypeIntervals {
    private static final Set<String> INTERVAL_DATA_TYPES;
    private static final Set<String> NON_INTERVAL_DATA_TYPES;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd'T'HH:mm:ss[XXX][X]");

    static {
        Set<String> interval = new HashSet<>();
        interval.add("sleep_duration");
        interval.add("sleep_episode");
        interval.add("step_count");
        INTERVAL_DATA_TYPES = Collections.unmodifiableSet(interval);

        Set<String> nonInterval = new HashSet<>();
        nonInterval.add("blood_pressure");
        nonInterval.add("body_height");
        nonInterval.add("body_weight");
        nonInterval.add("body_temperature");
        nonInterval.add("heart_rate");
        NON_INTERVAL_DATA_TYPES = Collections.unmodifiableSet(nonInterval);
    }

    /**
     * Checks if the current latest date is earlier than the date the data in the given data point was stored.
     * If it is, the latest data point needs to be updated.
     * @param dataType the type of data being checked
     * @param latestDate the current latest date the data type has been stored
     * @param dataPoint the data point which date is to be compared to latest date
     * @return the latest date the data type has been stored
     */
    public static LocalDateTime checkLatestDate(String dataType, LocalDateTime latestDate, JsonNode dataPoint) {
        LocalDateTime tempDate = LocalDateTime.MIN;
        if (DataTypeIntervals.NON_INTERVAL_DATA_TYPES.contains(dataType)) {
            tempDate = LocalDateTime.parse(dataPoint
                    .get("body")
                    .get("effective_time_frame")
                    .get("date_time")
                    .asText(), formatter);
        } else if (DataTypeIntervals.INTERVAL_DATA_TYPES.contains(dataType)) {
            tempDate = LocalDateTime.parse(dataPoint
                    .get("body")
                    .get("effective_time_frame")
                    .get("time_interval")
                    .get("end_date_time")
                    .asText(), formatter);
        }

        if (tempDate.isAfter(latestDate)) latestDate = tempDate;
        return latestDate;
    }
}
