package com.rips7.day;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day8Test extends DayTest {

    private final Day8 day8 = new Day8();

    @Test
    @Override
    public void testPart1() {
        assertEquals(21, day8.part1(readTestResourceLinesPt1()));
    }

    @Test
    @Override
    public void testPart2() {
        assertEquals(8, day8.part2(readTestResourceLinesPt2()));
    }

}
