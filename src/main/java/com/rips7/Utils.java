package com.rips7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

    public static List<String> readLines(final String filename) {
        final String resource = Objects.requireNonNull(Utils.class.getClassLoader().getResource(filename)).getPath();
        final Path path = Paths.get(resource);
        try (final Stream<String> stream = Files.lines(path)) {
            return stream.collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Long timeIt(final Runnable task) {
        final long start = System.currentTimeMillis();
        task.run();
        return System.currentTimeMillis() - start;
    }

}
