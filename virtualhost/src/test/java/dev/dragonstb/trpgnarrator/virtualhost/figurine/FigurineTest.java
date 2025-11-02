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
package dev.dragonstb.trpgnarrator.virtualhost.figurine;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import dev.dragonstb.trpgnarrator.virtualhost.generic.Message;
import dev.dragonstb.trpgnarrator.virtualhost.generic.MessageHeadlines;
import dev.dragonstb.trpgnarrator.virtualhost.generic.messagecontents.McFindPathForFigurine;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.FigurineDTO;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.FigurineTelemetryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Dragonstb
 */
public class FigurineTest {

    private final String figId = "hello";
    private final ColorRGBA figCol = ColorRGBA.Blue;
    private Figurine fig;

    @BeforeEach
    public void setUp() {
        fig = new Figurine(figId, figCol);
    }

    @Test
    public void testGetFindPathToFieldMessage() {
        int currentField = 19;
        fig.setFieldId(currentField);
        int toField = 20;
        Message msg = fig.getFindPathToFieldMessage(toField);

        assertNotNull(msg, "No message");
        String headline = msg.getHeadline();
        Object content = msg.getContent();

        assertNotNull(headline, "No headline");
        assertEquals(MessageHeadlines.PLEASE_FIND_PATH, headline, "Wrong headline");
        assertNotNull(content, "No content");

        assertTrue(content instanceof McFindPathForFigurine, "Wrong content type");

        McFindPathForFigurine cnt = (McFindPathForFigurine)content;
        assertEquals(figId, cnt.getFigurineId(), "Wrong figurine id");
        assertEquals(currentField, cnt.getFromFieldId(), "Wrong start field id");
        assertEquals(toField, cnt.getToFieldId(), "Wrong goal field id");
    }

    @Test
    public void testGetTelemetry() {
        int field = 10;
        Vector3f loc = new Vector3f(4, 5, 6);
        fig.setFieldId(field);
        fig.setLocation(loc);

        FigurineTelemetryDTO dto = fig.getTelemetry();
        assertNotNull(dto, "No dto");
        assertEquals(dto.getFigId(), figId, "Wrong id");
        assertEquals(fig.getLocation(), dto.getLocationAsVector3f().orElse(null), "Wrong location");
        assertEquals(dto.getFieldId(), field, "Wrong field");
    }

    @Test
    public void testAsDTO() {
        int field = 10;
        Vector3f loc = new Vector3f(4, 5, 6);
        fig.setFieldId(field);
        fig.setLocation(loc);

        FigurineDTO dto = fig.asDTO();
        assertNotNull(dto, "No dto");
        assertEquals(field, dto.getFieldId(), "Wrong field id");
        assertEquals(loc, dto.getLocationAsVector().orElse(null), "Wrong location");
        assertEquals(figCol, dto.getDiffuseAsColor(), "Wrong colour");
        assertEquals(figId, dto.getId(), "Wrong id");
    }

}
