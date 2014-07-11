package gll.grammar;

import com.oracle.truffle.api.Assumption;
import com.oracle.truffle.api.Truffle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Grammar {

    private final HashMap<SortIdentifier, Set<Production>> grammar = new HashMap<>();
    private final HashMap<SortIdentifier, Assumption> grammarUnchanged = new HashMap<>();

    public void addProductionsToSort(SortIdentifier sort, Production... newProductions) {
        Set<Production> existingProductions = grammar.get(sort);
        if (existingProductions == null) existingProductions = new HashSet<>();
        existingProductions.addAll(Arrays.asList(newProductions));
        grammar.put(sort, existingProductions);
        Assumption assumption = grammarUnchanged.get(sort);
        if (assumption != null) assumption.invalidate();
    }

    public SortCallCached cacheSortCall(SortIdentifier sort) {
        Production[] alternatives = grammar.get(sort) != null ? grammar.get(sort).toArray(new Production[]{}) : new Production[]{};
        Assumption assumption = grammarUnchanged.get(sort);
        if (assumption == null) {
            assumption = Truffle.getRuntime().createAssumption(sort.toString() + " unchanged");
            grammarUnchanged.put(sort, assumption);
        }
        return new SortCallCached(sort, assumption, alternatives);
    }
}
