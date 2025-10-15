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

package dev.dragonstb.trpgnarrator.virtualhost.board;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

/** A link from one {@link FieldData FieldData} to another.
 *
 * @author Dragonstb
 * @since 0.0.1;
 */
@Getter(AccessLevel.PACKAGE)
final class FieldLink {

    private final FieldData fieldA;
    private final FieldData fieldB;

    /** Generates.
     *
     * @param fieldA The one field.
     * @param fieldB The other field.
     */
    FieldLink(@NonNull FieldData fieldA, @NonNull FieldData fieldB) {
        this.fieldA = fieldA;
        this.fieldB = fieldB;
    }

}
