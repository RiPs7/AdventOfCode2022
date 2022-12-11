package com.rips7.day;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day10 extends Day<String> {

    @Override
    public String part1(final List<String> lines) {
        final List<Command> commands = lines.stream().map(Command::parse).toList();
        final Circuit circuit = new Circuit(new CPU(commands), new CRT());

        circuit.start();

        printfln("The sum of the six signal strengths is %s.", String.valueOf(circuit.cpu.signalStrength));

        return String.valueOf(circuit.cpu.signalStrength);
    }

    @Override
    public String part2(final List<String> lines) {
        final List<Command> commands = lines.stream().map(Command::parse).toList();
        final Circuit circuit = new Circuit(new CPU(commands), new CRT());

        circuit.start();

        final String display = circuit.crt.getDisplay();

        System.out.printf("The CRT displays the following:%n%s%n", display);

        return display;
    }

    private static final class Circuit {
        private final CPU cpu;
        private final CRT crt;

        int cycle = 1;

        private Circuit(final CPU cpu, final CRT crt) {
            this.cpu = cpu;
            this.crt = crt;
        }

        private void start() {
            final int programSize = cpu.program.size();
            final int crtSize = crt.pixels.length * crt.pixels[0].length;
            while (cpu.ip < programSize && cycle < crtSize) {
                tick();
            }
        }

        private void tick() {
            if (crt != null) {
                crt.onTick(cycle, cpu.X);
            }
            cycle++;
            cpu.onTick(cycle);
        }

    }

    private static final class CPU {
        final List<Command> program;
        int ip = 0; // instruction pointer

        int X = 1;
        int signalStrength = 0;
        boolean isAdding = false;

        private CPU(final List<Command> commands) {
            program = commands;
        }

        private void onTick(final int cycle) {
            final Command command = program.get(ip);
            switch (command.type) {
                case ADD_X -> {
                    if (isAdding) {
                        X += command.arg;
                        ip++;
                    }
                    isAdding = !isAdding;
                }
                case NOOP -> ip++;
            }

            if ((cycle - 20) % 40 == 0 && cycle <= 220) {
                signalStrength += (cycle * X);
            }
        }
    }

    private static final class CRT {
        boolean[][] pixels = new boolean[6][40];

        private void onTick(final int cycle, final int X) {
            final int row = (cycle - 1) / pixels[0].length;
            final int col = (cycle - 1) % pixels[row].length;

            if (X - 1 == col || X == col || X + 1 == col) {
                pixels[row][col] = true;
            }
        }

        private String getDisplay() {
            return Arrays.stream(pixels)
                .map(pxRow -> IntStream.range(0, pxRow.length)
                    .mapToObj(i -> pxRow[i] ? "#" : ".")
                    .collect(Collectors.joining()))
                .collect(Collectors.joining("\n"));
        }

    }

    private record Command(CommandType type, Integer arg) {
        private static Command parse(final String command) {
            final String[] parts = command.split(" ");
            final CommandType commandType = CommandType.fromType(parts[0]);
            if (commandType == CommandType.NOOP) {
                return new Command(commandType, null);
            } else {
                return new Command(commandType, Integer.parseInt(parts[1]));
            }
        }
    }

    private enum CommandType {
        NOOP("noop"),
        ADD_X("addx");

        private final String type;

        CommandType(final String type) {
            this.type = type;
        }

        private static CommandType fromType(final String type) {
            return Arrays.stream(CommandType.values())
                .filter(cmdType -> cmdType.type.equals(type))
                .findFirst()
                .orElseThrow();
        }
    }

}
