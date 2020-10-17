package com.melaka;

import com.melaka.log.analyze.AccessLogAnalyzable;
import com.melaka.log.analyze.CommonLogFormatLogAnalyzer;
import com.melaka.log.analyze.Result;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class App {

    Result analyzeLogFile(final String fileName,
                          final int numberOfTopMostVisitedURLs,
                          final int numberOfTopMostActiveIPs) throws IOException {
        final AccessLogAnalyzable logAnalyzer = new CommonLogFormatLogAnalyzer();

        final File f = new File(fileName);
        if (!f.exists() || f.isDirectory()) {
            throw new FileNotFoundException("File not found in the given path");
        }

        try (var logStream = Files.lines(Paths.get(fileName))) {
            logAnalyzer.analyze(logStream);
        }
        return logAnalyzer.getResults(numberOfTopMostVisitedURLs, numberOfTopMostActiveIPs);
    }

}
