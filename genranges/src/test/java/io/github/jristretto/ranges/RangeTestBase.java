//cs:ignore
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

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assumptions.assumeThat;

/**
 *
 * @author Pieter van den Hombergh
 * @param <P> point
 * @param <D> distance
 * @param <R> range
 */
@TestMethodOrder( MethodOrderer.MethodName.class )
public abstract class RangeTestBase<R extends Range<R, P, D>, P extends Comparable<? super P>, D extends Comparable<? super D>> {
//    RangeTestDataFactory

    abstract RangeTestDataFactory<R, P, D> helper();

    P getPoint( String p ) {
        return helper().lookupPoint( p );
    }

    R createRange( String pp ) {
        return helper().createRange( pp );
    }

    R createRange( P a, P b ) {
        return helper().createRange( a, b );
    }

    /**
     * Test the default max function in Range.
     *
     * @param as specifies a
     * @param bs specifies a
     * @param exs specifies expected point
     */
    @ParameterizedTest
    @CsvSource( {
        "a,b,b",
        "c,b,c",
        "a,a,a"
    } )
    void t01Max( String as, String bs, String exs ) {
        P a = getPoint( as );
        P b = getPoint( bs );
        P expected = getPoint( exs ); // the map will return the same instance

        assertThat( Range.max( a, b ) ).isSameAs( expected );
//        fail( "method maxReturnsAOnEqual reached end. You know what to do." );
    }

    /**
     * Test the default max function in Range.
     *
     * @param as specifies a
     * @param bs specifies a
     * @param exs specifies expected point
     */
    @ParameterizedTest
    @CsvSource( {
        "a,b,a",
        "c,b,b",
        "a,a,a"
    } )
    void t02Min( String as, String bs, String exs ) {
        P a = getPoint( as );
        P b = getPoint( bs );
        P expected = getPoint( exs ); // the map will return the same instance

        assertThat( Range.min( a, b ) ).isSameAs( expected );
//        fail( "method maxReturnsAOnEqual reached end. You know what to do." );
    }

    /**
     * Test the default minmax function in Range.
     *
     * @param as specifies a
     * @param bs specifies a
     * @param expected0 specifies expected0 point
     * @param expected1 specifies expected1 point
     */
    @ParameterizedTest
    @CsvSource( {
        "a,a,a,a",
        "a,b,a,b",
        "d,c,c,d",} )
    void t03MinmaxTest( String as, String bs, String expected0,
            String expected1 ) {
        P a = getPoint( as );
        P b = getPoint( bs );
        P exp0 = getPoint( expected0 );
        P exp1 = getPoint( expected1 );
        P[] t = Range.minmax( a, b );
        SoftAssertions.assertSoftly( softly -> {
            softly.assertThat( t[0] ).isSameAs( exp0 );
            softly.assertThat( t[1] ).isSameAs( exp1 );
        } );

//        fail( "method minmaxTest reached end. You know what to do." );
    }

    /**
     * Test Range#meets.
     *
     * @param as specifies a
     * @param bs specifies b
     * @param cs specifies c
     * @param ds specifies d
     * @param expected outcome
     */

    @ParameterizedTest
    @CsvSource( {
        "a,b,c,d,false",
        "c,d,a,b,false",
        "a,b,b,d,true",
        "c,d,a,c,true",} )
    void t04Meets( String as, String bs, String cs, String ds,
            boolean expected ) {
        P a = getPoint( as );
        P b = getPoint( bs );
        P c = getPoint( cs );
        P d = getPoint( ds );
        R r1 = createRange( a, b );
        R r2 = createRange( c, d );
        assertThat( r1.meets( r2 ) ).isEqualTo( expected );
//        fail( "method meets reached end. You know what to do." );
    }

    /**
     * Test helper method Range#between.
     */

    @Test
    void t05CreateBetween() {
        P a = getPoint( "a" );
        P b = getPoint( "b" );
        P c = getPoint( "c" );

        R r = createRange( c, c );
        R rt = r.between( a, b );
        Assertions.assertThat( rt )
                .extracting( "start", "end" )
                .containsExactly( a, b );

//        fail( "createBetween completed succesfully; you know what to do" );
    }

    /**
     * Test Range#rangeHashCode and Range#rangeEquals implicitly through
     * concrete IntegerRange.
     */

    @Test
    void t06HashCodeEquals() {
        P a = getPoint( "a" );
        P b = getPoint( "b" );
        P c = getPoint( "c" );

        R ref = createRange( a, b );
        R equ = createRange( a, b );
        R diffB = createRange( a, c );
        R diffC = createRange( c, b );

        TestUtils.verifyEqualsAndHashCode( ref, equ, diffB, diffC );
    }

    /**
     * Test length function. Bit dubious, does it really test anything in range?
     */

