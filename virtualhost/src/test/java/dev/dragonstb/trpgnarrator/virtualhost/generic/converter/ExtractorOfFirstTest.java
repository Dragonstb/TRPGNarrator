/*
 * Copyright (C) 2025 Dragonstb
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * See <http://www.gnu.org/licenses/gpl-2.0.html>
 */
package dev.dragonstb.trpgnarrator.virtualhost.generic.converter;

import dev.dragonstb.trpgnarrator.virtualhost.generic.converter.ExtractorOfFirst;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Dragonstb
 */
public class ExtractorOfFirstTest {

    private final String errCode = "code";
    private final String emptyListMsg = "emptyListMsg";
    private final String emptyOptMsg = "emptyOptMsg";
    private final String wrongTypeMsg = "wrongTypeMsg: {0} instead of {1}";

    private List<Optional<Object>> list;
    private ExtractorOfFirst extractor;

    @BeforeEach
    public void setUp() {
        list = new ArrayList<>();
        extractor = new ExtractorOfFirst(errCode)
                .setEmptyListMsg(emptyListMsg)
                .setEmptyOptionalMsg(emptyOptMsg)
                .setWrongTypeMsg(wrongTypeMsg);
    }

    @Test
    public void testExtractFirst_ok() {
        String expect = "hello";
        list.add(Optional.of(expect));
        list.add(Optional.of("nope"));

        String actual = extractor.extractFirst(list, expect.getClass());
        assertNotNull(actual, "Got nothing");
        assertEquals(expect, actual, "Wrong string");
    }

    @Test
    public void testExtractFirst_emptyList() {
        NoSuchElementException exc = assertThrows(NoSuchElementException.class, () -> extractor.extractFirst(list, String.class),
                "No exception");
        String msg = exc.getMessage();
        assertNotNull(msg, "No message");
        assertTrue(msg.contains(emptyListMsg), "Wrong message");
        assertTrue(msg.contains(errCode), "Wrong error code");
    }

    @Test
    public void testExtractFirst_emptyOptional() {
        list.add(Optional.empty());

        NoSuchElementException exc = assertThrows(NoSuchElementException.class, () -> extractor.extractFirst(list, String.class),
                "No exception");
        String msg = exc.getMessage();
        assertNotNull(msg, "No message");
        assertTrue(msg.contains(emptyOptMsg), "Wrong message");
        assertTrue(msg.contains(errCode), "Wrong error code");
    }

    @Test
    public void testExtractFirst_wrongType() {
        list.add(Optional.of(5));

        ClassCastException exc = assertThrows(ClassCastException.class, () -> extractor.extractFirst(list, String.class),
                "No exception");
        String msg = exc.getMessage();
        assertNotNull(msg, "No message");
        String expectMsg = MessageFormat.format(wrongTypeMsg, Integer.class.getSimpleName(), String.class.getSimpleName());
        assertTrue(msg.contains(expectMsg), "Wrong message");
        assertTrue(msg.contains(errCode), "Wrong error code");
    }

    @Test
    public void testSetErrCode() {
        String code = "???";
        extractor.setErrCode(code);
        NoSuchElementException exc = assertThrows(NoSuchElementException.class, () -> extractor.extractFirst(list, String.class),
                "No exception");
        String msg = exc.getMessage();
        assertNotNull(msg, "No message");
        assertTrue(msg.contains(emptyListMsg), "Wrong message");
        assertTrue(msg.contains(code), "Wrong error code");
    }

    @Test
    public void testSetEmptyListMsg() {
        String text = "hello hello hello";
        extractor.setEmptyListMsg(text);
        NoSuchElementException exc = assertThrows(NoSuchElementException.class, () -> extractor.extractFirst(list, String.class),
                "No exception");
        String msg = exc.getMessage();
        assertNotNull(msg, "No message");
        assertTrue(msg.contains(text), "Wrong message");
        assertTrue(msg.contains(errCode), "Wrong error code");
    }

    @Test
    public void testSetEmptyOptionalMsg() {
        String text = "hello hello hello";
        extractor.setEmptyOptionalMsg(text);
        list.add(Optional.empty());

        NoSuchElementException exc = assertThrows(NoSuchElementException.class, () -> extractor.extractFirst(list, String.class),
                "No exception");
        String msg = exc.getMessage();
        assertNotNull(msg, "No message");
        assertTrue(msg.contains(text), "Wrong message");
        assertTrue(msg.contains(errCode), "Wrong error code");
    }

    @Test
    public void testSetWrongTypeMsg() {
        String text = "hello hello hello";
        extractor.setWrongTypeMsg(text);
        list.add(Optional.of(5));

        ClassCastException exc = assertThrows(ClassCastException.class, () -> extractor.extractFirst(list, String.class),
                "No exception");
        String msg = exc.getMessage();
        assertNotNull(msg, "No message");
        assertTrue(msg.contains(text), "Wrong message");
        assertTrue(msg.contains(errCode), "Wrong error code");
    }

}
