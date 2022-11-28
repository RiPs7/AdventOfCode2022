package com.rips7.day;

import java.util.List;

import static com.rips7.Utils.readLines;

@SuppressWarnings("unused")
public abstract class DayTest {

    public abstract void testPart1();

    public abstract void testPart2();

    public List<String> readTestResourceLinesPt1() {
        return readLines(getResourceNamePt1());
    }

    public List<String> readTestResourceLinesPt2() {
        return readLines(getResourceNamePt2());
    }

    private String getResourceNamePt1() {
        final String day = getClass().getSimpleName().toLowerCase().replace("test", "");
        return day + "/" + "part1.txt";
    }

    private String getResourceNamePt2() {
        final String day = getClass().getSimpleName().toLowerCase().replace("test", "");
        return day + "/" + "part2.txt";
    }
}
