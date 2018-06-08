package common;

import common.enums.ShimKey;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * Load class used to get data from the config file.
 * Initiates an instance of the properties found in the config file, which can then be gotten using this class' methods.
 */
public class PropertiesLoad {
    private static PropertiesLoad ourInstance = null;
    private static Properties configProps;
    private static ArrayList<String> constantUpdate, dailyUpdate, weeklyUpdate;
    private static ShimKey shim;

    /**
     * @return the singleton of the properties
     */
    public static PropertiesLoad getInstance() {
        if(ourInstance == null) {
            ourInstance = new PropertiesLoad();
        }
        return ourInstance;
    }

    /**
     * Loads data from the config file.
     */
    private PropertiesLoad() {
        String rootPath = Objects.requireNonNull(Thread.currentThread()
                .getContextClassLoader()
                .getResource("config.properties"))
                .getPath();

        try {
            configProps = new Properties();
            configProps.load(new FileInputStream(rootPath));

            constantUpdate = new ArrayList<>();
            dailyUpdate = new ArrayList<>();
            weeklyUpdate = new ArrayList<>();

            shim = Enum.valueOf(ShimKey.class, configProps.getProperty("shimkey").toUpperCase());
            List<DataType> shims =  shim.getEndPoints();

            initialiseUpdateRateArrays(shims);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Populates the arrays used to determine how often a specific data type should be updated.
     */
    private void initialiseUpdateRateArrays(List<DataType> shims) {
        for (DataType shim : shims) {
            int updateValue = Integer.parseInt(configProps.getProperty(shim.getName()));

            switch (updateValue) {
                case 0:
                    constantUpdate.add(shim.getName());
                    break;

                case 1:
                    dailyUpdate.add(shim.getName());
                    break;

                case 2:
                    weeklyUpdate.add(shim.getName());
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * @param propertyName the name of the property
     * @return the value given to the property in the config file
     */
    public static String getProperty(String propertyName) {
        return getInstance().configProps.getProperty(propertyName);
    }

    /**
     * @return the shim key specified in the config file
     */
    public static ShimKey getShim(){
        return getInstance().shim;
    }

    /**
     * @return the data types which are updated constantly
     */
    public static ArrayList<String> getConstantUpdate(){
        return getInstance().constantUpdate;
    }

    /**
     * @return the data types which are updated once a day
     */
    public static ArrayList<String> getDailyUpdate(){
        return getInstance().dailyUpdate;
    }

    /**
     * @return the data types which are updated once a week
     */
    public static ArrayList<String> getWeeklyUpdate(){
        return getInstance().weeklyUpdate;
    }

    /**
     * @return the array containing all datatypes which are to be updated
     */
    public static ArrayList<String> getAllDataTypes() {
        ArrayList<String> allDataTypes = getConstantUpdate();
        allDataTypes.addAll(getDailyUpdate());
        allDataTypes.addAll(getWeeklyUpdate());
        return allDataTypes;
    }

}
