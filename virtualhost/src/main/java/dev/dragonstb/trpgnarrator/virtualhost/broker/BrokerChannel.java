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

package dev.dragonstb.trpgnarrator.virtualhost.broker;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

/** A channel of the {@link SynchonousBroker sync broker}.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
final class BrokerChannel {

    @Getter(AccessLevel.PACKAGE) private final String name;
    private final List<Receiver> receivers = new ArrayList<>();

    public BrokerChannel(@NonNull String name) {
        this.name = name;
    }

    /** Adds the receiver to the channel, if not already registered.
     *
     * @since 0.0.1
     * @author Dragonstb
     * @param receiver Receiver to be added.
     */
    void addReceiver(@NonNull Receiver receiver) {
        if(!receivers.contains(receiver)) {
            receivers.add(receiver);
        }
    }

    /** Removes the receiver from the channel.
     *
     * @author Dragonstb
     * @since 0.0.1
     * @param receiver Receiver to be removed.
     */
    void removeReceiver(@NonNull Receiver receiver) {
        receivers.remove(receiver);
    }


    /** Sends the object to all receivers listed in this channel.
     *
     * @since 0.0.1;
     * @param obj Object to be sent.
     */
    void send(Object obj) {
        receivers.forEach( receiver -> receiver.receive(obj) );
    }

    /** Fetches objects requested from the receivers of this channel.
     *
     * @param fetch Code for whatever the receivers are asked for. A receiver has to know by itself what a code means. This broker is just a
     * dumb pipe.
     * @param skipEmpties Shall empty optionals be excluded ({@code true}) or included ({@code false}) into the resulting list?
     * @return List with whatever we got.
     */
    List<Optional<Object>> request(@NonNull String fetch, boolean skipEmpties) {
        List<Optional<Object>> list = new ArrayList<>();
        receivers.forEach( receiver -> {
            Optional<Object> opt = receiver.request(fetch);
            if(opt.isPresent() || !skipEmpties) {
                list.add(opt);
            }
        });
        return list;
    }

    /** tells if there are no receivers listed.
     *
     * @author Dragonstb
     * @since 0.0.1
     * @return {@code True} if and only if no receivers are listed in this channel.
     */
    boolean isEmpty() {
        return receivers.isEmpty();
    }

}
