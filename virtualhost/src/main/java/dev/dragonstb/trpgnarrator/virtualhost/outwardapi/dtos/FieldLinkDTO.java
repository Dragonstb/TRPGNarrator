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

import lombok.Getter;

/** Immutable class that represents the data of a link between fields.
 *
 * @author Dragonstb
 * @since 0.0.1;
 */
@Getter
public final class FieldLinkDTO {

    private final int idFieldA;
    private final int idFieldB;

    public FieldLinkDTO(int idFieldA, int idFieldB) {
        this.idFieldA = idFieldA;
        this.idFieldB = idFieldB;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof FieldLinkDTO)) {
            return false;
        }

        FieldLinkDTO other = (FieldLinkDTO)obj;
        if(idFieldA != other.getIdFieldA()) {
            return false;
        }
        else if(idFieldB != other.getIdFieldB()) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.idFieldA;
        hash = 17 * hash + this.idFieldB;
        return hash;
    }

}
