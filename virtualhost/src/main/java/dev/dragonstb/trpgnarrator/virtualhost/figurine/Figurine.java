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

package dev.dragonstb.trpgnarrator.virtualhost.figurine;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 *
 * @author Dragonstb
 * @since 0.0.2
 */
final class Figurine {

    /** An id that must be unique among all figurines in a game. */
    @Getter private final int id;
    private final ColorRGBA diffuseColor;
    private final ColorRGBA ambientColor;
    private final ColorRGBA specularColor;
    @Getter @Setter private Vector3f location = new Vector3f();

    /** Generates.
     *
     * @since 0.0.2
     * @param diffuseColor
     * @param ambientColor
     * @param specularColor
     */
    Figurine(int id, @NonNull ColorRGBA diffuseColor, @NonNull ColorRGBA ambientColor, @NonNull ColorRGBA specularColor) {
        this.id = id;
        this.diffuseColor = diffuseColor;
        this.ambientColor = ambientColor;
        this.specularColor = specularColor;
    }

    /** Generates with deriving specular and ambient from diffuse.
     *
     * @since 0.0.2
     * @param diffuseColor
     */
    Figurine(int id, @NonNull ColorRGBA diffuseColor) {
        this(id, diffuseColor, diffuseColor.mult(.25f), diffuseColor.mult(1.2f));
    }

}
