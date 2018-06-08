package common;

import common.service.DataSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Scheduler which handles synchronisation between the DSU and Shimmer.
 */
@Component
public class Scheduler {

    private final
    DataSyncService DATA_SYNC_SERVICE;

    /**
     * Constructor for the scheduler. Is created by Spring. Contains injection of the Shimmer- and DSU service
     * @param dataSyncService service which synchronises data
     */
    @Autowired
    public Scheduler(DataSyncService dataSyncService) {
        this.DATA_SYNC_SERVICE = dataSyncService;
    }

    /**
     * Updates every minute.
     * @see DataSyncService#update(List)
     */
    @Scheduled(cron = "0 */1 * * * *")
    public void updateConstant() {
        DATA_SYNC_SERVICE.update(PropertiesLoad.getConstantUpdate());
    }

    /**
     * Updates every day.
     * @see DataSyncService#update(List)
     */
    @Scheduled(cron = "0 0 0 */1 * *")
    public void updateDaily() {
        DATA_SYNC_SERVICE.update(PropertiesLoad.getDailyUpdate());
    }

    /**
     * Updates every week.
     * @see DataSyncService#update(List)
     */
    @Scheduled(cron = "0 0 0 * * MON")
    public void updateWeekly() {
        DATA_SYNC_SERVICE.update(PropertiesLoad.getWeeklyUpdate());
    }

}