    @Test
    void t07Length() {
        P a = getPoint( "a" );
        P b = getPoint( "b" );
        R r = createRange( b, a );
        assertThat( r.length() ).isEqualTo( helper().distance( a, b ) );

//        fail( "method length reached end. You know what to do." );
    }

    /**
     * Test the overlaps function. The method is given. Add more test values.
     *
     * @param rp1 point pair 1
     * @param rp2 point pair 2
     * @param overlaps expected outcome
     */

    @ParameterizedTest
    @CsvSource( value = {
        "ab,cd,false", // disjunct
        "ac,cd,false", // meet
        "ac,bd,true", //  B < C < D
        "ac,bc,true", // points < B < C
        "ab,bd,false", // end misses B
        "bc,bd,true", //
        "bd,bc,true", //
        "bd,bd,true", // to same ranges
    }
    )
    void t08overlaps( String rp1, String rp2, boolean overlaps ) {
        R r1 = createRange( rp1 );
        R r2 = createRange( rp2 );
        assertThat( r1.overlaps( r2 ) )
                .as( rp1 + "+" + rp2 )
                .isEqualTo( overlaps );
    }

    /**
     * Compute the overlap function as long.
     *
     * @param rp1 point pair one defining first range
     * @param rp2 point pair two defining second range
     * @param rp3 point pair with expected length
     */

    @ParameterizedTest
    @CsvSource( value = {
        // first, second, distance  points
        "ab,cd,aa", // disjunct
        "ab,bc,bb", // disjunct
        "ac,bd,bc", //  B < C < D

        "ac,bc,bc", // points < B < C
        "bc,bd,bc", //
        "bc,bc,bc", // two same ranges
    }
    )
    void t09overLap( String rp1, String rp2, String rp3 ) {
        R r1 = createRange( rp1 );
        R r2 = createRange( rp2 );
        R r3 = createRange( rp3 );
        var expectedOverlap = r3.length();
        var actual = r1.overlap( r2 );
        assertThat( actual ).as( rp1 + "+" + rp2 ).isEqualTo( expectedOverlap );
//        fail("test overLap completed, you know what to do.");
    }

    /**
     * assert that the range constructor puts start and end in the proper order.
     */

    @Test
    void t10Normalizes() {
        P a = getPoint( "a" );
        P b = getPoint( "b" );

        D zero = createRange( a, a ).length();
        assertThat( createRange( b, a ).length()
                .compareTo( zero ) ).isGreaterThan( 0 );
    }

    /**
     * Check the contain(p) method word correctly. Method is given. Add test
     * values.
     *
     * @param pp first range lookup
     * @param point to check
     * @param contains expected value
     */
    //@Disabled( "Think TDD" )
    @ParameterizedTest
    @CsvSource( {
        "bc,a,false",
        "bc,d,false",
        "bc,a,false",
        "bd,c,true",
        "bc,c,false",
        "bc,b,true", //
    } )
    void t11containsPoint( String pp, String point, boolean contains ) {
        // coverage
        R r = createRange( pp );
        P p = helper().lookupPoint( point );
        assumeThat( p ).isNotNull();
        assertThat( r.contains( p ) )
                .as( pp + " contains " + point + " " + contains )
                .isEqualTo( contains );
    }

    @Test
    void t12ToStringTest() {
        P a = getPoint( "a" );
        P b = getPoint( "b" );
        R r1 = createRange( a, b );
        assertThat( r1.toString() ).contains( b.toString() );
//        fail( "hashCodeT toStringTest reached end. You know what to do." );
    }

    @ParameterizedTest
    @CsvSource( {
        "ab,bc",
        "ac,bd",} )
    void t13aCheckMeetsOrOverlaps( String pp1, String pp2 ) {
        R r1 = createRange( pp1 );
        R r2 = createRange( pp2 );
        // code that should throw or not throw exception.
        ThrowableAssert.ThrowingCallable code = () -> {
            r1.checkMeetsOrOverlaps( r2 );
        };

        assertThatCode( code ).doesNotThrowAnyException();

//        fail( "method checkMeetsOrOverlaps reached end. You know what to do." );
    }

    @ParameterizedTest
    @CsvSource( {
        "ab,cd,false"
    } )
    void t13bCheckMeetsOrOverlaps( String pp1, String pp2 ) {
        R r1 = createRange( pp1 );
        R r2 = createRange( pp2 );
        // code that should throw or not throw exception.
        ThrowableAssert.ThrowingCallable code = () -> {
            r1.checkMeetsOrOverlaps( r2 );
        };

        assertThatThrownBy( code ).isInstanceOf(
                IllegalArgumentException.class );
//        fail( "method checkMeetsOrOverlaps reached end. You know what to do." );
    }

    /**
     * Check joinWith. The test values should all produce a join, the exception
     * throwing is tested elsewhere.
     *
     * @param pp1 first range spec
     * @param pp2 second range spec.
     * @param expectedRange in the test
     */

