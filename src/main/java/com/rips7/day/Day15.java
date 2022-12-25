package com.rips7.day;

import com.rips7.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day15 extends Day<Long> {

    private static final Pattern PARSING_PATTERN = Pattern.compile("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)");

    @Override
    public Long part1(final List<String> lines) {
        final Map<Sensor, Coords> sensorsBeacons = parseSensorsBeacons(lines.subList(0, lines.size() - 1));
        final Set<Sensor> sensors = sensorsBeacons.keySet();
        final Set<Coords> beacons = new HashSet<>(sensorsBeacons.values());

        final int rowToCheck = Integer.parseInt(lines.get(lines.size() - 1).split(" = ")[1]);

        // Find the max Manhattan distance from the map above
        final int maxManhattanDist = sensors.stream()
            .map(Sensor::viewDistance)
            .max(Integer::compareTo)
            .orElseThrow();

        // For the required row, we start searching from the leftmost sensor/beacon - the max Manhattan distance
        final int minCol = Stream.of(sensors, beacons)
            .flatMap(Collection::stream)
            .map(Coords::col)
            .min(Integer::compareTo)
            .orElseThrow() - maxManhattanDist;

        // until the rightmost sensor/beacon + the max Manhattan distance
        final int maxCol = Stream.of(sensors, beacons)
            .flatMap(Collection::stream)
            .map(Coords::col)
            .max(Integer::compareTo)
            .orElseThrow() + maxManhattanDist;

        // The place is impossible for a beacon if the distance to any sensor is less than the sensors max distance,
        // and is not a reported beacon.
        final Set<Coords> impossiblePlaces = IntStream.rangeClosed(minCol, maxCol)
            .mapToObj(col -> new Coords(rowToCheck, col))
            .filter(toTest -> sensors.stream().anyMatch(sensor -> toTest.manhattanDistance(sensor) <= sensor.viewDistance))
            .filter(toTest -> !beacons.contains(toTest))
            .collect(Collectors.toSet());

        printfln("There are %s positions that cannot contain a beacon.", (long) impossiblePlaces.size());

        return (long) impossiblePlaces.size();
    }

    @Override
    public Long part2(final List<String> lines) {
        final Map<Sensor, Coords> sensorsBeacons = parseSensorsBeacons(lines.subList(0, lines.size() - 1));
        final Set<Sensor> sensors = sensorsBeacons.keySet();

        // Order the sensors based on their column, and then based on their row
        final List<Sensor> sensorListOrdered = new ArrayList<>(sensors).stream()
            .sorted(Comparator.comparingInt(Coords::col).thenComparing(Coords::row))
            .toList();

        // Get sensors in pairs
        final List<Utils.Pair<Sensor, Sensor>> sensorPairs = IntStream.range(0, sensorListOrdered.size() - 1)
            .mapToObj(i -> IntStream.range(i + 1, sensorListOrdered.size())
                .mapToObj(j -> new Utils.Pair<>(sensorListOrdered.get(i), sensorListOrdered.get(j)))
                .toList())
            .flatMap(List::stream)
            .toList();

        // Find the pairs where the sensors have a distance where 1 spot can be in the non-viewable distance
        final List<Utils.Pair<Sensor, Sensor>> sensorsPairsWithOneGap = sensorPairs.stream()
            .filter(pair -> pair.key().manhattanDistance(pair.value()) == pair.key().viewDistance + pair.value().viewDistance + 2)
            .toList();

        // The distress beacon can be anywhere on the line where the above sensors can't see
        // Find the lines on which the potential distress beacon can be on
        final Set<Line> potentialBeaconLines = sensorsPairsWithOneGap.stream()
            .map(pair -> {
                // Beacon line equation: y = λx + κ
                // λ = -1 or 1
                final int lambda = pair.key().row() > pair.key().col() ? 1 : -1;
                // It passes through the point after the sensor's viewDistance
                final Coords point = new Coords(pair.key().row(), pair.key().col() + pair.key().viewDistance + 1);
                // Solving for κ => κ = y - λx
                final int kappa = point.row() - lambda * point.col();
                return new Line(lambda, kappa);
            })
            .collect(Collectors.toSet());

        // Separate them in positive / negative
        final Set<Line> positiveLines = potentialBeaconLines.stream().filter(line -> line.lambda == 1).collect(Collectors.toSet());
        final Set<Line> negativeLines = potentialBeaconLines.stream().filter(line -> line.lambda == -1).collect(Collectors.toSet());

        // Find the intersection of positive / negative lines. These are potential positions for the distress beacon
        final Set<Coords> potentialBeacons = positiveLines.stream()
            .map(posLine -> negativeLines.stream().map(negLine -> negLine.intersects(posLine)).toList())
            .flatMap(List::stream)
            .collect(Collectors.toSet());

        // Apply the conditions:
        // 1. 0 <= row <= 4000000 && 0 <= col <= 4000000
        // 2. Should not be in the viewable distance of any other sensor
        final Coords distressBeacon = potentialBeacons.stream()
            .filter(beacon -> beacon.row() >= 0 && beacon.col() >= 0 && beacon.row() <= 4_000_000 && beacon.col() <= 4_000_000)
            .filter(beacon -> sensors.stream().noneMatch(sensor -> sensor.manhattanDistance(beacon) <= sensor.viewDistance))
            .findFirst()
            .orElseThrow();

        final long tuningFrequency = 4_000_000L * distressBeacon.col + distressBeacon.row;

        printfln("The tuning frequency of the distress beacon is %s.", tuningFrequency);

        return tuningFrequency;
    }

    private Map<Sensor, Coords> parseSensorsBeacons(final List<String> input) {
        return input.stream()
            .map(PARSING_PATTERN::matcher)
            .map(matcher -> {
                if (matcher.matches()) {
                    return new Integer[]{
                        Integer.parseInt(matcher.group(1)),
                        Integer.parseInt(matcher.group(2)),
                        Integer.parseInt(matcher.group(3)),
                        Integer.parseInt(matcher.group(4))
                    };
                }
                return null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(
                integers -> {
                    final Coords sensorCoords = new Coords(integers[1], integers[0]);
                    final Coords beaconCoords = new Coords(integers[3], integers[2]);
                    return new Sensor(integers[1], integers[0], sensorCoords.manhattanDistance(beaconCoords));
                },
                integers -> new Coords(integers[3], integers[2])));
    }

    private static class Sensor extends Coords {
        private final int viewDistance;

        public Sensor(final int row, final int col, final int viewDistance) {
            super(row, col);
            this.viewDistance = viewDistance;
        }

        private int viewDistance() {
            return viewDistance;
        }
    }

    private static class Coords {
        private final int row;
        private final int col;

        public Coords(final int row, final int col) {
            this.row = row;
            this.col = col;
        }

        protected int manhattanDistance(final Coords other) {
            return Math.abs(other.row - this.row) + Math.abs(other.col - this.col);
        }

        protected int row() {
            return row;
        }

        protected int col() {
            return col;
        }

        @Override
        public String toString() {
            return "[%s,%s]".formatted(row, col);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Coords coords = (Coords) o;
            return row == coords.row && col == coords.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }

    // y = λx + κ
    private record Line(int lambda, int k) {

        private Coords intersects(Line other) {
            final int intersectingX = (other.k - this.k) / (this.lambda - other.lambda);
            return new Coords(intersectingX * lambda + k, intersectingX);
        }
    }

}
