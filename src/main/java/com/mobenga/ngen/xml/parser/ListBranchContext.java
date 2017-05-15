package com.mobenga.ngen.xml.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * This class contains a list of objects; No rules imposed on objects themselves
 * If this BranchContext is used, it is the responsibility of the AttributeMapping to provide the Predicate by which to get
 * needed instance
 * ListBranchContext guarantees that objects are extracted by LIFO principle
 */
public class ListBranchContext implements BranchContext {
    private static final Logger log = LoggerFactory.getLogger(ListBranchContext.class);
    private List<Object> objectContext = new ArrayList<>();

    public ListBranchContext() {
    }

    @Override
    public <T> T getInstance(Class<T> objType) {
        return this.getInstance(objType::isInstance);
    }

    @Override
    public <T> T getInstance(Predicate<Object> predicate) {
        List<Object> reversed = new ArrayList<>(objectContext);
        Collections.reverse(reversed);
        return (T) reversed.stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }

    @Deprecated
    @Override
    public <T> T pop(Class<T> objType) {
        return this.pop(objType::isInstance);
    }

    @Override
    public <T> T pop(Predicate<Object> predicate) {
        T obj = this.getInstance(predicate);
        objectContext.remove(obj);
        if (log.isWarnEnabled() && null == obj) {
            log.warn("Object did not exist in the BranchContext during pop by predicate.");
        }
        return obj;
    }

    @Override
    public void put(Class objType, Object obj) {
        this.put(obj);
    }

    @Override
    public void put(Object object) {
        objectContext.add(object);
    }
}
