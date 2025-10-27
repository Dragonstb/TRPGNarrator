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
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.Clock;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.ClockReceiver;
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

    private final List<Future> pathfinders = new ArrayList<>();

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

    /** Gets a pathfinder from the board and submits it.
     *
     * @since 0.0.2
     * @author Dragonstb
     * @param parm Expected to be a List of Integers of size two. The first number is the id of the field where the path starts. The second
     * number is the id of the field where the path ends.
     */
    private void findPath(Object parm) {
        // step 1: assemble a request
        String code = VHostErrorCodes.V42664;
        List<Integer> conf;
        try {
            conf = (List<Integer>)parm;
        }
        catch (Exception e) {
            String msg = "Expected content to be a List<Integer>, but got an instance of class "
                    + (parm != null ? parm.getClass().getSimpleName() : "null") + " instead";
            String use = VHostErrorCodes.assembleCodedMsg(msg, code);
            throw new ClassCastException(use);
        }

        if(conf.size() != 2) {
            String msg = "Expected content to be a List<Integer> of size 2, but got a list with "+conf.size()+" entries instead";
            String use = VHostErrorCodes.assembleCodedMsg(msg, code);
            throw new IllegalArgumentException(use);
        }

        int fromField = conf.get(0);
        int toField = conf.get(1);

        PathfindingConfig pfCOnf = new PathfindingConfig(fromField, toField);
        FetchCommand cmd = new FetchCommand(FetchCodes.BOARD_PATHFINDER, pfCOnf);

        // step 2: deal with reply
        code = VHostErrorCodes.V97498;

        List<Optional<Object>> opt = broker.request(ChannelNames.GET_BOARD_DATA, cmd, true);
        ExtractorOfFirst extractor = new ExtractorOfFirst(code);
        Callable pathfinder = extractor.extractFirst(opt, Callable.class);
        Future future = executor.submit(pathfinder);

        // this method may be called at any time, and the list of pathfinders is also accessed from a different thread at each clock signal.
        synchronized (pathfinders) {
            pathfinders.add(future);
        }
    }



    // ____________________  answering requests  ____________________




    // ____________________  clock receiver  ____________________

    @Override
    public void update(float tpf) {
        // TODO: check pathfinders if completed
    }



}
