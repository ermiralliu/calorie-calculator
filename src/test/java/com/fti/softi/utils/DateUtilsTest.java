package com.fti.softi.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class DateUtilsTest {

    @Test
    public void testDayStart() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 10, 26, 15, 30, 45);
        LocalDateTime expected = LocalDateTime.of(2023, 10, 26, 0, 0, 0);

        assertEquals(expected, DateUtils.dayStart(dateTime));
    }

    @Test
    public void testWeekStart() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 10, 26, 15, 30, 45);
        LocalDateTime expected = LocalDateTime.of(2023, 10, 23, 0, 0, 0); // Monday of the week

        assertEquals(expected, DateUtils.weekStart(dateTime));
    }

    @Test
    public void testMonthStart() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 10, 26, 15, 30, 45);
        LocalDateTime expected = LocalDateTime.of(2023, 10, 1, 0, 0, 0);

        assertEquals(expected, DateUtils.monthStart(dateTime));
    }

    @Test
    public void testCurrentDayStart() {
        LocalDateTime expected = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);

        assertEquals(expected.toLocalDate(), DateUtils.currentDayStart().toLocalDate()); // Assert date only (time might change during test execution)
    }

    @Test
    public void testCurrentWeekStart() {
        LocalDateTime expected = LocalDateTime.now().with(DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0);

        assertEquals(expected.toLocalDate(), DateUtils.currentWeekStart().toLocalDate()); // Assert date only (time might change during test execution)
    }

    @Test
    public void testCurrentMonthStart() {
        LocalDateTime expected = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

        assertEquals(expected.toLocalDate(), DateUtils.currentMonthStart().toLocalDate()); // Assert date only (time might change during test execution)
    }
}
