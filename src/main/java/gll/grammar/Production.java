/**
 * 
 */
package gll.grammar;

import com.oracle.truffle.api.frame.VirtualFrame;
import gll.gss.Stack;
import gll.parser.State;
import gll.sppf.DerivationLabel;
import gll.sppf.Unary;

/**
 * @author Tillmann Rendel
 */
public class Production extends TruffleizedGrammarNode implements DerivationLabel {
	/**
	 * The first grammar slot in this production.
	 */
	@Child Slot first;

	/**
	 * The syntactic sort this production is parsing.
	 */
	private final SortIdentifier sort;

	/**
	 * Create a production from a sequence of symbols.
	 * 
	 * @param sort
	 *            the sort this production is parsing
	 * @param symbols
	 *            the symbols that form this production.
	 */
	public Production(final SortIdentifier sort, final Symbol... symbols) {
		this.sort = sort;

		Slot slot = new SortReturnSlot(this);

		for (int i = symbols.length - 1; i >= 0; i--) {
			slot = new SymbolSlot(symbols[i], slot);
		}

		first = slot;

        adoptChildren();
	}

	public void appendPrefix(final Slot slot, final StringBuilder prefix) {
		prefix.append(sort);
		prefix.append(" ::=");
		first.appendPrefix(slot, prefix);
	}

	public Object extract(final Unary derivation) {
		throw new Error("Parser problem");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fomegastar.syntax.parser.AbstractProduction#getSort()
	 */
	public SortIdentifier getSort() {
		return sort;
	}

	/**
	 * Schedule a process that parses according to this production.
	 * 
	 * @param state
	 *            the parser state
	 * @param callee
	 *            the stack frame to use for the process.
	 */
	public void schedule(VirtualFrame truffleFrame, int codepoint, final State state, final Stack callee) {
        // This used to call State.scheduleNow.
        // Can't put it into State, because Truffle for some reason does not see that the VirtualFrame does not escape.
        if (!state.deadNow(first, callee)) {
            first.parse(truffleFrame, state, callee, state.createEmpty(), codepoint);
        }
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		appendPrefix(null, builder);
		return builder.toString();
	}
}
