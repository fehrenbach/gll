package gll.parser;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;
import gll.grammar.Slot;
import gll.gss.Stack;
import gll.sppf.Intermediate;

public class SlotProcessRootNode extends RootNode {
    private final Slot slot;
    private final Stack stack;
    private final Intermediate<?> derivation;

    public SlotProcessRootNode(Slot slot, Stack stack, Intermediate<?> derivation) {
        this.slot = slot;
        this.stack = stack;
        this.derivation = derivation;
    }

    @Override
    public Object execute(VirtualFrame truffleFrame) {
        Object[] arguments = truffleFrame.getArguments();
        State state = (State) arguments[0];
        int codepoint = (int) arguments[1];
        slot.parse(truffleFrame, state, stack, derivation, codepoint);
        return null;
    }
}
