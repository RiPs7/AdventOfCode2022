package com.rips7.day;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day13Test extends DayTest {

    private final Day13 day13 = new Day13();

    @Test
    @Override
    public void testPart1() {
        assertEquals(13, day13.part1(readTestResourceLinesPt1()));
    }

    @Test
    @Override
    public void testPart2() {
        assertEquals(140, day13.part2(readTestResourceLinesPt2()));
    }

}
