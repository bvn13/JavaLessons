package me.bvn13.logsorter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class LogSorter {

    public static void main(String[] args) {
        int timestampLength;
        try {
            timestampLength = Integer.parseInt(args[0]) - 1;
        } catch (Exception e) {
            System.out.println("java -jar file.jar <TIMESTAMP_LENGTH> <FILE-1> <FILE-2> ... <FILE-N>");
            System.exit(1);
            throw new RuntimeException("Exit");
        }
        final Set<LogEntry> logs = new TreeSet<>();
        int maxLen = 0;
        for (int i = 1; i < args.length; i++) {
            String filename = args[i];
            maxLen = Math.max(maxLen, filename.length());
            File file = new File(filename);
            List<String> lines;
            try {
                lines = Files.readAllLines(file.toPath());
            } catch (IOException e) {
                System.err.println(e.getMessage());
                continue;
            }
            String previousLine = null;
            int lineNum = 1;
            for (String line : lines) {
                if (line.indexOf(" ") != timestampLength) {
                    previousLine = append(previousLine, line);
                } else {
                    if (previousLine != null) {
                        logs.add(new LogEntry(previousLine, filename));
                    }
                    previousLine = putLineNumber(timestampLength, lineNum++, lines.size(), line);
                }
            }
            if (previousLine != null) {
                logs.add(new LogEntry(previousLine, filename));
            }
        }
        String dummy = createDummy(maxLen);
        for (LogEntry entry : logs) {
            String filename = String.format("%s%s", entry.filename, dummy).substring(0, maxLen);
            String lines[] = entry.logMessage.split("\n");
            for (String line : lines) {
                System.out.println(filename + ": " + line);
            }
        }
    }

    private static String createDummy(int length) {
        return " ".repeat(Math.max(0, length));
    }

    private static String putLineNumber(int timestampLength, int lineNumber, int linesCount, String line) {
        if (line.length() <= timestampLength) {
            return line;
        }
        return line.substring(0, timestampLength) + "-" + fillNumber(lineNumber, digitsCount(linesCount)) + line.substring(timestampLength);
    }

    private static String fillNumber(int n, int length) {
        final String s = "0".repeat(length) + n;
        return s.substring(s.length() - length);
    }

    private static int digitsCount(int number) {
        return String.valueOf(number).length();
    }

    private static String append(String a, String b) {
        StringBuilder sb = new StringBuilder();
        if (a != null) {
            sb.append(a).append("\n");
        }
        sb.append(b);
        return sb.toString();
    }

    private static class LogEntry implements Comparable<LogEntry> {
        final String logMessage;
        final String filename;

        public LogEntry(String logMessage, String filename) {
            this.logMessage = logMessage;
            this.filename = filename;
        }

        @Override
        public int compareTo(LogEntry o) {
            return this.logMessage.compareTo(o.logMessage);
        }
    }
}
