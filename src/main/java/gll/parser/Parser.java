/**
 * 
 */
package gll.parser;

import gll.grammar.Sort;
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
	public static NonterminalSymbolDerivation parse(final Sort sort, final Reader reader) throws IOException {
		final Parser parser = new Parser(sort);
		parser.parse(reader);
		return parser.getResult();
	}

	/**
	 * Parse the contents of a string according to a syntactic sort.
	 */
	public static NonterminalSymbolDerivation parse(final Sort sort, final String string) {
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
	private ParsingState state;

	/**
	 * Create Parser.
	 */
	public Parser(final Sort start) {
		this.start = start;
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
        state = new ParsingState();

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

		// state.writeGSS("/home/stefan/gss.dot");
	}

    public void parse(final String s) throws IOException {
        parse(new StringReader(s));
    }
}
