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
import dev.dragonstb.trpgnarrator.client.error.ClientErrorCodes;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;

/** Data of a single field of the board.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
final class FieldData {

    /** An unique field id. */
    @Getter private final int id;
    /** Location of the center of the field. */
    @Getter private final Vector3f location;
    /** All links to linked other fields. These other field are usually the adjacent neighbours, but there might be reasons for linking
     * other fields. For example, when jumping over a gap is possible, this field at the edge of the gap can be linked to fields on the
     * other side. The links then may contain infos about the skills and the difficulties required for succeeding the leap. */
    private final List<FieldLink> links = new ArrayList<>();

    FieldData(int id, @NonNull Vector3f location) {
        this.id = id;
        this.location = location;
    }

    /** Adds the link to the list of links of this field.
     *
     * @author Dragonstb
     * @since 0.0.1
     * @param link Link to be added. Must be not null and contain this field.
     * @throws IllegalArgumentException If the link links two other fields than this one.
     */
    void addLink(@NonNull FieldLink link) throws IllegalArgumentException {
        if( this != link.getFieldA() && this != link.getFieldB() ) {
            String msg = "Can add only links to a field that actually link the field, but got a link for two other fields.";
            String errCode = ClientErrorCodes.C17679;
            String use = ClientErrorCodes.assembleCodedMsg(msg, errCode);
            throw new IllegalArgumentException(use);
        }

        links.add(link);
    }

}
