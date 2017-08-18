package com.mobenga.ngen.xml.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Parses an input stream with XML content using the provided document parser.
 */
public class XmlParser {

    private static final Logger log = LoggerFactory.getLogger(XmlParser.class);
    private static final String UTF_8 = "UTF-8";

    private Map<String, Object> xmlInputFactoryProperties;
    private String encoding;

    public static XmlParserBuilder builder() {
        return new XmlParserBuilder();
    }

    /**
     * Creates an XML Parser instance using UTF-8 encoding
     */
    public XmlParser() {
        this(UTF_8);
    }

    /**
     * Creates an XML Parser instance using the specified encoding
     *
     * @param encoding the character encoding of the stream
     */
    public XmlParser(String encoding) {
        this(encoding, null);
    }

    /**
     * Creates an XML Parser instance using the specified encoding
     *
     * @param encoding                  the character encoding of the stream
     * @param xmlInputFactoryProperties the properties that will be used during construction of XMLInputFactory
     */
    private XmlParser(String encoding, Map<String, Object> xmlInputFactoryProperties) {
        this.encoding = encoding;
        this.xmlInputFactoryProperties = xmlInputFactoryProperties;
    }

    /**
     * Parses an input stream with XML content using the provided document parser.
     *
     * @param documentParser Document parser to use for parsing the result
     * @param xmlStream      xml content to parse
     * @param resultClass    Class for the required result type
     * @param <T>            Required result type
     * @return an object of required result type.
     * @throws XMLStreamException    Malformed data causes this exception.
     * @throws IllegalStateException Misconfigured mapping files are the most common cause of this exception.
     */
    public <T> T parseXmlUnsafe(InputStream xmlStream, DocumentParser documentParser, Class<T> resultClass) throws XMLStreamException {
        XMLInputFactory inputFactory = buildXmlInputFactory();
        XMLEventReader eventReader = inputFactory.createXMLEventReader(xmlStream, encoding);

        while (eventReader.hasNext()) {
            XMLEvent xmlEvent = eventReader.nextEvent();

            if (xmlEvent.isStartElement()) {
                documentParser.parseStartElement(xmlEvent);
            } else if (xmlEvent.isCharacters()) {
                documentParser.parseCharacters(((Characters) xmlEvent).getData());
            } else if (xmlEvent.isEndElement()) {
                documentParser.parseEndElement(xmlEvent);
            }
        }
        T resObj = documentParser.getResult(resultClass);
        if (log.isWarnEnabled() && null == resObj) {
            log.warn("No object of requested class was available on the object branch in the document parser.");
        }
        return resObj;
    }

    protected XMLInputFactory buildXmlInputFactory() {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        if (xmlInputFactoryProperties != null && xmlInputFactoryProperties.size() > 0) {
            xmlInputFactoryProperties.forEach(inputFactory::setProperty);
        }
        return inputFactory;
    }

    private static class XmlParserBuilder {
        private Map<String, Object> xmlInputFactoryProperties = new HashMap<>();
        private String encoding = UTF_8;

        private XmlParserBuilder() {
        }

        public XmlParserBuilder encoding(String encoding) {
            this.encoding = encoding;
            return this;
        }

        public XmlParserBuilder addXmlInputFactoryProperty(String key, Object value) {
            xmlInputFactoryProperties.put(key, value);
            return this;
        }

        public XmlParser build() {
            return new XmlParser(encoding, xmlInputFactoryProperties);
        }

    }
}
