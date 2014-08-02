package gll.parser;

import com.oracle.truffle.api.frame.VirtualFrame;
import gll.grammar.SortCallCached;
import gll.grammar.SortIdentifier;
import gll.grammar.Symbol;
import gll.gss.Stack;

class StartProcessRootNode extends com.oracle.truffle.api.nodes.RootNode {
    @Child Symbol sort;
    private final Stack stack;

    public StartProcessRootNode(Stack stack, SortIdentifier sort) {
        this.stack = stack;
        this.sort = new SortCallCached(sort);
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
