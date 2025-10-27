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

package dev.dragonstb.trpgnarrator.virtualhost.generic.fetchparms;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Configuration parameters for a pathfinding.
 *
 * @author Dragonstb
 * @since 0.0.2
 */
@Getter
@AllArgsConstructor
public final class PathfindingConfig {

    /** Id of the field where the path starts. */
    private final int fromField;
    /** Id of the field where the pass should end. */
    private final int toField;

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof PathfindingConfig)) {
            return false;
        }

        PathfindingConfig other = (PathfindingConfig)obj;
        return fromField == other.getFromField() && toField == other.getToField();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.fromField;
        hash = 89 * hash + this.toField;
        return hash;
    }

}
