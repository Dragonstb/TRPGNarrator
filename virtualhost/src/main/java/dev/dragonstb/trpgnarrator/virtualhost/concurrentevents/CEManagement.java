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

package dev.dragonstb.trpgnarrator.virtualhost.concurrentevents;

import com.jme3.math.Vector3f;
import dev.dragonstb.trpgnarrator.virtualhost.broker.ChannelNames;
import dev.dragonstb.trpgnarrator.virtualhost.broker.Receiver;
import dev.dragonstb.trpgnarrator.virtualhost.broker.SynchronousBroker;
import dev.dragonstb.trpgnarrator.virtualhost.error.VHostErrorCodes;
import dev.dragonstb.trpgnarrator.virtualhost.generic.FetchCodes;
import dev.dragonstb.trpgnarrator.virtualhost.generic.FetchCommand;
import dev.dragonstb.trpgnarrator.virtualhost.generic.Message;
import dev.dragonstb.trpgnarrator.virtualhost.generic.MessageHeadlines;
import dev.dragonstb.trpgnarrator.virtualhost.generic.converter.ExtractorOfFirst;
import dev.dragonstb.trpgnarrator.virtualhost.generic.fetchparms.PathfindingConfig;
import dev.dragonstb.trpgnarrator.virtualhost.generic.messagecontents.McFindPathForFigurine;
import dev.dragonstb.trpgnarrator.virtualhost.generic.messagecontents.McPathForFigurine;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.Clock;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.ClockReceiver;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.VHStreamTypes;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.VHStreamed;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.FigurineTelemetryDTO;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.TelemetryDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.NonNull;

/** Manages concurrent events.
 *
 * @author Dragonstb
 * @since 0.0.2
 */
final class CEManagement implements ConcurrentEventManager, Receiver, ClockReceiver {

    private final Clock clock;
    private final SynchronousBroker broker;
    private final ScheduledThreadPoolExecutor executor; // TODO: add a smart shutdown
    /** The functions called when receiving events/messages via the broker. */
    private final Map<String, Consumer<Object>> receiveMap = new HashMap<>();
    /** The functions called when receiving requests via the broker. */
    private final Map<String, Function<Object, Optional<Object>>> requestMap = new HashMap<>();
    /** Maps the id of a figurine to a pathfinder that is computing a path for that figurine. */
    private final Map<String, Future<Optional<List<Vector3f>>>> pathfinders = new HashMap<>();

    /** Generates.
     *
     * @since 0.0.2
     * @author Dragonstb
     * @param clock A clock for times signals. Must not be {@null}!
     * @param broker Internal message broker. Must not be {@null}!
     * @param executor An executor for asynchronous tasks. Must not be {@null}!
     */
    CEManagement(@NonNull Clock clock, @NonNull SynchronousBroker broker, @NonNull ScheduledThreadPoolExecutor executor) {
        this.clock = clock;
        this.broker = broker;
        this.executor = executor;

        broker.registerToChannel(this, ChannelNames.CONCURRENT_MANAGEMENT);

        receiveMap.put(MessageHeadlines.PLEASE_FIND_PATH, this::findPath);
    }


    // ____________________  receiver  ____________________

    @Override
    public void receive(Message msg) {
        String headline = msg.getHeadline();
        Object content = msg.getContent();

        var function = receiveMap.get(headline);
        if(function != null) {
            function.accept(content);
        }
    }

    @Override
    public Optional<Object> request(FetchCommand fetch) {
        String cmd = fetch.getCommand();
        Object parm = fetch.getParms();

        var function = requestMap.get(cmd);
        Optional<Object> opt = function != null ? function.apply(parm) : Optional.empty();
        return opt;
    }


    // ____________________  reacting on messages  ____________________

