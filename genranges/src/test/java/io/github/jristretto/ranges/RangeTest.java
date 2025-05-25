package io.github.jristretto.ranges;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assumptions.assumeThat;

/**
 * Tests the Range interface via leaf class IntegerRange.
 *
 * @author Pieter van den Hombergh
 */
@TestMethodOrder( MethodOrderer.MethodName.class )
class RangeTest {

    // use as            a,  b,  c,  d,    e,    f
    Integer[] points = {42, 51, 55, 1023, 1610, 2840};
    RangeTestDataFactory<IntegerRange, Integer, Integer> dataFactory;

    RangeTestDataFactory<IntegerRange, Integer, Integer> helper() {
        if ( null == dataFactory) {
            dataFactory = new RangeTestDataFactory<>(points) {

                @Override
                IntegerRange createRange( Integer start, Integer end ) {
                    return IntegerRange.of( start, end );
                }

                @Override
                Integer distance( Integer a, Integer b ) {
                    return b - a;
                }

            };

        }
        return dataFactory;
    }

    /**
     * Create a range from the range specifier using helper.
     *
     * @param spec range specifier
     * @return a range
     */
    IntegerRange createRange( String spec ) {
        return helper().createRange( spec );
    }

    /**
     * Create range using helper.
     *
     * @param p1 first point
     * @param p2 second point
     * @return range between p1 and p2
     */
    IntegerRange createRange( Integer p1, Integer p2 ) {
        return helper().createRange( p1, p2 );
    }

    /**
     * Helper to shorten writing the tests.
     * Looks up the point as a String, e.g. "a" will return 42
     *
     * @param key the point to lookup
     * @return the point value
     */
    Integer lookupPoint( String key ) {
        return helper().lookupPoint( key );
    }

    /**
     * Helper to compute distance.
     *
     * @param a name of start point
     * @param b name of end point
     * @return integer distance
     */
    Integer distance( Integer a, Integer b ) {
        return helper().distance( a, b );
    }

    /**
     * Test the default max function in Range.
     *
     * @param pName1 name of the first point
     * @param pName2 name of the second point
     * @param expectedPName name of expected point
     */
//    @DisplayName("find max")
    @ParameterizedTest
    @CsvSource({
        "a,b,b",
        "c,b,c",
        "a,a,a"
    })
    public void t01Max( String pName1, String pName2, String expectedPName ) {
        //TODO write assert
        Integer a = lookupPoint( pName1 );
        Integer b = lookupPoint( pName2 );
        Integer expected = lookupPoint( expectedPName ); // the map will return the same instance

        assertThat( Range.max( a, b ) ).isSameAs( expected );//cs:replace:fail("Implement test");
    }

    /**
     * Test the default min function in Range.
     *
     * @param pName1 name of the first point
     * @param pName2 name of the second point
     * @param expectedPName name of expected point
     */
    @ParameterizedTest
    @CsvSource( {
        "a,b,a",
        "c,b,b",
        "a,a,a"
    } )
    public void t02Min( String pName1, String pName2, String expectedPName ) {
        //TODO implement test
        //cs:remove:start
        Integer a = lookupPoint( pName1 );
        Integer b = lookupPoint( pName2 );
        Integer expected = lookupPoint( expectedPName );

        assertThat( Range.min( a, b ) ).isSameAs( expected );
        //cs:remove:end
        //cs:add:fail("Implement test");
    }

    /**
     * Test the default minmax function in Range.
     *
     * @param pName1 name of first point
     * @param pName2 name of second point
     * @param expPMin expected minimum point
     * @param expPMax expected maximum point
     */
    @ParameterizedTest
    @CsvSource( {
        "a,a,a,a",
        "a,b,a,b",
        "d,c,c,d",} )
    public void t03MinmaxTest( String pName1, String pName2, String expPMin,
            String expPMax ) {
        Integer point1 = lookupPoint( pName1 );
        Integer point2 = lookupPoint( pName2 );
        Integer expMin = lookupPoint( expPMin );
        Integer expMax = lookupPoint( expPMax );
        Integer[] t = Range.minmax( point1, point2 );
        SoftAssertions.assertSoftly( softly -> {
            //TODO write assertions
            //cs:remove:start
            softly.assertThat( t[0] ).isSameAs( expMin );
            softly.assertThat( t[1] ).isSameAs( expMax );
            //cs:remove:end
        } );
        //cs:add:fail("Implement test");
    }

