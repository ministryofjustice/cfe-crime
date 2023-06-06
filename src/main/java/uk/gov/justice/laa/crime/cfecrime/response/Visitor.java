package uk.gov.justice.laa.crime.cfecrime.response;

import java.math.BigDecimal;

// This can be used to create anything - JSON, maps whatever we want
public interface Visitor {
    void visit(Iterable<String> context, String name, boolean value);

    void visit(Iterable<String> context, String name, BigDecimal value);

    void visit(Iterable<String> context, String name, String value);
}
