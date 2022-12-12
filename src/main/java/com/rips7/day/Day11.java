package com.rips7.day;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day11 extends Day<BigInteger> {

    @Override
    public BigInteger part1(final List<String> lines) {
        final Map<Integer, Monkey> monkeys = Arrays.stream(String.join("\n", lines).split("\n\n"))
            .map(Monkey::parseMonkey)
            .collect(Collectors.toMap(monkey -> monkey.id, Function.identity()));

        for (int round = 0; round < 20; round++) {
            monkeys.forEach((id, monkey) -> monkey.inspect(monkeys, null));
        }

        final BigInteger twoMostActive = monkeys.values().stream()
            .map(monkey -> monkey.inspectedItems)
            .sorted(Comparator.reverseOrder())
            .limit(2)
            .reduce(BigInteger::multiply)
            .orElse(BigInteger.ZERO);

        printfln("The level of monkey business after 20 rounds is %s.", twoMostActive);

        return twoMostActive;
    }

    @Override
    public BigInteger part2(final List<String> lines) {
        final Map<Integer, Monkey> monkeys = Arrays.stream(String.join("\n", lines).split("\n\n"))
            .map(Monkey::parseMonkey)
            .collect(Collectors.toMap(monkey -> monkey.id, Function.identity()));

        final BigInteger lcm = monkeys.values().stream()
            .map(monkey -> monkey.divisibleBy)
            .map(BigInteger::valueOf)
            .reduce(BigInteger::multiply)
            .orElse(BigInteger.ONE);

        for (int round = 1; round <= 10000; round++) {
            monkeys.forEach((id, monkey) -> monkey.inspect(monkeys, lcm));
        }

        final BigInteger twoMostActive = monkeys.values().stream()
            .map(monkey -> monkey.inspectedItems)
            .sorted(Comparator.reverseOrder())
            .limit(2)
            .reduce(BigInteger::multiply)
            .orElse(BigInteger.ZERO);

        printfln("The level of monkey business after 10000 rounds is %s.", twoMostActive);

        return twoMostActive;
    }

    private static final class Monkey {
        private static final Pattern MONKEY_ID = Pattern.compile("\\s*Monkey (?<id>\\d+):");
        private static final Pattern STARTING_ITEMS = Pattern.compile("\\s*Starting items: (?<items>\\d+(, \\d+)*)");
        private static final Pattern OPERATION = Pattern.compile("\\s*Operation: new = old (?<op>.) (?<what>\\w+)");
        private static final Pattern TEST = Pattern.compile("\\s*Test: divisible by (?<div>\\d+)");
        private static final Pattern IF_TRUE = Pattern.compile("\\s*If true: throw to monkey (?<to>\\d+)");
        private static final Pattern IF_FALSE = Pattern.compile("\\s*If false: throw to monkey (?<to>\\d+)");

        private final int id;
        private final Queue<BigInteger> worryLevels;
        private final char operation;
        private final String operand;
        private final int divisibleBy;
        private final int throwsToOnTrue;
        private final int throwsToOnFalse;

        private BigInteger inspectedItems;

        private Monkey(final int id, final List<BigInteger> worryLevels, final char operation, final String operand,
               final int divisibleBy, final int throwsToOnTrue, final int throwsToOnFalse) {
            this.id = id;
            this.worryLevels = new ArrayDeque<>(worryLevels);
            this.operation = operation;
            this.operand = operand;
            this.divisibleBy = divisibleBy;
            this.throwsToOnTrue = throwsToOnTrue;
            this.throwsToOnFalse = throwsToOnFalse;
            this.inspectedItems = BigInteger.ZERO;
        }

        private static Monkey parseMonkey(final String group) {
            final String[] groupLines = group.split("\n");
            final int id = Optional.of(MONKEY_ID.matcher(groupLines[0]))
                .map(matcher -> matcher.matches() ? Integer.parseInt(matcher.group("id")) : null)
                .orElseThrow();
            final List<BigInteger> worryLevels = Optional.of(STARTING_ITEMS.matcher(groupLines[1]))
                .map(matcher -> matcher.matches() ?
                    Arrays.stream(matcher.group("items")
                            .split(", "))
                        .map(BigInteger::new).toList() :
                    null)
                .orElseThrow();
            final String[] operationParts = Optional.of(OPERATION.matcher(groupLines[2]))
                .map(matcher -> {
                    if (matcher.matches()) {
                        return new String[] {matcher.group("op"), matcher.group("what")};
                    }
                    return null;
                })
                .orElseThrow();
            final char operation = operationParts[0].charAt(0);
            final String operand = operationParts[1];
            final int divisibleBy = Optional.of(TEST.matcher(groupLines[3]))
                .map(matcher -> matcher.matches() ? Integer.parseInt(matcher.group("div")) : null)
                .orElseThrow();
            final int throwsToOnTrue = Optional.of(IF_TRUE.matcher(groupLines[4]))
                .map(matcher -> matcher.matches() ? Integer.parseInt(matcher.group("to")) : null)
                .orElseThrow();
            final int throwsToOnFalse = Optional.of(IF_FALSE.matcher(groupLines[5]))
                .map(matcher -> matcher.matches() ? Integer.parseInt(matcher.group("to")) : null)
                .orElseThrow();

            return new Monkey(id, worryLevels, operation, operand, divisibleBy, throwsToOnTrue, throwsToOnFalse);
        }

        private void inspect(final Map<Integer, Monkey> monkeys, final BigInteger lcm) {
            final Function<BigInteger, BigInteger> operationFn = old -> switch (operation) {
                case '+' -> old.add(((operand.equals("old") ? old : new BigInteger(operand))));
                case '*' -> old.multiply((operand.equals("old") ? old : new BigInteger(operand)));
                default -> throw new RuntimeException();
            };
            while (!worryLevels.isEmpty()) {
                final BigInteger worryLevel = worryLevels.poll();
                BigInteger newWorryLevel = operationFn.apply(worryLevel);
                newWorryLevel = lcm == null ? newWorryLevel.divide(new BigInteger("3")) : newWorryLevel.mod(lcm);
                final int throwsTo = newWorryLevel.mod(BigInteger.valueOf(divisibleBy)).equals(BigInteger.ZERO) ? throwsToOnTrue : throwsToOnFalse;
                monkeys.get(throwsTo).worryLevels.add(newWorryLevel);
                inspectedItems = inspectedItems.add(BigInteger.ONE);
            }
        }
    }



}
