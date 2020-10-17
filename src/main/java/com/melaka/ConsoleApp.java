package com.melaka;

import com.melaka.log.analyze.Result;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

@Slf4j
public class ConsoleApp {

    public static void main(String[] args) {
        final var consoleApp = new ConsoleApp();

        consoleApp.start();
    }

    void start() {
        final Scanner scanner = new Scanner(System.in);
        final App app = new App();
        Result logAnalyzerResult = null;
        String fileName;
        int numberOfTopURLs;
        int numberOfTopIPs;

        log.info("\n" +
                ".____                      _____                .__                             \n" +
                "|    |    ____   ____     /  _  \\   ____ _____  |  | ___.__________ ___________ \n" +
                "|    |   /  _ \\ / ___\\   /  /_\\  \\ /    \\\\__  \\ |  |<   |  \\___   _/ __ \\_  __ \\\n" +
                "|    |__(  <_> / /_/  > /    |    |   |  \\/ __ \\|  |_\\___  |/    /\\  ___/|  | \\/\n" +
                "|_______ \\____/\\___  /  \\____|__  |___|  (____  |____/ ____/_____ \\\\___  |__|   \n" +
                "        \\/    /_____/           \\/     \\/     \\/     \\/          \\/    \\/      ");

        while (true) {
            fileName = captureFileName(scanner);
            numberOfTopURLs = captureTop("Enter the number of top most visited URLs to show: ", scanner);
            numberOfTopIPs = captureTop("Enter the number of top most active IPs to show: ", scanner);

            try {
                logAnalyzerResult = app.analyzeLogFile(fileName, numberOfTopURLs, numberOfTopIPs);
            } catch (IOException e) {
                log.warn("Error while reading the file {}", fileName, e);
            }

            if (logAnalyzerResult != null) {
                log.info("---------------------- Number of distinct IPs: {}",
                        logAnalyzerResult.getNumberOfUniqueIPs());
                log.info("---------------------- Top {} most visited URLs: {}",
                        logAnalyzerResult.getMostVisitedURLs().size(), logAnalyzerResult.getMostVisitedURLs().toString());
                log.info("---------------------- Top {} most active IP addresses: {}",
                        logAnalyzerResult.getMostActiveIPs().size(), logAnalyzerResult.getMostActiveIPs());
            }
            exitOnCheck(scanner);
        }
    }

    String captureFileName(final Scanner scanner) {
        log.info("Enter the absolute path of a log file to analyze: ");
        return scanner.nextLine();
    }

    int captureTop(final String message, final Scanner scanner) {
        boolean validIntFound = false;
        int top = 0;
        while (!validIntFound) {
            validIntFound = true;
            log.info(message);
            try {
                top = scanner.nextInt();
            } catch (InputMismatchException ex) {
                log.warn("Please enter a valid integer");
                validIntFound = false;
            }
        }
        return Math.abs(top);
    }

    void exitOnCheck(final Scanner scanner) {
        log.info("Do you want to exit? (y/N)");
        if ("y".equalsIgnoreCase(scanner.next())) {
            log.info("---------------------- log-analyzer exiting . . . . .");
            System.exit(0);
        }
    }
}
