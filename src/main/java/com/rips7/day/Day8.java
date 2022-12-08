package com.rips7.day;

import java.util.Arrays;
import java.util.List;

public class Day8 extends Day<Integer> {

    @Override
    public Integer part1(final List<String> lines) {
        final int[][] trees = parseTrees(lines);

        final int visibleOuterTrees = 2 * (trees.length + trees[0].length) - 4;
        final int visibleInnerTrees = findVisibleTrees(trees);
        final int visibleTotalTrees = visibleInnerTrees + visibleOuterTrees;

        printfln("There are %s trees visible from outside the grid.", visibleTotalTrees);

        return visibleTotalTrees;
    }

    @Override
    public Integer part2(final List<String> lines) {
        final int[][] trees = parseTrees(lines);

        final int[][] scenicScores = findScenicScores(trees);

        final int bestScenicScore = Arrays.stream(scenicScores).flatMapToInt(Arrays::stream).max().orElse(0);

        printfln("The highest scenic score is %s.", bestScenicScore);

        return bestScenicScore;
    }

    private int[][] parseTrees(final List<String> lines) {
        return lines.stream()
            .map(line -> line.chars()
                .mapToObj(c -> (char) c)
                .map(String::valueOf)
                .mapToInt(Integer::parseInt)
                .toArray())
            .toArray(int[][]::new);
    }

    private int findVisibleTrees(final int[][] trees) {
        int visibleTreesNum = 0;

        for (int row = 1; row < trees.length - 1; row++) {
            for (int col = 1; col < trees[row].length - 1; col++) {

                final int currentTree = trees[row][col];

                boolean isVisibleTop = true;
                for (int up = row - 1; up >= 0; up--) {
                    if (trees[up][col] >= currentTree) {
                        isVisibleTop = false;
                        break;
                    }
                }
                if (isVisibleTop) {
                    visibleTreesNum++;
                    continue;
                }

                boolean isVisibleBottom = true;
                for (int down = row + 1; down < trees.length; down++) {
                    if (trees[down][col] >= currentTree) {
                        isVisibleBottom= false;
                        break;
                    }
                }
                if (isVisibleBottom) {
                    visibleTreesNum++;
                    continue;
                }

                boolean isVisibleLeft = true;
                for (int left = col - 1; left >= 0; left--) {
                    if (trees[row][left] >= currentTree) {
                        isVisibleLeft = false;
                        break;
                    }
                }
                if (isVisibleLeft) {
                    visibleTreesNum++;
                    continue;
                }

                boolean isVisibleRight = true;
                for (int right = col + 1; right < trees[row].length; right++) {
                    if (trees[row][right] >= currentTree) {
                        isVisibleRight = false;
                        break;
                    }
                }
                if (isVisibleRight) {
                    visibleTreesNum++;
                }
            }
        }

        return visibleTreesNum;
    }

    private int[][] findScenicScores(final int[][] trees) {
        final int[][] scenicScores = new int[trees.length][trees[0].length];

        for (int row = 1; row < trees.length - 1; row++) {
            for (int col = 1; col < trees[row].length - 1; col++) {

                final int currentTree = trees[row][col];

                int topDistance = 0;
                for (int up = row - 1; up >= 0; up--) {
                    topDistance++;
                    if (trees[up][col] >= currentTree) {
                        break;
                    }
                }

                int bottomDistance = 0;
                for (int down = row + 1; down < trees.length; down++) {
                    bottomDistance++;
                    if (trees[down][col] >= currentTree) {
                        break;
                    }
                }

                int leftDistance = 0;
                for (int left = col - 1; left >= 0; left--) {
                    leftDistance++;
                    if (trees[row][left] >= currentTree) {
                        break;
                    }
                }

                int rightDistance = 0;
                for (int right = col + 1; right < trees[row].length; right++) {
                    rightDistance++;
                    if (trees[row][right] >= currentTree) {
                        break;
                    }
                }

                scenicScores[row][col] = topDistance * bottomDistance * leftDistance * rightDistance;
            }
        }

        return scenicScores;
    }

}
