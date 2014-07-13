/**
 * 
 */
package gll.grammar.common;

import gll.grammar.TerminalSymbol;

/**
 * TerminalSymbols for commonly used characters and character classes.
 * 
 * @author Tillmann Rendel
 */
public class Characters {
	/**
	 * The terminal symbol “<code>:</code>”.
	 */
	public static TerminalSymbol COLON () { return TerminalSymbol.singleton("colon", ':'); }

	/**
	 * The terminal symbol “<code>,</code>”.
	 */
	public static TerminalSymbol COMMA () { return TerminalSymbol.singleton("comma", ','); }

	/**
	 * The terminal symbol “<code>-</code>”.
	 */
	public static TerminalSymbol DASH () { return TerminalSymbol.singleton("dash", '-'); }

	/**
	 * A terminal symbol that is a digit (as determined by
	 * {@link Character#isDigit(int)}).
	 */
	public static TerminalSymbol DIGIT() {
        return new TerminalSymbol("digit") {
            @Override
            public boolean accept(final int codepoint) {
                return Character.isDigit(codepoint);
            }
        };
    }

	/**
	 * The terminal symbol “<code>.</code>”.
	 */
	public static TerminalSymbol DOT () { return TerminalSymbol.singleton("dot", '.'); }

	/**
	 * The terminal symbol “<code>=</code>”.
	 */
	public static TerminalSymbol EQUALS () { return TerminalSymbol.singleton("equals", '='); }

	/**
	 * The terminal symbol “<code>></code>”.
	 */
	public static TerminalSymbol GT () { return TerminalSymbol.singleton("greater than", '>'); }

	/**
	 * The terminal symbol “<code>{</code>”.
	 */
	public static TerminalSymbol LBRACE () { return TerminalSymbol.singleton("left brace", '{'); }

	/**
	 * The terminal symbol “<code>[</code>”.
	 */
	public static TerminalSymbol LBRACKET () { return TerminalSymbol.singleton("left bracket", '['); }

	/**
	 * A terminal symbol that is a letter (as determined by
	 * {@link Character#isLetter(int)}).
	 */
	public static TerminalSymbol LETTER () { return new TerminalSymbol("letter") {
		@Override
		public boolean accept(final int codepoint) {
			return Character.isLetter(codepoint);
		}
	};}

	/**
	 * A terminal symbol that is an lower case letter (as determined by
	 * {@link Character#isLowerCase(int)}).
	 */
	public static TerminalSymbol LOWER_CASE () { return new TerminalSymbol("upper case letter") {
		@Override
		public boolean accept(final int codepoint) {
			return Character.isLowerCase(codepoint);
		}
	};}

	/**
	 * The terminal symbol “<code>(</code>”.
	 */
	public static TerminalSymbol LPAREN () { return TerminalSymbol.singleton("left parenthesis", '('); }

	/**
	 * The terminal symbol “<code>|</code>”.
	 */
	public static TerminalSymbol PIPE () { return TerminalSymbol.singleton("pipe", '|'); }

	/**
	 * The terminal symbol “<code>}</code>”.
	 */
	public static TerminalSymbol RBRACE () { return TerminalSymbol.singleton("right brace", '}'); }

	/**
	 * The terminal symbol “<code>]</code>”.
	 */
	public static TerminalSymbol RBRACKET () { return TerminalSymbol.singleton("right bracket", ']'); }

	/**
	 * The terminal symbol “<code>)</code>”.
	 */
	public static TerminalSymbol RPAREN () { return TerminalSymbol.singleton("right parenthesis", ')'); }

	/**
	 * The terminal symbol “<code>;</code>”.
	 */
	public static TerminalSymbol SEMICOLON () { return TerminalSymbol.singleton("semicolon", ';'); }

	/**
	 * The terminal symbol “<code> </code>” (space).
	 */
	public static TerminalSymbol SPACE () { return TerminalSymbol.singleton("space", ' '); }

	/**
	 * A terminal symbol that is an upper case letter (as determined by
	 * {@link Character#isUpperCase(int)}).
	 */
	public static TerminalSymbol UPPER_CASE() { return new TerminalSymbol("upper case letter") {
		@Override
		public boolean accept(final int codepoint) {
			return Character.isUpperCase(codepoint);
		}
	};}
}
