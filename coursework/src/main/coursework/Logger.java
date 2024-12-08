package main.coursework;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private final PrintWriter logWriter;
    private final DateTimeFormatter dateTimeFormatter;

    public Logger(PrintWriter logWriter) {
        this.logWriter = logWriter;
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    }

    public void log(String message) {
        String timestampedMessage = String.format("[%s] %s",
                LocalDateTime.now().format(dateTimeFormatter),
                message);
        logWriter.println(timestampedMessage);
        System.out.println(timestampedMessage);
    }

    public void close() {
        logWriter.close();
    }
}
