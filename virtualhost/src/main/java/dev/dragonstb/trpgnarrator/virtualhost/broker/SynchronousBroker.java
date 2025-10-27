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

import dev.dragonstb.trpgnarrator.virtualhost.generic.FetchCommand;
import dev.dragonstb.trpgnarrator.virtualhost.generic.Message;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.ClockReceiver;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;

/** A synchronous broker of messages and parcels.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
public interface SynchronousBroker extends ClockReceiver {

    /** Registers the receiver to the channel. As a consequence, the receiver will receive all messages sent to the channel from now on
     * until the receiver deregisters again. This method is idempotent.
     *
     * @param receiver Receiver that becomes registered.
     * @param channelName Channel the receiver is registered to.
     */
    public void registerToChannel(@NonNull Receiver receiver, @NonNull String channelName);

    /** Deregisters the receiver from the channel. As a consequence, the receiver won't receive any messages sent to the channel any more
     * from now on until the receiver reregisters again. This method is idempotent.
     *
     * @param receiver Receiver that becomes deregistered.
     * @param channelName Channel the receiver is deregistered from.
     */
    public void deregisterFromChannel(@NonNull Receiver receiver, @NonNull String channelName);

    /** Send the object to all receivers registered to the channel. This means that the sender receives its own message if it is registered
     * to the channel.
     *
     * @param msg Object send. Might be {@code null}
     * @param channelName Channel the object is send to. Must <i>not</i> be {@code null}.
     */
    public void send(Message msg, @NonNull String channelName);

    /** Request an object from the receivers of a channel.
     *
     * @since 0.0.1
     * @param channelName Name of the channel.
     * @param fetch A code encoding whatever the receiver is asked for. The receiver has to know what it means. The broker is just a dumb
     * pipe.
     * @param If {@code true}, empty optionals <i>are not</i> included in the resulting list. If {@code false}, empty optionals <i>are</i>
     * included in the resulting list.
     * @return A list with whatever the registered receivers send back. Might be empty.
     */
    @NonNull
    public List<Optional<Object>> request(@NonNull String channelName, @NonNull FetchCommand fetch, boolean skipEmpties);

}
