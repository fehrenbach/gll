package gll.parser;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;
import gll.gss.Stack;
import gll.sppf.TerminalSymbolDerivation;

/**
 * This used to be the FutureProcess.
 *
 * This is used to delay execution of something for the next input token.
 * These nodes should only ever be created in State.scheduleLater and then stored (as call targets) in State.future.
 */
public class FutureProcessRootNode extends RootNode {
    private final Stack stack;
    private final TerminalSymbolDerivation derivation;

    public FutureProcessRootNode(Stack stack, TerminalSymbolDerivation derivation) {
        this.stack = stack;
        this.derivation = derivation;
    }

    @Override
    public Object execute(VirtualFrame truffleFrame) {
        Object[] arguments = truffleFrame.getArguments();
        ParsingState state = (ParsingState) arguments[0];
        int codepoint = (int) arguments[1];
        stack.schedule(truffleFrame, state, derivation, codepoint);
        return null;
    }
}
