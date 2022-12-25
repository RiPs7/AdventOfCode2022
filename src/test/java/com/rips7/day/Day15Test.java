package com.rips7.day;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day15Test extends DayTest {

    private final Day15 day15 = new Day15();

    @Test
    @Override
    public void testPart1() {
        assertEquals(26, day15.part1(readTestResourceLinesPt1()));
    }

    @Test
    @Override
    public void testPart2() {
        assertEquals(56000011, day15.part2(readTestResourceLinesPt2()));
    }

}
