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

import com.jme3.app.state.AbstractAppState;
import com.jme3.scene.Node;
import dev.dragonstb.trpgnarrator.client.Globals;
import dev.dragonstb.trpgnarrator.client.error.BoardFieldNotFoundException;
import dev.dragonstb.trpgnarrator.client.ingame.board.Board;
import dev.dragonstb.trpgnarrator.client.ingame.board.BoardFactory;
import dev.dragonstb.trpgnarrator.client.ingame.figurine.Figurine;
import lombok.Getter;
import lombok.NonNull;

/** App state active while actuallz in game.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
public final class IngameAppState extends AbstractAppState{

    /** Root node of the app state. */
    @Getter private final Node ingameRoot = new Node(Globals.INGAME_ROOTNODE_NAME);
    private Board board;

    public IngameAppState() {
        setEnabled(false);
        board = BoardFactory.makeBoard(); // TODO: set on demand with data model
        ingameRoot.attachChild(board.getNode()); // TODO: attach (detach) on demand
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
    }

    /** Adds a figurine and places it on the given field.
     * 
     * @author Dragonstb
     * @since 0.0.1
     * @param fig Figurine to be added to the scene.
     * @param fieldId Id of the field the figurine is placed on.
     * @throws BoardFieldNotFoundException When ther eis no field with the gievn id.
     */
    public void addFigurine(@NonNull Figurine fig, int fieldId) throws BoardFieldNotFoundException {
        // TODO: place by story data model
        // TODO: check if figurine already in scene
        ingameRoot.attachChild(fig.getNode());
        board.placeFigurineOnField(fig, fieldId);
    }

}
