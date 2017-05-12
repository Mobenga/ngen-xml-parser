package com.mobenga.ngen.xml.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class ListBranchContextTest {
    @Test
    public void getInstance_reverseOrder() throws Exception {
        BranchContext pcm = new ListBranchContext();
        pcm.put("test-string-1");
        pcm.put("test-string-2");
        assertEquals("test-string-2", pcm.getInstance(o -> true));
        assertEquals("test-string-1", pcm.getInstance( o -> ((String) o).endsWith("-1")));
    }

    @Test
    public void getInstance_byClass() throws Exception {
        BranchContext pcm = new ListBranchContext();
        pcm.put("test-string-1");
        pcm.put("test-string-2");
        assertEquals("test-string-2", pcm.getInstance(String.class));
    }

    @Test
    public void pop_reverseOrder() throws Exception {
        BranchContext pcm = new ListBranchContext();
        pcm.put("test-string-1");
        pcm.put("test-string-2");
        assertEquals("test-string-2", pcm.pop(o -> true));
        assertEquals("test-string-1", pcm.pop(o -> true));
        assertNull(pcm.pop(o -> true));
    }

    @Test
    public void pop_byClass() throws Exception {
        BranchContext pcm = new ListBranchContext();
        pcm.put("test-string-1");
        pcm.put("test-string-2");
        assertEquals("test-string-2", pcm.pop(String.class));
        assertEquals("test-string-1", pcm.pop(String.class));
        assertNull(pcm.pop(String.class));
    }

    @Test
    public void put() throws Exception {
        BranchContext pcm = new ListBranchContext();
        pcm.put("test-string-1");
        pcm.put("test-string-2");
        pcm.put(5);
        assertEquals("test-string-2", pcm.pop(String.class::isInstance));
        assertEquals("test-string-1", pcm.pop(String.class::isInstance));
        assertEquals(5, (int) pcm.pop(Integer.class));
        assertNull(pcm.pop(o -> true));
    }

}