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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/** An immutable, three-dimensional vector.
 *
 * @author Dragonstb
 * @since 0.0.2
 */
@Getter
public final class Vector3DTO {

    private final float x;
    private final float y;
    private final float z;

    /** Generates.
     *
     * @since 0.0.2
     * @param x
     * @param y
     * @param z
     */
    public Vector3DTO(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /** Generates.
     *
     * @since 0.0.2
     * @param vec
     */
    public Vector3DTO(@NonNull Vector3f vec) {
        this(vec.x, vec.y, vec.z);
    }

    /** This vector as new instance of Vector3f.
     *
     * @since 0.0.2
     * @return
     */
    public Vector3f getAsVector() {
        return new Vector3f(x, y, z);
    }

}
