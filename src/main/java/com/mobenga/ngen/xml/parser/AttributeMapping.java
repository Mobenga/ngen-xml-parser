package com.mobenga.ngen.xml.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The Attribute Mapping is used to describe how to map an XML Element attribute
 * to a Java object. The data transfer is described in two steps. In the first
 * step (the mapping) the data is transformed from a String to a Java type. In the second step
 * (setting the attribute) the data is injected into a Java object via a lambda function.
 *
 * @param <T> Map the field to an object of this class
 * @param <K> Type of the value to be mapped
 */

public class AttributeMapping<T, K> {
    private final Class<T> resultingFieldType;
    private final Predicate<Object> objectBranchExtractor;
    private final Map<String, String> values;
    private BiConsumer<T, K> setter;
    private Function<Map<String, String>, K> mapper = null;
    private BiFunction<Map<String, String>, BranchContext, K> biMapper = null;

    /**
     * Constructs an Attribute Mapping for a single attribute
     * @param resultingFieldType Map the field to an object of this class
     * @param setter Setter method on the class described as a BiConsumer.
     *               (A non static method reference executed with a single argument and no return value.)
     * @param mapper Mapper method used to map the XML string to desired java type.
     *               (A static method reference executed with a single argument and a return value.)
     *               The most common data mappers are available in {@link  com.mobenga.ngen.xml.util.MappingUtil}
     *               By providing several xml attributes, them mapper will be invoked with several inputs.
     *               Several attributes are provided by listing several fields as the last parameters.
     *               This is needed when a java attribute is the result of a mapping from several input values.
     * @param fields One or several attribute name whose value shall be passed to the Mapper invocation.
     *               If no attribute names are provided the mapper and the setter will not be executed.
     *               To set values that does not depend on an attribute, use the Start or End Processor callback.
     */
    public AttributeMapping(Class<T> resultingFieldType, BiConsumer<T, K> setter, Function<Map<String, String>, K> mapper, String... fields) {
        values = new HashMap<>(fields.length);
        for (String field : fields) {
            values.put(field, null);
        }
        this.setter = setter;
        this.mapper = mapper;
        this.resultingFieldType = resultingFieldType;
        this.objectBranchExtractor = null;
    }

    /**
     * Constructs an Attribute Mapping for a single attribute
     * @param objectBranchPredicate predicate to find object in objectBranch collection
     * @param setter Setter method on the class described as a BiConsumer.
     *               (A non static method reference executed with a single argument and no return value.)
     * @param mapper Mapper method used to map the XML string to desired java type.
     *               (A static method reference executed with a single argument and a return value.)
     *               The most common data mappers are available in {@link  com.mobenga.ngen.xml.util.MappingUtil}
     *               By providing several xml attributes, them mapper will be invoked with several inputs.
     *               Several attributes are provided by listing several fields as the last parameters.
     *               This is needed when a java attribute is the result of a mapping from several input values.
     * @param fields One or several attribute name whose value shall be passed to the Mapper invocation.
     *               If no attribute names are provided the mapper and the setter will not be executed.
     *               To set values that does not depend on an attribute, use the Start or End Processor callback.
     */
    public  AttributeMapping(Predicate<Object> objectBranchPredicate, BiConsumer<T, K> setter, Function<Map<String, String>, K> mapper, String... fields) {
        values = new HashMap<>(fields.length);
        for (String field : fields) {
            values.put(field, null);
        }
        this.setter = setter;
        this.mapper = mapper;
        this.resultingFieldType = null;
        this.objectBranchExtractor = objectBranchPredicate;
    }

    void apply(BranchContext objectBranch, AttributeMapping<T, ?> m) {
        T obj = (m.getResultingFieldType() != null) ? objectBranch.getInstance(m.getResultingFieldType()) : objectBranch.getInstance(m.getObjectBranchExtractor());
        if (null != obj && !values.isEmpty()) {
            K mappedValue = null;
            if (null != mapper) {
                mappedValue = mapper.apply(values);
            } else if (null != biMapper) {
                mappedValue = biMapper.apply(values, objectBranch);
            }
            setter.accept(obj, mappedValue);
        }
    }

    void setValue(String key, String value) {
        values.put(key, value);
    }

    void setBiMapper(BiFunction<Map<String, String>, BranchContext, K> biMapper) {
        if (null != mapper) {
            throw new IllegalStateException("Use either of the mappers");
        }
        this.biMapper = biMapper;
    }

    Set<String> getKeys() {
        return values.keySet();
    }

    Class<T> getResultingFieldType() {
        return resultingFieldType;
    }

    public Predicate<Object> getObjectBranchExtractor() {
        return objectBranchExtractor;
    }
}
