package dk.au.st7bac.toothbrushapp.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataCleanerTest {

    DataCleaner uut;

    @BeforeEach
    void setup() {
        uut = new DataCleaner(10);
    }

    // Test setDateTime()
    // kilde til at teste private metoder: https://stackoverflow.com/questions/34571/how-do-i-test-a-class-that-has-private-methods-fields-or-inner-classes
    @Test
    void setDateTime_EpochConverted_ReturnCorrectEpoch() throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 20.0, "[nr:616D7A62",
                0, LocalDateTime.now(), 0));

        // act
        Method testMethod = DataCleaner.class.getDeclaredMethod("setDateTime", List.class);
        testMethod.setAccessible(true);
        List<TbData> resultData = (List<TbData>) testMethod.invoke(uut, testData);

        // assert
        assertEquals(1634564706, resultData.get(0).getEpoch());
    }

    @Test
    void setDateTime_DateConverted_ReturnCorrectDate() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 20.0, "[nr:616D7A62",
                0, LocalDateTime.now(), 0));

        // act
        Method testMethod = DataCleaner.class.getDeclaredMethod("setDateTime", List.class);
        testMethod.setAccessible(true);
        List<TbData> resultData = (List<TbData>) testMethod.invoke(uut, testData);

        // assert
        LocalDateTime expectedDate = LocalDateTime.of(2021, 10, 18, 15, 45, 6); // når vi overgår til vintertid vil denne test fejle - hvordan tager jeg højde for tidsforskel i local date time
        assertEquals(expectedDate, resultData.get(0).getDateTime());
    }

    // Test cleanData()
    @Test
    void cleanData_AddZeroMeasurements_ReturnSizeZero() {
        // arrange
        List<TbData> testData = new ArrayList<>();

        // act
        List<TbData> resultData = uut.cleanData(testData);

        // assert
        assertEquals(0, resultData.size());
    }

    @Test
    void cleanData_AddOneMeasurement_ReturnSizeOne() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 20.0,
                "[nr:616D72FF", 0, LocalDateTime.now(), 0));

        // act
        List<TbData> resultData = uut.cleanData(testData);

        // assert
        assertEquals(1, resultData.size());
    }

    @Test
    void cleanData_AddOneMeasurement_Return20s(){
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 20.0,
                "[nr:616D72FF", 0, LocalDateTime.now(), 0));

        // act
        List<TbData> resultData = uut.cleanData(testData);

        // assert
        assertEquals(20.0, resultData.get(0).getTbSecs());
    }


    @Test
    void cleanData_FiveTbDataAtSameTime_ReturnSizeOne() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            testData.add(new TbData("", 0, 20.0,
                    "[nr:616D72FF", 0, LocalDateTime.now(), 0));
        }

        // act
        List<TbData> resultData = uut.cleanData(testData);

        // assert
        assertEquals(1, resultData.size());
    }


    @Test
    void cleanData_FiveTbDataAtSameTime_Return100s() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            testData.add(new TbData("", 0, 20.0,
                    "[nr:616D72FF", 0, LocalDateTime.now(), 0));
        }

        // act
        List<TbData> resultData = uut.cleanData(testData);

        // assert
        assertEquals(100.0, resultData.get(0).getTbSecs());
    }

    @Test
    void cleanData_TwoMeasurementsInsideInterval_ReturnSizeOne() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 20.0, "[nr:617FE052",
                0, LocalDateTime.now(), 0)); // Monday 1. November 2021 12:40:50
        testData.add(new TbData("", 0, 20.0, "[nr:617FDDFB",
                0, LocalDateTime.now(), 0)); // Monday 1. November 2021 12:30:51

        // act
        List<TbData> resultData = uut.cleanData(testData);

        // assert
        assertEquals(1, resultData.size());
    }

    @Test
    void cleanData_TwoMeasurementsOutsideInterval_ReturnSizeTwo() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 20.0, "[nr:617FE052",
                0, LocalDateTime.now(), 0)); // Monday 1. November 2021 12:40:50
        testData.add(new TbData("", 0, 20.0, "[nr:617FDDFA",
                0, LocalDateTime.now(), 0)); // Monday 1. November 2021 12:30:50

        // act
        List<TbData> resultData = uut.cleanData(testData);

        // assert
        assertEquals(2, resultData.size());
    }
}