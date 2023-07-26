package uk.gov.justice.laa.crime.cfecrime.cma.enums;

//import net.minidev.json.JSONObject;

import uk.gov.justice.laa.crime.cfecrime.cma.response.Visitable;
import uk.gov.justice.laa.crime.cfecrime.cma.response.Visitor;

import java.math.BigDecimal;
import java.util.Collection;

public class DecimalValue implements Visitable {
    private final String name;
    private final BigDecimal value;

    public DecimalValue(String name, BigDecimal value)
    {
        this.name = name;
        this.value = value;
    }

    @Override
    public void visit(Collection<String> context, Visitor visitor) {
        visitor.visit(context, name, value);
    }
}
