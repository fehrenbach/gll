package gll.grammar;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.utilities.CyclicAssumption;
import gll.sppf.DerivationLabel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Objects of this class identify sorts (= non-terminals) that appear in the grammar.
 * This is needed to break cycles in the grammar graph (now an AST).
 */
public class SortIdentifier implements DerivationLabel, PrettyGrammar {
    public final String name;

    public SortIdentifier(String name) {
        assert name != null;
        this.name = name;
        this.productionUnchanged = new CyclicAssumption(name + " unchanged");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SortIdentifier that = (SortIdentifier) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "‹" + name + "›";
    }

    final CyclicAssumption productionUnchanged;
    CallTarget sortCallTarget;

    public void setProductions(ProductionBuilder... productionBuilders) {
        List<Production> productions = Arrays.asList(productionBuilders).stream().map(x -> x.build(this)).collect(Collectors.toList());
        productionUnchanged.invalidate();
        sortCallTarget = Truffle.getRuntime().createCallTarget(new SortCallRootNode(this, productions.toArray(new Production[productions.size()])));
    }

    /* Stuff for pretty grammar representation */
    public static ProductionBuilder production(PrettyGrammar... symbols) {
        return new ProductionBuilder(Arrays.asList(symbols));
    }

    @Override
    public Symbol toSymbol() {
        return new SortCall(this);
    }
}
