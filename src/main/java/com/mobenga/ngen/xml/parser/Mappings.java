package com.mobenga.ngen.xml.parser;

/**
 * Interface used to receive the Element Parser Settings object for the top element.
 * When writing implementations of this interface, make sure you follow the flow of the xml you want to parse.
 * This helps when reading the Mappings implementation side by side with an example xml file.
 * <p>
 * The Mapping Class typically contains methods to create one {@link ElementParserSettings} for each parsable XML element type. (For larger XML-files, this can be distributed into several files.)
 */
public interface Mappings {

    /**
     * This method is invoked by the parser to get the first Element Parser. This element parser must be build for the
     * top XML element expected in the input.
     *
     * @return Element Parser Settings for the top most XML element that this parser expects.
     */
    ElementParserSettings getSettings();
}
