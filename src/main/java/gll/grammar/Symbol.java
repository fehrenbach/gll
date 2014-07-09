/**
 * 
 */
package gll.grammar;

import gll.gss.Stack;
import gll.parser.State;

/**
 * A symbol in a grammar.
 * 
 * @author Tillmann Rendel
 */
public abstract class Symbol {
	/**
	 * A human-readable name, used for debugging and error messages.
	 */
	protected final String name;

	/**
	 * Create a symbol.
	 * 
	 * @param name
	 *            a human-readable name, used for debugging and error messages
	 */
	public Symbol(final String name) {
		this.name = name;
	}

	/**
	 * Process this symbol during parsing.
	 * 
	 * @param state
	 *            the parser state
	 * @param frame
	 *            the stack frame of the running parser process
	 * @param codepoint
	 *            the current token
	 */
	public abstract void call(State state, Stack frame, int codepoint);

	@Override
	public String toString() {
		return name;
	}
}
