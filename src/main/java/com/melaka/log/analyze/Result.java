package com.melaka.log.analyze;

import lombok.Value;

import java.util.List;

@Value
public class Result {
    private final long numberOfUniqueIPs;
    private final List<String> mostActiveIPs;
    private final List<String> mostVisitedURLs;
}
