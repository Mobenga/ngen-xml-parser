package com.mobenga.ngen.xml.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.InputStream;

/**
 * Parses an input stream with XML content using the provided document parser.
 */
public class XmlParser {

    private static final Logger log = LoggerFactory.getLogger(XmlParser.class);
    private String encoding;

    /**
     * Creates an XML Parser instance using UTF-8 encoding
     */
    public XmlParser() {
        this.encoding = "UTF-8";
    }

    /**
     * Creates an XML Parser instance using the specified encoding
     * @param encoding the character encoding of the stream
     */
    public XmlParser(String encoding) {
        this.encoding = encoding;
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
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
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
}
