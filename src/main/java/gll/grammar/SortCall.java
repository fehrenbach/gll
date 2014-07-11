package gll.grammar;

import com.oracle.truffle.api.frame.VirtualFrame;
import gll.gss.Stack;
import gll.parser.State;

public class SortCall extends Symbol {
    final SortIdentifier sort;

    public SortCall(SortIdentifier sort) {
        super(sort.name);
        this.sort = sort;
    }

    public SortCall(String name) {
        this(new SortIdentifier(name));
    }

    @Override
    public void call(VirtualFrame truffleFrame, State state, Stack frame, int codepoint) {
        SortCallCached replacement = state.getGrammar().cacheSortCall(sort);
        replace(replacement, "Called " + this);
        replacement.call(truffleFrame, state, frame, codepoint);
    }
}
