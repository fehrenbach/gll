/**
 * 
 */
package gll.grammar;

import com.oracle.truffle.api.frame.VirtualFrame;
import gll.gss.Stack;
import gll.parser.State;
import gll.sppf.Intermediate;
import gll.sppf.IntermediateCons;
import gll.sppf.SymbolDerivation;

/**
 * A grammar slot that is associated with parsing a symbol.
 * 
 * @author Tillmann Rendel
 */
public class SymbolSlot extends Slot {
	/**
	 * The next slot.
	 */
	@Child private Slot next;

	/**
	 * The action.
	 */
	@Child private Symbol symbol;

	/**
	 * Create a symbol slot.
	 * 
	 * @param symbol
	 *            the symbol to associate the new slot with
	 * @param next
	 *            the slot after the {@code symbol}
	 */
	public SymbolSlot(final Symbol symbol, final Slot next) {
		super();
		this.symbol = symbol;
		this.next = next;
        adoptChildren();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void appendPrefix(final Slot slot, final StringBuilder prefix) {
		if (slot != this) {
			prefix.append(" ");
			prefix.append(symbol.toString());
			next.appendPrefix(slot, prefix);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fomegastar.syntax.parser.Slot#append(java.lang.StringBuilder,
	 * fomegastar.syntax.parser.Slot)
	 */
	@Override
	public void appendSuffix(final Slot slot, final StringBuilder prefix, final StringBuilder suffix) {
		suffix.append(" ");
		suffix.append(symbol.toString());
		next.appendSuffix(slot, prefix, suffix);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fomegastar.syntax.parser.Slot#createDerivation(fomegastar.syntax.parser
	 * .Derivation, fomegastar.syntax.parser.Derivation)
	 */
	@Override
	public IntermediateCons createDerivation(final State state, final Intermediate<?> lhs,
			final SymbolDerivation<?, ?> rhs) {
		return state.append(this, lhs, rhs);
	}

	/**
	 * Parse according to this slot.
	 * 
	 * <p>
	 * This implementation parses according to the symbol this grammar slot is
	 * associated with.
	 * </p>
     *
     * @param truffleFrame
     *            the Truffle frame, needed for Truffle internals, probably unused
     * @param state
	 *            the parser state
	 * @param caller
	 *            the stack frame of the current parser process
	 * @param derivation
	 *            the current derivation
	 * @param codepoint
	 *            the current codepoint to parse
	 */
	@Override
	public void parse(VirtualFrame truffleFrame, final State state, final Stack caller, final Intermediate<?> derivation, final int codepoint) {
		final Stack callee = state.push(next, caller, state.getPosition(), derivation);
		symbol.call(truffleFrame, state, callee, codepoint);
	}
}
