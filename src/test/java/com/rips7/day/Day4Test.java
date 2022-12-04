package com.rips7.day;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day4Test extends DayTest {

    private final Day4 day4 = new Day4();

    @Test
    @Override
    public void testPart1() {
        assertEquals(2, day4.part1(readTestResourceLinesPt1()));
    }

    @Test
    @Override
    public void testPart2() {
        assertEquals(4, day4.part2(readTestResourceLinesPt2()));
    }

}
