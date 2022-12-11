package com.rips7.day;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;

public class Day9 extends Day<Integer> {

    @Override
    public Integer part1(List<String> lines) {
        final Position start = new Position(0, 0);
        final Rope rope = new Rope(2, start);

        lines.stream()
            .map(line -> line.split(" "))
            .forEach(command -> rope.move(command[0].charAt(0), Integer.parseInt(command[1])));

        printfln("The rope tail visits %s positions at least once.", rope.tailTrail.size());

        return rope.tailTrail.size();
    }

    @Override
    public Integer part2(List<String> lines) {
        final Position start = new Position(0, 0);
        final Rope rope = new Rope(10, start);

        lines.stream()
            .map(line -> line.split(" "))
            .forEach(command -> rope.move(command[0].charAt(0), Integer.parseInt(command[1])));

        printfln("The rope tail visits %s positions at least once.", rope.tailTrail.size());

        return rope.tailTrail.size();
    }

    private static class Rope {
        private final Set<Position> tailTrail;
        private final List<Position> body;

        private Rope(int length, Position start) {
            body = new ArrayList<>(IntStream.range(0, length)
                .mapToObj(i -> start.copy())
                .toList());
            tailTrail = new HashSet<>();
            tailTrail.add(start.copy());
        }

        private void move(final char dir, final int val) {
            for (int i = 0; i < val; i++) {
                final Position head = body.get(0);
                final Position newHead = switch (dir) {
                    case 'U' -> new Position(head.row - 1, head.col);
                    case 'D' -> new Position(head.row + 1, head.col);
                    case 'L' -> new Position(head.row, head.col - 1);
                    default -> new Position(head.row, head.col + 1);
                };
                body.set(0, newHead);

                for(int j = 1; j < body.size(); j++) {
                    final Position prev = body.get(j - 1);
                    Position curr = body.get(j);
                    final int rowDif = Math.abs(prev.row - curr.row);
                    final int colDir = Math.abs(prev.col - curr.col);
                    if (rowDif > colDir) {
                        if (prev.row - curr.row > 1) {
                            curr = new Position(curr.row + 1, prev.col);
                        } else if (curr.row - prev.row > 1) {
                            curr = new Position(curr.row - 1, prev.col);
                        }
                    } else if (rowDif < colDir) {
                        if (prev.col - curr.col > 1) {
                            curr = new Position(prev.row, curr.col + 1);
                        } else if (curr.col - prev.col > 1) {
                            curr = new Position(prev.row, curr.col - 1);
                        }
                    } else if (rowDif > 1) {
                        if (prev.row - curr.row > 1) {
                            curr = new Position(curr.row + 1, curr.col);
                        }
                        if (curr.row - prev.row > 1) {
                            curr = new Position(curr.row - 1, curr.col);
                        }
                        if (prev.col - curr.col > 1) {
                            curr = new Position(curr.row, curr.col + 1);
                        }
                        if (curr.col - prev.col > 1) {
                            curr = new Position(curr.row, curr.col - 1);
                        }
                    }

                    body.set(j, curr);
                }

                tailTrail.add(body.get(body.size() - 1).copy());
            }
        }
    }

    public record Position(int row, int col) {
        private Position copy() {
            return new Position(this.row, this.col);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return row == position.row && col == position.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }

}
