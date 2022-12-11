package com.rips7.day;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day9Test extends DayTest {

    private final Day9 day9 = new Day9();

    @Test
    @Override
    public void testPart1() {
        assertEquals(13, day9.part1(readTestResourceLinesPt1()));
    }

    @Test
    @Override
    public void testPart2() {
        assertEquals(36, day9.part2(readTestResourceLinesPt2()));
    }

}
