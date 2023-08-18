package uk.gov.justice.laa.crime.meansassessment.model.common.stateless;

//TODO - copied from generated code in CMA(2) - needs to be in source control instead

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.stateless.AgeRange;

import javax.annotation.processing.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "age_range",
    "count"
})
@Generated("jsonschema2pojo")
public class DependantChild {

    /**
     * Age Range of children
     * (Required)
     * 
     */
    @JsonProperty("age_range")
    @JsonPropertyDescription("Age Range of children")
    @Valid
    @NotNull
    private AgeRange ageRange;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("count")
    @NotNull
    private Integer count;
    protected final static Object NOT_FOUND_VALUE = new Object();

    /**
     * No args constructor for use in serialization
     * 
     */
    public DependantChild() {
    }

    /**
     * 
     * @param ageRange
     * @param count
     */
    public DependantChild(AgeRange ageRange, Integer count) {
        super();
        this.ageRange = ageRange;
        this.count = count;
    }

    /**
     * Age Range of children
     * (Required)
     * 
     */
    @JsonProperty("age_range")
    public AgeRange getAgeRange() {
        return ageRange;
    }

    /**
     * Age Range of children
     * (Required)
     * 
     */
    @JsonProperty("age_range")
    public void setAgeRange(AgeRange ageRange) {
        this.ageRange = ageRange;
    }

    public DependantChild withAgeRange(AgeRange ageRange) {
        this.ageRange = ageRange;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("count")
    public Integer getCount() {
        return count;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("count")
    public void setCount(Integer count) {
        this.count = count;
    }

    public DependantChild withCount(Integer count) {
        this.count = count;
        return this;
    }

    protected boolean declaredProperty(String name, Object value) {
        if ("age_range".equals(name)) {
            if (value instanceof AgeRange) {
                setAgeRange(((AgeRange) value));
            } else {
                throw new IllegalArgumentException(("property \"age_range\" is of type \"uk.gov.justice.laa.crime.meansassessment.staticdata.enums.stateless.AgeRange\", but got "+ value.getClass().toString()));
            }
            return true;
        } else {
            if ("count".equals(name)) {
                if (value instanceof Integer) {
                    setCount(((Integer) value));
                } else {
                    throw new IllegalArgumentException(("property \"count\" is of type \"java.lang.Integer\", but got "+ value.getClass().toString()));
                }
                return true;
            } else {
                return false;
            }
        }
    }

    protected Object declaredPropertyOrNotFound(String name, Object notFoundValue) {
        if ("age_range".equals(name)) {
            return getAgeRange();
        } else {
            if ("count".equals(name)) {
                return getCount();
            } else {
                return notFoundValue;
            }
        }
    }

    @SuppressWarnings({
        "unchecked"
    })
    public<T >T get(String name) {
        Object value = declaredPropertyOrNotFound(name, DependantChild.NOT_FOUND_VALUE);
        if (DependantChild.NOT_FOUND_VALUE!= value) {
            return ((T) value);
        } else {
            throw new IllegalArgumentException((("property \""+ name)+"\" is not defined"));
        }
    }

    public void set(String name, Object value) {
        if (!declaredProperty(name, value)) {
            throw new IllegalArgumentException((("property \""+ name)+"\" is not defined"));
        }
    }

    public DependantChild with(String name, Object value) {
        if (!declaredProperty(name, value)) {
            throw new IllegalArgumentException((("property \""+ name)+"\" is not defined"));
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(DependantChild.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("ageRange");
        sb.append('=');
        sb.append(((this.ageRange == null)?"<null>":this.ageRange));
        sb.append(',');
        sb.append("count");
        sb.append('=');
        sb.append(((this.count == null)?"<null>":this.count));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.ageRange == null)? 0 :this.ageRange.hashCode()));
        result = ((result* 31)+((this.count == null)? 0 :this.count.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DependantChild) == false) {
            return false;
        }
        DependantChild rhs = ((DependantChild) other);
        return (((this.ageRange == rhs.ageRange)||((this.ageRange!= null)&&this.ageRange.equals(rhs.ageRange)))&&((this.count == rhs.count)||((this.count!= null)&&this.count.equals(rhs.count))));
    }

}
