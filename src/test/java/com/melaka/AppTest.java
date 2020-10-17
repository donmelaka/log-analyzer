package com.melaka;

import com.melaka.log.analyze.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest implements TestDataLoadable {

    @Test
    @DisplayName("Test with valid logs")
    void testWithValidLog() throws IOException {

        final var app = new App();
        Result result =
                app.analyzeLogFile(getFilePath("valid-data.log"),
                        3,
                        3);

        assertEquals(11,
                result.getNumberOfUniqueIPs(), "Number of unique IPs must be correct");
        assertEquals("[/docs/manage-websites/, /, /asset.css]",
                result.getMostVisitedURLs().toString(), "Most visited URLs must be correct");
        assertEquals("[168.41.191.40, 177.71.128.21, 50.112.00.11]", result.getMostActiveIPs().toString(),
                "Most active IPs must be correct");
    }

    @Test
    @DisplayName("Test with many repeated log lines")
    void testWithRepeatedLinesLogFile() throws IOException {
        final var app = new App();
        Result result =
                app.analyzeLogFile(getFilePath("repeated-lines.log"),
                        3,
                        3);

        assertEquals(4,
                result.getNumberOfUniqueIPs(), "Number of unique IPs must be correct");
        assertEquals("[/faq/how-to-install/, /intranet-analytics/, /]",
                result.getMostVisitedURLs().toString(), "Most visited URLs must be correct");
        assertEquals("[50.112.00.28, 177.71.128.21, 72.44.32.10]",
                result.getMostActiveIPs().toString(), "Most active IPs must be correct");
    }

    @Test
    @DisplayName("Test with large log file")
    void testWithLargeLogFile() throws IOException {
        final var app = new App();
        Result result =
                app.analyzeLogFile(getFilePath("large-data.log"),
                        3,
                        3);

        assertEquals(11, result.getNumberOfUniqueIPs(), "Number of unique IPs must be correct");
    }

    @Test
    @DisplayName("Test when file not found")
    void testWhenFileNotFound() {
        final var app = new App();
        assertThrows(FileNotFoundException.class,
                () -> app.analyzeLogFile("not-found.log",
                        3,
                        3));
    }
}
