package com.mobenga.ngen.xml.parser;

import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

import static org.junit.Assert.*;

public class protectedClassMapTest {

    @Test
    public void testProtectedClassMapInit() throws IOException, XMLStreamException {
        ProtectedClassMap pcm = new ProtectedClassMap(String.class, "TestStr");
        assertEquals("TestStr", pcm.getInstance(String.class));
        assertNull(pcm.getInstance(Integer.class));
    }

    @Test
    public void testProtectedClassMapInsert() throws IOException, XMLStreamException {
        ProtectedClassMap pcm = new ProtectedClassMap();
        pcm.put(String.class, "TestStr");
        assertEquals("TestStr", pcm.getInstance(String.class));
        pcm.put(Integer.class, 2);
        assertEquals(new Integer(2), pcm.getInstance(Integer.class));
    }

    @Test
    public void testProtectedClassMapDuplicate() throws IOException, XMLStreamException {
        ProtectedClassMap pcm = new ProtectedClassMap();
        pcm.put(String.class, "TestStr");
        assertEquals("TestStr", pcm.getInstance(String.class));
        try {
            pcm.put(String.class, "TestStr 2");
            fail();
        } catch (IllegalStateException e) {
            assertNotNull(e);
        } catch (Exception e) {
            fail();
        }
        assertEquals("TestStr", pcm.getInstance(String.class));
    }

    @Test
    public void testProtectedClassMapPop() throws IOException, XMLStreamException {
        ProtectedClassMap pcm = new ProtectedClassMap();
        pcm.put(String.class, "TestStr");
        assertEquals("TestStr", pcm.pop(String.class));
        assertNull(pcm.getInstance(String.class));
    }
}
