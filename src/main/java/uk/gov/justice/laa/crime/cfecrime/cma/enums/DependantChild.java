package uk.gov.justice.laa.crime.cfecrime.cma.enums;

import lombok.Getter;
import lombok.Setter;

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
@Setter
@Getter
public class DependantChild {
    private AgeCategory ageCategory;
    private int count;
}
