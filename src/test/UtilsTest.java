package test;

import app.Utils;

import java.time.LocalDate;

public class UtilsTest {


    public static void testGetFormattedDate() {
        LocalDate date = LocalDate.of(2023, 1, 1);
        String formattedDate = Utils.getFormattedDate(date);
        assert formattedDate.equals("20230101") : "testGetFormattedDate failed";
    }

    public static void testConvertToDateValid() {
        String dateString = "20230101";
        LocalDate date = Utils.convertToDate(dateString);
        assert date.equals(LocalDate.of(2023, 1, 1)) : "testConvertToDateValid failed";
    }

    public static void testConvertToDateInvalid() {
        String invalidDateString = "2023-01-01";
        try {
            Utils.convertToDate(invalidDateString);
            assert false : "testConvertToDateInvalid failed - expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Invalid date format, expected YYYYMMdd") : "testConvertToDateInvalid failed - wrong exception message";
        }
    }

    public void runAllTests() {
        System.out.println("Running Util tests...");
        testGetFormattedDate();
        testConvertToDateValid();
        testConvertToDateInvalid();
        System.out.println("All tests passed.");
    }
}
