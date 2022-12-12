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

    public static class Pair<K, V> {
        private final K key;
        private final V value;

        public Pair(final K key, final V value) {
            this.key = key;
            this.value = value;
        }

        public K key() {
            return key;
        }

        public V value() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Pair<?, ?> pair = (Pair<?, ?>) o;
            return key.equals(pair.key) && value.equals(pair.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }

}
