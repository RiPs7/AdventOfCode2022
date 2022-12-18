package com.rips7.day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day14 extends Day<Integer> {

    @Override
    public Integer part1(final List<String> lines) {
        final Set<Coords> rocks = parseRocks(lines);
        final int lastRock = rocks.stream().map(Coords::y).max(Integer::compareTo).orElse(0);
        final int totalSands = simulate(rocks, sand -> sand.y > lastRock);

        printfln("%s units of sand come to rest before sand starts flowing into the abyss", totalSands);

        return totalSands;
    }

    @Override
    public Integer part2(final List<String> lines) {
        final Set<Coords> rocks = parseRocks(lines);
        final int lastRock = rocks.stream().map(Coords::y).max(Integer::compareTo).orElse(0);
        rocks.addAll(parseRocks(List.of(String.format("%s,%s -> %s,%s", 300, lastRock + 2, 700, lastRock + 2))));
        final int totalSands = simulate(rocks, sand -> sand.x == 500 && sand.y == 0);

        printfln("%s units of sand come to rest", totalSands);

        return totalSands;
    }

    private int simulate(final Set<Coords> rocks, final Predicate<Coords> endingCondition) {
        final Set<Coords> sands = new HashSet<>();
        boolean simulating = true;
        while(simulating) {
            Coords sand = new Coords(500, 0);
            boolean sandMoving = true;
            while (sandMoving) {
                final List<Coords> possibleNextPositions = Stream.of(
                        new Coords(sand.x, sand.y + 1),
                        new Coords(sand.x - 1, sand.y + 1),
                        new Coords(sand.x + 1, sand.y + 1))
                    .toList();
                Coords nextPosition = null;
                for (final Coords possibleNextPosition : possibleNextPositions) {
                    if (!rocks.contains(possibleNextPosition) && !sands.contains(possibleNextPosition)) {
                        nextPosition = possibleNextPosition;
                        break;
                    }
                }
                if (nextPosition != null) {
                    sand = nextPosition;
                } else {
                    sands.add(new Coords(sand.x, sand.y));
                    sandMoving = false;
                }
                if (endingCondition.test(sand)) {
                    simulating = false;
                    break;
                }
            }
        }
        return sands.size();
    }

    private Set<Coords> parseRocks(final List<String> input) {
        return input.stream().flatMap(line -> {
            final List<Coords> startEndCoords = Arrays.stream(line.split(" -> ")).map(Coords::from).toList();
            final List<Coords> allCoords = new ArrayList<>();
            for (int i = 0; i < startEndCoords.size() - 1; i++) {
                final Coords start = startEndCoords.get(i);
                final Coords end = startEndCoords.get(i + 1);
                if (start.x == end.x) {
                    for (int y = Math.min(start.y, end.y); y <= Math.max(start.y, end.y); y++) {
                        allCoords.add(new Coords(start.x, y));
                    }
                } else if (start.y == end.y) {
                    for (int x = Math.min(start.x, end.x); x <= Math.max(start.x, end.x); x++) {
                        allCoords.add(new Coords(x, start.y));
                    }
                }
            }
            return allCoords.stream();
        }).collect(Collectors.toSet());
    }

    private record Coords(int x, int y) {

        private static Coords from(final String input) {
            final String[] parts = input.split(",");
            return new Coords(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coords coords = (Coords) o;
            return x == coords.x && y == coords.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

}
