package gll.grammar;

import com.oracle.truffle.api.Assumption;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.nodes.InvalidAssumptionException;
import gll.gss.Stack;
import gll.parser.State;

public class SortCallCached extends Symbol {
    final SortIdentifier sort;
    @Child private DirectCallNode directCallNode;
    final Assumption grammarUnchanged;

    public SortCallCached(SortIdentifier sort) {
        super(sort.name);
        this.sort = sort;
        this.grammarUnchanged = sort.productionUnchanged.getAssumption();
        directCallNode = Truffle.getRuntime().createDirectCallNode(sort.sortCallTarget);
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
