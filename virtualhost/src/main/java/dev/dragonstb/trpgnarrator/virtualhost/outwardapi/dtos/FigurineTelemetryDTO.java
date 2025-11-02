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
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;

/**
 *
 * @author Dragonstb
 * @since 0.0.2
 */
public final class FigurineTelemetryDTO {

    /** Id of the figurine this telemetry data is from. */
    @Getter @NonNull private final String figId;
    /** Location of the figurine. Might be {@coed null} if the figurine is not (anymore) located on the board. */
    private Vector3DTO location;
    /** Id of the field the figurine os located on. */
    @Getter private int fieldId;

    public FigurineTelemetryDTO(@NonNull String figId, Vector3f loc, int fieldId) {
        this.figId = figId;
        this.location = loc != null ? new Vector3DTO(loc) : null;
        this.fieldId = fieldId;
    }

    /** Gets the location as Vector or an empty optional if no location is present.
     *
     * @since 0.0.2;
     * @author Dragonstb
     * @return Optional either with the location present or empty.
     */
    public Optional<Vector3f> getLocationAsVector3f() {
        return location != null ? Optional.of(location.getAsVector()) : Optional.empty();
    }

}
