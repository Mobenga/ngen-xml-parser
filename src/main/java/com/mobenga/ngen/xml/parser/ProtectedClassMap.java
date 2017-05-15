package com.mobenga.ngen.xml.parser;

import com.google.common.collect.MutableClassToInstanceMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

/**
 * This class contains a set of object, with the single rule that each object in the set must be of a unique class.
 */
public class ProtectedClassMap implements BranchContext {
    private static final Logger log = LoggerFactory.getLogger(ProtectedClassMap.class);
    private final MutableClassToInstanceMap<Object> branch = MutableClassToInstanceMap.create();

    /**
     * Creates an empty Class Map
     */
    public ProtectedClassMap() {
    }

    /**
     * Creates a new Class Map pre-populated with with one object
     *
     * @param objType provided object type.
     * @param obj     provided object to store.
     */
    public ProtectedClassMap(final Class objType, final Object obj) {
        put(objType, obj);
    }

    /**
     * Returns the value the specified class is mapped to, or null if no entry for this class is present.
     *
     * @param objType requested object type.
     * @param <T>     class of requested object
     * @return requested object if available, else null.
     */
    @Override
    public <T> T getInstance(final Class<T> objType) {
        return branch.getInstance(objType);
    }

    @Override
    public <T> T getInstance(Predicate<Object> predicate) {
        return (T) branch.values().stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns the value the specified class is mapped to and removed it from the map
     *
     * @param objType requested object type.
     * @param <T>     class of requested object
     * @return requested object if available, else null.
     */
    @Override
    public <T> T pop(final Class<T> objType) {
        log.debug("Pop object of type {}", objType.getName());
        T obj = branch.getInstance(objType);
        this.branch.remove(objType);
        if (log.isWarnEnabled() && null == obj) {
            log.warn("Object of type {} did not exist in the BranchContext during pop.", objType.getName());
        }
        return obj;
    }

    @Override
    public <T> T pop(Predicate<Object> predicate) {
        T obj = this.getInstance(predicate);
        branch.remove(obj.getClass());
        if (log.isWarnEnabled() && null == obj) {
            log.warn("Object of type {} did not exist in the BranchContext during pop.", obj.getClass().getName());
        }
        return obj;
    }

    /**
     * Put an object into the map of the specified type.
     *
     * @param objType provided object type.
     * @param obj     provided object to store.
     * @throws IllegalStateException Exception is thrown if the list already contains an object of the specified type/class.
     */
    @Override
    public void put(final Class objType, final Object obj) {
        log.debug("Push object of type {}", objType.getName());
        if (null != branch.get(objType)) {
            throw new IllegalStateException("Misconfiguration of Mappings File.\nAn object of type " + objType.getName() + " already exists on this branch. Pop object type first!");
        }
        this.branch.put(objType, obj);
    }

    @Override
    public void put(Object object) {
        this.branch.put(object.getClass(), object);
    }
}
