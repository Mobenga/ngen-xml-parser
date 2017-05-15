package com.mobenga.ngen.xml.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The NGen XML Document Parser is a direct mapped XML StAX parser.
 * It maps XML tags directly to an arbitrary java object structure that
 * does not necessarily have to reflect the XML document structure.
 * More information including examples are found at Confluence.
 */
public class DocumentParser {
    private static final Logger log = LoggerFactory.getLogger(DocumentParser.class);
    private final Deque<ElementParserSettings> documentParserStack = new ArrayDeque<>();
    private final BranchContext currentElementBranch;

    /**
     * Create a new XML document parser with the provided mappings
     *
     * @param mappings ElementParserSettings for the top XML element.
     */
    public DocumentParser(Mappings mappings) {
        this(mappings, new ProtectedClassMap());
    }

    /**
     * Create a new XML document parser with the provided mappings, starting of with the
     * provided {@link com.mobenga.ngen.xml.parser.BranchContext object branch}. By providing an existing object branch, it is possible to
     * parse an XML and map it into an existing java object structure.
     *
     * @param mappings     ElementParserSettings for the top XML element.
     * @param objectBranch Object branch typically pre loaded with the top element in order to add information to existing java objects.
     * @see BranchContext
     */
    public DocumentParser(Mappings mappings, BranchContext objectBranch) {
        this.currentElementBranch = objectBranch;
        if (null == mappings || null == mappings.getSettings()) {
            throw new IllegalArgumentException("Incorrect Mappings was provided to the " + getClass());
        }
        ElementParserSettings elementParserSettings = mappings.getSettings();
        ElementParserSettings root = new ElementParserSettings("root");
        root.setSubElementParsers(elementParserSettings);
        this.documentParserStack.push(root);
    }

    void parseStartElement(XMLEvent xmlEvent) {
        StartElement startElement = xmlEvent.asStartElement();
        String elementName = startElement.getName().getLocalPart();
        log.debug("parseStartElement({})", elementName);
        if (documentParserStack.isEmpty()) {
            throw new IllegalStateException("Event Parser must be initialized with initializeStartDocumentParser.");
        }
        ElementParserSettings documentParserSettings = getParserSettings(elementName);
        if (documentParserSettings != null) {
            documentParserStack.push(documentParserSettings);
            executeStartProcessor(startElement);
            parseAttributes(startElement);
        }
    }

    private void executeStartProcessor(StartElement startElement) {
        Consumer<BranchContext> startProcessor = documentParserStack.peek().getElementStartProcessor();
        if (startProcessor != null) {
            startProcessor.accept(this.currentElementBranch);
        } else {
            BiConsumer<BranchContext, String> startProcessorBi = documentParserStack.peek().getElementStartProcessorBi();
            String field = documentParserStack.peek().getElementStartProcessorBiAttributeName();
            if (null != startProcessorBi && null != field) {
                String attribute = getAttribute(field, startElement);
                startProcessorBi.accept(this.currentElementBranch, attribute);
            }
        }
    }

    private ElementParserSettings getParserSettings(String elementName) {
        ElementParserSettings documentParserSettings = null;
        Map<String, ElementParserSettings> subElementParsers = documentParserStack.peek().getSubElementParsers();
        if (null != subElementParsers && !subElementParsers.isEmpty()) {
            documentParserSettings = subElementParsers.get(elementName);
        }
        if (null == documentParserSettings) {
            log.debug("Element \"{}\" is skipped (by DocumentParserSettings) as a sub element of \"{}\".", elementName, documentParserStack.peek().getElementName());
            return null;
        }
        return documentParserSettings;
    }

    private void parseAttributes(StartElement startElement) {
        List<AttributeMapping> mappings = documentParserStack.peek().getAttributeMappings();
        if (null != mappings) {
            for (AttributeMapping<?, ?> m : mappings) {
                applyMapping(startElement, m);
            }
        }
    }

    private <T> void applyMapping(StartElement startElement, AttributeMapping<T, ?> m) {
        if (!m.getKeys().isEmpty()) {
            for (String key : m.getKeys()) {
                m.setValue(key, getAttribute(key, startElement));
            }
        }
        m.apply(this.currentElementBranch, m);
    }

    private static String getAttribute(String qName, StartElement startElement) {
        Attribute idAttr = startElement.getAttributeByName(QName.valueOf(qName));
        return null == idAttr ? null : idAttr.getValue();
    }

    void parseEndElement(XMLEvent xmlEvent) {
        String elementName = xmlEvent.asEndElement().getName().getLocalPart();
        log.debug("parseEndElement({})", elementName);

        if (elementName.equals(documentParserStack.peek().getElementName())) {
            Consumer<BranchContext> endProcessor = documentParserStack.pop().getElementEndProcessor();
            if (endProcessor != null) {
                endProcessor.accept(this.currentElementBranch);
            }
        }
    }

    <T> T getResult(Class<T> objectType) {
        return currentElementBranch.pop(objectType);
    }

    void parseCharacters(String data) {
        String trimmedData = data.trim();
        if (!trimmedData.isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug("parseCharacters({}) for {}", trimForLogging(trimmedData), documentParserStack.peek().getElementName());
            }
            List<ElementTextMapping> mappings = documentParserStack.peek().getElementTextMappings();
            if (null != mappings) {
                for (ElementTextMapping<?, ?> m : mappings) {
                    applyElementTextMapping(trimmedData, m);
                }
            }
        }
    }

    private <T, K> void applyElementTextMapping(String data, ElementTextMapping<T, K> m) {
        m.setValue(data);
        if (log.isWarnEnabled() && null == this.currentElementBranch.getInstance(m.getType())) {
            log.warn("No object of required type {} is created and setting this content is depending on that object. Content data: \"{}\" will be dismissed.", m.getType().getName(), trimForLogging(data));
        }
        m.apply(this.currentElementBranch.getInstance(m.getType()));
    }

    private String trimForLogging(String data) {
        final int MAX_LOG_OUTPUT = 100;
        return data.substring(0, Math.min(data.length(), MAX_LOG_OUTPUT)) + (data.length() > MAX_LOG_OUTPUT ? "..." : "");
    }
}