    /**
     * Test the default meets method of Range
     *
     * @param r1StartName specifies a
     * @param r1EndName specifies b
     * @param r2StartName specifies c
     * @param r2EndName specifies d
     * @param expected outcome
     */
    @ParameterizedTest
    @CsvSource( {
        "a,b,c,d,false",
        "c,d,a,b,false",
        "a,b,b,d,true",
        "c,d,a,c,true",} )
    public void t04Meets( String r1StartName, String r1EndName,
                          String r2StartName, String r2EndName,
                          boolean expected ) {
        Integer r1Start = lookupPoint( r1StartName );
        Integer r1End = lookupPoint( r1EndName );
        Integer r2Start = lookupPoint( r2StartName );
        Integer r2End = lookupPoint( r2EndName );
        // Make sure to implement IntegerRange.of
        // TODO create ranges and test meets method
        //cs:remove:start
        IntegerRange r1 = createRange( r1Start, r1End );
        IntegerRange r2 = createRange( r2Start, r2End );
        assertThat( r1.meets( r2 ) ).isEqualTo( expected );
        //cs:remove:end
        //cs:add:fail("Implement test");
    }

    /**
     * Test the default between method of Range.
     * Nothing to do here.
     */
    @Test
    public void t05CreateBetween() {
        Integer a = lookupPoint( "a" );
        Integer b = lookupPoint( "b" );
        Integer c = lookupPoint( "c" );
        // helper is needed to get access to the between method.
        IntegerRange helper = createRange( c, c );
        IntegerRange rt = helper.between( a, b );
        assertThat( rt )
                .extracting( "start", "end" )
                .containsExactly( a, b );
    }

    /**
     * Test Range#rangeHashCode and Range#rangeEquals implicitly through concrete IntegerRange.
     * Nothing to do here.
     */
    @Test
    public void t06HashCodeEquals() {
        Integer a = lookupPoint( "a" );
        Integer b = lookupPoint( "b" );
        Integer c = lookupPoint( "c" );

        IntegerRange ref = createRange( a, b );
        IntegerRange equ = createRange( a, b );
        IntegerRange diffB = createRange( a, c );
        IntegerRange diffC = createRange( c, b );

        TestUtils.verifyEqualsAndHashCode( ref, equ, diffB, diffC );
    }

    /**
     * Test default length method of range.
     */
    @Test
    public void t07Length() {
        // TODO test length with distance function
        //cs:remove:start
        Integer a = lookupPoint( "a" );
        Integer b = lookupPoint( "b" );
        IntegerRange r = createRange( b, a );
        assertThat( r.length() ).isEqualTo( distance( a, b ) );
        //cs:remove:end
        //cs:add:fail("Implement test");
    }

    /**
     * Test the default overlaps method of Range.
     * Add more test values.
     *
     * @param rangeSpec1 specifier for first range
     * @param rangeSpec2 specifier for second range
     * @param expected if overlap expected
     */
    @ParameterizedTest
    @CsvSource({
        "ab,cd,false", // disjunct
        "ac,cd,false", // meet
        "ac,bd,true", //  B < C < D
        //cs:remove:start
        "ac,bc,true", // points < B < C
        "ab,bd,false", // end misses B
        "bc,bd,true", //
        "bd,bc,true", //
        "bd,bd,true", // to same ranges
        //cs:remove:end
    })
    void t08Overlaps( String rangeSpec1, String rangeSpec2, boolean expected ) {
        // TODO: add more test values to improve coverage + write asserts
        IntegerRange range1 = createRange( rangeSpec1 );
        IntegerRange range2 = createRange( rangeSpec2 );
        //cs:remove:start
        assertThat( range1.overlaps( range2 ) )
                .as( rangeSpec1 + "+" + rangeSpec2 )
                .isEqualTo( expected );
        //cs:remove:end
        //cs:add:fail("Implement test");
    }

