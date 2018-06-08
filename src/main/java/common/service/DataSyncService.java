package common.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DataSyncService {

    /**
     * Updates the database with the given data types.
     * Makes sure to sync all data types if user has never synced before.
     * @param dataTypes the type of data to be synchronised
     */
    void update(List<String> dataTypes);

}
