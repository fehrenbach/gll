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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ChangingGrammarAsToBs extends TestParser {
    private SortIdentifier S = new SortIdentifier("S");
    private SortIdentifier A = new SortIdentifier("A");
    private SortIdentifier B = new SortIdentifier("B");
	/**
	 * Create the grammar.
	 */
	@Before
	public void setUp() {
        A.setProductions(
                production(),
                production(TerminalSymbol.singleton('a'), A));

        B.setProductions(
                production(),
                production(TerminalSymbol.singleton('b'), B));
	}

    private void parseAs() {
        S.setProductions(production(A));
    }

    private void parseBs() {
        S.setProductions(production(B));
    }

	/**
	 * Test that the empty word {@code ""} is accepted.
	 *
	 * @throws java.io.IOException
	 *             when something goes wrong with the Reader.
	 */
	@Test
	public final void testEmptyA() throws IOException {
		parseAs();
        assertAccepted(S, "");
	}

    /**
     * Test that the empty word {@code ""} is accepted.
     *
     * @throws java.io.IOException
     *             when something goes wrong with the Reader.
     */
    @Test
    public final void testEmptyB() throws IOException {
        parseBs();
        assertAccepted(S, "");
    }

	/**
	 * Test that the word {@code "b"} is rejected.
	 *
	 * @throws java.io.IOException
	 *             when something goes wrong with the Reader.
	 */
	@Test
	public final void testExample01() throws IOException {
		parseAs();
        assertRejected(S, "b");
	}

	/**
	 * Test that the word {@code "a"} is accepted.
	 *
	 * @throws java.io.IOException
	 *             when something goes wrong with the Reader.
	 */
	@Test
	public final void testExample02() throws IOException {
		parseAs();
        assertAccepted(S, "a");
	}

    /**
     * Test that the word {@code "a"} is rejected.
     *
     * @throws java.io.IOException
     *             when something goes wrong with the Reader.
     */
    @Test
    public final void testExample03() throws IOException {
        parseBs();
        assertRejected(S, "a");
    }

    /**
     * Test that the word {@code "b"} is accepted.
     *
     * @throws java.io.IOException
     *             when something goes wrong with the Reader.
     */
    @Test
    public final void testExample04() throws IOException {
        parseBs();
        assertAccepted(S, "b");
    }

    /**
     * Test that the word {@code "a""b"} is accepted, when the grammar changes in the middle to actually accept {@code "b"}.
     *
     * @throws java.io.IOException
     *             when something goes wrong with the Reader.
     */
    @Test
    public final void testExample05() throws IOException {
        Parser p = new Parser(S);
        parseAs();
        p.parse("a");
        assertFalse("False negative", p.getResult().getChildren().isEmpty());
        System.out.println("Changing grammar");
        parseBs();
        p.parse("b");
        assertFalse("False negative", p.getResult().getChildren().isEmpty());
    }

    /**
     * Test that the word {@code "a""a"} is not accepted, when the grammar changes in the middle to actually accept {@code "b"}.
     *
     * @throws java.io.IOException
     *             when something goes wrong with the Reader.
     */
    @Test
    public final void testExample06() throws IOException {
        Parser p = new Parser(S);
        parseAs();
        p.parse("a");
        assertFalse("False negative", p.getResult().getChildren().isEmpty());
        System.out.println("Changing grammar");
        parseBs();
        p.parse("a");
        assertTrue(p.getResult().getChildren().isEmpty());
    }
}
