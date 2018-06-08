package common.service;

import com.fasterxml.jackson.databind.JsonNode;
import common.CurrentUsers;
import common.PropertiesLoad;
import common.User;
import dsu.service.DsuService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shimmer.service.ShimmerService;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataSyncServiceImpl implements DataSyncService {

    private final
    ShimmerService SHIMMER_SERVICE;

    private final
    DsuService DSU_SERVICE;

    @Autowired
    public DataSyncServiceImpl(ShimmerService shimmerService, DsuService dsuService) {
        this.SHIMMER_SERVICE = shimmerService;
        this.DSU_SERVICE = dsuService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(List<String> dataTypes) {
        try {
            for (User user : CurrentUsers.getUsers().values()) {
                if (user.getIsSynced()) {
                    standardUserSynchronisation(dataTypes, user);
                } else {
                    initialUserSynchronisation(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * If user has been sync'ed before, this method will only attempt to retrieve data
     * that hasn't been retrieved before.
     */
    private void standardUserSynchronisation(List<String> dataTypes, User user) {
        long time1 = System.currentTimeMillis();
        for (String dataType : dataTypes) {
            Pair<Integer, JsonNode> response = SHIMMER_SERVICE.getShimmerData(
                    PropertiesLoad.getShim(),
                    dataType, user.getUsername(),
                    true,
                    (user.getLatestRetrievalDates().get(dataType)),
                    LocalDate.now(), true);
            // if there is data, try to store and log it
            JsonNode shimmerData = response.getValue().get("body");
         //   user.addLatestRetrievalDate(dataType, (DSU_SERVICE.storeData(user, shimmerData, dataType, user.getLatestRetrievalDates().get(dataType))));

        }
        long time2 = System.currentTimeMillis();
        System.out.println(time2 - time1);
    }

    /**
     * If user has never been sync'ed before, this method will get all data from Shimmer and store it in the DSU.
     */
    private void initialUserSynchronisation(User user) {
        List<String> dataTypesToSync = PropertiesLoad.getConstantUpdate();
        dataTypesToSync.addAll(PropertiesLoad.getDailyUpdate());
        dataTypesToSync.addAll(PropertiesLoad.getWeeklyUpdate());

        for(String dataType : dataTypesToSync){
            Pair<Integer,JsonNode> response = SHIMMER_SERVICE.getShimmerData(
                    PropertiesLoad.getShim(),
                    dataType, user.getUsername(),
                    true,
                    user.getLatestRetrievalDates().get(dataType),
                    LocalDate.now(), false);
            if(response.getKey() == 500){
                break;
            }
            else{
                JsonNode shimmerData = response.getValue().get("body");
                /* user.addLatestRetrievalDate(dataType,
                        (DSU_SERVICE.storeData(user, shimmerData, dataType, user.getLatestRetrievalDates().get(dataType)))); */
                user.setIsSynced(true);
            }
        }
    }
}
