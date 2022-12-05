package com.rips7.day;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day5Test extends DayTest {

    private final Day5 day5 = new Day5();

    @Test
    @Override
    public void testPart1() {
        assertEquals("CMZ", day5.part1(readTestResourceLinesPt1()));
    }

    @Test
    @Override
    public void testPart2() {
        assertEquals("MCD", day5.part2(readTestResourceLinesPt2()));
    }

}
