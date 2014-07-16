/**
 * 
 */
package gll.parser;

import gll.grammar.SortIdentifier;
import gll.gss.Initial;
import gll.sppf.NonterminalSymbolDerivation;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * @author Tillmann Rendel
 */
public class Parser {
	/**
	 * Parse the contents of a reader according to a syntactic sort.
	 * 
	 * @throws IOException
	 */
	public static NonterminalSymbolDerivation parse(final SortIdentifier sort, final Reader reader) throws IOException {
		final Parser parser = new Parser(sort);
		parser.parse(reader);
		return parser.getResult();
	}

	/**
	 * Parse the contents of a string according to a syntactic sort.
	 */
	public static NonterminalSymbolDerivation parse(final SortIdentifier sort, final String string) {
		final Parser parser = new Parser(sort);
		try {
			parser.parse(new StringReader(string));
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		return parser.getResult();
	}

    /**
     * The start nonterminal.
     */
    private final SortIdentifier start;

	/**
	 * Parser state.
	 */
	private final ParsingState state;

	/**
	 * Create Parser.
	 */
	public Parser(final SortIdentifier start) {
		this.start = start;
        this.state = new ParsingState();
	}

	public NonterminalSymbolDerivation getResult() {
		return state.getResult();
	}

	/**
	 * Extract the next token from the token stream.
	 * 
	 * @throws IOException
	 */
	private int nextToken(final Reader reader) throws IOException {
		final int high = reader.read();
		if (high < 0) {
			return high;
		} else if (Character.isHighSurrogate((char) high)) {
			final int low = reader.read();
			if (low < 0) {
				throw new IOException("Missing low surrogate at end of input");
			} else if (Character.isLowSurrogate((char) low)) {
				return Character.toCodePoint((char) high, (char) low);
			} else {
				throw new IOException("Invalid low surrogate");
			}
		} else {
			return high;
		}
	}

	/**
	 * The parser.
	 * 
	 * @throws IOException
	 */
	public void parse(final Reader reader) throws IOException {
		state.start = start;
		state.active.add(new StartProcess(new Initial(), start));

		int codepoint;
		do {
			codepoint = nextToken(reader);
			state.nextToken(codepoint);

			while (!state.active.isEmpty()) {
				final Process current = state.active.poll();
				current.execute(state, codepoint);
			}
		} while (codepoint >= 0);

        // Prepare state for next call to parse.
        // We will parse as if the token stream just starts, but we keep the unchanged (and optimized) parts of the
        // grammar AST.
        state.reset();

		// state.writeGSS("/home/stefan/gss.dot");
	}

    public void parse(final String s) throws IOException {
        parse(new StringReader(s));
    }
}
