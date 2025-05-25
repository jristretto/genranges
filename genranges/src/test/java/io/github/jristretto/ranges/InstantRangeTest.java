//cs:ignore
package io.github.jristretto.ranges;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import static java.time.temporal.ChronoUnit.HOURS;

/**
 *
 * @author Pieter van den Hombergh
 */
public class InstantRangeTest extends RangeTestBase<InstantRange, Instant, Duration> {

    RangeTestDataFactory<InstantRange, Instant, Duration> daf;

    static final Instant A = Instant.EPOCH.plus( 12, ChronoUnit.HOURS );

    static Instant[] points = {
        A,
        A.plus( 6, HOURS ),
        A.plus( 10, HOURS ),
        A.plus( 12, HOURS ),
        A.plus( 14, HOURS ),
        A.plus( 16, HOURS )
    };

    @Override
    RangeTestDataFactory<InstantRange, Instant, Duration> helper() {
        if ( null == daf ) {
            daf = new RangeTestDataFactory<>( points ) {

                @Override
                InstantRange createRange( Instant start, Instant end ) {
                    return InstantRange.of( start, end );
                }

                @Override
                Duration distance( Instant a, Instant b ) {
                    return Duration.between( a, b );
                }

            };
        }
        return daf;
    }
}