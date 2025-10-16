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

package dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos;

import com.jme3.math.Vector3f;
import lombok.Getter;
import lombok.NonNull;

/** Immutable class that represents the data of a field on the board <i>except for the links to other fields</i>. These links follow from
 * a list of {@link FieldLinkDTO field link DTOs}.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
@Getter
public final class FieldDataDTO {

    private final int id;
    private final float x;
    private final float y;
    private final float z;

    public FieldDataDTO(int id, float x, float y, float z) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public FieldDataDTO(int id, @NonNull Vector3f location) {
        this(id, location.x, location.y, location.z);
    }

    /** Returns the location as a vector;
     *
     * @since 0.0.1
     * @author Dragonstb
     * @return A new Vector3f.
     */
    public Vector3f getLocationAsVector() {
        return new Vector3f(x, y, z);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof FieldDataDTO)) {
            return false;
        }

        FieldDataDTO other = (FieldDataDTO)obj;
        return id == other.getId() &&
                x == other.getX() &&
                y == other.getY() &&
                z == other.getZ();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.id;
        hash = 83 * hash + Float.floatToIntBits(this.x);
        hash = 83 * hash + Float.floatToIntBits(this.y);
        hash = 83 * hash + Float.floatToIntBits(this.z);
        return hash;
    }

}