    /**
     * Test the default overlap method of Range
     *
     * @param rangeSpec1 specifier for first range
     * @param rangeSpec2 specifier for first range
     * @param expectedRangeSpec specifier for expected range
     */
    @ParameterizedTest
    @CsvSource({
        // first, second, distance  points
        "ab,cd,aa", // disjunct
        "ab,bc,bb", // disjunct
        "ac,bd,bc", //  B < C < Integer
        //cs:remove:start
        "ac,bc,bc", // points < B < C
        "bc,bd,bc", //
        "bc,bc,bc", // two same ranges
        //cs:remove:end
    })
    void t09OverLap( String rangeSpec1, String rangeSpec2, String expectedRangeSpec ) {
        // TODO  add test values for coverage and test overlap method
        //cs:remove:start
        IntegerRange r1 = createRange( rangeSpec1 );
        IntegerRange r2 = createRange( rangeSpec2 );
        IntegerRange r3 = createRange( expectedRangeSpec );
        var expectedOverlap = r3.length();
        var actual = r1.overlap( r2 );
        assertThat( actual ).as( rangeSpec1 + "+" + rangeSpec2 ).isEqualTo( expectedOverlap );
        //cs:remove:end
        //cs:add:fail("Implement test");
    }

    /**
     * Assert that the range constructor puts start and end in the proper order.
     * E.g. IntegerRange(5,4) -> start: 4 and end: 5
     * Assert that end of range is less or equal to start, using compareTo.
     */
    @Test
    void t10Normalizes() {
        // TODO test that start and end are in natural order
        //cs:remove:start
        Integer a = lookupPoint( "a" );
        Integer b = lookupPoint( "b" );
        IntegerRange range = createRange( b, a );
        assertThat( range.end().compareTo( range.start() ) ).isGreaterThanOrEqualTo(0 );
        //cs:remove:end
        //cs:add:fail("Implement test");

    }

    /**
     * Test the default contain method from Range
     *
     * @param rangeSpec range specifier
     * @param pName name of point to check
     * @param expected if point is in range
     */
    @ParameterizedTest
    @CsvSource({
        "bc,a,false",
        "bc,d,false",
        //cs:remove:start
        "bc,a,false",
        "bd,c,true",
        "bc,c,false",
        "bc,b,true",
        //cs:remove:end
    })
    void t11ContainsPoint( String rangeSpec, String pName, boolean expected ) {
        // TODO add more test values to improve coverage ad test contains point method
        //cs:remove:start
        IntegerRange r = createRange( rangeSpec );
        Integer p = lookupPoint( pName );
        assumeThat( p ).isNotNull();
        assertThat( r.contains( p ) )
                .as( rangeSpec + " contains " + pName + " " + expected )
                .isEqualTo( expected );
        //cs:remove:end
        //cs:add:fail("Implement test");
    }

    /**
     * Test the toString method of IntegerRange.
     * Should be in the form of "[start, end)".
     */
    @Test
    void t12ToStringTest() {
        //TODO test tostring
        //cs:remove:start
        Integer a = lookupPoint( "a" );
        Integer b = lookupPoint( "b" );
        IntegerRange r1 = createRange( a, b );
        assertThat( r1.toString() ).contains( a.toString(), b.toString(), "[", ")" );
        //cs:remove:end
        //cs:add:fail("Implement test");
    }

