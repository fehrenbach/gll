/**
 * 
 */
package gll.parser;

import gll.grammar.SortIdentifier;
import gll.sppf.SymbolDerivation;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static gll.grammar.SortIdentifier.production;
import static org.junit.Assert.assertTrue;

/**
 * Test that the parser correctly handles the grammar that accepts only the
 * empty word:
 * 
 * <pre>
 * S  ::=  ε
 * </pre>
 * 
 * <p>
 * This grammar should only accept the empty word.
 * </p>
 * 
 * @author Tillmann Rendel
 */
public class TestParserWithSingletonGrammar extends TestParser {
	private final SortIdentifier S = new SortIdentifier("S");

	/**
	 * Create the grammar.
	 */
	@Before
	public void setUp() {
        S.setProductions(production());
	}

    /**
	 * Test that the empty word {@code ""} is accepted.
	 * 
	 * @throws IOException
	 *             when something goes wrong with the Reader.
	 */
	@Test
	public final void testEmpty() throws IOException {
		final SymbolDerivation<?, ?> result = assertAccepted(S, "");
		assertTrue("unexpected ambiguities", result.getChildren().size() == 1);
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