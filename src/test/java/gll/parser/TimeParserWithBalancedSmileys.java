/**
 * 
 */
package gll.parser;

import com.google.caliper.Param;
import com.google.caliper.SimpleBenchmark;
import gll.grammar.SortIdentifier;

import static gll.grammar.SortIdentifier.production;
import static gll.grammar.common.Characters.*;

/**
 * Benchmark the parser using various variants of the "balanced smileys"
 * grammar.
 * 
 * @author Tillmann Rendel
 */
public class TimeParserWithBalancedSmileys extends SimpleBenchmark {
	/**
	 * The start nonterminal symbol of the grammar.
	 */
	private final SortIdentifier S = new SortIdentifier("S");

	/**
	 * The string to be parsed.
	 */
	@Param({ "abc", "def", "xyz" })
	String input;

	/**
	 * Create the grammar.
	 */
	@Override
	public void setUp() {

		final SortIdentifier P = new SortIdentifier("P");

        S.setProductions(
                production(),
                production(LETTER()),
                production(SPACE()),
                production(COLON()),
                production(COLON(), P),
                production(LPAREN(), S, RPAREN()),
                production(S, S));

        P.setProductions(
                production(LPAREN()),
                production(RPAREN()));
	}

	public void timeParser(final int reps) {
		for (int i = 0; i < reps; i++) {
			Parser.parse(S, input);
		}
	}
}
