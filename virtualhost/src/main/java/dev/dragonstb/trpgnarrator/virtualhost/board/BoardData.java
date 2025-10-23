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

import com.jme3.math.Vector3f;
import dev.dragonstb.trpgnarrator.virtualhost.generic.Globals;
import java.util.HashMap;
import java.util.Map;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.BoardDataDTO;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.FieldDataDTO;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.FieldLinkDTO;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;

/** Collection of all {@link FieldData FieldData} and some stuff for dealing with them.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
final class BoardData {

    /** All fields of the board. */
    @Getter(AccessLevel.PACKAGE) private final Map<Integer, FieldData> fields = new HashMap<>();

    BoardData() {
        init();
    }

    /** A mocking method used in early development.
     * @since 0.0.1
     */
    private void init() {
        // TODO: stop using this simple, stupid initialization and derive the objects from some sort of data
        float d = Globals.FIELD_RADIUS;
        float dx = d * (float)Math.sqrt(3); // this is also the distance between the centers of two adjacent fields
        float dz = 1.5f * d;

        int rangeZ = 5;
        int rangeX = 5;

        float x, z;
        float y = 0f;
        int nextId = 0;
        for (int tileZ = -rangeZ; tileZ <= rangeZ; tileZ++) {
            for (int tileX = -rangeX; tileX < rangeX; tileX++) {
                x = tileX * dx;
                z = tileZ * dz;
                if(tileZ%2==0) {
                    // every second row the hex fields are shifted a bit
                    x -= dx*.5f;
                }
                Vector3f location = new Vector3f(x, y, z);
                FieldData data = new FieldData(nextId++, location);
                fields.put(data.getId(), data); // TODO: In real-use methods, check if id is unique and throw exception if it is not
            }
        }

        fields.values().forEach(field -> {
            fields.values().stream().filter(otherField -> otherField.getId() < field.getId()).forEach(otherField -> {
                // for each pair of adjacent fields, create a link once
                float dist = field.getLocation().subtract( otherField.getLocation() ).length();
                if( Math.abs(dist-dx) < .01f ) {
                    FieldLink link = new FieldLink(field, otherField);
                    field.addLink(link);
                    otherField.addLink(link);
                }
            });
        });

    }

    /** Creates a new, immutable representation of the board model data.
     *
     * @since 0.0.1
     * @author Dragonstb
     * @return An object for transferring the board data.
     */
    BoardDataDTO asDTO() {
        List<FieldDataDTO> fieldDtos = fields.values().stream().map(fld -> fld.asDTO()).collect(Collectors.toList());
        Set<FieldLinkDTO> linkDtos = fields.values().stream()
                .flatMap(fld -> fld.getLinks()
                        .stream()
                        .map(lnk -> lnk.asDTO()))
                .collect(Collectors.toSet());

        BoardDataDTO dto = new BoardDataDTO(fieldDtos, linkDtos);
        return dto;
    }

    /** Fetches the location of a board field.
     *
     * @author Dragonstb
     * @since 0.0.1
     * @param fieldId Id of the field of interest.
     * @return An optional containing the location of the field of interest. The optional is empty if no field has the given id.
     */
    Vector3f getLocationOfField(int fieldId) {
        FieldData field = fields.get(fieldId);
        return field != null ? field.getLocation() : null;
    }

}
