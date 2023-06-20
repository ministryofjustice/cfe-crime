package uk.gov.justice.laa.crime.cfecrime.cma.response;

//import net.minidev.json.JSONObject;

import java.util.Collection;

public class BooleanValue implements Visitable {
    private final String name;
    private final boolean value;

    public BooleanValue(String name, boolean value) {
        this.name = name;
        this.value = value;
    }
    @Override
    public void visit(Collection<String> context, Visitor visitor) {
        visitor.visit(context, name, value);
    }
}
