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

package dev.dragonstb.trpgnarrator.client.ingame;

import com.jme3.app.state.AbstractAppState;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import dev.dragonstb.trpgnarrator.client.Globals;
import dev.dragonstb.trpgnarrator.client.clientconnector.ClientForIngame;
import dev.dragonstb.trpgnarrator.client.error.BoardFieldNotFoundException;
import dev.dragonstb.trpgnarrator.client.error.ClientErrorCodes;
import dev.dragonstb.trpgnarrator.client.ingame.board.Board;
import dev.dragonstb.trpgnarrator.client.ingame.board.BoardFactory;
import dev.dragonstb.trpgnarrator.client.ingame.figurine.Figurine;
import dev.dragonstb.trpgnarrator.client.tweens.ActionTween;
import dev.dragonstb.trpgnarrator.client.tweens.SequenceTween;
import dev.dragonstb.trpgnarrator.client.tweens.ShiftTween;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.BoardDataDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/** App state active while actually in game.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
public final class IngameAppState extends AbstractAppState {

    /** The client connector that has contact with the virtual host. */
    @Setter private ClientForIngame connector;
    /** Root node of the app state. */
    @Getter private final Node ingameRoot = new Node(Globals.INGAME_ROOTNODE_NAME);
    private Board board;
    private Future<Optional<Integer>> fieldPick;
    private Future<Optional<List<Vector3f>>> pathfinder;
    private SequenceTween path; // TODO: at some point, this is done on the virtual host
    private Figurine figurine;

    public IngameAppState() {
        setEnabled(false);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);

        if(fieldPick != null && fieldPick.isDone()) {
            try {
                Optional<Integer> opt = fieldPick.get();
                if(opt.isPresent()) {
                    board.highlightJustField(opt.get());
                }
                else {
                    board.unhighlightAllFields();
                }
            } catch (InterruptedException | ExecutionException ex) {
                // TODO: possibly log and go on
            }
            finally {
                fieldPick = null;
            }
        }

        evaluatePathfinder();

        // TODO: this will be done by the virtual host in future. The host will tell the path-ignorant client where to put the figurine
        if(path != null) {
            path.internalAction(tpf);
            // TODO: typesafetiness
            ShiftTween tween = (ShiftTween)path.getCurrentActionTween();
            figurine.setLocalTranslation(tween.getCurrentPos());
            // TODO: update figurine's current field
            if(tween.isDone()) {
                this.path = null;
            }
        }
    }

    private void evaluatePathfinder() {
        // TODO: this evaluatin will be ddone by the virtual host in future
        if(pathfinder != null && pathfinder.isDone()) {
            try {
                Optional<List<Vector3f>> opt = pathfinder.get();
                if(opt.isPresent()) {
                    List<Vector3f> list = opt.get();
                    // TODO: know which figurine this path is meant for
                    this.path = getTweensFromPath(list);
                }
            } catch (Exception e) {
                // TODO: log and go on
            } finally {
                pathfinder = null;
            }
        }
    }

    private SequenceTween getTweensFromPath(List<Vector3f> path) {
        // TODO: at some point this is done on the virtual host

        // TODO: derive from figurine
        float timePerHex = .5f;


        Vector3f from = path.get(0);
        Vector3f to;
        List<ActionTween> shifts = new ArrayList<>();
        ShiftTween tween;
        for (int idx = 1; idx < path.size(); idx++) {
            to = path.get(idx);
            tween = new ShiftTween(from, to, timePerHex);
            shifts.add(tween);
            from = to;
        }

        SequenceTween sequence = new SequenceTween(shifts);
        return sequence;
    }

    /** Adds a figurine and places it on the given field.
     *
     * @author Dragonstb
     * @since 0.0.1
     * @param fig Figurine to be added to the scene.
     * @param fieldId Id of the field the figurine is placed on.
     * @throws BoardFieldNotFoundException When there is no field with the given id.
     */
    public void addFigurine(@NonNull Figurine fig, int fieldId) throws BoardFieldNotFoundException {
        // TODO: place by story data model
        // TODO: check if figurine is already in scene
        ingameRoot.attachChild(fig.getNode());
        board.placeFigurineOnField(fig, fieldId);
        this.figurine = fig;
    }

    /** Starts a callable that finds the (closest) field hit by the ray.
     *
     * @author Dragonstb
     * @param ray Ray that picks the field.
     * @param executor Executor for threads.
     */
    public void pickField(@NonNull Ray ray, @NonNull ScheduledThreadPoolExecutor executor) {
        if(fieldPick != null) {
            fieldPick.cancel(true);
        }
        fieldPick = board.pickField(ray, executor);
    }

    /** Initializes a movement of the figurine to another field. The first step is finding the path, which is evaluated asynchronously. This
     * is started in this method.
     *
     * @author Dragonstb
     * @since 0.0.1
     * @param fig Figurine to be moved.
     * @param executor Thread pool executor the callable is submitted to,
     */
    public void setIntoMovementTo(@NonNull Figurine fig, @NonNull ScheduledThreadPoolExecutor executor) {
        Optional<Integer> opt = fig.getCurrentFieldId();
        if(opt.isEmpty()) {
            return;
        }
        int fromField = opt.get();

        opt = board.getCurrentlyHighlightedFieldId();
        if(opt.isEmpty()) {
            return;
        }
        int toField = opt.get();

        if(toField == fromField) {
            return;
        }
        // TODO: once there, sent this path request to the virtual host who looks for the path and moves the figurine.

        Future<Optional<List<Vector3f>>> future;
        try {
            future = board.findPath(fromField, toField, executor);
        } catch (BoardFieldNotFoundException e) {
            // TODO: Log and go on
            return;
        }

        if(pathfinder != null) {
            pathfinder.cancel(true);
        }
        pathfinder = future;
    }

    /**
     * @since 0.0.1
     */
    public void load() {
        if(connector == null) {
            String code = ClientErrorCodes.C58856;
            String msg = "No client connector available.";
            String use = ClientErrorCodes.assembleCodedMsg(msg, code);
            throw new NullPointerException(use);
        }

        ingameRoot.detachAllChildren();
        BoardDataDTO dto = connector.getBoardData();
        board = BoardFactory.makeBoard(dto);
        ingameRoot.attachChild(board.getNode());
    }

    // TODO: The attempt of enabling an improperly configured IngameAppState shall throw an exception
}
