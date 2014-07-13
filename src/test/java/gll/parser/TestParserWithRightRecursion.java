/**
 * 
 */
package gll.parser;

import gll.grammar.SortIdentifier;
import gll.grammar.TerminalSymbol;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static gll.grammar.SortIdentifier.production;

/**
 * Test that the parser correctly handles a (right-recursive) grammar that
 * accepts the phrase {@code "hello world"}:
 * 
 * <pre>
 * S  ::=  HELLO SPACE WORLD
 * HELLO  ::=  'h' 'e' 'l' 'l' 'o'
 * WORLD  ::=  'w' 'o' 'r' 'l' 'd'
 * SPACE  ::=  ' '
 *         |   ' ' SPACE
 * </pre>
 * 
 * <p>
 * This grammar should accept the phrase "hello world" with one or more space
 * characters between the words.
 * </p>
 * 
 * @author Tillmann Rendel
 */
public class TestParserWithRightRecursion extends TestParser {
	private SortIdentifier S = new SortIdentifier("S");

	/**
	 * Create the grammar.
	 */
	@Before
	public void setUp() {
		final SortIdentifier HELLO = new SortIdentifier("HELLO");
		final SortIdentifier WORLD = new SortIdentifier("WORLD");
		final SortIdentifier SPACE = new SortIdentifier("SPACE");

        S.setProductions(production(HELLO, SPACE, WORLD));

        HELLO.setProductions(production(
                TerminalSymbol.singleton('h'),
                TerminalSymbol.singleton('e'),
                TerminalSymbol.singleton('l'),
                TerminalSymbol.singleton('l'),
                TerminalSymbol.singleton('o')));

        WORLD.setProductions(production(
                TerminalSymbol.singleton('w'),
                TerminalSymbol.singleton('o'),
                TerminalSymbol.singleton('r'),
                TerminalSymbol.singleton('l'),
                TerminalSymbol.singleton('d')));

        SPACE.setProductions(
                production(TerminalSymbol.singleton(' ')),
                production(TerminalSymbol.singleton(' '), SPACE));
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
	 * Test that the word {@code "helloworld"} (without spaces) is rejected.
	 * 
	 * @throws IOException
	 *             when something goes wrong with the Reader.
	 */
	@Test
	public final void testNoSpace() throws IOException {
		assertRejected(S, "helloworld");
	}

	/**
	 * Test that the word {@code "hello world"} (with 1 space) is accepted.
	 * 
	 * @throws IOException
	 *             when something goes wrong with the Reader.
	 */
	@Test
	public final void testOneSpace() throws IOException {
		assertAccepted(S, "hello world");
	}

	/**
	 * Test that the word {@code "hello  world"} (with 2 spaces) is accepted.
	 * 
	 * @throws IOException
	 *             when something goes wrong with the Reader.
	 */
	@Test
	public final void testTwoSpaces() throws IOException {
		assertAccepted(S, "hello  world");
	}

	/**
	 * Test that the word {@code "hello   world"} (with 3 spaces) is accepted.
	 * 
	 * @throws IOException
	 *             when something goes wrong with the Reader.
	 */
	@Test
	public final void testThreeSpaces() throws IOException {
		assertAccepted(S, "hello  world");
	}

}