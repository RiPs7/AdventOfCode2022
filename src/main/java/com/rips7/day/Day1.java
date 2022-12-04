package com.rips7.day;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class Day1 extends Day<Integer> {

    @Override
    public Integer part1(final List<String> lines) {
        final int max = getElfTotalCalories(lines)
            .max(Integer::compareTo)
            .orElse(0);

        printfln("The Elf carrying the most calories, is carrying %s calories.", max);

        return max;
    }

    @Override
    public Integer part2(final List<String> lines) {
        final int top3ElvesTotal = getElfTotalCalories(lines)
            .sorted(Comparator.reverseOrder())
            .limit(3)
            .reduce(Integer::sum)
            .orElse(0);

        printfln("The top three Elves are carrying %s calories in total.", top3ElvesTotal);

        return top3ElvesTotal;
    }

    private Stream<Integer> getElfTotalCalories(final List<String> input) {
        return Arrays.stream(String.join("\n", input).split("\n\n"))
            .map(group -> Arrays.stream(group.split("\n"))
                .map(Integer::parseInt)
                .reduce(Integer::sum)
                .orElse(0));

    }
}
