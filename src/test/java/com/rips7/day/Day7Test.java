package com.rips7.day;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day7Test extends DayTest {

    private final Day7 day7 = new Day7();

    @Test
    @Override
    public void testPart1() {
        assertEquals(95437, day7.part1(readTestResourceLinesPt1()));
    }

    @Test
    @Override
    public void testPart2() {
        assertEquals(24933642, day7.part2(readTestResourceLinesPt2()));
    }

}
