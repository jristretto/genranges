package io.github.jristretto.ranges;

import java.util.function.BiFunction;

/**
 * Simple integer based range. This is the first leaf class and it is used to
 * test the default methods in Range.
 *
 *
 * @author Pieter van den Hombergh
 */
public record IntegerRange(Integer start, Integer end) implements
        Range<IntegerRange, Integer, Integer> {
    //TODO implement integerRange constructor that normalizes the inputs.
    //cs:remove:start

    public IntegerRange  {
        if ( start > end ) {
            int t = start;
            start = end;
            end = t;
        }
    }
    //cs:remove:end

    @Override
    public BiFunction<Integer, Integer, Integer> meter() {
        // TODO implement meter function, returning lambda or method ref
        return ( a, b ) -> b - a;//cs:replace:return (a,b)-> 0;
    }

    @Override
    public IntegerRange between(Integer start, Integer end) {
        // TODO implement between factory
        return IntegerRange.of( start, end );//cs:replace:return null;
    }

    @Override
    public String toString() {
        return rangeToString();
    }

    @Override
    public Integer zero() {
        return 0;
    }

    /**
     * ConvenienceFactory.
     *
     * @param start of range
     * @param end of range
     * @return the range
     */
    public static IntegerRange of(Integer start, Integer end) {
        // TODO implement of(Start, End)
        return new IntegerRange( start, end );//cs:replace:return null;
    }

}
