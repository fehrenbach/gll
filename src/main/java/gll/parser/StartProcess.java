/**
 * 
 */
package gll.parser;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import gll.grammar.Sort;
import gll.gss.Stack;

/**
 * @author Tillmann Rendel
 * 
 */
public class StartProcess extends Process {
    private class RootNode extends com.oracle.truffle.api.nodes.RootNode {
        @Child Sort sort;

        public RootNode(Sort sort) {
            this.sort = sort;
        }

        @Override
        public Object execute(VirtualFrame frame) {
            Object[] arguments = frame.getArguments();
            State state = (State) arguments[0];
            int codepoint = (int) arguments[1];
            sort.call(frame, state, stack, codepoint);
            return null;
        }
    }


    private final CallTarget callTarget;

	/**
	 * Create start process.
	 * 
	 * @param stack
	 * @param sort
	 */
	public StartProcess(final Stack stack, final Sort sort) {
		super(stack);
		this.callTarget = Truffle.getRuntime().createCallTarget(new RootNode(sort));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(final State state, final int codepoint) {
        callTarget.call(state, codepoint);
	}
}
