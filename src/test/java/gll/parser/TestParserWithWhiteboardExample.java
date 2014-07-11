/**
 * 
 */
package gll.parser;

import gll.grammar.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Test that the parser correctly handles the grammar we used on the whiteboard
 * on July 6, 2012, to discuss how we could make the parser variability-aware.
 * 
 * <pre>
 * S  ::=  A S  |  ε
 * A  ::=  x  |  ε
 * </pre>
 * 
 * @author Tillmann Rendel
 */
public class TestParserWithWhiteboardExample extends TestParser {
	private final SortIdentifier S = new SortIdentifier("S");

	/**
	 * Create the grammar.
	 */
	@Before
	public void setUp() {
        g = new Grammar();
		final SortIdentifier A = new SortIdentifier("A");

		final TerminalSymbol x = TerminalSymbol.singleton('x');

        g.addProductionsToSort(S,
                new Production(S, new SortCall(A), new SortCall(S)),
                new Production(S));

        g.addProductionsToSort(A,
                new Production(A, x),
                new Production(A));
	}

	/**
	 * Test that the empty word {@code ""} is accepted.
	 * 
	 * @throws IOException
	 *             when something goes wrong with the Reader.
	 */
	@Test
	public final void testEmpty() throws IOException {
		assertAccepted(S, "");
	}

	/**
	 * Test that the word {@code "hello world"} is rejected
	 * 
	 * @throws IOException
	 *             when something goes wrong with the Reader.
	 */
	@Test
	public final void testHelloWorld() throws IOException {
		assertRejected(S, "hello world");
	}

	/**
	 * Test that the word {@code "x"} is accepted.
	 * 
	 * @throws IOException
	 *             when something goes wrong with the Reader.
	 */
	@Test
	public final void testX() throws IOException {
		assertAccepted(S, "x");
	}

	/**
	 * Test that the word {@code "xx"} is accepted.
	 * 
	 * @throws IOException
	 *             when something goes wrong with the Reader.
	 */
	@Test
	public final void testXX() throws IOException {
		assertAccepted(S, "xx");
	}

	/**
	 * Test that the word {@code "xxx"} is accepted.
	 * 
	 * @throws IOException
	 *             when something goes wrong with the Reader.
	 */
	@Test
	public final void testXXX() throws IOException {
		assertAccepted(S, "xxx");
	}
}