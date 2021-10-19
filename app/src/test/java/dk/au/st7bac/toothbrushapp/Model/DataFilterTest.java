package dk.au.st7bac.toothbrushapp.Model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataFilterTest {

    @Test
    void filterData_AddZeroMeasurements_ReturnSizeZero() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        DataFilter uut = new DataFilter(0, 0, 100);

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertEquals(0, resultData.size());
    }

    @Test
    void filterData_AddOneMeasurement_ReturnSizeOne() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 20.0, "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]", 0, LocalDateTime.now(), 0));
        DataFilter uut = new DataFilter(0, 0, 100);

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertEquals(1, resultData.size());
    }


    @Test
    void filterData_AddTwoDiffMeasurements_ReturnSizeTwo() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 20.0, "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]", 0, LocalDateTime.now(), 0));
        testData.add(new TbData("", 0, 20.0, "[nr:616eb9a8:3f:00:00:0ceed0:87654321:nr]", 0, LocalDateTime.now(), 0));
        DataFilter uut = new DataFilter(0, 0, 100);

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertEquals(2, resultData.size());
    }

    @Test
    void filterData_AddTwoIdenticalMeasurements_ReturnSizeOne() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 20.0, "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]", 0, LocalDateTime.now(), 0));
        testData.add(new TbData("", 0, 20.0, "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]", 0, LocalDateTime.now(), 0));
        DataFilter uut = new DataFilter(0, 0, 100);

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertEquals(1, resultData.size());
    }

    @Test
    void filterData_Add20sMeasurementOffset6_TbsecsEqualTo14() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 20.0, "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]", 0, LocalDateTime.now(), 0));
        DataFilter uut = new DataFilter(6.0, 0, 100);

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertEquals(14.0, resultData.get(0).getTbSecs());
    }

    @Test
    void filterData_Add20sMeasurementOffset0_TbsecsEqualTo20() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 20.0, "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]", 0, LocalDateTime.now(), 0));
        DataFilter uut = new DataFilter(0.0, 0, 100);

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertEquals(20.0, resultData.get(0).getTbSecs());
    }


    @Test
    void filterData_Add9sMeasurement_ReturnSizeZero() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 9.0, "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]", 0, LocalDateTime.now(), 0));
        DataFilter uut = new DataFilter(0, 10, 100);

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertEquals(0, resultData.size());
    }

    @Test
    void filterData_Add10sMeasurement_ReturnSizeZero() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 10.0, "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]", 0, LocalDateTime.now(), 0));
        DataFilter uut = new DataFilter(0, 10, 100);

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertEquals(0, resultData.size());
    }

    @Test
    void filterData_AddAbove10sMeasurement_ReturnSizeOne() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 10.1, "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]", 0, LocalDateTime.now(), 0));
        DataFilter uut = new DataFilter(0, 10, 100);

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertEquals(1, resultData.size());
    }

    @Test
    void filterData_AddAbove100sMeasurement_ReturnSizeZero() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 100.1, "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]", 0, LocalDateTime.now(), 0));
        DataFilter uut = new DataFilter(0, 0, 100);

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertEquals(0, resultData.size());
    }

    @Test
    void filterData_Add100sMeasurement_ReturnSizeZero() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 100, "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]", 0, LocalDateTime.now(), 0));
        DataFilter uut = new DataFilter(0, 0, 100);

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertEquals(0, resultData.size());
    }

    @Test
    void filterData_Add99sMeasurement_ReturnSizeOne() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 99, "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]", 0, LocalDateTime.now(), 0));
        DataFilter uut = new DataFilter(0, 0, 100);

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertEquals(1, resultData.size());
    }

    @Test
    void filterData_Tbval1Measurement_ReturnSizeOne() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 1, 20.0, "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]", 0, LocalDateTime.now(), 0));
        DataFilter uut = new DataFilter(0, 0, 100);

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertEquals(0, resultData.size());
    }

    @Test
    void filterData_Tbhb1Measurement_ReturnSizeOne() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 20.0, "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]", 1, LocalDateTime.now(), 0));
        DataFilter uut = new DataFilter(0, 0, 100);

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertEquals(0, resultData.size());
    }

    @Test
    void filterData_Tbval1Tbhb1Measurement_ReturnSizeOne() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 1, 20.0, "[nr:616eb9a8:3f:00:00:0ceed0:12345678:nr]", 1, LocalDateTime.now(), 0));
        DataFilter uut = new DataFilter(0, 0, 100);

        // act
        List<TbData> resultData = uut.filterData(testData);

        // assert
        assertEquals(0, resultData.size());
    }
}