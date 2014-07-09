/**
 * 
 */
package gll.parser;

import gll.grammar.Sort;
import gll.grammar.TerminalSymbol;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static gll.grammar.common.Characters.*;

public class TestParserWithAs extends TestParser {
	private final Sort S = new Sort("S");

	/**
	 * Create the grammar.
	 */
	@Before
	public void setUp() {
        S.add();
		S.add(TerminalSymbol.singleton('a'), S);
	}

	/**
	 * Test that the empty word {@code ""} is accepted.
	 *
	 * @throws java.io.IOException
	 *             when something goes wrong with the Reader.
	 */
	@Test
	public final void testEmpty() throws IOException {
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
		assertAccepted(S, "a");
	}

	/**
	 * Test that the word {@code "aaaaa"} is accepted.
	 *
	 * @throws java.io.IOException
	 *             when something goes wrong with the Reader.
	 */
	@Test
	public final void testExample03() throws IOException {
		assertAccepted(S, "aaaaa");
	}

    /**
     * Test that the word {@code "aaaaabaaaaa"} is rejected.
     *
     * @throws java.io.IOException
     *             when something goes wrong with the Reader.
     */
    @Test
    public final void testExample05() throws IOException {
        assertRejected(S, "aaaaabaaaaa");
    }

    public static void main(String[] args) throws IOException {
        System.out.println("foo");
        TestParserWithAs foo = new TestParserWithAs();
        foo.setUp();
        foo.testExample03();
        System.out.println("bar");
    }
}
