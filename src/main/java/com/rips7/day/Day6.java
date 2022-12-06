package com.rips7.day;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day6 extends Day<Integer> {

    @Override
    public Integer part1(final List<String> lines) {
        final int markerPosition = findMarker(lines.get(0), 4);

        printfln("The start-of-packet marker is after position %s.", markerPosition);

        return markerPosition;
    }

    @Override
    public Integer part2(final List<String> lines) {
        final int markerPosition = findMarker(lines.get(0), 14);

        printfln("The start-of-message marker is after position %s.", markerPosition);

        return markerPosition;
    }

    private int findMarker(final String input, final int length) {
        for (int i = 0; i < input.length() - length; i++) {
            final String check = input.substring(i, i + length);
            final Set<Character> diffChars = check.chars().mapToObj(c -> (char) c).collect(Collectors.toSet());
            if (diffChars.size() == length) {
                return i + length;
            }
        }
        return 0;
    }


}
