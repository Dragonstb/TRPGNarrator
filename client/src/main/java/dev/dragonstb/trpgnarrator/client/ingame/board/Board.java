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

import com.jme3.scene.Node;
import dev.dragonstb.trpgnarrator.client.error.BoardFieldNotFoundException;
import dev.dragonstb.trpgnarrator.client.ingame.figurine.Figurine;
import lombok.NonNull;

/** Possible interactions with the {@link GameBoard GameBoard}.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
public interface Board {

    /** Gets the internal {@link BoardNode BoardNode}, but as its superclass Node.
     *
     * @since 0.0.1
     * @author Dragonstb
     * @return The node with the visuals of the board.
     */
    public Node getNode();

    /** Places the givern figurine on the given field.
     *
     * @param fig Figurine to be placed.
     * @param fieldId Id of the field the figurine is place on.
     * @throws BoardFieldNotFoundException When there is no field with the giben id.
     */
    public void placeFigurineOnField(@NonNull Figurine fig, int fieldId) throws BoardFieldNotFoundException;

    /** Highlights the field with the given id and unhighlights all fields that had been highlighted so far.
     *
     * @param fieldId Id of field.
     */
    public void highlightJustField(int fieldId);

    /** Unhighlights all highlighted fields.
     *
     */
    public void unhighlightAllFields();
}
