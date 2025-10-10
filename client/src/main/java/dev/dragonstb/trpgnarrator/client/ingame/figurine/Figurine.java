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

package dev.dragonstb.trpgnarrator.client.ingame.figurine;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import dev.dragonstb.trpgnarrator.client.tweens.SequenceTween;
import java.util.Optional;
import lombok.NonNull;

/**
 *
 * @author Dragonstb
 * @since 0.0.1
 */
public interface Figurine {

    /** Returns the root node of the figurine.
     * @since 0.0.1
     * @author Dragonstb
     * @return The root node of the figurine.
     */
    @NonNull
    public Node getNode();

    /** Sets the local translation of the root node of the figurine. This effectively puts the figurine in the given position.
     *
     * @param pos New position, in WU.
     */
    public void setLocalTranslation(@NonNull Vector3f pos);

    /** Sets the value where the figurine remembers the field it is currently on. If the figurine is not on the board, the value is invalid
     * anyway.
     *
     * @param fieldId Id of the field.
     */
    public void setCurrentFieldId(int fieldId);

    /** Gets the id of the field the figurine is currently placed on. If the figurine is currently <i>on</i> the board, the optional will
     * contain the if of the field as set by the last invocation of {@link #setCurrentField(int) setCurrentField(int)} on the figurine. If
     * the figurine is currently <i>not on</i> the board, the optional will be empty.
     *
     * @return Optional with the field id if on board, empty optional if not.
     */
    @NonNull
    public Optional<Integer> getCurrentFieldId();

}
