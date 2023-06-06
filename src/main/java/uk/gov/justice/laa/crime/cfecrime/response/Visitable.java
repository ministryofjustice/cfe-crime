package uk.gov.justice.laa.crime.cfecrime.response;

import java.util.Collection;

// This and Visitor represent the input/output data structure from CMA - essentially a hierarchy of fields that can
// be traversed by a 'visitor' to create whatever concrete structures are desired (2 examples being HashMap, JSON (via JSONObject)

public interface Visitable {
    // context is the tree 'keys' in order - so the Visitor doesn't have to keep track
    // of flow up and down the tree as nodes are visited.
    void visit(Collection<String> context, Visitor v);
}
