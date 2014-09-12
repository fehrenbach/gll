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

public class TestParserWithSExp extends TestParser {
    private SortIdentifier S = new SortIdentifier("S");
    private SortIdentifier SExp = new SortIdentifier("SExp");
    private SortIdentifier Atom = new SortIdentifier("Atom");
    private SortIdentifier Pair = new SortIdentifier("Pair");
    private SortIdentifier Symbol = new SortIdentifier("Symbol");
    private SortIdentifier Number = new SortIdentifier("Number");


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
        S.setProductions(production(SExp));
        SExp.setProductions(production(Atom), production(Pair));
        Atom.setProductions(production(Symbol), production(Number));
        Pair.setProductions(production(
                TerminalSymbol.singleton('('),
                SExp,
                TerminalSymbol.singleton('.'),
                SExp,
                TerminalSymbol.singleton(')')));
        Symbol.setProductions(
                production(LETTER()),
                production(LETTER(), Symbol));
        Number.setProductions(
                production(DIGIT()),
                production(DIGIT(), Number));
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
        SortIdentifier S = foo.S;
        Parser p = new Parser(S);
        String as = "((car.cdr).42)";
        p.parse(as);
    }
}
