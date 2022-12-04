package com.rips7;

import com.rips7.day.Day;

public class Main {

    public static void main(String[] args) {
        Day.DAYS.forEach(Day::run);
    }

}
