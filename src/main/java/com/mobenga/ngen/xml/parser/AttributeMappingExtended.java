package com.mobenga.ngen.xml.parser;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * The Extended Attribute Mapping an specialization of {@link AttributeMapping} with access
 * to the Object Branch. See {@link com.mobenga.ngen.xml.parser.ProtectedClassMap}
 *
 * @param <T> Map the field to an object of this class
 * @param <K> Type of the value to be mapped
 */
public class AttributeMappingExtended<T, K> extends AttributeMapping<T, K> {

    /**
     * Constructs an Attribute Mapping for a single attribute, with access to the Object Branch in the mapper function.
     * This is a special case of the {@link AttributeMapping} setup that's needed when more data from previously parsed xml
     * elements and tags are needed by the mapper function. When parsing for example a product tree for an e-commerce
     * data structure, the attribute mapper for a product needs knowledge of the product category that is currently parsed.
     *
     * @param type Map the field to an object of this class
     * @param setter Setter method on the class described as a BiConsumer.
     *               (A non static method reference executed with a single argument and no return value.)
     * @param mapper Mapper method used to map the XML string to desired java type.
     *               (A static method reference executed with a two arguments and a return value.
     *               The second argument will be of type ProtectedClassMap and contain a reference to the object branch.)
     * @param fields One or several attribute name whose value shall be passed to the Mapper invocation.
     *               If no attribute names are provided the mapper and the setter will not be executed.
     *               To set values that does not depend on an attribute, use the Start or End Processor callback.
     */
    public AttributeMappingExtended(Class<T> type, BiConsumer<T, K> setter, BiFunction<Map<String, String>, ProtectedClassMap, K> mapper, String... fields) {
        super(type, setter, null, fields);
        super.setBiMapper(mapper);
    }
}
