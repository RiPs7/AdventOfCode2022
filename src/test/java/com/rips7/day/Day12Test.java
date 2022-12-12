package com.rips7.day;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day12Test extends DayTest {

    private final Day12 day12 = new Day12();

    @Test
    @Override
    public void testPart1() {
        assertEquals(31, day12.part1(readTestResourceLinesPt1()));
    }

    @Test
    @Override
    public void testPart2() {
        assertEquals(29, day12.part2(readTestResourceLinesPt2()));
    }

}
