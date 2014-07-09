/**
 * 
 */
package gll.grammar;

import com.oracle.truffle.api.frame.VirtualFrame;
import gll.gss.Stack;
import gll.parser.State;
import gll.sppf.*;

/**
 * @author Tillmann Rendel
 * 
 */
public class SortReturnSlot extends Slot {
	/**
	 * The production this slot is associated with.
	 */
	@Child private Production production;

	/**
	 * Create ReturnSlot.
	 * 
	 * @param production
     *            the production this slot is associated with.
	 */
	public SortReturnSlot(final Production production) {
		this.production = production;
        adoptChildren();
	}

    @Override
    public void appendPrefix(Slot slot, StringBuilder prefix) {
    }

    /**
	 * {@inheritDoc}
	 */
	@Override
	public void appendSuffix(final Slot slot, final StringBuilder prefix, final StringBuilder suffix) {
		production.appendPrefix(slot, prefix);
	}

	/**
	 * {@inheritDoc}
	 */
	public DerivationLabel getLabel() {
		return production.getSort();
	}

	/**
	 * @return
	 */
	public Sort getSort() {
		return production.getSort();
	}

	/**
	 * Parse according to this slot.
	 * 
	 * <p>
	 * This implementation schedules processes at the return addresses according
	 * to the stack frame of the current process.
	 * </p>
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
	@Override
	public void parse(VirtualFrame truffleFrame, final State state, final Stack frame, final Intermediate<?> derivation, final int codepoint) {
		final Unary wrapped = new Unary(production, derivation);
		final NonterminalSymbolDerivation result = state.createNonterminalSymbolDerivation(production.getSort(),
				derivation.getFirst(), wrapped);
		frame.schedule(state, result, codepoint);
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
}
