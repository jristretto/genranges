//cs:ignore
package io.github.jristretto.ranges;

import java.time.Duration;
import java.time.Instant;
import java.util.function.BiFunction;

/**
 *
 * @author "Pieter van den Hombergh"
 */
public record InstantRange(Instant start, Instant end) implements
        Range<InstantRange, Instant, Duration> {

    public InstantRange  {
        if ( start.compareTo( end ) > 0 ) {
            Instant temp = end;
            end = start;
            start = temp;
        }
    }

    @Override
    public BiFunction<Instant, Instant, Duration> meter() {
        return ( a, b ) -> Duration.between( a, b );//cs:replace Duration.ZERO;
    }

    @Override
    public InstantRange between( Instant startInclusive, Instant endExclusive ) {
        return InstantRange.of( startInclusive, endExclusive );
    }

    @Override
    public Duration zero() {
        return Duration.ZERO;
    }

    public static InstantRange of( Instant startInclusive, Instant endExclusive ) {
        return new InstantRange( startInclusive, endExclusive );
    }

    @Override
    public String toString() {
        return rangeToString();
    }

}