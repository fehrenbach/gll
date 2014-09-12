/**
 * 
 */
package gll.parser;

import gll.grammar.Sort;
import gll.grammar.TerminalSymbol;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;


public class TestParserWithSExp extends TestParser {
    private Sort S = new Sort("S");
    private Sort SExp = new Sort("SExp");
    private Sort Atom = new Sort("Atom");
    private Sort Pair = new Sort("Pair");
    private Sort Symbol = new Sort("Symbol");
    private Sort Number = new Sort("Number");


    public static TerminalSymbol LETTER() { return new TerminalSymbol("letter") {
        @Override
        public boolean accept(final int codepoint) {
            return Character.isLetter(codepoint);
        }
    };}

    public static TerminalSymbol DIGIT() { return new TerminalSymbol("digit") {
        @Override
        public boolean accept(final int codepoint) {
            return Character.isDigit(codepoint);
        }
    };}

    /**
	 * Create the grammar.
	 */
	@Before
	public void setUp() {
        S.add(SExp);
        SExp.add(Atom);
        SExp.add(Pair);
        Atom.add(Symbol);
        Atom.add(Number);
        Pair.add(
                TerminalSymbol.singleton('('),
                SExp,
                TerminalSymbol.singleton('.'),
                SExp,
                TerminalSymbol.singleton(')'));
        Symbol.add(LETTER());
        Symbol.add(LETTER(), Symbol);
        Number.add(DIGIT());
        Number.add(DIGIT(), Number);
	}

    @Test
    public final void testEmpty() throws IOException {
        assertRejected(S, "");
    }

    @Test
    public final void test01() throws IOException {
        assertAccepted(S, "foo");
    }

    @Test
    public final void test02() throws IOException {
        assertAccepted(S, "42");
    }

    @Test
    public final void test03() throws IOException {
        assertAccepted(S, "(foo.42)");
    }

    @Test
    public final void test04() throws IOException {
        assertAccepted(S, "(foo.(bar.42))");
    }

    @Test
    public final void test05() throws IOException {
        assertRejected(S, "(abc.(def).4)");
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        TestParserWithSExp foo = new TestParserWithSExp();
        foo.setUp();
        Sort S = foo.S;
        Parser p = new Parser(S);
        String as = "((car.cdr).42)";
        p.parse(as);
    }
}
