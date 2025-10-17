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
package dev.dragonstb.trpgnarrator.client.ingame.board;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import dev.dragonstb.trpgnarrator.client.Globals;
import dev.dragonstb.trpgnarrator.client.error.BoardFieldNotFoundException;
import dev.dragonstb.trpgnarrator.client.error.ClientErrorCodes;
import dev.dragonstb.trpgnarrator.client.ingame.figurine.Figurine;
import dev.dragonstb.trpgnarrator.client.ingame.figurine.FigurineBuilder;
import dev.dragonstb.trpgnarrator.testslices.WithAssetManager;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.BoardDataDTO;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.FieldDataDTO;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.FieldLinkDTO;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Dragonstb
 */
public class GameBoardTest {

    private final FieldDataDTO fieldDto1 = new FieldDataDTO(0, 0, 0, 1);
    private final FieldDataDTO fieldDto2 = new FieldDataDTO(1, 1, 0, 0);
    private final FieldDataDTO fieldDto3 = new FieldDataDTO(2, -1, 0, 0);
    private final FieldLinkDTO linkDto1 = new FieldLinkDTO(0, 1);
    private final FieldLinkDTO linkDto2 = new FieldLinkDTO(1, 2);
    private final FieldLinkDTO linkDto3 = new FieldLinkDTO(2, 0);
    private final List<FieldDataDTO> fieldDtos = Arrays.asList(fieldDto1, fieldDto2, fieldDto3);
    private final List<FieldLinkDTO> linkDtos = Arrays.asList(linkDto1, linkDto2, linkDto3);
    private final BoardDataDTO boardDto = new BoardDataDTO(fieldDtos, linkDtos);

    private GameBoard board;

    @BeforeAll
    public static void setUpClass() {
        WithAssetManager.initAssetManager();
    }

    @BeforeEach
    public void setUp() {
        board = new GameBoard(boardDto);
    }

    @Test
    public void testHasNodeAfterInit() {
        Node node = board.getNode();
        assertNotNull(node);
        assertEquals(Globals.BOARD_NODE_NAME, node.getName());
    }

    @Test
    public void testPlaceFigurineOnField_Ok() {
        FieldDataDTO field = fieldDto2;
        int id = field.getId();

        Figurine fig = FigurineBuilder.ofId("doesntmatterhere").setColor(ColorRGBA.Blue).build();
        Node node = new Node();
        node.attachChild(fig.getNode()); // figurine assumes now to be part of the scene
        Vector3f oldPos = new Vector3f(-1, -2, -3);
        fig.setLocalTranslation(oldPos);

        board.placeFigurineOnField(fig, id);
        Vector3f newPos = fig.getNode().getLocalTranslation();
        float diff = oldPos.distance(newPos);
        assertTrue(diff > 1, "figurine has not been moved");
        assertEquals(field.getLocationAsVector(), newPos, "wrong position");
        // TODO: check actual position once the game board is build based on model data rather than hardcoded in the init.

        Optional<Integer> opt = fig.getCurrentFieldId();
        assertEquals(id, opt.get(), "wrong value"); // if opt is empty or has a wrong value, then something could be wrong with the figurine

        // field with other id, just in case the figurine's current field id was initialized with the same value as 'id'
        int newId = fieldDto3.getId();
        board.placeFigurineOnField(fig, newId);
        Optional<Integer> newOpt = fig.getCurrentFieldId();
        // if opt is empty or has a wrong value, then something could be wrong with the figurine
        assertEquals(newId, newOpt.get(), "wrong value again");
    }

    @Test
    public void testPlaceFigurineOnField_NoFieldWithThatId() {
        Figurine fig = FigurineBuilder.ofId("doesntmatterhere").setColor(ColorRGBA.Blue).build();
        Vector3f oldPos = new Vector3f(1, 2, 3);
        fig.setLocalTranslation(oldPos);
        int id = -15;

        BoardFieldNotFoundException exc = assertThrows(BoardFieldNotFoundException.class, () -> board.placeFigurineOnField(fig, id));

        assertTrue( exc.getMessage().contains( ClientErrorCodes.C38587 ) );
    }
}