    /** Gets a pathfinder from the board and submits it to the executor.
     * <br><br>
     * <b>HINT:</b> This method locks the map of {@code this.pathfinders} for some time.
     *
     * @since 0.0.2
     * @author Dragonstb
     * @param parm Expected to be a List of Integers of size two. The first number is the id of the field where the path starts. The second
     * number is the id of the field where the path ends.
     */
    private void findPath(Object parm) {
        // step 1: assemble a request
        String code = VHostErrorCodes.V42664;
        McFindPathForFigurine conf;
        try {
            conf = (McFindPathForFigurine)parm;
        }
        catch (Exception e) {
            String msg = "Expected content to be a FindPathForFigurine, but got an instance of class "
                    + (parm != null ? parm.getClass().getSimpleName() : "null") + " instead";
            String use = VHostErrorCodes.assembleCodedMsg(msg, code);
            throw new ClassCastException(use);
        }

        int fromField = conf.getFromFieldId();
        int toField = conf.getToFieldId();
        String figId = conf.getFigurineId();


        // step 2: deal with reply
        code = VHostErrorCodes.V97498;
        PathfindingConfig pfConf = new PathfindingConfig(fromField, toField);
        FetchCommand cmd = new FetchCommand(FetchCodes.BOARD_PATHFINDER, pfConf);

        List<Optional<Object>> opt = broker.request(ChannelNames.GET_BOARD_DATA, cmd, true);
        ExtractorOfFirst extractor = new ExtractorOfFirst(code);
        Callable pathfinder = extractor.extractFirst(opt, Callable.class);
        Future future = executor.submit(pathfinder);

        // this method may be called at any time, and the list of pathfinders is also accessed from a different thread at each clock signal.
        synchronized (pathfinders) {
            Future oldFuture = pathfinders.get(figId);
            if(oldFuture != null) {
                oldFuture.cancel(true);
            }
            pathfinders.put(figId, future);
        }
    }



    // ____________________  answering requests  ____________________




    // ____________________  clock receiver  ____________________

    @Override
    public void update(float tpf) {
        checkAndCleanPathfinders();

        // iterate internal state
        try {
            broker.update(tpf);
        } catch (Exception e) {
            // TODO: log
            // TODO: analyze problem and see if more than just logging can/has to be done.
        }

        streamDataToClients();
    }

    /** Checks the pathfinders and notifies the figurine controller about found paths. Removes pathfinders that have become done since the
     * last check.
     * <br><br>
     * <b>HINT:</b> This method locks the map of {@code this.pathfinders} for some time.
     *
     * @since 0.0.2
     * @author Dragonstb
     */
    private void checkAndCleanPathfinders() {
        List<McPathForFigurine> pathes = new ArrayList<>();
        synchronized (pathfinders) {
            pathfinders.keySet().stream().filter(key -> pathfinders.get(key).isDone()).forEach(key -> {
                var pathfinder = pathfinders.get(key);
                try {
                    Optional<List<Vector3f>> opt = pathfinder.get();
                    if(opt.isPresent()) {
                        List<Vector3f> list = opt.get();
                        // TODO: know which figurine this path is meant for
                        McPathForFigurine path = new McPathForFigurine(key, list);
                        pathes.add(path);
                    }
                } catch (Exception e) {
                    // TODO: log and go on
                } finally {
                    pathfinders.remove(key);
                }
            });
        }

        pathes.forEach(path -> {
            // TODO: all pathes in one message?
            Message msg = new Message(MessageHeadlines.FOUND_PATH, path);
            broker.send(msg, ChannelNames.GET_FIGURINE_DATA);
        });
    }

    /** Send data to the clients.
     *
     * @since 0.0.2
     * @author Dragonstb
     */
    void streamDataToClients() {
        String errCode = VHostErrorCodes.V91691;

        FetchCommand cmd = new FetchCommand(FetchCodes.FIGURINE_TELEMETRY);
        List<Optional<Object>> list = broker.request(ChannelNames.GET_FIGURINE_DATA, cmd, true);
        List<FigurineTelemetryDTO> teles = new ExtractorOfFirst(errCode).extractFirst(list, List.class);

        if(!teles.isEmpty()) {
            if(!(teles.getFirst() instanceof FigurineTelemetryDTO)) {
                Object obj = teles.getFirst();
                String msg = "Expected List of FigurineTelemetrDTO, but got list of "+(
                        obj != null ? obj.getClass().getSimpleName() : "null"
                )+" instead.";
                String use = VHostErrorCodes.assembleCodedMsg(msg, errCode);
                throw new IllegalArgumentException(use);
            }

            TelemetryDTO dto = new TelemetryDTO(teles);
            VHStreamed obj = new VHStreamed(VHStreamTypes.telemetry, dto);
            broker.sendOutbound(obj);
        }
    }


}
