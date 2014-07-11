/**
 * 
 */
package gll.parser;

import gll.grammar.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Test that the parser correctly handles a (left-recursive) grammar that
 * accepts the phrase {@code "hello world"}:
 * 
 * <pre>
 * S  ::=  HELLO SPACE WORLD
 * HELLO  ::=  'h' 'e' 'l' 'l' 'o'
 * WORLD  ::=  'w' 'o' 'r' 'l' 'd'
 * SPACE  ::=  ' '
 *         |   SPACE ' '
 * </pre>
 * 
 * <p>
 * This grammar should accept the phrase "hello world" with one or more space
 * characters between the words.
 * </p>
 * 
 * @author Tillmann Rendel
 */
public class TestParserWithLeftRecursion extends TestParser {
	private SortIdentifier S = new SortIdentifier("S");

	/**
	 * Create the grammar.
	 */
	@Before
	public void setUp() {
        g = new Grammar();
		final SortIdentifier HELLO = new SortIdentifier("HELLO");
		final SortIdentifier WORLD = new SortIdentifier("WORLD");
		final SortIdentifier SPACE = new SortIdentifier("SPACE");

        g.addProductionsToSort(S,
                new Production(S, new SortCall(HELLO), new SortCall(SPACE), new SortCall(WORLD)));

        g.addProductionsToSort(HELLO,
                new Production(HELLO,
                        TerminalSymbol.singleton('h'),
                        TerminalSymbol.singleton('e'),
                        TerminalSymbol.singleton('l'),
                        TerminalSymbol.singleton('l'),
                        TerminalSymbol.singleton('o')));

        g.addProductionsToSort(WORLD,
                new Production(WORLD,
                        TerminalSymbol.singleton('w'),
                        TerminalSymbol.singleton('o'),
                        TerminalSymbol.singleton('r'),
                        TerminalSymbol.singleton('l'),
                        TerminalSymbol.singleton('d')));

        g.addProductionsToSort(SPACE,
                new Production(SPACE, TerminalSymbol.singleton(' ')),
                new Production(SPACE, new SortCall(SPACE), TerminalSymbol.singleton(' ')));
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