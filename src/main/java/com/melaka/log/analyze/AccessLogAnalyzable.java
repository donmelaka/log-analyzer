package com.melaka.log.analyze;

import com.google.common.base.Splitter;
import java.util.stream.Stream;

public interface AccessLogAnalyzable {

    /**
     * Analyze the provided stream of log lines and store results.
     *
     * @param logStream
     */
    void analyze(final Stream<String> logStream);

    /**
     * Returns the results generated from the analyze method. Two parameters below are used to limit the number of
     * results to be returned.
     *
     * @param numberOfTopMostVisitedURLs
     * @param numberOfTopMostActiveIPs
     * @return Result
     */
    Result getResults(final int numberOfTopMostVisitedURLs, final int numberOfTopMostActiveIPs);

    /**
     * Clears the results of the analysis. It is required to invoke this method before starting a new analysis,
     * otherwise results from multiple analyze method calls will be aggregated.
     */
    void clear();

    default Stream<String> split(final String str, final String delimiter) {
        return Splitter.on(delimiter)
                .trimResults()
                .omitEmptyStrings()
                .splitToStream(str);
    }
}
