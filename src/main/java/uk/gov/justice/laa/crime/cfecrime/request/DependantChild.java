package uk.gov.justice.laa.crime.cfecrime.request;

//            "dependant_children":
//      [
//            {
//                "age_category": "5-7"   enum
//                "count": 2
//            },
//            {
//                "age_category": "8-9"
//                "count": 2
//            }
//     ]

import uk.gov.justice.laa.crime.cfecrime.request.AgeCategory;

public class DependantChild {
    private AgeCategory ageCategory;
    private int count;
}
