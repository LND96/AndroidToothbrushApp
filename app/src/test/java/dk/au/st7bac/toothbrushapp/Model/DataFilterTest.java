package dk.au.st7bac.toothbrushapp.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataFilterTest {

    DataFilter uut;

    @BeforeEach
    void setup() {
        uut = new DataFilter(0.0, 0, 100);
    }

    // Test addition of measurements
    @Test
    void filterData_AddZeroMeasurements_ReturnSizeZero() {
        // arrange
        List<TbData> testData = new ArrayList<>();

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertEquals(0, resultData.size());
    }

    @Test
    void filterData_AddOneMeasurement_ReturnSizeOne() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 20.0,
                "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]",
                0, LocalDateTime.now(), 0));

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertEquals(1, resultData.size());
    }


    @Test
    void filterData_AddTwoDiffMeasurements_ReturnSizeTwo() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 20.0,
                "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]",
                0, LocalDateTime.now(), 0));
        testData.add(new TbData("", 0, 20.0,
                "[nr:616eb9a8:3f:00:00:0ceed0:87654321:nr]",
                0, LocalDateTime.now(), 0));

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertEquals(2, resultData.size());
    }

    @Test
    void filterData_AddTwoIdenticalMeasurements_ReturnSizeOne() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 20.0,
                "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]",
                0, LocalDateTime.now(), 0));
        testData.add(new TbData("", 0, 20.0,
                "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]",
                0, LocalDateTime.now(), 0));

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertEquals(1, resultData.size());
    }

    // Test offset
    @Test
    void filterData_Add20sMeasurementOffset0_TbsecsEqualTo20() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 20.0,
                "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]",
                0, LocalDateTime.now(), 0));

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertEquals(20.0, resultData.get(0).getTbSecs());
    }

    @Test
    void filterData_Add20sMeasurementOffset6_TbsecsEqualTo14() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 20.0,
                "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]",
                0, LocalDateTime.now(), 0));
        DataFilter uut = new DataFilter(6.0, 0, 100);

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertEquals(14.0, resultData.get(0).getTbSecs());
    }

    @ParameterizedTest
    @ValueSource(doubles = {11.9, 12.0})
    void filterData_AddUnder12sMeasurementOffset12_0_TbsecsEqualTo0(double tbSecs) {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, tbSecs,
                "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]",
                0, LocalDateTime.now(), 0));
        DataFilter uut = new DataFilter(12.0, -10, 100);

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertEquals(0.0, resultData.get(0).getTbSecs());
    }

    @Test
    void filterData_Add12_1sMeasurementOffset12_0_TbsecsGreaterThan0() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 12.1,
                "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]",
                0, LocalDateTime.now(), 0));
        DataFilter uut = new DataFilter(12.0, -10, 100);

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertTrue(resultData.get(0).getTbSecs() > 0.0);
    }

    // Test minimum time of tb
    @Test
    void filterData_Add10_0sMeasurement_ReturnSizeZero() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 10.0,
                "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]",
                0, LocalDateTime.now(), 0));
        DataFilter uut = new DataFilter(0.0, 10, 100);

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertEquals(0, resultData.size());
    }

    @Test
    void filterData_Add10_1sMeasurement_ReturnSizeOne() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 10.1,
                "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]",
                0, LocalDateTime.now(), 0));
        DataFilter uut = new DataFilter(0, 10, 100);

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertEquals(1, resultData.size());
    }

    // Test maximum time of tb
    @Test
    void filterData_Add100_0sMeasurement_ReturnSizeZero() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 100.0,
                "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]",
                0, LocalDateTime.now(), 0));

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertEquals(0, resultData.size());
    }

    @Test
    void filterData_Add99_9sMeasurement_ReturnSizeOne() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 99.9,
                "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]",
                0, LocalDateTime.now(), 0));

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertEquals(1, resultData.size());
    }

    // Test filtration of tbVal = 1 and tbHb = 1
    @Test
    void filterData_Tbval1Measurement_ReturnSizeZero() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 1, 20.0,
                "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]",
                0, LocalDateTime.now(), 0));

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertEquals(0, resultData.size());
    }

    @Test
    void filterData_Tbhb1Measurement_ReturnSizeZero() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 20.0,
                "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]",
                1, LocalDateTime.now(), 0));

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertEquals(0, resultData.size());
    }

    @Test
    void filterData_Tbval1Tbhb1Measurement_ReturnSizeZero() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 1, 20.0,
                "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]",
                1, LocalDateTime.now(), 0));

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertEquals(0, resultData.size());
    }
}