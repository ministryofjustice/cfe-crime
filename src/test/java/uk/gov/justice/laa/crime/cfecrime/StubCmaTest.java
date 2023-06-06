package uk.gov.justice.laa.crime.cfecrime;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StubCmaTest {
    @Test
    void callCma() {
        var result = new StubCma().callCma(null);
        var resultAsMap = MapResponse.toMap(result);
        assertEquals(Collections.EMPTY_MAP, resultAsMap.getData());
        Map<String, MapResponse.TreeNode> children = resultAsMap.getChildren();
        assertEquals(Set.of("response"), children.keySet());
        Map<String, MapResponse.TreeNode> responseChildren = children.get("response").getChildren();
        assertEquals(Set.of("full_means_assessment", "initial_means_assessment"), responseChildren.keySet());
        var fullAssessment = responseChildren.get("full_means_assessment");
        assertEquals(Set.of("disposable_income", "result", "eligibility_threshold", "adjusted_living_allowance",
                "total_annual_aggregated_expenditure", "full_threshold", "outcome"), fullAssessment.getData().keySet());
    }
}