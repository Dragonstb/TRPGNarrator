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

/** Immutable representation of the board data.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
@Getter
public final class BoardDataDTO {

    private final List<FieldDataDTO> fields;
    private final List<FieldLinkDTO> links;

    public BoardDataDTO(@NonNull Collection<FieldDataDTO> fields, @NonNull Collection<FieldLinkDTO> links) {
        this.fields = fields.stream().collect(Collectors.toUnmodifiableList());
        this.links = links.stream().collect(Collectors.toUnmodifiableList());
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null || !(obj instanceof BoardDataDTO)) {
            return false;
        }

        BoardDataDTO other = (BoardDataDTO)obj;
        // TODO: the order of entries should not matter, as long as this and other have equal entries. Please check and correct if needed.
        return fields.equals(other.getFields()) && links.equals(other.getLinks());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.fields);
        hash = 37 * hash + Objects.hashCode(this.links);
        return hash;
    }

}
