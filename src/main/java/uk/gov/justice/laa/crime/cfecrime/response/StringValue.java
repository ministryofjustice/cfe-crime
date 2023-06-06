package uk.gov.justice.laa.crime.cfecrime.response;

//import net.minidev.json.JSONObject;

import java.util.Collection;

public class StringValue implements Visitable {
    private final String name;
    private final String value;

    public StringValue(String name, String value) {
        this.name = name;
        this.value = value;
    }
    @Override
    public void visit(Collection<String> context, Visitor visitor) {
        visitor.visit(context, name, value);
    }
}
