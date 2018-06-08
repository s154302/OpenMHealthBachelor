package shimmer.service;

import com.fasterxml.jackson.databind.JsonNode;
import common.enums.ShimKey;
import javafx.util.Pair;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Service class used to retrieve data from Shimmer.
 */
public interface ShimmerService {

    /**
     * Used to retrieve data from Shimmer supported database.
     * Returns a pair containing the data as well as the response, in order to be able to perform some error handling.
     * @param shimkey the shimkey, to specify where to get the data
     * @param dataType the type of data being retrieved
     * @param userName the user's username
     * @param normalize whether or not the data should be normalized
     * @param startDate the date to retrieve data from
     * @param endDate the date to retrieve data up to
     * @param filterData specifies whether the data should be filtered or not
     * @return a pair containing the response status code and the returned data
     */
    Pair<Integer,JsonNode> getShimmerData(ShimKey shimkey, String dataType, String userName, boolean normalize,
                                          LocalDateTime startDate, LocalDate endDate, boolean filterData);
}
