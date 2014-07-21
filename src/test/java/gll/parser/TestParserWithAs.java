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

public class TestParserWithAs extends TestParser {
    private SortIdentifier S = new SortIdentifier("S");

	/**
	 * Create the grammar.
	 */
	@Before
	public void setUp() {
        S.setProductions(
                production(),
                production(TerminalSymbol.singleton('a'), S));
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

    public static void main(String[] args) throws IOException, InterruptedException {
        TestParserWithAs foo = new TestParserWithAs();
        foo.setUp();
        SortIdentifier S = foo.S;
        Parser p = new Parser(S);
        String as = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        System.out.println("before first");
        p.parse(as);
        System.out.println("after first");
        Thread.sleep(1000);
        for (int i = 0; i < 1000; i++)
            p.parse(as);
    }
}
