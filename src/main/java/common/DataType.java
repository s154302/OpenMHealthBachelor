package common;

import javax.xml.crypto.Data;

/**
 * Data type class which contains the name and the version used for storing in the DSU.
 */
public class DataType {
    private String name;
    private String version;

    /**
     * @param name the name of the data type
     * @param version the version used when storing the data type
     */
    public DataType(String name, String version){
        this.name = name;
        this.version= version;
    }

    /**
     * @return the name of the data type
     */
    public String getName(){
        return name;
    }

    /**
     * @return the version used by the data type when storing it
     */
    public String getVersion(){
        return version;
    }

    /**
     * Overridden for testing purposes
     */
    @Override
    public boolean equals(Object b) {
        return this.getName().equals(((DataType)b).getName()) &&
                this.getVersion().equals(((DataType)b).getVersion());
    }
}
