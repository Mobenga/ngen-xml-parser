package com.mobenga.ngen.xml.parser;

import com.google.common.collect.MutableClassToInstanceMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains a set of object, with the single rule that each object in the set must be of a unique class.
 * <p>
 * It is used to keep the XML Parsers "Object Branch". This contains all objects in the current branch of the destination
 * tree. The destination tree is a tree (or a single node) that describes the Java structure that becomes the result
 * of this parsing. The Object Branch does not contain any relations itself, the relation between objects in the destination
 * tree is kept in the objects themselves like in an ordinary tree. Thus a protected class map is a good data storage
 * for a branch in the tree.
 * <p>
 * Typically, each time a result object is created during the XML parsing, it is pushed to this map. When the parsing
 * is done with the object it is poped from this map. this makes the object in this map reflect the current branch in the XML.
 * Finally getInstance can be used to peek at an object in the current object branch.
 * <p>
 * For example, when parsing a retail range for an e-commerce we have a product category that contains a set of products.
 * Each product contains a set of spare parts, and each spare part contains a set of prices for different markets.
 * During the parsing process, the Object Branch will typically contain the object that is currently "written" to and
 * all its ancestors. Data in current XML level can be injected in any object in the Object Branch.
  *
 */
public class ProtectedClassMap {
    private static final Logger log = LoggerFactory.getLogger(ProtectedClassMap.class);
    private final MutableClassToInstanceMap<Object> branch = MutableClassToInstanceMap.create();

    /**
     * Creates an empty Class Map
     */
    public ProtectedClassMap() {
        // Empty blank constructor to create an empty class map.
    }

    /**
     * Creates a new Class Map prepopulated with with one object
     * @param objType provided object type.
     * @param obj provided object to store.
     */
    public ProtectedClassMap(final Class objType, final Object obj) {
        put(objType, obj);
    }

    /**
     * Returns the value the specified class is mapped to, or null if no entry for this class is present.
     *
     * @param objType requested object type.
     * @param <T> class of requested object
     * @return requested object if available, else null.
     */
    public <T> T getInstance(final Class<T> objType) {
        return branch.getInstance(objType);
    }

    /**
     * Returns the value the specified class is mapped to and removed it from the map
     * @param objType requested object type.
     * @param <T> class of requested object
     * @return requested object if available, else null.
     */
    public <T> T pop(final Class<T> objType) {
        log.debug("Pop object of type {}", objType.getName());
        T obj = branch.getInstance(objType);
        this.branch.remove(objType);
        if (log.isWarnEnabled() && null == obj) {
            log.warn("Object of type {} did not exist in the ProtectedClassMap during pop.", objType.getName());
        }
        return obj;
    }

    /**
     * Put an object into the map of the specified type.
     * @param objType provided object type.
     * @param obj provided object to store.
     * @throws IllegalStateException Exception is thrown if the list already contains an object of the specified type/class.
     */
    public void put(final Class objType, final Object obj) {
        log.debug("Push object of type {}", objType.getName());
        if (null != branch.get(objType)) {
            throw new IllegalStateException("Misconfiguration of Mappings File.\nAn object of type " + objType.getName() + " already exists on this branch. Pop object type first!");
        }
        this.branch.put(objType, obj);
    }
}