    /**
     * Test that default method checkMeetsOrOverlaps of Range does NOT throw exception
     * if ranges meet or overlap
     *
     * @param rSpec1 specifier for first range
     * @param rSpec2 specifier for second range
     */
    @ParameterizedTest
    @CsvSource( {
        "ab,bc",
        "ac,bd"
    } )
    void t13aCheckMeetsOrOverlaps( String rSpec1, String rSpec2) {
        IntegerRange range1 = createRange( rSpec1 );
        IntegerRange range2 = createRange( rSpec2 );
        // TODO: assert that no exception is thrown
        //cs:remove:start
        ThrowableAssert.ThrowingCallable code = () -> {
            range1.checkMeetsOrOverlaps( range2 );
        };

        assertThatCode( code ).doesNotThrowAnyException();
        //cs:remove:end
        //cs:add:fail("Implement test");
    }

    /**
     * Test that default method checkMeetsOrOverlaps of Range DOES throw exception
     * if ranges do NOT meet NOR overlap
     *
     * @param rSpec1 specifier for first range
     * @param rSpec2 specifier for second range
     */
    @ParameterizedTest
    @CsvSource( {
        "ab,cd"
    } )
    void t13bCheckMeetsOrOverlaps( String rSpec1, String rSpec2) {
        IntegerRange range1 = createRange( rSpec1 );
        IntegerRange range2 = createRange( rSpec2 );
        // TODO: assert that (correct) exception is thrown
        //cs:remove:start
        ThrowableAssert.ThrowingCallable code = () -> {
            range1.checkMeetsOrOverlaps( range2 );
        };

        assertThatThrownBy( code ).isInstanceOf( IllegalArgumentException.class );
        //cs:remove:end
        //cs:add:fail("Implement test");
    }

    /**
     * Test default method joinWith of Range.
     * Test cases where a join is possible, so no exception expected
     *
     * @param rSpec1 specifier for first range
     * @param rSpec2 specifier for second range
     * @param expectedRSpec specifier for expected range
     */
    @ParameterizedTest
    @CsvSource( {
        "ab,bc,ac",
        "ac,bd,ad",
        "ad,bc,ad"
    } )
    void t14JoinWith( String rSpec1, String rSpec2, String expectedRSpec ) {
        // TODO test joinWith method
        //cs:remove:start
        IntegerRange r1 = createRange( rSpec1 );
        IntegerRange r2 = createRange( rSpec2 );
        IntegerRange expected = createRange( expectedRSpec );
        assertThat( r2.joinWith( r1 ) ).isEqualTo( expected );
        //cs:remove:end
        //cs:add:fail("Implement test");
    }

    /**
     * Test default method intersectWith of Range, in case an intersection is possible
     * All test cases should produce a non-empty intersection.
     *
     * @param range1Spec first range specification
     * @param range2Spec second range specification
     * @param expectedSpec intersection specification
     */
    @ParameterizedTest
    @CsvSource({
        "ac,bd,bc",
        //cs:remove:start
        "ac,bc,bc",
        "bc,ad,bc",
        "ad,bc,bc",
        //cs:remove:end
    })
    void t15aCommonRangeSuccess( String range1Spec, String range2Spec, String expectedSpec ) {
        // TODO: assert ranges that intersect works and add more test cases
        IntegerRange range = createRange( range1Spec );
        IntegerRange intersection = createRange( range2Spec );
        //cs:remove:start
        Optional<IntegerRange> result = range.intersectWith( intersection );
        IntegerRange expected = createRange( expectedSpec );
        Assertions.assertThat( result ).isNotEmpty().get().isEqualTo( expected );
        //cs:remove:end
        //cs:add:fail("Implement test");
    }

