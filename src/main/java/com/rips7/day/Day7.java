package com.rips7.day;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day7 extends Day<Long> {

    @Override
    public Long part1(final List<String> lines) {
        final FileSystem fs = new FileSystem();

        for (int i = 1; i < lines.size(); i++) {
            fs.parse(lines.get(i));
        }

        fs.computeSizeRecursively(fs.root);

        final List<Directory> directories = fs.findAllDirectoriesRecursively(fs.root);

        final long maximumDirectorySize = 100000;

        final long sumOfDirSizesLessThanLimit = directories.stream()
            .filter(directory -> directory.totalSize < maximumDirectorySize)
            .map(directory -> directory.totalSize)
            .reduce(Long::sum)
            .orElse(0L);

        printfln("The sum of the total sizes of directories less than 10000 in size is %s.", sumOfDirSizesLessThanLimit);

        return sumOfDirSizesLessThanLimit;
    }

    @Override
    public Long part2(final List<String> lines) {
        final FileSystem fs = new FileSystem();

        for (int i = 1; i < lines.size(); i++) {
            fs.parse(lines.get(i));
        }

        fs.computeSizeRecursively(fs.root);

        final List<Directory> directories = fs.findAllDirectoriesRecursively(fs.root);

        final long totalSystemSize = 70000000;
        final long totalUsedSize = fs.root.totalSize;
        final long requiredFreeSpace = 30000000;

        final long smallestSufficientSize = directories.stream()
            .filter(directory -> totalSystemSize - (totalUsedSize - directory.totalSize) >= requiredFreeSpace)
            .min(Comparator.comparing(directory -> directory.totalSize))
            .orElseThrow()
            .totalSize;

        printfln("The smallest sufficient directory has a size of %s.", smallestSufficientSize);

        return smallestSufficientSize;
    }

    private static final class FileSystem {
        final Directory root = new Directory(null, "/");

        Directory currentDir = root;

        private void parse(final String command) {
            final String[] parts = command.split(" ");
            if (parts[0].equals("$")) {
                if (parts[1].equals("cd")) {
                    currentDir = currentDir.getSubdirectory(parts[2]);
                }
                // Command "$ ls" is not important
            } else {
                if (parts[0].equals("dir")) {
                    currentDir.addSubdirectory(parts[1]);
                } else {
                    currentDir.addFile(parts[1], Long.parseLong(parts[0]));
                }
            }
        }

        private void computeSizeRecursively(final Directory current) {
            final long filesSize = current.files.stream()
                .map(file -> file.size)
                .reduce(Long::sum)
                .orElse(0L);
            final long subdirectoriesSize = current.subdirectories.stream()
                .peek(this::computeSizeRecursively)
                .map(subdir -> subdir.totalSize)
                .reduce(Long::sum)
                .orElse(0L);
            current.totalSize = filesSize + subdirectoriesSize;
        }

        private List<Directory> findAllDirectoriesRecursively(final Directory current) {
            final List<Directory> directories = new ArrayList<>();
            directories.add(current);
            current.subdirectories.stream().map(this::findAllDirectoriesRecursively).forEach(directories::addAll);
            return directories;
        }
    }

    private static final class Directory extends File {
        private final List<File> files;
        private final List<Directory> subdirectories;
        private long totalSize;

        private Directory(final Directory parent, final String name) {
            super(parent, name);
            files = new ArrayList<>();
            subdirectories = new ArrayList<>();
        }

        private Directory getSubdirectory(final String name) {
            if (name.equals("..")) {
                return parent;
            }
            return subdirectories.stream().filter(dir -> dir.name.equals(name)).findFirst().orElseThrow();
        }

        private void addSubdirectory(final String name) {
            subdirectories.add(new Directory(this, name));
        }

        private void addFile(final String name, final Long size) {
            files.add(new File(this, name, size));
        }
    }

    private static class File {
        protected final Directory parent;
        protected final String name;
        private final Long size;

        private File(final Directory parent, final String name) {
            this(parent, name, null);
        }

        private File(final Directory parent, final String name, final Long size) {
            this.parent = parent;
            this.name = name;
            this.size = size;
        }

        @Override
        public String toString() {
            return parent == null ? "" : parent + "/" + name;
        }

    }

}
