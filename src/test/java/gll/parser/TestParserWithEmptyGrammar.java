/**
 * 
 */
package gll.parser;

import gll.grammar.Grammar;
import gll.grammar.SortIdentifier;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Test that the parser correctly handles the empty grammar.
 * 
 * <pre>
 * S  ::=
 * </pre>
 * 
 * <p>
 * This grammar should not accept any words, not even the empty word.
 * </p>
 * 
 * @author Tillmann Rendel
 */
public class TestParserWithEmptyGrammar extends TestParser {
	private SortIdentifier S = new SortIdentifier("S");

	/**
	 * Create the grammar.
	 */
	@Before
	public void setUp() {
        g = new Grammar();
        g.addProductionsToSort(S);
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
	 * Test that the word {@code "hello world"} is rejected.
	 * 
	 * @throws IOException
	 *             when something goes wrong with the Reader.
	 */
	@Test
	public final void testHelloWorld() throws IOException {
		assertRejected(S, "hello world");
	}
}
