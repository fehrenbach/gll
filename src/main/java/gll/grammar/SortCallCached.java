package gll.grammar;

import com.oracle.truffle.api.Assumption;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.nodes.InvalidAssumptionException;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.RootNode;
import gll.gss.Stack;
import gll.parser.State;

public class SortCallCached extends Symbol {
    class ArgumentNode<T> extends Node {

        private final int i;

        public ArgumentNode(int i) {
            this.i = i;
        }

        T execute(VirtualFrame truffleFrame) {
            return (T) truffleFrame.getArguments()[i];
        }
    }
    class SortCallRootNode extends RootNode {
        @Children private final Production[] alternatives;
        @Child private ArgumentNode<State> stateArgument;
        @Child private ArgumentNode<Stack> frameArgument;

        SortCallRootNode(Production[] alternatives) {
            this.alternatives = alternatives;
            this.stateArgument = new ArgumentNode<State>(0);
            this.frameArgument = new ArgumentNode<Stack>(1);
            adoptChildren();
        }

        @Override
        public Object execute(VirtualFrame truffleFrame) {
            State state = stateArgument.execute(truffleFrame);
            Stack frame = frameArgument.execute(truffleFrame);
            for (Production production : alternatives) {
                production.schedule(truffleFrame, state, frame);
            }
            return null;
        }
    }

    final SortIdentifier sort;
    @Child private DirectCallNode directCallNode;
    final Assumption grammarUnchanged;

    /**
     * Do not call this directly. Always go through State.cacheSortCall
     */
    public SortCallCached(SortIdentifier sort, Assumption grammarUnchanged, Production[] productions) {
        super(sort.name);
        this.sort = sort;
        this.grammarUnchanged = grammarUnchanged;
        directCallNode = Truffle.getRuntime().createDirectCallNode(Truffle.getRuntime().createCallTarget(new SortCallRootNode(productions)));
        adoptChildren();
    }

    @Override
    public void call(VirtualFrame truffleFrame, State state, Stack frame, int codepoint) {
        try {
            grammarUnchanged.check();
            directCallNode.call(truffleFrame, new Object[]{state, frame});
        } catch (InvalidAssumptionException e) {
            SortCall replacementNode = new SortCall(sort);
            replace(replacementNode, this + " changed");
            replacementNode.call(truffleFrame, state, frame, codepoint);
        }
    }
}
