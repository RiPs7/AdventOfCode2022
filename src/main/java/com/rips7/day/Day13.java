package com.rips7.day;

import com.rips7.Utils.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day13 extends Day<Integer> {

    @Override
    public Integer part1(final List<String> lines) {
        final List<Pair<Node, Node>> signals = parseSignalPairs(lines);

        final int count = IntStream.range(0, signals.size()).mapToObj(i -> {
                final Pair<Node, Node> currentSignals = signals.get(i);
                final Node left = currentSignals.key();
                final Node right = currentSignals.value();
                if (left.compareTo(right) <= 0) {
                    return i + 1;
                }
                return null;
            })
            .filter(Objects::nonNull)
            .reduce(Integer::sum)
            .orElse(0);

        printfln("There are %s signals in the right order.", count);

        return count;
    }

    @Override
    public Integer part2(final List<String> lines) {
        final List<Node> signals = new ArrayList<>(parseSignals(lines));
        final List<Node> dividerPackets = Stream.of("[[2]]", "[[6]]")
            .map(Parser::new)
            .map(Parser::parse)
            .toList();
        signals.addAll(dividerPackets);
        final List<Node> sortedSignals = signals.stream()
            .sorted()
            .toList();

        int decoderKey = dividerPackets.stream()
            .map(sortedSignals::indexOf)
            .map(i -> i + 1)
            .reduce((a, b) -> a * b)
            .orElse(0);

        printfln("The decode key is %s.", decoderKey);

        return decoderKey;
    }

    private static List<Pair<Node, Node>> parseSignalPairs(final List<String> input) {
        return Arrays.stream(String.join("\n", input).split("\n\n"))
            .map(group -> group.split("\n"))
            .map(parts -> new Pair<>(
                new Parser(parts[0]).parse(),
                new Parser(parts[1]).parse()))
            .toList();
    }

    private static List<Node> parseSignals(final List<String> input) {
        return input.stream().filter(line -> !line.isBlank()).map(line -> new Parser(line).parse()).toList();
    }

    private static final class Parser {
        private final Tokenizer tokenizer;

        public Parser(final String input) {
            this.tokenizer = new Tokenizer(input);
        }

        private Node parse() {
            final Token currentToken = tokenizer.getToken();
            switch (currentToken.type) {
                case NUMBER -> {
                    return new NumberNode(Integer.parseInt(currentToken.value));
                }
                case LEFT_PARENTHESIS -> {
                    return new ListNode(parseList());
                }
                default -> throw new RuntimeException("");
            }
        }

        private List<Node> parseList() {
            List<Node> nodes = new ArrayList<>();
            tokenizer.nextToken();
            final Token token = tokenizer.getToken();
            if (token.type != TokenType.RIGHT_PARENTHESIS) {
                nodes.add(parse());
                tokenizer.nextToken();
                while (tokenizer.getToken().type == TokenType.COMMA) {
                    tokenizer.nextToken();
                    nodes.add(parse());
                    tokenizer.nextToken();
                }
            }
            return nodes;
        }
    }

    private static final class Tokenizer {
        private final List<Token> tokens;
        private final AtomicInteger tokenIndex;

        private Tokenizer(final String input) {
            tokens = new ArrayList<>();
            for (int i = 0; i < input.length(); i++) {
                final char c = input.charAt(i);
                switch (c) {
                    case '[' -> {
                        tokens.add(new Token("[", TokenType.LEFT_PARENTHESIS));
                        continue;
                    }
                    case ']' -> {
                        tokens.add(new Token("]", TokenType.RIGHT_PARENTHESIS));
                        continue;
                    }
                    case ',' -> {
                        tokens.add(new Token(",", TokenType.COMMA));
                        continue;
                    }
                }
                final StringBuilder numberBuilder = new StringBuilder(String.valueOf(c));
                for (int j = i + 1; Character.isDigit(input.charAt(j)); j++) {
                    numberBuilder.append(input.charAt(j));
                    i++;
                }
                tokens.add(new Token(numberBuilder.toString(), TokenType.NUMBER));
            }
            tokens.add(new Token("", TokenType.END));
            tokenIndex = new AtomicInteger();
        }

        private void nextToken() {
            tokenIndex.incrementAndGet();
        }

        private Token getToken() {
            return tokens.get(tokenIndex.get());
        }

        @Override
        public String toString() {
            return String.format("{tokenIndex=%s,currentToken=%s}", tokenIndex, getToken());
        }
    }

    private record Token(String value, TokenType type) { }

    private enum TokenType {
        LEFT_PARENTHESIS,
        RIGHT_PARENTHESIS,
        COMMA,
        NUMBER,
        END
    }

    private static final class NumberNode extends Node {

        private final int value;

        public NumberNode(final int value) {
            this.value = value;
        }

        @Override
        public int compareTo(final Node node) {
            if (node instanceof final NumberNode other) {
                return Integer.compare(this.value, other.value);
            } else if (node instanceof final ListNode other) {
                return new ListNode(this).compareTo(other);
            }
            throw new RuntimeException("");
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    private static final class ListNode extends Node {

        private final List<Node> list;

        private ListNode(final List<Node> list) {
            this.list = list;
        }

        private ListNode(final NumberNode numberNode) {
            this(new ArrayList<>());
            list.add(numberNode);
        }

        @Override
        public int compareTo(final Node node) {
            if (node instanceof final NumberNode other) {
                return this.compareTo(new ListNode(other));
            } else if (node instanceof final ListNode other) {
                if (other.list.isEmpty()) {
                    return 1;
                }
                if (this.list.isEmpty()) {
                    return -1;
                }
                for (int i = 0; i < Math.min(this.list.size(), other.list.size()); i++) {
                    final int res;
                    if ((this.list.get(i) instanceof final ListNode thisNode) &&
                        (other.list.get(i) instanceof final ListNode otherNode)) {
                        res = thisNode.compareTo(otherNode);
                    } else if ((this.list.get(i) instanceof final ListNode thisNode) &&
                        (other.list.get(i) instanceof final NumberNode otherNode)) {
                        res = thisNode.compareTo(new ListNode(otherNode));
                    } else if ((this.list.get(i) instanceof final NumberNode thisNode) &&
                        (other.list.get(i) instanceof final ListNode otherNode)) {
                        res = (new ListNode(thisNode)).compareTo(otherNode);
                    } else if ((this.list.get(i) instanceof final NumberNode thisNode) &&
                        (other.list.get(i) instanceof final NumberNode otherNode)) {
                        res = thisNode.compareTo(otherNode);
                    } else {
                        throw new RuntimeException("");
                    }
                    if (res != 0) {
                        return res;
                    }
                }
                return Integer.compare(this.list.size(), other.list.size());
            }
            throw new RuntimeException("");
        }

        @Override
        public String toString() {
            return "[" + list.stream().map(Objects::toString).collect(Collectors.joining(",")) + "]";
        }
    }

    private static abstract class Node implements Comparable<Node> { }

}
