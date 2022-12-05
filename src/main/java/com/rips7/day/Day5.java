package com.rips7.day;

import java.util.EmptyStackException;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Day5 extends Day<String> {

    private static final Pattern MOVE_PATTERN = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)");

    @Override
    public String part1(final List<String> lines) {
        final List<Stack<Character>> stacks = readStacks(lines);
        final List<Move> moves = readMoves(lines);

        moves.forEach(move -> {
            final Stack<Character> fromStack = stacks.get(move.from);
            final Stack<Character> toStack = stacks.get(move.to);
            for (int i = 0; i < move.num; i++) {
                try {
                    toStack.push(fromStack.pop());
                } catch (final EmptyStackException ese) {
                    break;
                }
            }
        });

        final StringBuilder sb = new StringBuilder();
        stacks.forEach(stack -> sb.append(stack.peek()));

        final String result = sb.toString();

        printfln("After the rearrangement procedure, the top crates are %s.", result);

        return result;
    }

    @Override
    public String part2(final List<String> lines) {
        final List<Stack<Character>> stacks = readStacks(lines);
        final List<Move> moves = readMoves(lines);

        moves.forEach(move -> {
            final Stack<Character> fromStack = stacks.get(move.from);
            final Stack<Character> toStack = stacks.get(move.to);
            final Stack<Character> temp = new Stack<>();
            for (int i = 0; i < move.num; i++) {
                try {
                    temp.push(fromStack.pop());
                } catch (final EmptyStackException ese) {
                    break;
                }
            }
            while(!temp.isEmpty()) {
                toStack.push(temp.pop());
            }
        });

        final StringBuilder sb = new StringBuilder();
        stacks.forEach(stack -> sb.append(stack.peek()));

        final String result = sb.toString();

        printfln("After the rearrangement procedure, the top crates are %s.", result);

        return result;
    }

    private List<Stack<Character>> readStacks(final List<String> lines) {
        final List<String> stackLines = lines.stream()
            .filter(line -> line.contains("[") && line.contains("]"))
            .toList();

        final int totalStacks = Optional.of(lines.get(stackLines.size()).trim().split("\s+"))
            .map(stacks -> Integer.parseInt(stacks[stacks.length - 1]))
            .orElseThrow(RuntimeException::new);

        final List<Stack<Character>> stacks = IntStream.range(0, totalStacks)
            .mapToObj(i -> new Stack<Character>())
            .toList();

        for (int col = 0; col < totalStacks; col++) {
            final Stack<Character> stack = stacks.get(col);
            final int characterIndex = 1 + 4 * col;
            for (int row = stackLines.size() - 1; row >= 0; row--) {
                final String stackCharacters = stackLines.get(row);
                try {
                    final char item = stackCharacters.charAt(characterIndex);
                    if (item == ' ') {
                        break;
                    }
                    stack.push(item);
                } catch (final StringIndexOutOfBoundsException sioobe) {
                    break;
                }
            }
        }

        return stacks;
    }

    private List<Move> readMoves(final List<String> lines) {
        final List<String> moveLines = lines.stream()
            .filter(line -> line.contains("move"))
            .toList();

        return moveLines.stream().map(line -> {
                final Matcher matcher = MOVE_PATTERN.matcher(line);
                if (!matcher.matches()) {
                    throw new RuntimeException();
                }
                final int total = Integer.parseInt(matcher.group(1));
                final int from = Integer.parseInt(matcher.group(2)) - 1;
                final int to = Integer.parseInt(matcher.group(3)) - 1;
                return new Move(total, from, to);

            })
            .toList();
    }

    private record Move(int num, int from, int to) {

    }


}
