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

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;

/**
 *
 * @author Dragonstb
 * @since 0.0.2;
 */
@Getter
public final class FigurinesListDTO {

    private final List<FigurineDTO> figurines;

    public FigurinesListDTO(@NonNull Collection<FigurineDTO> figurines) {
        this.figurines = figurines.stream().collect(Collectors.toUnmodifiableList());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(obj == null || !(obj instanceof FigurinesListDTO)) {
            return false;
        }

        FigurinesListDTO other = (FigurinesListDTO)obj;
        // TODO: the order of entries should not matter, as long as this and other have equal entries. Please check and correct if needed.
        return figurines.equals(other.getFigurines());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.figurines);
        return hash;
    }

}
