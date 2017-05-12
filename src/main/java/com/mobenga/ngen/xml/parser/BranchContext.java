package com.mobenga.ngen.xml.parser;

import java.util.function.Predicate;

/**
 * It is used to keep the XML Parsers "Object Branch". This contains all objects in the current branch of the destination
 * tree. The destination tree is a tree (or a single node) that describes the Java structure that becomes the result
 * of this parsing. The Object Branch does not contain any relations itself, the relation between objects in the destination
 * tree is kept in the objects themselves like in an ordinary tree.
 * <p>
 * Typically, each time a result object is created during the XML parsing, it is pushed to this map. When the parsing
 * is done with the object it is popped from this map. this makes the object in this map reflect the current branch in the XML.
 * Finally getInstance can be used to peek at an object in the current object branch.
 * <p>
 * For example, when parsing a retail range for an e-commerce we have a product category that contains a set of products.
 * Each product contains a set of spare parts, and each spare part contains a set of prices for different markets.
 * During the parsing process, the Object Branch will typically contain the object that is currently "written" to and
 * all its ancestors. Data in current XML level can be injected in any object in the Object Branch.
 *
 * @see ProtectedClassMap for classed-based data storage
 */
public interface BranchContext {

    /**
     * may be viewed as shortcut to predicate by instance type
     * @param objType class of object to extract
     * @param <T> object type
     * @return object
     */
    <T> T getInstance(final Class<T> objType);

    <T> T getInstance(Predicate<Object> predicate);

    /**
     * may be viewed as shortcut to predicate by instance type
     * @param objType class of object to extract
     * @param <T> object type
     * @return object
     */
    <T> T pop(final Class<T> objType);

    <T> T pop(Predicate<Object> predicate);

    /**
     * @param objType class of object to extract
     * @param obj object to add to underlying branch context
     */
    void put(final Class objType, final Object obj);

    void put(final Object object);

}
