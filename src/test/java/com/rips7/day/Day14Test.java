package com.rips7.day;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day14Test extends DayTest {

    private final Day14 day14 = new Day14();

    @Test
    @Override
    public void testPart1() {
        assertEquals(24, day14.part1(readTestResourceLinesPt1()));
    }

    @Test
    @Override
    public void testPart2() {
        assertEquals(93, day14.part2(readTestResourceLinesPt2()));
    }

}
