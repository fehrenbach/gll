/**
 * 
 */
package gll.parser;

import gll.grammar.SortIdentifier;
import gll.sppf.NonterminalSymbolDerivation;
import gll.sppf.SymbolDerivation;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.*;

/**
 * @author Tillmann Rendel
 * 
 */
public class TestParser {

//	@Rule
//	public MethodRule timeout = new Timeout(500);

	public NonterminalSymbolDerivation assertAccepted(final SortIdentifier sort, final String text) throws IOException {
		final NonterminalSymbolDerivation result = Parser.parse(sort, new StringReader(text));
		assertFalse("False negative", result.getChildren().isEmpty());
		return result;
	}

	public NonterminalSymbolDerivation assertCorrect(final SortIdentifier sort, final String text, final Object expected)
			throws IOException {
		final NonterminalSymbolDerivation result = Parser.parse(sort, new StringReader(text));
		assertFalse("False negative", result.getChildren().isEmpty());
		assertEquals("Too many results", 1, result.getChildren().size());
		assertEquals("Wrong result", expected, result.extract());
		return result;
	}

	public void assertRejected(final SortIdentifier sort, final String text) throws IOException {
		final SymbolDerivation<?, ?> result = Parser.parse(sort, new StringReader(text));
		assertTrue("False positive", result.getChildren().isEmpty());
	}
}
