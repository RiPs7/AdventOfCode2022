package com.rips7.day;

import com.rips7.Utils.Pair;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Day12 extends Day<Integer> {

    @Override
    public Integer part1(final List<String> lines) {
        final Integer[][] heights = parseHeights(lines);

        final Pair<Coord, Coord> startAndEnd = findStartAndEnd(heights);
        final Coord start = startAndEnd.key();
        final Coord end = startAndEnd.value();

        int length = BFSStartToEnd(heights, start, end);

        printfln("The shortest path from S to E has length %s.", length);

        return length;
    }

    @Override
    public Integer part2(final List<String> lines) {
        final Integer[][] heights = parseHeights(lines);

        final Pair<Coord, Coord> startAndEnd = findStartAndEnd(heights);
        final Coord end = startAndEnd.value();

        int length = BFSEndToAnyStart(heights, end);

        printfln("The shortest path from an elevation 'a' to E has length %s.", length);

        return length;
    }

    private Integer[][] parseHeights(List<String> input) {
        return input.stream()
            .map(line -> line.chars()
                .mapToObj(c -> (char) c)
                .map(c -> (int) c)
                .map(i -> i >= 97 ? i - 96 : (i == 83 ? 0 : 27))
                .toArray(Integer[]::new))
            .toArray(Integer[][]::new);
    }

    private Pair<Coord, Coord> findStartAndEnd(final Integer[][] heights) {
        Coord start = null;
        Coord end = null;
        for (int i = 0; i < heights.length; i++) {
            for (int j = 0; j < heights[i].length; j++) {
                if (heights[i][j] == 0) {
                    start = new Coord(i, j);
                }
                if (heights[i][j] == 27) {
                    end = new Coord(i, j);
                }
                if (start != null && end != null) {
                    return new Pair<>(start, end);
                }
            }
        }
        throw new RuntimeException("No start or end");
    }

    private int BFSStartToEnd(final Integer[][] heights, final Coord start, final Coord end) {
        return BFSLength(heights, start, coord -> coord.equals(end), (neighbor, current) -> neighbor <= current + 1);
    }

    private int BFSEndToAnyStart(final Integer[][] heights, final Coord end) {
        return BFSLength(heights, end, coord -> heights[coord.key()][coord.value()] == 1, (neighbor, current) -> neighbor >= current - 1);
    }

    private int BFSLength(final Integer[][] heights, final Coord start, final Predicate<Coord> endCondition,
                          final BiPredicate<Integer, Integer> validNeighbor) {
        final Queue<Coord> frontier = new ArrayDeque<>();
        final Set<Coord> closedSet = new HashSet<>();
        frontier.add(start);
        while (!frontier.isEmpty()) {
            final Coord current = frontier.poll();
            if (endCondition.test(current)) {
                return current.length;
            }
            if (closedSet.contains(current)) {
                continue;
            }
            final int row = current.key();
            final int col = current.value();
            final int[] xOffset = {-1, 0, 1, 0};
            final int[] yOffset = {0, -1, 0, 1};
            for (int i = 0; i < 4; i++) {
                try {
                    final int nRow = row + yOffset[i];
                    final int nCol = col + xOffset[i];
                    if (!validNeighbor.test(heights[nRow][nCol], heights[row][col])) {
                        continue;
                    }
                    frontier.add(new Coord(nRow, nCol, current.length + 1));
                } catch (final ArrayIndexOutOfBoundsException aioob) {
                    // ignore
                }
            }
            closedSet.add(current);
        }
        throw new RuntimeException("No path");
    }

    private static final class Coord extends Pair<Integer, Integer> {
        int length;

        public Coord(final Integer key, final Integer value) {
            this(key, value, 0);
        }

        public Coord(final Integer key, final Integer value, final int length) {
            super(key, value);
            this.length = length;
        }

    }
}
