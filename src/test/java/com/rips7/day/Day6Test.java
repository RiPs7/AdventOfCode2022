package com.rips7.day;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day6Test extends DayTest {

    private final Day6 day6 = new Day6();

    @Test
    @Override
    public void testPart1() {
        assertEquals(7, day6.part1(readTestResourceLinesPt1()));
    }

    @Test
    @Override
    public void testPart2() {
        assertEquals(19, day6.part2(readTestResourceLinesPt2()));
    }

}
