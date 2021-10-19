package dk.au.st7bac.toothbrushapp.Model;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataCleanerTest {
    @Test
    public void setDateTime_epochConverted_ReturnCorrect_Epoch() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // arrange
        Method testMethod = DataCleaner.class.getDeclaredMethod("setDateTime", List.class);
        testMethod.setAccessible(true);
        DataCleaner _uut = new DataCleaner();
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 20.0, "[nr:616D7A62", 0, LocalDateTime.now(), 0));

        // act
        List<TbData> resultData = (List<TbData>) testMethod.invoke(_uut, testData);

        // assert
        assertEquals(1634564706, resultData.get(0).getEpoch());
    }

    @Test
    public void setDateTime_dateConverted_ReturnCorrectDate() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // arrange
        Method testMethod = DataCleaner.class.getDeclaredMethod("setDateTime", List.class);
        testMethod.setAccessible(true);
        DataCleaner _uut = new DataCleaner();
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 20.0, "[nr:616D7A62", 0, LocalDateTime.now(), 0));

        // act
        List<TbData> resultData = (List<TbData>) testMethod.invoke(_uut, testData);

        // assert
        LocalDateTime expectedDate = LocalDateTime.of(2021, 10, 18, 15, 45, 6); // når vi overgår til vintertid vil denne test fejle - hvordan tager jeg højde for tidsforskel i local date time
        assertEquals(expectedDate, resultData.get(0).getDateTime());
    }

    @Test
    public void cleanData_fiveIdenticalTbData_ReturnSizeOne() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            testData.add(new TbData("", 0, 20.0, "[nr:616D72FF", 0, LocalDateTime.now(), 0));
        }
        DataCleaner _uut = new DataCleaner();

        // act
        List<TbData> resultData = _uut.cleanData(testData);

        // assert
        assertEquals(1, resultData.size());
    }

    @Test
    public void cleanData_twoSeparateTbData_ReturnSizeTwo() {
        // arrange
        List<TbData> testData = new ArrayList<>();
        testData.add(new TbData("", 0, 20.0, "[nr:616D7A62", 0, LocalDateTime.now(), 0));
        testData.add(new TbData("", 0, 20.0, "[nr:616D72FF", 0, LocalDateTime.now(), 0));

        DataCleaner _uut = new DataCleaner();

        // act
        List<TbData> resultData = _uut.cleanData(testData);

        // assert
        assertEquals(2, resultData.size());
    }
}