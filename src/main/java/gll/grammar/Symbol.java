/**
 * 
 */
package gll.grammar;

import com.oracle.truffle.api.frame.VirtualFrame;
import gll.gss.Stack;
import gll.parser.State;

/**
 * A symbol in a grammar.
 * 
 * @author Tillmann Rendel
 */
public abstract class Symbol extends TruffleizedGrammarNode {
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
	 * @param truffleFrame
     *            the Truffle frame, needed for Truffle internals, probably unused
	 * @param state
	 *            the parser state
	 * @param frame
	 *            the stack frame of the running parser process
	 * @param codepoint
	 *            the current token
	 */
	public abstract void call(VirtualFrame truffleFrame, State state, Stack frame, int codepoint);

	@Override
	public String toString() {
		return name;
	}
}
