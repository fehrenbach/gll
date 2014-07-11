/**
 * 
 */
package gll.parser;

import gll.grammar.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Test that the parser correctly handles a grammar with hidden left-recursion.
 * 
 * <pre>
 * S  ::=  C a  |  d
 * B  ::=  ε  |  a
 * C  ::=  b  |  B C b  |  b b
 * </pre>
 * 
 * <p>
 * This is grammar Γ₁ from Scott and Johnstone (2010, Sec. 5).
 * </p>
 * 
 * @author Tillmann Rendel
 */
public class TestParserWithHiddenLeftRecursion extends TestParser {
	private SortIdentifier S = new SortIdentifier("S");

	/**
	 * Create the grammar.
	 */
	@Before
	public void setUp() {
        g = new Grammar();
		final SortIdentifier B = new SortIdentifier("B");
		final SortIdentifier C = new SortIdentifier("C");

		final TerminalSymbol a = TerminalSymbol.singleton('a');
		final TerminalSymbol b = TerminalSymbol.singleton('b');
		final TerminalSymbol d = TerminalSymbol.singleton('d');

        g.addProductionsToSort(S,
                new Production(S, new SortCall(C), a),
                new Production(S, d));

        g.addProductionsToSort(B,
                new Production(B),
                new Production(B, a));

        g.addProductionsToSort(C,
                new Production(C, b),
                new Production(C, new SortCall(B), new SortCall(C), b),
                new Production(C, b, b));
	}

	/**
	 * Test that the empty word {@code ""} is rejected.
	 * 
	 * @throws IOException
	 *             when something goes wrong with the Reader.
	 */
	@Test
	public final void testEmpty() throws IOException {
		assertRejected(S, "");
	}

	/**
	 * Test that the word {@code "d"} is accepted.
	 * 
	 * @throws IOException
	 *             when something goes wrong with the Reader.
	 */
	@Test
	public final void testD() throws IOException {
		assertAccepted(S, "d");
	}

	/**
	 * Test that the word {@code "ba"} is accepted.
	 * 
	 * @throws IOException
	 *             when something goes wrong with the Reader.
	 */
	@Test
	public final void testBA() throws IOException {
		assertAccepted(S, "ba");
	}

	/**
	 * Test that the word {@code "bba"} is accepted.
	 * 
	 * @throws IOException
	 *             when something goes wrong with the Reader.
	 */
	@Test
	public final void testBBA() throws IOException {
		assertAccepted(S, "bba");
	}

	/**
	 * Test that the word {@code "aba"} is rejected.
	 * 
	 * @throws IOException
	 *             when something goes wrong with the Reader.
	 */
	@Test
	public final void testABA() throws IOException {
		assertRejected(S, "aba");
	}

	/**
	 * Test that the word {@code "abba"} is accepted.
	 * 
	 * @throws IOException
	 *             when something goes wrong with the Reader.
	 */
	@Test
	public final void testABBA() throws IOException {
		assertAccepted(S, "abba");
	}

	/**
	 * Test that the word {@code "aabbba"} is accepted.
	 * 
	 * @throws IOException
	 *             when something goes wrong with the Reader.
	 */
	@Test
	public final void testAABBBA() throws IOException {
		assertAccepted(S, "aabbba");
	}
}