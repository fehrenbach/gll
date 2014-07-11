package gll.grammar;

import gll.sppf.DerivationLabel;

/**
 * Objects of this class identify sorts (= non-terminals) that appear in the grammar.
 * This is needed to break cycles in the grammar graph (now an AST).
 */
public class SortIdentifier implements DerivationLabel {
    public final String name;

    public SortIdentifier(String name) {
        assert name != null;
        this.name = name;
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
}
