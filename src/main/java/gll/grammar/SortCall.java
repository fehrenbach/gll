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
        SortCallCached replacement = new SortCallCached(sort);
        replace(replacement, "Called " + this + " for the first time at this call site");
        replacement.call(truffleFrame, state, frame, codepoint);
    }
}
