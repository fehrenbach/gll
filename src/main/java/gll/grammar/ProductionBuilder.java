package gll.grammar;

import java.util.List;
import java.util.stream.Collectors;

public class ProductionBuilder {
    final List<PrettyGrammar> symbols;

    ProductionBuilder(List<PrettyGrammar> symbols) {
        this.symbols = symbols;
    }

    Production build(SortIdentifier sort) {
        List<Symbol> symbols = this.symbols.stream().map(PrettyGrammar::toSymbol).collect(Collectors.toList());
        return new Production(sort, symbols.toArray(new Symbol[symbols.size()]));
    }
}
