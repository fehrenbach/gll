package gll.grammar;

import com.oracle.truffle.api.Assumption;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.nodes.InvalidAssumptionException;
import com.oracle.truffle.api.nodes.RootNode;
import gll.gss.Stack;
import gll.parser.State;

public class SortCallCached extends Symbol {
    class SortCallRootNode extends RootNode {
        @Children private final Production[] alternatives;

        SortCallRootNode(Production[] alternatives) {
            this.alternatives = alternatives;
            adoptChildren();
        }

        @Override
        public Object execute(VirtualFrame truffleFrame) {
            Object[] arguments = truffleFrame.getArguments();
            State state = (State) arguments[0];
            Stack frame = (Stack) arguments[1];
            for (final Production production : alternatives) {
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
