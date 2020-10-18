package com.melaka.log.analyze;

import com.melaka.TestDataLoadable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CommonLogFormatLogAnalyzerTest implements TestDataLoadable {

    @Test
    @DisplayName("Text extractIp method")
    void testExtractIP() {
        final var logAnalyzer = new CommonLogFormatLogAnalyzer();

        final List<String> ips = Stream
                .of("68.75.151.0 - - [14/Oct/2020:04:34:22 +0000] \"PATCH /customers HTTP/1.1\" 404 3745",
                        "78.79.160.90 - - [14/Oct/2020:04:34:22 +0000] \"PATCH /customers HTTP/1.1\" 404 3745 extra stuff ",
                        "211.191.40.254 - Jhon [14/Oct/2020:04:34:22 +0000] \"PATCH /customers HTTP/1.1\" 404 3745",
                        "",
                        "194.248.98.236 - Jhon [14/Oct/2020:04:34:22 +0000] \"PATCH /customers HTTP/1.1\" 404 3745 " +
                                "\"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_7) AppleWebKit/534.24 " +
                                "(KHTML, like Gecko) RockMelt/0.9.58.494 Chrome/11.0.696.71 Safari/534.24\""
                )
                .map(logAnalyzer::extractIP)
                .collect(Collectors.toList());

        assertEquals("68.75.151.0", ips.get(0), "Extact ");
        assertEquals("78.79.160.90", ips.get(1));
        assertEquals("211.191.40.254", ips.get(2));
        assertNull(ips.get(3));
        assertEquals("194.248.98.236", ips.get(4));
    }

    @Test
    @DisplayName("Text testExtractURL method")
    void testExtractURL() {
        final var logAnalyzer = new CommonLogFormatLogAnalyzer();

        final List<String> urls = Stream
                .of("68.75.151.0 - - [14/Oct/2020:04:34:22 +0000] \"PATCH /customers HTTP/1.1\" 404 3745",
                        "78.79.160.90 - - [14/Oct/2020:04:34:22 +0000] \"PATCH /customers/2345 HTTP/1.1\" 404 3745 extra stuff ",
                        "211.191.40.254 - Jhon [14/Oct/2020:04:34:22 +0000] \"PATCH http://example.net/faq/ HTTP/1.1\" 404 3745",
                        "211.191.40.254 - Jhon [14/Oct/2020:04:34:22 +0000] \"PATCH /asset.js HTTP/1.1\" 404 3745",
                        "",
                        "194.248.98.236 - Jhon [14/Oct/2020:04:34:22 +0000] \"PATCH / HTTP/1.1\" 404 3745 " +
                                "\"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_7) AppleWebKit/534.24 " +
                                "(KHTML, like Gecko) RockMelt/0.9.58.494 Chrome/11.0.696.71 Safari/534.24\""
                )
                .map(logAnalyzer::extractURL)
                .collect(Collectors.toList());

        assertEquals("/customers", urls.get(0));
        assertEquals("/customers/2345", urls.get(1));
        assertEquals("http://example.net/faq/", urls.get(2));
        assertEquals("/asset.js", urls.get(3));
        assertNull(urls.get(4));
        assertEquals("/", urls.get(5));
    }

    @Test
    @DisplayName("Test analyzing 2 files together")
    void testAnalyze2FileTogether() {
        final var logAnalyzer = new CommonLogFormatLogAnalyzer();

        logAnalyzer.analyze(Stream.of("211.191.40.254 - - [14/Oct/2020:04:34:24 +0000] \"POST /lists HTTP/1.1\" 400 2908",
                "154.23.174.24 - - [14/Oct/2020:04:34:24 +0000] \"DELETE /parsers HTTP/1.1\" 400 3231",
                "252.123.145.254 - - [14/Oct/2020:04:34:24 +0000] \"PATCH /users HTTP/1.1\" 403 2655",
                "45.212.24.66 - - [14/Oct/2020:04:34:24 +0000] \"PATCH /collectors HTTP/1.1\" 201 4458",
                "140.224.76.136 - Admin [14/Oct/2020:04:34:24 +0000] \"POST /collectors HTTP/1.1\" 503 3821",
                "172.201.109.60 - - [14/Oct/2020:04:34:24 +0000] \"GET /lists HTTP/1.1\" 503 4703",
                "78.79.160.90 - - [14/Oct/2020:04:34:24 +0000] \"GET /events HTTP/1.1\" 503 4166",
                "185.10.182.215 - - [14/Oct/2020:04:34:25 +0000] \"GET /lists HTTP/1.1\" 301 2597",
                "205.68.87.36 - - [14/Oct/2020:04:34:25 +0000] \"PATCH /fieldsets HTTP/1.1\" 204 2714",
                "47.43.184.217 - - [14/Oct/2020:04:34:25 +0000] \"POST /fieldsets HTTP/1.1\" 401 2151",
                "122.104.57.92 - - [14/Oct/2020:04:34:25 +0000] \"PUT /fieldsets HTTP/1.1\" 403 2476"));

        logAnalyzer.analyze(Stream.of("7.158.224.225 - - [14/Oct/2020:04:34:33 +0000] \"PATCH /fieldsets HTTP/1.1\" 401 4737",
                "189.179.244.218 - - [14/Oct/2020:04:34:33 +0000] \"GET /playbooks HTTP/1.1\" 503 2823",
                "39.18.40.179 - - [14/Oct/2020:04:34:33 +0000] \"POST /fieldsets HTTP/1.1\" 300 2622",
                "101.181.160.107 - - [14/Oct/2020:04:34:33 +0000] \"GET /fieldsets HTTP/1.1\" 500 3618",
                "99.63.144.218 - - [14/Oct/2020:04:34:33 +0000] \"PATCH /playbooks HTTP/1.1\" 503 3289",
                "205.188.45.201 - - [14/Oct/2020:04:34:33 +0000] \"POST /customers HTTP/1.1\" 403 3279",
                "10.38.37.77 - - [14/Oct/2020:04:34:33 +0000] \"PATCH /events HTTP/1.1\" 301 3878",
                "241.231.166.74 - - [14/Oct/2020:04:37:24 +0000] \"DELETE /customers HTTP/1.1\" 500 538370362",
                "10.114.116.32 - SuperAdmin [14/Oct/2020:04:37:24 +0000] \"PATCH /parsers HTTP/1.1\" 401 617443706",
                "38.108.230.98 - - [14/Oct/2020:04:37:24 +0000] \"PUT /alerts HTTP/1.1\" 204 1412027232",
                "111.68.238.138 - - [14/Oct/2020:04:37:24 +0000] \"DELETE /users HTTP/1.1\" 300 535307599"));

        final Result result = logAnalyzer.getResults(5, 5);

        assertEquals(22, result.getNumberOfUniqueIPs());
    }

    @Test
    @DisplayName("Test analyzing a second file after cleaning")
    void testAnalyzingSecondFileAfterCleaning() {
        final var logAnalyzer = new CommonLogFormatLogAnalyzer();

        logAnalyzer.analyze(Stream.of("211.191.40.254 - - [14/Oct/2020:04:34:24 +0000] \"POST /lists HTTP/1.1\" 400 2908",
                "154.23.174.24 - - [14/Oct/2020:04:34:24 +0000] \"DELETE /parsers HTTP/1.1\" 400 3231",
                "252.123.145.254 - - [14/Oct/2020:04:34:24 +0000] \"PATCH /users HTTP/1.1\" 403 2655",
                "45.212.24.66 - - [14/Oct/2020:04:34:24 +0000] \"PATCH /collectors HTTP/1.1\" 201 4458",
                "140.224.76.136 - Alan [14/Oct/2020:04:34:24 +0000] \"POST /collectors HTTP/1.1\" 503 3821",
                "172.201.109.60 - - [14/Oct/2020:04:34:24 +0000] \"GET /lists HTTP/1.1\" 503 4703",
                "78.79.160.90 - - [14/Oct/2020:04:34:24 +0000] \"GET /events HTTP/1.1\" 503 4166",
                "185.10.182.215 - - [14/Oct/2020:04:34:25 +0000] \"GET /lists HTTP/1.1\" 301 2597",
                "205.68.87.36 - - [14/Oct/2020:04:34:25 +0000] \"PATCH /fieldsets HTTP/1.1\" 204 2714",
                "47.43.184.217 - - [14/Oct/2020:04:34:25 +0000] \"POST /fieldsets HTTP/1.1\" 401 2151",
                "122.104.57.92 - - [14/Oct/2020:04:34:25 +0000] \"PUT /fieldsets HTTP/1.1\" 403 2476"));

        logAnalyzer.clear();

        logAnalyzer.analyze(Stream.of("7.158.224.225 - - [14/Oct/2020:04:34:33 +0000] \"PATCH /fieldsets HTTP/1.1\" 401 4737",
                "189.179.244.218 - - [14/Oct/2020:04:34:33 +0000] \"GET /playbooks HTTP/1.1\" 503 2823",
                "39.18.40.179 - - [14/Oct/2020:04:34:33 +0000] \"POST /fieldsets HTTP/1.1\" 300 2622",
                "101.181.160.107 - - [14/Oct/2020:04:34:33 +0000] \"GET /fieldsets HTTP/1.1\" 500 3618",
                "99.63.144.218 - - [14/Oct/2020:04:34:33 +0000] \"PATCH /playbooks HTTP/1.1\" 503 3289",
                "205.188.45.201 - - [14/Oct/2020:04:34:33 +0000] \"POST /customers HTTP/1.1\" 403 3279",
                "10.38.37.77 - - [14/Oct/2020:04:34:33 +0000] \"PATCH /events HTTP/1.1\" 301 3878",
                "241.231.166.74 - - [14/Oct/2020:04:37:24 +0000] \"DELETE /customers HTTP/1.1\" 500 538370362",
                "10.114.116.32 - Smith [14/Oct/2020:04:37:24 +0000] \"PATCH /parsers HTTP/1.1\" 401 617443706",
                "38.108.230.98 - - [14/Oct/2020:04:37:24 +0000] \"PUT /alerts HTTP/1.1\" 204 1412027232",
                "111.68.238.138 - - [14/Oct/2020:04:37:24 +0000] \"DELETE /users HTTP/1.1\" 300 535307599"));

        final Result result = logAnalyzer.getResults(5, 5);

        assertEquals(11, result.getNumberOfUniqueIPs());
    }

    @Test
    @DisplayName("Test with null stream")
    void testWithNullStream() {
        final var logAnalyzer = new CommonLogFormatLogAnalyzer();

        assertDoesNotThrow(() -> logAnalyzer.analyze(null));

        final Result results = logAnalyzer.getResults(5, 5);

        assertEquals(0, results.getNumberOfUniqueIPs());
        assertEquals("[]", results.getMostVisitedURLs().toString());
        assertEquals("[]", results.getMostActiveIPs().toString());
    }

    @Test
    @DisplayName("Test analyzeLine with empty line")
    void testAnalyzeLineWithEmptyIP() {
        final var logAnalyzer = new CommonLogFormatLogAnalyzer();

        logAnalyzer.analyzeLine("");

        final Result result = logAnalyzer.getResults(1, 1);

        assertEquals(0, result.getNumberOfUniqueIPs());
        assertEquals("[]", result.getMostVisitedURLs().toString());
        assertEquals("[]", result.getMostActiveIPs().toString());
    }

}