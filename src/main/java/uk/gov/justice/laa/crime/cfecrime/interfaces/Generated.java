package uk.gov.justice.laa.crime.cfecrime.interfaces;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD,ElementType.CONSTRUCTOR})
public @interface Generated {
}
