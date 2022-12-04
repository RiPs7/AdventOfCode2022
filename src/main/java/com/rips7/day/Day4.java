package com.rips7.day;

import java.util.List;
import com.rips7.Utils.Pair;

public class Day4 extends Day<Integer> {

    @Override
    public Integer part1(final List<String> lines) {
        final int total = (int) lines.stream()
            .filter(this::rangesContained)
            .count();

        printfln("There are %s assignment pairs that fully overlap.", total);

        return total;
    }

    @Override
    public Integer part2(final List<String> lines) {
        final int total = (int) lines.stream()
            .filter(this::rangesOverlap)
            .count();

        printfln("There are %s assignment pairs that overlap.", total);

        return total;
    }

    private boolean rangesContained(final String line) {
        final Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> ranges = getRanges(line);
        final int start1 = ranges.key().key();
        final int end1 = ranges.key().value();
        final int start2 = ranges.value().key();
        final int end2 = ranges.value().value();

        return (start2 >= start1 && end2 <= end1) || (start1 >= start2 && end1 <= end2);
    }

    private boolean rangesOverlap(final String line) {
        final Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> ranges = getRanges(line);
        final int start1 = ranges.key().key();
        final int end1 = ranges.key().value();
        final int start2 = ranges.value().key();
        final int end2 = ranges.value().value();

        return (start1 <= end2) && (start2 <= end1);
    }

    private Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> getRanges(final String line) {
        final String[] parts = line.split(",");
        final String[] startEnd1 = parts[0].split("-");
        final int start1 = Integer.parseInt(startEnd1[0]);
        final int end1 = Integer.parseInt(startEnd1[1]);
        final String[] startEnd2 = parts[1].split("-");
        final int start2 = Integer.parseInt(startEnd2[0]);
        final int end2 = Integer.parseInt(startEnd2[1]);
        return new Pair<>(new Pair<>(start1, end1), new Pair<>(start2, end2));
    }

}
