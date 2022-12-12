package com.rips7.day;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day11Test extends DayTest {

    private final Day11 day11 = new Day11();

    @Test
    @Override
    public void testPart1() {
        assertEquals(new BigInteger("10605"), day11.part1(readTestResourceLinesPt1()));
    }

    @Test
    @Override
    public void testPart2() {
        assertEquals(new BigInteger("2713310158"), day11.part2(readTestResourceLinesPt2()));
    }

}
