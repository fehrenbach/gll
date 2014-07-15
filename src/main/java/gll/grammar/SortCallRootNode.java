package gll.grammar;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.RootNode;
import gll.gss.Stack;
import gll.parser.State;

class SortCallRootNode extends RootNode {
    class ArgumentNode<T> extends Node {
        private final int i;

        public ArgumentNode(int i) {
            this.i = i;
        }

        T execute(VirtualFrame truffleFrame) {
            return (T) truffleFrame.getArguments()[i];
        }
    }

    @Children private final Production[] alternatives;
    @Child private ArgumentNode<State> stateArgument;
    @Child private ArgumentNode<Stack> frameArgument;
    @Child private ArgumentNode codepointArgument;

    private final SortIdentifier sort;

    public SortCallRootNode(SortIdentifier sortIdentifier, Production[] alternatives) {
        this.alternatives = alternatives;
        this.stateArgument = new ArgumentNode<State>(0);
        this.frameArgument = new ArgumentNode<Stack>(1);
        this.codepointArgument = new ArgumentNode(2);
        this.sort = sortIdentifier;
        adoptChildren();
    }

    @Override @ExplodeLoop
    public Object execute(VirtualFrame truffleFrame) {
        State state = stateArgument.execute(truffleFrame);
        Stack frame = frameArgument.execute(truffleFrame);
        int codepoint = (int) codepointArgument.execute(truffleFrame);
        for (Production production : alternatives) {
            production.schedule(truffleFrame, codepoint, state, frame);
        }
        return null;
    }
}