    @ParameterizedTest
    @CsvSource( {
        "ab,bc,ac",
        "ac,bd,ad"
    } )
    void t14JoinWith( String pp1, String pp2, String expectedRange ) {
        R r1 = createRange( pp1 );
        R r2 = createRange( pp2 );
        R expected = createRange( expectedRange );
        assertThat( r2.joinWith( r1 ) ).isEqualTo( expected );
//        fail( "method joinWith reached end. You know what to do." );
    }

    /**
     * Check the commonRange method between a range and a 'cutter'.
     *
     *
     * @param rp1 range specification
     * @param rp2 cutter range spec
     * @param cuts expected value 1
     * @param intersection expected result of cut.
     */

    @ParameterizedTest
    @CsvSource( value = {
        // this, cutter, cuts, expected result
        "ac,bd,bc",
        "ac,bc,bc",
        "bc,ad,bc",}
    )
    void t15acommonRangeSuccess( String rp1, String rp2, String intersection ) {
        R range = createRange( rp1 );
        R cutter = createRange( rp2 );
        Optional<R> result = range.intersectWith( cutter );
        R expected = createRange( intersection );
        assertThat( result ).isNotEmpty().get().isEqualTo( expected );
        SoftAssertions.assertSoftly( softly -> {
            R actual = result.get();
            softly.assertThat( actual.rangeEquals( expected ) ).isTrue();
        } );

    }

    /**
     * Check the commonRange method between a range and a 'cutter'.
     *
     * Test cases where intersection is empty.
     *
     * @param rp1 range specification
     * @param rp2 cutter range spec
     * @param cuts expected value 1
     * @param intersection expected result of cut.
     */

    @ParameterizedTest
    @CsvSource( value = {
        // this, cutter, cuts, expected result
        "ab,cd",
        "ab,bc",
        "cd,ab"
    }
    )
    void t15bCommonRangeEmpty( String rp1, String rp2 ) {
        R range = createRange( rp1 );
        R cutter = createRange( rp2 );
        Optional<R> result = range.intersectWith( cutter );
        assertThat( result ).isEmpty();
    }

    /**
     * Test if range is fully contained in other. (contains method)
     *
     * Method is given. Add test values
     *
     * @param rp1 this range
     * @param rp2 other range
     * @param expected outcome.
     */

    @ParameterizedTest
    @CsvSource( value = {
        // this, cutter, cuts, expected result
        "ab,cd,false", // disjunct
        "ab,bc,false", // meets
        "ac,bd,false",// overlap, but not contains
        "bd,ac,false",// overlap, but not contains, swapped
        "ac,bc,true", // at end
        "ac,ab,true", // at start
        "bc,bc,true", // self/same
        "ac,bc,true", // middle
    }
    )
    void t16ContainsRange( String rp1, String rp2, boolean expected ) {
        R range = createRange( rp1 );
        R other = createRange( rp2 );
        assertThat( range.contains( other ) ).as( rp1 + " contains " + rp1 ).
                isEqualTo( expected );

    }

    /**
     * Test the punchThrough method. Test is given. Add test values.
     *
     * In expected value ab|bc means a stream with exactly the elements [ab) and
     * [bc).
     *
     *
     * @param rangeP range value
     * @param punchP punch value
     * @param restPairs, | separated list of expected ranges in stream
     */

    @ParameterizedTest
    @CsvSource( value = {
        // range, punch, results, | separated
        "ab,ab,ab", // replace
        "ac,ab,ab|bc", // left punch
        "ab,bc,ab", // just missed
        "ad,bc,ab|bc|cd", // smack in the middle
        "ad,cd,ac|cd", // right punch
        "ab,cd,ab" // complete miss
    }
    )
    void t17PunchThrough( String rangeP, String punchP, String restPairs ) {
        R range = createRange( rangeP );
        R punch = createRange( punchP );
        var expectedParts = helper().restRanges( "\\|", restPairs );
        var result = range.punchThrough( punch );
        assertThat( result )
                .as( "punch " + range + " with " + punch )
                .containsExactlyElementsOf( expectedParts );
    }

    /**
     * Test compareTo. The outcome is negative, zero or positive, which is
     * expressed in the table as -1, 0. or 1.
     *
     * To test for zero is easy, but a special case. To test for the negative
     * value, multiply expected with the actual value and test it to be greater
     * than 0.
     *
     * we need to detect that result and expected have the same // sign or are
     * equal. // we can achieve
     *
     * @param pp1 range 1
     * @param pp2 range 2
     * @param expectedSignum sign value or zero
     */

    @ParameterizedTest
    @CsvSource( {
        "ab,ac,0", // same start
        "ac,bd,-1", // start left of
        "bc,ad,1", // start right of
    } )
    void t18CompareTo( String pp1, String pp2, int expectedSignum ) {
        R r1 = createRange( pp1 );
        R r2 = createRange( pp2 );
        assertThat( Integer.signum( r1.compareTo( r2 ) ) ).isEqualTo(
                expectedSignum );
    }

}