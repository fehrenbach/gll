/**
 * 
 */
package gll.grammar;

import com.oracle.truffle.api.frame.VirtualFrame;
import gll.gss.Stack;
import gll.parser.State;
import gll.sppf.DerivationLabel;
import gll.sppf.Intermediate;
import gll.sppf.IntermediateCons;
import gll.sppf.SymbolDerivation;

/**
 * A grammar slot.
 * 
 * @author Tillmann Rendel
 */
public abstract class Slot extends TruffleizedGrammarNode implements DerivationLabel {
	/**
	 * Create Slot.
	 */
	public Slot() {
	}

	public abstract void appendPrefix(Slot slot, StringBuilder prefix);

	public abstract void appendSuffix(Slot slot, StringBuilder prefix, StringBuilder suffix);

	/**
	 * @param state
	 * @param derivation
	 * @param result
	 * @return
	 */
	public abstract IntermediateCons createDerivation(State state, Intermediate<?> derivation,
			SymbolDerivation<?, ?> result);

	/**
	 * Parse according to this slot.
	 *
 	 * @param truffleFrame
     *            the Truffle frame, needed for Truffle internals, probably unused
     * @param state
	 *            the parser state
	 * @param frame
	 *            the stack frame of the current parser process
	 * @param derivation
	 *            the current derivation
	 * @param codepoint
	 *            the current codepoint to parse
	 */
	public abstract void parse(VirtualFrame truffleFrame, State state, Stack frame, Intermediate<?> derivation, int codepoint);

	@Override
	public String toString() {
		final StringBuilder prefix = new StringBuilder();
		final StringBuilder suffix = new StringBuilder();
		suffix.append(" ·");
		appendSuffix(this, prefix, suffix);
		prefix.append(suffix);
		return prefix.toString();
	}
}
