package com.mobenga.ngen.xml.util;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Predefined mapping functions used when mapping data from xml to specific data types.
 */
public class MappingUtil {

    private MappingUtil() {
    }

    /**
     * This method is provided to an {@link com.mobenga.ngen.xml.parser.AttributeMapping#AttributeMapping(Class, BiConsumer, Function, String...) AttributeMapping}
     * as a mapping method for a single xml attribute to a Boolean Java object. The XML attribute value "YES", "Y" or "TRUE" (case ignored) will be nmapped to true.
     * This method is invoked by the NGen XML XmlParserI engine.
     * @param values Values as a map
     * @return boolean value from string representation
     */
    public static boolean getBoolean(Map<String, String> values) {
        return "TRUE".equalsIgnoreCase(getFirst(values)) || "Y".equalsIgnoreCase(getFirst(values)) || "YES".equalsIgnoreCase(getFirst(values));
    }

    /**
     * This method is provided to an {@link com.mobenga.ngen.xml.parser.AttributeMapping#AttributeMapping(Class, BiConsumer, Function, String...) AttributeMapping}
     * as a mapping method for method for a single xml attribute to an Integer Java object
     * @param values Values as a map
     * @return integer value from string representation. Returns 0 if NumberFormatException.
     */
    public static int getInteger(Map<String, String> values) {
        return toInt(getFirst(values));
    }

    /**
     * This method is provided to an {@link com.mobenga.ngen.xml.parser.AttributeMapping#AttributeMapping(Class, BiConsumer, Function, String...) AttributeMapping}
     * as a mapping method for a single xml attribute to a String Java object.
     * @param values Values as a map
     * @return the first value in the Map. This is convenient to use when only one value is expected.
     */
    public static String getFirst(Map<String, String> values) {
        return values.values().iterator().next();
    }

    /**
     * This method is provided to an {@link com.mobenga.ngen.xml.parser.ElementTextMapping#ElementTextMapping(Class, BiConsumer, Function) ElementTextMapping}
     * as a mapping method for a a xml content to a String Java object. This dummy function if used since the NGen XML XmlParserI engine requires a mapping function.
     * @param value Text Element Value
     * @return Text Element Value
     * */
    public static String getElementText(String value) {
        return value;
    }

    private static int toInt(String str) {
        if(str == null) {
            return 0;
        } else {
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException var3) {
                return 0;
            }
        }
    }
}
