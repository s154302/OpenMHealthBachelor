package common.enums;

import common.DataType;

import java.util.*;

/**
 * Contains all Shimmer supported shimkeys. Also contains all the data types supported by each shimkey.
 * In turn each Data type contains what version it is supported by.
 * The shim key can be set within the config properties file, and should then be loaded from there.
 * @see DataType
 * @see common.PropertiesLoad
 */
public enum ShimKey {
    FITBIT("fitbit",
            new DataType("body_mass_index","2.0"),
            new DataType("body_weight","1.0"),
            new DataType("heart_rate","1.1"),
            new DataType("physical_activity","1.2"),
            new DataType("sleep_duration","2.0"),
            new DataType("sleep_episode", "1.0"),
            new DataType("step_count","2.0")),

    GOOGLEFIT("googlefit",
            new DataType("body_height","1.0"),
            new DataType("body_weight","1.0"),
            new DataType("calories_burned","2.0"),
            new DataType("geoposition","1.0"),
            new DataType("heart_rate","1.1"),
            new DataType("physical_activity","1.2"),
            new DataType("speed","1.0"),
            new DataType("step_count","2.0")),

    IHEALTH("ihealth",
            new DataType("blood_glucose","1.0"),
            new DataType("blood_pressure","1.0"),
            new DataType("body_mass_index","1.0"),
            new DataType("body_weight","1.0"),
            new DataType("heart_rate","1.1"),
            new DataType("physical_activity","1.2"),
            new DataType("sleep_duration","2.0"),
            new DataType("step_count","2.0")),

    JAWBONE("jawbone",
            new DataType("body_mass_index","1.0"),
            new DataType("body_weight","1.0"),
            new DataType("heart_rate","1.1"),
            new DataType("physical_activity","1.2"),
            new DataType("sleep_duration","1.0"),
            new DataType("step_count","1.0")),

    MISFIT("misfit",
            new DataType("physical_activity","1.2"),
            new DataType("step_count","1.0"),
            new DataType("sleep_duration","2.0"),
            new DataType("sleep_episode","1.0")),

    MOVES("moves",
            new DataType("physical_activity","1.2"),
            new DataType("step_count","1.0")),

    RUNKEEPER("runkeeper",
            new DataType("calories_burned","2.0"),
            new DataType("physical_activity","1.2")),

    WITHINGS("withings",
            new DataType("blood_pressure","1.0"),
            new DataType("body_height","1.0"),
            new DataType("body_weight","1.0"),
            new DataType("body_temperature","1.0"),
            new DataType("heart_rate","1.1"),
            new DataType("sleep_duration","2.0"),
            new DataType("sleep_episode","1.0"),
            new DataType("step_count","2.0"));


    private final List<DataType> endPoints;
    private final String name;
    private final Map<String, DataType> dataTypes;

    /**
     * Constructor method for the ShimKeys
     * @param name the name of the shim key
     * @param endPoints the data types supported by the shim key
     */
    ShimKey(String name, DataType... endPoints) {
        this.name = name;
        this.endPoints = Arrays.asList(endPoints);
        this.dataTypes = new HashMap<>();
        for(DataType dataType : endPoints) {
            dataTypes.put(dataType.getName(), dataType);
        }
    }

    /**
     * @return the list of data types supported by the given shim key
     */
    public List<DataType> getEndPoints() {
        return new ArrayList<>(endPoints);
    }

    /**
     * @return returns the name of the shim key
     */
    public String getName() {
        return name;
    }

    /**
     * @param dataType the data type of which a version should be gotten
     * @return the version used to store the data type
     */
    public String getVersion(String dataType) {
        dataType =  dataType.replace("-","_");
        if(dataTypes.get(dataType) != null) {
            return dataTypes.get(dataType).getVersion();
        }
        return null;
    }
}
