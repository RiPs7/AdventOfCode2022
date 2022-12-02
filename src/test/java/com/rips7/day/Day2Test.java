package com.rips7.day;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day2Test extends DayTest {

    private final Day2 day2 = new Day2();

    @Test
    @Override
    public void testPart1() {
        assertEquals(15, day2.part1(readTestResourceLinesPt1()));
    }

    @Test
    @Override
    public void testPart2() {
        assertEquals(12, day2.part2(readTestResourceLinesPt2()));
    }

}
