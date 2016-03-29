package com.mobenga.ngen.xml.parser;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Contains settings for one XML element to be utilized by the NGen XML Parser ({@code DocumentParser}).
 * One ElementParserSettings object must exist for each unique XML tag that shall be parsed. The NGen XML
 * Document Parser is a StAX parser and this setup structure describes how to handle each event is the StAX Parser
 * output stream. All setup expect the element name in this object is optional.
 * <p>
 * By specifying expected sub XML tags for each XML tag using the {@link ElementParserSettings#setSubElementParsers setSubElementParsers}, a tree of ElementParserSettings-objects is created where each node contains parser settings for all legal sub nodes. Examples are available in the project readme-file including links to example source code.
 *
 * @see DocumentParser
 */
public class ElementParserSettings {
    private String elementName;
    private List<AttributeMapping> attributeMappings;
    private List<ElementTextMapping> elementTextMappings;
    private Map<String, ElementParserSettings> subElementParsers;
    private Consumer<ProtectedClassMap> elementStartProcessor;
    private BiConsumer<ProtectedClassMap, String> elementStartProcessorBi;
    private String elementStartProcessorBiAttributeName;
    private Consumer<ProtectedClassMap> elementEndProcessor;

    /**
     * Name of the XML Element that this setting must be applied to.
     *
     * @param elementName Case sensitive name of the XML element.
     */
    public ElementParserSettings(String elementName) {
        this.elementName = elementName;
    }

    /**
     * Get the name of the XML Element that this setting applies to.
     *
     * @return The XML Element name.
     */
    public String getElementName() {
        return elementName;
    }

    /**
     * Returns a Map of {@code ElementParserSettings} for handled sub element.
     * The key is the sub element name and the value is the settings structure.
     *
     * @return Map of handled sub events. May be null.
     */
    public Map<String, ElementParserSettings> getSubElementParsers() {
        return this.subElementParsers;
    }

    /**
     * Set one or several {@code ElementParserSettings} that may handle sub
     * elements to this element.
     *
     * @param subElementParsers One or several ElementParserSettings, or null to clear the list.
     */
    public void setSubElementParsers(ElementParserSettings... subElementParsers) {
        if (null == subElementParsers) {
            this.subElementParsers = new HashMap<>();
        } else {
            this.subElementParsers = new HashMap<>(subElementParsers.length);
            Arrays.asList(subElementParsers).stream().forEach(m -> this.subElementParsers.put(m.getElementName(), m));
        }
    }

    List<AttributeMapping> getAttributeMappings() {
        return attributeMappings;
    }

    /**
     * Set one or several {@code AttributeMapping} that may handle attributes
     * to this element.
     *
     * @param attributeMappings One or several AttributeMapping, or null to clear the list.
     */
    public void setAttributeMappings(AttributeMapping... attributeMappings) {
        if (null == attributeMappings) {
            this.attributeMappings = new ArrayList<>();
        } else {
            this.attributeMappings = Arrays.asList(attributeMappings);
        }
    }

    List<ElementTextMapping> getElementTextMappings() {
        return elementTextMappings;
    }

    /**
     * Set one or several {@code ElementTextMapping} that may handle text content
     * within this element.
     *
     * @param elementTextMappings One or several ElementTextMapping, or null to clear the list.
     */
    public void setElementTextMappings(ElementTextMapping... elementTextMappings) {
        if (null == elementTextMappings) {
            this.elementTextMappings = new ArrayList<>();
        } else {
            this.elementTextMappings = Arrays.asList(elementTextMappings);
        }
    }

    Consumer<ProtectedClassMap> getElementStartProcessor() {
        return elementStartProcessor;
    }

    BiConsumer<ProtectedClassMap, String> getElementStartProcessorBi() {
        return elementStartProcessorBi;
    }

    String getElementStartProcessorBiAttributeName() {
        return elementStartProcessorBiAttributeName;
    }

    /**
     * The start processor is a Lambda based callback that is invoked each time a start tag
     * for this XML element is found. The responsibilities of the start processor is typically
     * to create new java objects that this XML element maps to.
     *
     * @param elementStartProcessor A Consumer method to be invoked. (See {@code java.util.function.Consumer})
     */
    public void setElementStartProcessor(Consumer<ProtectedClassMap> elementStartProcessor) {
        this.elementStartProcessor = elementStartProcessor;
        this.elementStartProcessorBi = null;
    }

    /**
     * The start processor is a Lambda based callback that is invoked each time a start tag
     * for this XML element is found. The responsibilities of the start processor is typically
     * to create new java objects that this XML element maps to.
     *
     * @param elementStartProcessor A BiConsumer method to be invoked. (See {@code java.util.function.BiConsumer})
     * @param field An attribute name whose value shall be passed to the BiConsumer invocation.
     */
    public void setElementStartProcessor(BiConsumer<ProtectedClassMap, String> elementStartProcessor, String field) {
        this.elementStartProcessorBi = elementStartProcessor;
        this.elementStartProcessorBiAttributeName = field;
        this.elementStartProcessor = null;
    }

    Consumer<ProtectedClassMap> getElementEndProcessor() {
        return elementEndProcessor;
    }

    /**
     * The end processor is a Lambda based callback that is invoked each time an end tag
     * for this XML element is found. The responsibilities of the end processor is typically
     * to clean up objects in the current object tree.
     *
     * @param elementEndProcessor A Consumer method to be invoked. (See {@code java.util.function.Consumer})
     */
    public void setElementEndProcessor(Consumer<ProtectedClassMap> elementEndProcessor) {
        this.elementEndProcessor = elementEndProcessor;
    }
}
