package com.melaka.log.analyze;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
public class CommonLogFormatLogAnalyzer implements AccessLogAnalyzable {

    static final String SPACE = " ";
    static final String QUOTATION = "\"";

    private final Map<String, Integer> ipMap;
    private final Map<String, Integer> urlMap;

    public CommonLogFormatLogAnalyzer() {
        this.ipMap = new HashMap<>();
        this.urlMap = new HashMap<>();
    }

    @Override
    public void analyze(final Stream<String> logStream) {
        if (null == logStream) {
            log.warn("Provided log stream is null");
            return;
        }
        logStream.filter(StringUtils::isNotEmpty)
                .forEach(line -> analyzeLine(line));
    }

    @Override
    public Result getResults(final int numberOfTopMostVisitedURLs, final int numberOfTopMostActiveIPs) {
        return new Result(getNumberOfUniqueIPs(),
                getTopActiveIPs(numberOfTopMostActiveIPs),
                getMostVisitedURLs(numberOfTopMostVisitedURLs));
    }

    @Override
    public void clear() {
        ipMap.clear();
        urlMap.clear();
    }

    void analyzeLine(final String line) {
        final String ip = extractIP(line);
        final String url = extractURL(line);

        if (!StringUtils.isEmpty(ip)) {
            incrementCounter(ipMap, ip);
        }
        if (!StringUtils.isEmpty(url)) {
            incrementCounter(urlMap, url);
        }
    }

    long getNumberOfUniqueIPs() {
        return ipMap.keySet().stream().distinct().count();
    }

    List<String> getTopActiveIPs(final int top) {
        return getTopNFromMap(ipMap, top);
    }

    List<String> getMostVisitedURLs(final int top) {
        return getTopNFromMap(urlMap, top);
    }

    String extractIP(final String line) {
        return split(line, SPACE).findFirst().orElse(null);
    }

    String extractURL(final String line) {
        final String urlSegment = split(line, QUOTATION).skip(1).findFirst().orElse(null);
        if (StringUtils.isEmpty(urlSegment)) {
            return null;
        }
        return split(urlSegment, SPACE).skip(1).findFirst().orElse(null);
    }

    void incrementCounter(final Map<String, Integer> map, final String value) {
        int count = map.containsKey(value) ? map.get(value) : 0;
        map.put(value, ++count);
    }

    List<String> getTopNFromMap(final Map<String, Integer> map, final int top) {
        return map.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(Math.min(top, map.size()))
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());
    }
}
