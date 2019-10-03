package com.example.aturgajian.utils;

import java.util.Calendar;


public class DateFormat {

    public static Calendar calendar   = Calendar.getInstance();
    public static int year            = calendar.get(Calendar.YEAR); // current year
    public static int month           = calendar.get(Calendar.MONTH); // current month
    public static int day             = calendar.get(Calendar.DAY_OF_MONTH); // current day

}
