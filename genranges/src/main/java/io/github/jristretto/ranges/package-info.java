/**
 * Ranges of various en boundary types,
 * implemented mostly in the generic declared interface {@link Range Range}.
 *
 * <p>In this package the following definitions are used.</p>
 *
 *
 * <img src='./doc-files/rangesoperations.svg' alt='ranges and overlap'>
 * <br>
 * In all operations, ranges are in order a &le; b &le; c &le; d.
 *
 * All ranges are <i>half-open</i>, often denoted as [a,b), which means
 * that a <b>is</b> included in the range whereas b <b>is not</b> included.
 *
 * <ul>
 * <li>Two ranges overlap when b &gt; c.</li>
 * <li>Two ranges meet when b == c,</li>
 * <li>Two ranges are disjoint when b &lt; c</li>
 * </ul>
 *
 * <p>Implementation note: In all computations the ranges are organized in such a way
 * that range ab has the earliest start. Helper methods, either default or static,
 * are available in the Range interface to make this rearrangement easy.
 *
 * This version uses {@code record} as the implementing types.
 *
 * @author Pieter van den Hombergh
 */
package io.github.jristretto.ranges;