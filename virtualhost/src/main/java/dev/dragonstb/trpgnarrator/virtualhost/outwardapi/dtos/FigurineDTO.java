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

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;

/** Immutable representation of a figurine data.
 *
 * HINT: This ignores the alpha channel of the colours. The alpha channel can be assumed as unity (at the moment).
 *
 * @author Dragonstb
 * @since 0.0.2
 */
@Getter
public final class FigurineDTO {

    private final int id;
    @NonNull private final Vector3DTO diffuse;
    @NonNull private final Vector3DTO ambient;
    @NonNull private final Vector3DTO specular;
    private final Vector3DTO location;
    private final int fieldId;

    /**
     * @since 0.0.2
     * @param id Id of the figurine.
     * @param diffuse Diffuse colour. Must not be {@code null}.
     * @param ambient Ambient colour. Must not be {@code null}.
     * @param specular Specular colour. Must not be {@code null}.
     * @param location Location in the world. Can be {@code null}, which usually means that the figurine is currently not on the board.
     */
    public FigurineDTO(int id, @NonNull ColorRGBA diffuse, @NonNull ColorRGBA ambient, @NonNull ColorRGBA specular, Vector3f location,
            int fieldId) {
        this.id = id;
        this.diffuse = new Vector3DTO(diffuse.r, diffuse.g, diffuse.b);
        this.ambient = new Vector3DTO(ambient.r, ambient.g, ambient.b);
        this.specular = new Vector3DTO(specular.r, specular.g, specular.b);
        this.location = location != null ? new Vector3DTO(location) : null;
        this.fieldId = fieldId;
    }

    /**
     * @since 0.0.2
     * @return The diffuse colour.
     */
    public ColorRGBA getDiffuseAsColor() {
        return asColor(diffuse);
    }


    /**
     * @since 0.0.2
     * @return The ambient colour.
     */
    public ColorRGBA getAmbientAsColor() {
        return asColor(ambient);
    }


    /**
     * @since 0.0.2
     * @return The specular colour.
     */
    public ColorRGBA getSpecularAsColor() {
        return asColor(specular);
    }

    /**
     * @since 0.0.2
     * @return The location as vector, which might be {@code null}, putted into an optional.
     */
    public Optional<Vector3f> getLocationAsVector() {
        return location != null ? Optional.of(location.getAsVector()) : Optional.empty();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(obj == null || !(obj instanceof FigurineDTO)) {
            return false;
        }

        FigurineDTO other = (FigurineDTO)obj;
        boolean locEquals = (location != null && other.getLocation() != null && location.equals(other.getLocation()) )
                || (location == null && other.location == null);
        return id == other.getId()
                && diffuse.equals(other.getDiffuse())
                && ambient.equals(other.getAmbient())
                && specular.equals(other.getSpecular())
                && locEquals
                && fieldId == other.getFieldId();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + this.id;
        hash = 73 * hash + Objects.hashCode(this.diffuse);
        hash = 73 * hash + Objects.hashCode(this.ambient);
        hash = 73 * hash + Objects.hashCode(this.specular);
        hash = 73 * hash + Objects.hashCode(this.location);
        hash = 73 * hash + this.fieldId;
        return hash;
    }

    /** Returns a vector3 as colour. X becomes red, y becomes green, z becomes blue, and alpha is set to unity.
     *
     * @author Dragonstb
     * @since 0.0.2
     * @param vec three-dimensional vector.
     * @return Colour.
     */
    private static ColorRGBA asColor(Vector3DTO vec) {
        return new ColorRGBA(vec.getAsVector());
    }

}
