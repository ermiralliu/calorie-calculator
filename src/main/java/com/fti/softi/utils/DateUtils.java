package com.fti.softi.utils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class DateUtils {
  private DateUtils(){} // Qe te mos konstruktohet dot
  
  public static LocalDateTime dayStart(LocalDateTime dateTime) {
    return dateTime.withHour(0).withMinute(0).withSecond(0);
  }

  public static LocalDateTime weekStart(LocalDateTime dateTime) {
    return dayStart(dateTime.with(DayOfWeek.MONDAY));
  }

  public static LocalDateTime monthStart(LocalDateTime dateTime) {
    return dayStart(dateTime.withDayOfMonth(1));
  }

  public static LocalDateTime currentDayStart(){
    return dayStart(LocalDateTime.now());
  }
  public static LocalDateTime currentWeekStart(){
    return dayStart(LocalDateTime.now().with(DayOfWeek.MONDAY));
  }
  public static LocalDateTime currentMonthStart(){
    return dayStart(LocalDateTime.now().withDayOfMonth(1));
  }
}
