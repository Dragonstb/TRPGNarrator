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

import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import dev.dragonstb.trpgnarrator.client.error.BoardFieldNotFoundException;
import dev.dragonstb.trpgnarrator.client.error.ClientErrorCodes;
import dev.dragonstb.trpgnarrator.client.ingame.figurine.Figurine;
import java.util.Optional;
import lombok.NonNull;

/** The board with all the {@link FieldData data} and the visuals of its fields, the figurines, and the objects.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
final class GameBoard implements Board {

    /** The data of the fields. */
    private final BoardData data;
    /** The node of the fields. */
    private final BoardNode node;

    /** Generates
     *
     * @since 0.0.1
     * @author Dragonstb
     */
    public GameBoard() {
        // TODO: parametrize constructor with a data model
        data = new BoardData();
        node = new BoardNode(data);
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public void placeFigurineOnField(@NonNull Figurine fig, int fieldId) throws BoardFieldNotFoundException {
        // TODO: check for occuation
        Optional<Vector3f> opt = data.getLocationOfField(fieldId);
        if(opt.isEmpty()) {
            String errCode = ClientErrorCodes.C38587;
            String msg = "Requesting location of a field that does not exist.";
            String use = ClientErrorCodes.assembleCodedMsg(msg, errCode);
            throw new BoardFieldNotFoundException(use);
        }

        Vector3f loc = opt.get();
        fig.setLocalTranslation(loc);
        fig.setCurrentFieldId(fieldId);
    }

    @Override
    public void highlightJustField(int fieldId) {
        // TODO: optimize finding
        Optional<Geometry> child = node.getFieldGeometry(fieldId);

        Geometry geom = child.orElse(null);
        node.highlightField(geom);
    }

    @Override
    public void unhighlightAllFields() {
        node.highlightField(null);
    }

    @Override
    public Optional<Integer> getCurrentlyHighlightedFieldId() {
        return node.getCurrentlyHighlightedFieldId();
    }


}
