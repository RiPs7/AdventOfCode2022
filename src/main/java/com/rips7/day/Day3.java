package com.rips7.day;

import com.rips7.Utils.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day3 extends Day<Integer> {

    private static final Function<Character, Integer> PRIORITY_MAPPER = c -> Character.isLowerCase(c) ? c - 96 : c - 38;

    private static final Function<String, Set<Character>> CHARS_EXTRACTOR = items -> items.chars().mapToObj(c -> (char) c).collect(Collectors.toSet());

    @Override
    public Integer part1(final List<String> lines) {
        final int totalPriorities = lines.stream()
            .map(line -> new Pair<>(line.substring(0, line.length() / 2), line.substring(line.length() / 2)))
            .map(pair -> new Pair<>(CHARS_EXTRACTOR.apply(pair.key()), CHARS_EXTRACTOR.apply(pair.value())))
            .map(pair -> {
                pair.key().retainAll(pair.value());
                return pair.key().iterator().next();
            })
            .map(PRIORITY_MAPPER)
            .reduce(Integer::sum)
            .orElse(0);

        print("The sum of the priorities of item types in both compartments is %s.%n", totalPriorities);

        return totalPriorities;
    }

    @Override
    public Integer part2(final List<String> lines) {
        final List<Set<Character>> rucksacks = lines.stream()
            .map(CHARS_EXTRACTOR)
            .toList();
        final int totalPriorities = IntStream.range(0, rucksacks.size() / 3)
            .mapToObj(i -> {
                final Set<Character> commonItems = new HashSet<>(rucksacks.get(3 * i));
                commonItems.retainAll(rucksacks.get(3 * i + 1));
                commonItems.retainAll(rucksacks.get(3 * i + 2));
                return commonItems.iterator().next();
            })
            .map(PRIORITY_MAPPER)
            .reduce(Integer::sum)
            .orElse(0);

        print("The sum of the priorities of item types in all three rucksacks is %s.%n", totalPriorities);

        return totalPriorities;
    }

}
