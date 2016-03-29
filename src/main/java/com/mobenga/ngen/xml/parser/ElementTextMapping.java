package com.mobenga.ngen.xml.parser;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The element text mapping is used to describe how to map an XML Element text content
 * to a Java object. The data transfer is described in two steps. In the first
 * step the data is transformed from a String to an arbitrary Java type with arbitrary content.
 * In the second step the data is injected into a Java object via a lambda function.
 * <p>
 * Note that if the element text is used to set several different java object fields, several
 * ElementTextMappings can be specified each using a different mappers and setters.
 *
 * @param <T> Map the field to an object of this class
 * @param <K> Type of the value to be mapped
 */
public class ElementTextMapping<T, K> {
    private final Class<T> type;
    private String value = "";
    private BiConsumer<T, K> setter;
    private Function<String, K> mapper;

    /**
     * Constructs a element text mapping to set a single field to a single java object.
     *
     * @param type Map the field to an object of this class
     * @param setter Setter method on the class described as a BiConsumer.
     *               (A non static method reference executed with a single argument and no return value.)
     * @param mapper Mapper method used to map the XML string to desired java type.
     *               (A static method reference executed with a single argument and a return value.)
     */
    public ElementTextMapping(Class<T> type, BiConsumer<T, K> setter, Function<String, K> mapper) {
        this.type = type;
        this.setter = setter;
        this.mapper = mapper;
    }

    void apply(T obj) {
        if (null != obj) {
            K mappedValue = mapper.apply(value);
            setter.accept(obj, mappedValue);
        }
    }
    Class<T> getType() {
        return type;
    }

    void setValue(String value) {
        this.value = value;
    }
}
