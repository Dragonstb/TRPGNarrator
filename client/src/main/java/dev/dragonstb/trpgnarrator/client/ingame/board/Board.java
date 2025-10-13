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

import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import dev.dragonstb.trpgnarrator.client.error.BoardFieldNotFoundException;
import dev.dragonstb.trpgnarrator.client.ingame.figurine.Figurine;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
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

    /** Places the given figurine on the given field. This includes the following steps:
     * <ul>
     *   <li> Setting the figurine's local translation
     *   <li> Setting the figurine's current field
     * </ul>
     *
     * @param fig Figurine to be placed.
     * @param fieldId Id of the field the figurine is place on.
     * @throws BoardFieldNotFoundException When there is no field with the given id.
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

    /** Gets the id of the currently highlighted field if present.
     *
     * @author Dragonstb
     * @since 0.0.1
     * @return If a field is highlighted, the id of this field will be in this optional. If no field is highlighted, the optional will be
     * empty.
     */
    @NonNull
    public Optional<Integer> getCurrentlyHighlightedFieldId();

    /** Gets the id of the closest field hit by the ray.
     *
     * @param ray Ray for picking.
     * @param executor The thread pool where the callable that does the pathfinding is submitted to.
     * @return An optional containing the id of the field picked. The optional is empty if the ray misses all fields.
     */
    @NonNull
    public Future<Optional<Integer>> pickField(@NonNull Ray ray, @NonNull ScheduledThreadPoolExecutor executor);

    /** The board start a thread for finding a path from the field with id {@code fromField} to the field with the id {@code toField}.
     *
     * @param fromField Id of the field where the path starts.
     * @param toField Id of the field where the path ends.
     * @param executor The thread pool where the callable that does the pathfinding is submitted to.
     * @return Future with an optional with the list of the locations of the fields along the path if such a path exists. Otherwise, the
     * optional is empty().
     * @throws BoardFieldNotFoundException One of the two ids does not lead to an existing field.
     */
    @NonNull
    public Future<Optional<List<Vector3f>>> findPath(int fromField, int toField, @NonNull ScheduledThreadPoolExecutor executor)
            throws BoardFieldNotFoundException;
}
