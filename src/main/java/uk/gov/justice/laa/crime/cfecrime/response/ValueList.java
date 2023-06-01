package uk.gov.justice.laa.crime.cfecrime.response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ValueList implements Visitable{
    private final String name;
    private final Collection<Visitable> children;

    public ValueList(String name, Collection<Visitable> children) {
        this.name = name;
        this.children = children;
    }

    @Override
    public void visit(Collection<String> context, Visitor visitor) {
        List<String> newContext = new ArrayList<>(context);
        newContext.add(name);
        for (var child: children) {
            child.visit(newContext, visitor);
        }
    }
}
