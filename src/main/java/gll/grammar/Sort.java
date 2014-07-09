/**
 * 
 */
package gll.grammar;

import com.oracle.truffle.api.frame.VirtualFrame;
import gll.gss.Stack;
import gll.parser.State;
import gll.sppf.DerivationLabel;

import java.util.ArrayList;
import java.util.List;

/**
 * A syntactic sort (= nonterminal symbol) in the grammar.
 * 
 * @author Tillmann Rendel
 */
public class Sort extends Symbol implements DerivationLabel {
	/**
	 * The list of alternative productions for this syntactic sort.
	 */
    @Children private final List<Production> alternatives = new ArrayList<Production>();

	/**
	 * Create a named sort.
	 * 
	 * @param name
	 *            a human-readable name, used for debugging and error messages.
	 */
	public Sort(final String name) {
		super("‹" + name + "›");
	}

	/**
	 * Add an alternative to this syntactic sort.
	 * 
	 * The alternative is specified as a production.
	 * 
	 * @param production
	 *            the production to add as an alternative.
	 */
	public void add(final Production production) {
		alternatives.add(production);
        adoptChildren();
	}

	/**
	 * Add an alternative to this syntactic sort.
	 * 
	 * The alternative is specified as a list of actions that are automatically
	 * wrapped in a production.
	 * 
	 * @param symbols
	 *            the sequence of symbols to add as an alternative.
	 * @return the new production
	 */
	public Production add(final Symbol... symbols) {
		final Production production = new Production(this, symbols);
		add(production);
		return production;
	}

    /**
	 * Process this symbol during parsing.
	 * 
	 * <p>
	 * This implementation processes each alternative production.
	 * </p>
     *
	 * @param truffleFrame
     *            the Truffle frame, needed for Truffle internals, probably unused
	 * @param state
	 *            the parser state
	 * @param frame
	 *            the stack frame of the running parser process
	 * @param codepoint
	 *            the current token
	 */
	@Override
	public void call(VirtualFrame truffleFrame, final State state, final Stack frame, final int codepoint) {
        for (final Production production : alternatives) {
            production.schedule(truffleFrame, state, frame);
        }
    }
}
