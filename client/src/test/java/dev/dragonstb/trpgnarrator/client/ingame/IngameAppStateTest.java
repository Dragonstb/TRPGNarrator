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
package dev.dragonstb.trpgnarrator.client.ingame;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import dev.dragonstb.trpgnarrator.client.Globals;
import dev.dragonstb.trpgnarrator.client.ingame.board.Board;
import dev.dragonstb.trpgnarrator.client.ingame.board.BoardFactory;
import dev.dragonstb.trpgnarrator.client.ingame.figurine.Figurine;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author Dragonstb
 */
@ExtendWith(MockitoExtension.class)
public class IngameAppStateTest {

    @Mock
    private Board board;

    public IngameAppStateTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testGetIngameRoot() {
        Node boardNode = new Node();

        try(MockedStatic<BoardFactory> boardFactory = Mockito.mockStatic(BoardFactory.class)) {
            boardFactory.when(BoardFactory::makeBoard).thenReturn(board);
            when(board.getNode()).thenReturn(boardNode);

            IngameAppState appState = new IngameAppState();

            Node ingameRoot = appState.getIngameRoot();
            assertNotNull(ingameRoot);
            assertEquals(Globals.INGAME_ROOTNODE_NAME, ingameRoot.getName());

            List<Spatial> children = ingameRoot.getChildren();
            assertEquals(1, children.size());

            assertEquals(boardNode, children.get(0));
        }
    }

    @Test
    public void testUpdate() {
    }

    @Test
    public void testAddFigurine() {
        Node boardNode = new Node();
        Node figNode = new Node();
        Figurine fig = mock(Figurine.class);
        when(fig.getNode()).thenReturn(figNode);
        int id = -15;

        try(MockedStatic<BoardFactory> boardFactory = Mockito.mockStatic(BoardFactory.class)) {
            boardFactory.when(BoardFactory::makeBoard).thenReturn(board);
            when(board.getNode()).thenReturn(boardNode);

            IngameAppState appState = new IngameAppState();
            Node ingameRoot = appState.getIngameRoot();

            appState.addFigurine(fig, id);
            assertEquals(ingameRoot, figNode.getParent(), "Ingame root is not the parent of the figurine.");

            verify(board).placeFigurineOnField(fig, id);
        }
    }

}
