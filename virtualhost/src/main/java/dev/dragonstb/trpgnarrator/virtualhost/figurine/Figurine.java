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
import dev.dragonstb.trpgnarrator.virtualhost.generic.Locateable;
import dev.dragonstb.trpgnarrator.virtualhost.generic.Message;
import dev.dragonstb.trpgnarrator.virtualhost.generic.MessageHeadlines;
import dev.dragonstb.trpgnarrator.virtualhost.generic.messagecontents.McFindPathForFigurine;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.FigurineDTO;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.FigurineTelemetryDTO;
import dev.dragonstb.trpgnarrator.virtualhost.tweens.ActionTween;
import dev.dragonstb.trpgnarrator.virtualhost.tweens.SequenceTween;
import dev.dragonstb.trpgnarrator.virtualhost.tweens.ShiftObjectTween;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 *
 * @author Dragonstb
 * @since 0.0.2
 */
final class Figurine implements Locateable {

    private final Object currentTweenLock = new Object();

    /** An id that must be unique among all figurines in a game. */
    @Getter private final String id;
    private final ColorRGBA diffuseColor;
    private final ColorRGBA ambientColor;
    private final ColorRGBA specularColor;
    @Getter @Setter private Vector3f location = new Vector3f();
    /** When on board, this field holds the id of the board field the figurine currently stand on. When off board, this field is
     * meaningless. */
    @Getter @Setter private int fieldId;
    /** Has the telemetry changed since it was read the last time? */
    @Getter @Setter private boolean telemetryChanged = true;

    /** The current action tween that controls the figurine. */
    private ActionTween currentTween = null;

    /** Generates.
     *
     * @since 0.0.2
     * @param diffuseColor
     * @param ambientColor
     * @param specularColor
     */
    Figurine(@NonNull String id, @NonNull ColorRGBA diffuseColor, @NonNull ColorRGBA ambientColor, @NonNull ColorRGBA specularColor) {
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
    Figurine(@NonNull String id, @NonNull ColorRGBA diffuseColor) {
        this(id, diffuseColor, diffuseColor.mult(.25f), diffuseColor.mult(1.2f));
    }

    /** Iterates the figurine one time step further.
     *
     * @since 0.0.2
     * @author Dragonstb
     * @param tpf Time per frame in seconds.
     */
    void update(float tpf) {
        synchronized (currentTweenLock) {
            if(currentTween != null) {
                currentTween.internalAction(tpf);
                telemetryChanged = true;
                if(currentTween.isDone()) {
                    currentTween = null;
                }
            }
        }
    }

    /**
     * @since 0.0.2
     * @author Dragonstb
     * @param waypoints Way points of the path. TODO: a path object that also includes the ids of the fields related to the way points.
     */
    void setPath(List<Vector3f> waypoints) {
        if(waypoints == null || waypoints.size() < 2) {
            // TODO: log
            return;
        }

        float timePerHex = .33f; // TODO: derive from figurine's characteristics

        Vector3f from = waypoints.get(0);
        Vector3f to;
        List<ActionTween> shifts = new ArrayList<>();
        ShiftObjectTween tween;
        for (int idx = 1; idx < waypoints.size(); idx++) {
            to = waypoints.get(idx);
            tween = new ShiftObjectTween(this, from, to, timePerHex);
            shifts.add(tween);
            from = to;
        }

        SequenceTween sequence = new SequenceTween(shifts);
        // TODO: smooth transition from current position into the path. Currently, the figurine jumps to the start of the path.
        currentTween = sequence;
    }

    /** Writes a message with a pleading that a path from this' current field to the specified goal field is found for this.
     *
     * @since 0.0.2
     * @author Dragonstb
     * @param toFieldId Id of the field where the path shall end.
     * @return
     */
    Message getFindPathToFieldMessage(int toFieldId) {
        McFindPathForFigurine pfRequest;
        // synced because the current tween may change the location of the figurine, thus also the if of the current field
        synchronized (currentTweenLock) {
            // TODO: place figurine centrally on ist current field where it awaits the new path.
            setIdleUnsafe();
            pfRequest = new McFindPathForFigurine(id, fieldId, toFieldId);
        }
        return new Message(MessageHeadlines.PLEASE_FIND_PATH, pfRequest);
    }

    /** Gets the telemetry of this figurine and sets the telemetryChanged flag to false.
     *
     * @since 0.0.2
     * @author Dragonstb
     * @return Telemetry data that can be streamed to the clients.
     */
    FigurineTelemetryDTO getTelemetry() {
        return new FigurineTelemetryDTO(id, location, fieldId);
    }

    FigurineDTO asDTO() {
        FigurineDTO dto = new FigurineDTO(id, diffuseColor, ambientColor, specularColor, location, fieldId);
        return dto;
    }

    // ____________________  internal controlling  ____________________

    /** Sets the figurine idle. This means no action tweens and an idle animation.
     * <br><br>
     * <b>Note:</b> This method writes on fields which are accessed by several concurrent threads without any syncing. Please call it in
     * a synchronized environment to become threadsafe.
     *
     * @since 0.0.2
     * @author Dragonstb
     */
    private void setIdleUnsafe() {
        currentTween = null;
    }

}
