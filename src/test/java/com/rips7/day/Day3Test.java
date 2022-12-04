package com.rips7.day;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day3Test extends DayTest {

    private final Day3 day3 = new Day3();

    @Test
    @Override
    public void testPart1() {
        assertEquals(157, day3.part1(readTestResourceLinesPt1()));
    }

    @Test
    @Override
    public void testPart2() {
        assertEquals(70, day3.part2(readTestResourceLinesPt2()));
    }

}
