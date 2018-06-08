package common.enums;

import common.DataType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ShimKeyTest {

    ArrayList<DataType> endPoints;
    String name;

    @Before
    public void setUp() {
        DataType[] dataTypes = {
        new DataType("blood_pressure","1.0"),
        new DataType("body_height","1.0"),
        new DataType("body_weight","1.0"),
        new DataType("body_temperature","1.0"),
        new DataType("heart_rate","1.1"),
        new DataType("sleep_duration","2.0"),
        new DataType("sleep_episode","1.0"),
        new DataType("step_count","2.0")};
        endPoints = new ArrayList<>(Arrays.asList(dataTypes));

        name = "withings";
    }

    @Test
    public void getEndPoints() {
        for(ShimKey shimKey : ShimKey.values()) {
            assertFalse(shimKey.getEndPoints() == null);
        }
        assertTrue(ShimKey.WITHINGS.getEndPoints().equals(endPoints));
    }

    @Test
    public void getName() {
        for(ShimKey shimKey : ShimKey.values()) {
            for(DataType dataType : shimKey.getEndPoints()) {
                assertFalse(dataType.getName() == null);
                assertFalse(dataType.getName().equals(""));
            }
        }
        assertTrue(ShimKey.WITHINGS.getName().equals(name));
    }

    @Test
    public void getVersion() {
        for(ShimKey shimKey : ShimKey.values()) {
            for(DataType dataType : shimKey.getEndPoints()) {
                assertFalse(dataType.getVersion() == null);
                assertFalse(dataType.getVersion().equals(""));
            }
        }
        assertTrue(ShimKey.WITHINGS.getVersion("blood-pressure").equals((endPoints.get(0)).getVersion()));
        assertTrue(ShimKey.WITHINGS.getVersion("body-height").equals((endPoints.get(1)).getVersion()));
        assertTrue(ShimKey.WITHINGS.getVersion("body-weight").equals((endPoints.get(2)).getVersion()));
        assertTrue(ShimKey.WITHINGS.getVersion("body-temperature").equals((endPoints.get(3)).getVersion()));
        assertTrue(ShimKey.WITHINGS.getVersion("heart-rate").equals((endPoints.get(4)).getVersion()));
        assertTrue(ShimKey.WITHINGS.getVersion("sleep-duration").equals((endPoints.get(5)).getVersion()));
        assertTrue(ShimKey.WITHINGS.getVersion("sleep-episode").equals((endPoints.get(6)).getVersion()));
        assertTrue(ShimKey.WITHINGS.getVersion("step-count").equals((endPoints.get(7)).getVersion()));
    }
}