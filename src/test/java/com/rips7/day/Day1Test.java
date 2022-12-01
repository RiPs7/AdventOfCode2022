package com.rips7.day;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day1Test extends DayTest {

    private final Day1 day1 = new Day1();

    @Test
    @Override
    public void testPart1() {
        assertEquals(24000, day1.part1(readTestResourceLinesPt1()));
    }

    @Test
    @Override
    public void testPart2() {
        assertEquals(45000, day1.part2(readTestResourceLinesPt2()));
    }

}
