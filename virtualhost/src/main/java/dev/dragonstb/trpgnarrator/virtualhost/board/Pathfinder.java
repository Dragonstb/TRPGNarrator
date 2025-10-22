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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/** Finds the path using A*.
 *
 * @author Dragonstb
 * @since 0.0.2
 */
final class Pathfinder implements Callable<Optional<List<Vector3f>>>{

    /** Id of the field where the path starts. */
    private final int fromField;
    /** Id of the field where the path ends. */
    private final int toField;
    /** The map of the{@link FieldData FieldData}. */
    private final Map<Integer, FieldData> rawMap;
    /** The map of the decorated field data. */
    private final Map<Integer, FieldWrapper> map = new HashMap<>();
    /** A data structure that easily sorts FieldWrappers by their total path lengths. TODO: please use such a data structure. */
    private final List<FieldWrapper> pool = new ArrayList<>(); // TODO: use efficient data structure.
    /** The start field data. */
    private final FieldData startField;

    Pathfinder(int fromField, int toField, @NonNull BoardData data) {
        this.fromField = fromField;
        this.toField = toField;
        this.rawMap = data.getFields();
        this.startField = rawMap.get(fromField);
    }

    @Override
    public Optional<List<Vector3f>> call() throws Exception {
        FieldWrapper field = new FieldWrapper(rawMap.get(toField), startField);
        map.put(toField, field);

        // set distance of goal field to 0, path length to heuristic distance
        field.setKnownPathLengthToGoal(0);
        field.setFullPathLength(field.getHeuristicDistToStart());

        // unreachable and occupied fields can be waived already now
        // -- tbd --

        // drop the goal field into the pool, as a starting point of our search
        insert(field);

        boolean success = false;
        do {
            field = removeMin();
            if(field == null){
                // should not happen
                continue;
            }
            if(field.getId() == fromField) {
                // reached start field => path found
                success = true;
                break;
            }
            field.setInvestigated(true);
            relax(field);
        } while(!pool.isEmpty());

        if(success) {
            List<Vector3f> path = new ArrayList<>();
            FieldWrapper next = field;
            do {
                path.add(next.getField().getLocation());
                next = next.getNext();
            } while(next != null);
            return Optional.of(path);
        }
        else {
            return Optional.empty();
        }
    }

    private void insert(FieldWrapper field) {
        pool.add(field);
    }

    /** Removes the field with the lowest, approximated pathlength
     *
     * @return Field with shortest approximated path.
     */
    private FieldWrapper removeMin() {
        // TODO: optimize
        if(pool.isEmpty()) {
            return null;
        }

        float length = Float.POSITIVE_INFINITY;
        FieldWrapper field = null;
        for(FieldWrapper current: pool) {
            if(current.getFullPathLength()< length) {
                length = current.getFullPathLength();
                field = current;
            }
        }

        if(field == null) {
            return null;
        }

        pool.remove(field);
        return field;
    }

    private void relax(FieldWrapper field) {
        // get linked fields
        List<FieldWrapper> linked = new ArrayList<>();
        for(FieldLink link: field.getField().getLinks()) {
            int idOfLinked = (link.getFieldA().getId() == field.getId() ? link.getFieldB() : link.getFieldA()).getId();
            if(!map.containsKey(idOfLinked)) {
                FieldData other = rawMap.get(idOfLinked);
                FieldWrapper wrapper = new FieldWrapper(other, startField);
                map.put(idOfLinked, wrapper);
            }

            // already investigated fields are skipped
            FieldWrapper other = map.get(idOfLinked);
            if(!other.isInvestigated()) {
                linked.add(map.get(idOfLinked));
            }
        }

        float newDist;
        for(FieldWrapper other: linked) {
            // preleminary length of path to goal when walking from 'other' to 'field' (field has a known path length already)
            newDist = field.getKnownPathLengthToGoal()+ 1; // TODO: add real costs/distances rather than unity
            if(newDist < other.getKnownPathLengthToGoal()) {
                // checking this field for the first time?
                boolean newField = Float.isInfinite(other.getFullPathLength());
                // append path
                other.setNext(field);
                // update known path length from 'other' to goal
                other.setKnownPathLengthToGoal(newDist);
                // update heuristic total path length
                other.setFullPathLength(newDist + other.getHeuristicDistToStart());

                if(newField) {
                    insert(other);
                }
                else {
                    decreaseKey(other);
                }
            }
        }

    }

    /** Decreases the key (fullPathLength) of the field in the pool.
     * @since 0.0.1
     * @param field
     */
    private void decreaseKey(FieldWrapper field) {
        // TODO: use a proper data structure where this action makes sense, not a list that checks the keys of every entry every time
    }


    @Getter
    @Setter
    private class FieldWrapper {

        // TODO: make this its own first-level class, for easier unit testing

        /** The decorated field. */
        private final FieldData field;
        /** This field has been taken into account already. */
        private boolean investigated = false;
        /** Distance to the start field of the search (which is the goal field of the path, aka the field with id 'toField'). */
        private float knownPathLengthToGoal = Float.POSITIVE_INFINITY;
        /** A heuristic distance to the field with id 'fromField', used during computation. */
        private final float heuristicDistToStart;
        /** The length of the path including this field according to the current state of the computation. */
        private float fullPathLength = Float.POSITIVE_INFINITY;
        /** If not null, the next field on the path from here to the goal. */
        private FieldWrapper next = null;

        FieldWrapper(FieldData field, FieldData startField) {
            this.field = field;
            // horizontal distance as heuristic distance
            Vector3f here = new Vector3f(field.getLocation()).setY(0);
            Vector3f start = new Vector3f(startField.getLocation()).setY(0);
            heuristicDistToStart = here.distance(start) / Globals.FIELD_DIAMETER;
        }

        int getId() {
            return field.getId();
        }
    }

}
