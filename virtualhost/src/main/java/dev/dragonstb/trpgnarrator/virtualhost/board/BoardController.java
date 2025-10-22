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
import dev.dragonstb.trpgnarrator.virtualhost.broker.ChannelNames;
import dev.dragonstb.trpgnarrator.virtualhost.broker.Receiver;
import dev.dragonstb.trpgnarrator.virtualhost.broker.SynchronousBroker;
import dev.dragonstb.trpgnarrator.virtualhost.error.BoardFieldNotFoundException;
import dev.dragonstb.trpgnarrator.virtualhost.error.VHostErrorCodes;
import dev.dragonstb.trpgnarrator.virtualhost.generic.FetchCodes;
import dev.dragonstb.trpgnarrator.virtualhost.generic.FetchCommand;
import dev.dragonstb.trpgnarrator.virtualhost.generic.fetchparms.PathfindingConfig;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.BoardDataDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.function.Function;
import lombok.NonNull;

/** Controls action on the board
 *
 * @author Dragonstb
 * @since 0.0.2
 */
final class BoardController implements Board, Receiver {

    /** The broker this instance connects to. */
    private final SynchronousBroker broker;
    /** The data that describes the board itself. */
    private final BoardData data;
    private final Map<String, Function<Object, Optional<Object>>> requestMap = new HashMap<>();

    BoardController(@NonNull SynchronousBroker broker, @NonNull BoardData data) {
        this.broker = broker;
        this.data = data;
        broker.registerToChannel(this, ChannelNames.GET_BOARD_DATA);

        requestMap.put(FetchCodes.BOARD_DATA, this::getBoardData);
        requestMap.put(FetchCodes.BOARD_FIELD_LOCATION, this::getBoardFieldLocation);
    }

    @Override
    public void receive(Object obj) {

    }

    @Override
    public Optional<Object> request(FetchCommand fetch) {
        String cmd = fetch.getCommand();
        Object parm = fetch.getParms();

        var function = requestMap.get(cmd);
        Optional<Object> opt = function != null ? function.apply(parm) : Optional.empty();
        return opt;
    }

    // ____________________  answering requests  ____________________

    /** Returns the board data as immutable data transfer object.
     *
     * @since 0.0.2
     * @author Dragonstb
     * @param parm is ignored, just required due to the structure of the code.
     * @return The boar data.
     */
    private Optional<Object> getBoardData(Object parm) {
        BoardDataDTO dto = data.asDTO();
        return Optional.of(dto);
    }

    /** Gets the location of a field.
     *
     * @since 0.0.2
     * @author Dragonstb
     * @param parm An Integer with the Id of the field.
     * @return Location of the field. Might be empty if no field has the provided id
     * @throws ClassCastException When the argument is null or of another class than integer.
     */
    private Optional<Object> getBoardFieldLocation(Object parm) {
        int id;
        try {
            id = (Integer)parm;
        }
        catch (Exception e) {
            String code = VHostErrorCodes.V78642;
            String msg = "Expected id of field to be an integer, but got instance of class "
                    + (parm != null ? parm.getClass().getSimpleName() : "null") + " instead";
            String use = VHostErrorCodes.assembleCodedMsg(msg, code);
            throw new ClassCastException(use);
        }

        Vector3f loc = data.getLocationOfField(id);
        return Optional.ofNullable(loc);
    }

    /** Submits a {@link Pathfinder Pathfinder} to an thread pool executor.
     *
     * @since 0.0.2
     * @author Dragonstb
     * @param parm An object of class {@link dev.dragonstb.trpgnarrator.virtualhost.generic.fetchparms.PathfindingConfig {athfinderConfig}.
     * @return The future that follows from submitting the pathfinder to the executor. Be careful: the return type is spiky!
     */
    private Optional<Future<Optional<List<Vector3f>>>> findPath(Object parm) {
        String errCode = VHostErrorCodes.V11349;
        PathfindingConfig conf ;
        try {
            conf = (PathfindingConfig)parm;
        }
        catch (Exception e) {
            String code = VHostErrorCodes.V78642;
            String msg = "Expected parameter to be a PathfindingConfig, but got an instance of class "
                    + (parm != null ? parm.getClass().getSimpleName() : "null") + " instead";
            String use = VHostErrorCodes.assembleCodedMsg(msg, code);
            throw new ClassCastException(use);
        }

        int fromField = conf.getFromField();
        int toField = conf.getToField();
        ScheduledThreadPoolExecutor executor = conf.getExecutor();

        if(!data.getFields().containsKey(fromField)) {
            String msg = "Possible starting field with id "+fromField+" does not exists.";
            String use = VHostErrorCodes.assembleCodedMsg(msg, errCode);
            throw new BoardFieldNotFoundException(use);
        }
        if(!data.getFields().containsKey(toField)) {
            String msg = "Possible goal field with id "+toField+" does not exists.";
            String use = VHostErrorCodes.assembleCodedMsg(msg, errCode);
            throw new BoardFieldNotFoundException(use);
        }

        Pathfinder finder = new Pathfinder(fromField, toField, data);
        Future<Optional<List<Vector3f>>> future = executor.submit(finder);
        return Optional.of(future);
    }

}
