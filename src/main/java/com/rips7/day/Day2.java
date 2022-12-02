package com.rips7.day;

import java.util.List;

public class Day2 extends Day<Integer> {

    @Override
    public Integer part1(final List<String> lines) {
        final int totalScore = lines.stream().map(line -> {
                final String[] selections = line.split(" ");
                return part1ScoreCalculation(selections[0], selections[1]);
            })
            .reduce(Integer::sum)
            .orElse(0);

        print("Total score is %s.%n", totalScore);

        return totalScore;
    }

    @Override
    public Integer part2(final List<String> lines) {
        final int totalScore = lines.stream().map(line -> {
                final String[] selections = line.split(" ");
                return part2ScoreCalculation(selections[0], selections[1]);
            })
            .reduce(Integer::sum)
            .orElse(0);

        print("Total score is %s.%n", totalScore);

        return totalScore;
    }

    private int part1ScoreCalculation(final String opponentSelection, final String selfSelection) {
        final RockPaperScissors opponent = RockPaperScissors.decryptOpponentSelection(opponentSelection);
        final RockPaperScissors self;
        if (selfSelection.equals("X")) {
            self = RockPaperScissors.ROCK;
        } else if (selfSelection.equals("Y")) {
            self = RockPaperScissors.PAPER;
        } else {
            self = RockPaperScissors.SCISSORS;
        }
        final Boolean outcome = RockPaperScissors.check(self, opponent);
        return calculateRoundScore(self, outcome);
    }

    private int part2ScoreCalculation(final String opponentSelection, final String selfSelection) {
        final RockPaperScissors opponent = RockPaperScissors.decryptOpponentSelection(opponentSelection);
        final Boolean outcome;
        if (selfSelection.equals("X")) {
            outcome = false;
        } else if (selfSelection.equals("Y")) {
            outcome = null;
        } else {
            outcome = true;
        }
        final RockPaperScissors self = RockPaperScissors.fromOutcome(outcome, opponent);
        return calculateRoundScore(self, outcome);
    }

    private int calculateRoundScore(final RockPaperScissors self, final Boolean outcome) {
        final int selfScore = self.equals(RockPaperScissors.ROCK) ? 1 : (self.equals(RockPaperScissors.PAPER) ? 2 : 3);
        final int outcomeScore = outcome == null ? 3 : (outcome ? 6 : 0);
        return selfScore + outcomeScore;
    }

    private enum RockPaperScissors {
        ROCK,
        PAPER,
        SCISSORS;

        private static RockPaperScissors decryptOpponentSelection(final String value) {
            if (value.equals("A") || value.equals("X")) {
                return RockPaperScissors.ROCK;
            } else if (value.equals("B") || value.equals("Y")) {
                return RockPaperScissors.PAPER;
            } else {
                return RockPaperScissors.SCISSORS;
            }
        }

        private static Boolean check(final RockPaperScissors self, final RockPaperScissors opponent) {
            if (self == opponent) {
                return null;
            }
            if (self == ROCK) {
                return opponent != PAPER;
            } else if (self == PAPER) {
                return opponent != SCISSORS;
            } else {
                return opponent != ROCK;
            }
        }

        private static RockPaperScissors fromOutcome(final Boolean outcome, final RockPaperScissors opponent) {
            if (outcome == null) {
                return opponent;
            } else {
                if (opponent == ROCK) {
                    return outcome ? PAPER : SCISSORS;
                } else if (opponent == PAPER) {
                    return outcome ? SCISSORS : ROCK;
                } else {
                    return outcome ? ROCK : PAPER;
                }
            }
        }
    }

}