    /**
     * Test default method intersectWith of Range, in case an intersection is NOT possible
     * All test cases should produce an empty intersection.
     *
     * @param range1Spec range specification
     * @param range2Spec cutter range spec
     */
    @ParameterizedTest
    @CsvSource( value = {
        "ab,cd,",
        //cs:remove:start
        "ab,bc,",
        "cd,ab,"
        //cs:remove:end
    } )
    void t15bCommonRangeEmpty( String range1Spec, String range2Spec) {
        // TODO: assert that intersection between the two ranges is empty and add more test cases
        IntegerRange range = createRange( range1Spec );
        IntegerRange intersection = createRange( range2Spec );
        //cs:remove:start
        Optional<IntegerRange> result = range.intersectWith( intersection );
        Assertions.assertThat( result ).isEmpty();
        //cs:remove:end
        //cs:add:fail("Implement test");
    }

    /**
     * Test default method contains of Range
     *
     * @param rangeSpec1 specifier for first range
     * @param rangeSpec2 specifier for second range
     * @param expected if second range is contained in first range
     */
    @ParameterizedTest
    @CsvSource({
        "ab,cd,false", // disjunct
        //cs:remove:start
        "ab,bc,false", // meets
        "ac,bd,false",// overlap, but not contains
        "bd,ac,false",// overlap, but not contains, swapped
        "ac,bc,true", // at end
        "ac,ab,true", // at start
        "bc,bc,true", // self/same
        "ac,bc,true",
        //cs:remove:end
    })
    void t16ContainsRange( String rangeSpec1, String rangeSpec2, boolean expected ) {
        // TODO: check if contains is equal to expected and add more test cases
        IntegerRange range = createRange( rangeSpec1 );
        IntegerRange other = createRange( rangeSpec2 );
        //cs:remove:start
        assertThat(range.contains(other))
                .as(rangeSpec1 + " contains " + rangeSpec1).
                isEqualTo(expected);
        //cs:remove:end
        //cs:add:fail("Implement test");
    }

    /**
     * Test the default punchThrough method of Range. The method is already given.
     * For expectedRanges "ab|bc" means a stream with exactly the ranges [ab) and [bc).
     *
     * @param rangeSpec specifier for the range
     * @param punchSpec specifier for the "punch" range
     * @param expectedRanges, '|' separated list of specifiers of the expected ranges
     */
    @ParameterizedTest
    @CsvSource({
        "ab,ab,ab", // replace
        "ac,ab,ab|bc", // left punch
        //cs:remove:start
        "ab,bc,ab", // just missed
        "ad,bc,ab|bc|cd", // smack in the middle
        "ad,cd,ac|cd", // right punch
        "ab,cd,ab" // complete miss
        //cs:remove:end
    })
    void t17PunchThrough( String rangeSpec, String punchSpec, String expectedRanges ) {
        // TODO: assert that all expected ranges (ond no more) are present and add more test cases
        IntegerRange range = createRange( rangeSpec );
        IntegerRange punch = createRange( punchSpec );
        var expectedParts = helper().restRanges( "\\|", expectedRanges );
        //cs:remove:start
        Stream<IntegerRange> result = range.punchThrough( punch );
        Assertions.assertThat( result )
                .as( "punch " + range + " with " + punch )
                .containsExactlyElementsOf( expectedParts );
        //cs:remove:end
        //cs:add:fail("Implement test");
    }

    /**
     * Test default method compareTo of Range.
     * Have a look at Integer.signum() to help with the assertion
     *
     * @param range1Spec range 1
     * @param range2Spec range 2
     * @param expected value
     */
    @ParameterizedTest
    @CsvSource( {
        "ab,ac,0", // same start
        "ac,bd,-1", // start left of
        "bc,ad,1", // start right of
    } )
    void t18CompareTo( String range1Spec, String range2Spec, int expected ) {
        // TODO: assert result is as expected. HINT: use Integer.signum()
        IntegerRange range1 = createRange( range1Spec );
        IntegerRange range2 = createRange( range2Spec );
        //cs:remove:start
        assertThat(Integer.signum(range1.compareTo(range2)))
                .isEqualTo(expected);
        //cs:remove:end
        //cs:add:fail("Implement test");
    }
}
