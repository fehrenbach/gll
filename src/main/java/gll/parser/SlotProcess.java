/**
 * 
 */
package gll.parser;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import gll.grammar.Slot;
import gll.gss.Stack;
import gll.sppf.Intermediate;

/**
 * A process descriptor during parsing.
 * 
 * @author Tillmann Rendel
 */
public class SlotProcess extends Process {
    private class RootNode extends com.oracle.truffle.api.nodes.RootNode {
        @Child Slot slot;

        public RootNode(Slot slot) {
            this.slot = slot;
        }

        @Override
        public Object execute(VirtualFrame frame) {
            Object[] arguments = frame.getArguments();
            State state = (State) arguments[0];
            int codepoint = (int) arguments[1];
            slot.parse(frame, state, stack, derivation, codepoint);
            return null;
        }
    }

	/**
	 * Current derivation.
	 */
	private final Intermediate<?> derivation;

	/**
	 * This encapsulates the Slot we have to parse next
	 */
	private final CallTarget callTarget;

	/**
	 * Create Descriptor.
	 * 
	 * @param slot
	 *            the grammar slot we have to parse next
	 * @param stack
	 *            our stack
	 */
	public SlotProcess(final Slot slot, final Stack stack, final Intermediate<?> derivation) {
		super(stack);
		this.callTarget = Truffle.getRuntime().createCallTarget(new RootNode(slot));
		this.derivation = derivation;
	}

	/**
	 * Execute one step of this process.
	 * 
	 * @param state
	 *            the parser state
	 * @param codepoint
	 *            the codepoint to parse
	 */
	@Override
	public void execute(final State state, final int codepoint) {
        callTarget.call(state, codepoint);
	}
}
