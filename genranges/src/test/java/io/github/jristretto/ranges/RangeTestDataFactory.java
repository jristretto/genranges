package io.github.jristretto.ranges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author Pieter van den Hombergh
 * @param <R> range type
 * @param <P> demarcation type of range
 * @param <D> unit of distance
 */
public abstract class RangeTestDataFactory< R, P, D> {

    protected final P[] points;

    /**
     * Remember the type of the class under test.
     *
     * @param points test values used as a,b,c,d,e, and f in tests
     */
    protected RangeTestDataFactory( P[] points ) {
        this.points = points;
    }

    /**
     * Lookup up a point from the spec.
     *
     * @param key for lookup
     * @return the mapped value.
     */
    protected P lookupPoint( String key ) {
        return points[key.charAt( 0 ) - 'a'];
    }

    /**
     * Create a test range.
     *
     * @param start or range
     * @param end of range
     * @return the range
     */
    abstract R createRange( P start, P end );

    /**
     * Compute the distance between two points.
     *
     * @param a point
     * @param b point
     * @return distance from a to b
     */
    abstract D distance( P a, P b );

    /**
     * Helper method to get from a two char string to list of points. The method
     * uses the lookup map to get a supplier of points that returns the actual
     * points.
     *
     * @param label specifying the point
     * @return the points as list
     */
    List<P> pointList( String label ) {
        List<P> pt2 = new ArrayList<>();
        String[] abcd = label.split( "" );
        for ( String i : abcd ) {
            pt2.add( this.lookupPoint( i ) );
        }
        return pt2;
    }

    /**
     * Helper to create a range from spec.
     *
     * @param spec of range, e.g bc = Range [b,c).
     * @return the range
     */
    R createRange( String spec ) {
        List<P> p1 = pointList( spec );
        return createRange( p1.get( 0 ), p1.get( 1 ) );
    }
    
    
        /**
     * Helper to turn a string into a list of Ranges.
     *
     * @param split split string.
     * @param pairs string to be split
     * @return the list of pairs.
     */
    List<R> restRanges( String split, String pairs ) {
        return Arrays.stream( pairs.split( split ) )
                .map( this::createRange )
                .collect( toList() );
    }

}
