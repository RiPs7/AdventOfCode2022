package com.rips7.day;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.IntStream;

import static com.rips7.Utils.readLines;
import static com.rips7.Utils.timeIt;

public abstract class Day<OUTPUT> {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_BOLD = "\u001B[1m";
    private static final String ANSI_ITALIC = "\u001B[3m";
    private static final String ANSI_UNDERLINE = "\u001B[4m";

    public static final List<? extends Day<?>> DAYS =
        IntStream.rangeClosed(1, 4).mapToObj(i -> Day.class.getName() + i).map(d -> {
                try {
                    return (Day<?>) Class.forName(d).getConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            })
            .toList();

    public abstract OUTPUT part1(final List<String> input);

    public abstract OUTPUT part2(final List<String> input);

    public void run() {
        System.out.printf("%s=== Running %s ===%s%n", ANSI_RED, getClass().getSimpleName(), ANSI_RESET);

        System.out.printf("%s--- Part 1 ---%s%n", ANSI_YELLOW, ANSI_RESET);
        System.out.printf("%s--- Took %s ms ---%s%n", ANSI_BLUE, timeIt(() -> this.part1(readResourceLinesPt1())), ANSI_RESET);

        System.out.printf("%s--- Part 2 ---%s%n", ANSI_YELLOW, ANSI_RESET);
        System.out.printf("%s--- Took %s ms ---%s%n", ANSI_BLUE, timeIt(() -> this.part2(readResourceLinesPt2())), ANSI_RESET);

        System.out.printf("%s=====================%s%n%n", ANSI_RED, ANSI_RESET);
    }

    void printfln(final String str, final OUTPUT result) {
        final String formattedString = str.replace("%s", ANSI_BOLD + ANSI_ITALIC + ANSI_UNDERLINE + "%s" + ANSI_RESET) + "%n";
        System.out.printf(formattedString, result);
    }

    private List<String> readResourceLinesPt1() {
        return readLines(getResourceNamePt1());
    }

    private List<String> readResourceLinesPt2() {
        return readLines(getResourceNamePt2());
    }

    private String getResourceNamePt1() {
        final String day = getClass().getSimpleName().toLowerCase();
        return day + "/" + "part1.txt";
    }

    private String getResourceNamePt2() {
        final String day = getClass().getSimpleName().toLowerCase();
        return day + "/" + "part2.txt";
    }

}
