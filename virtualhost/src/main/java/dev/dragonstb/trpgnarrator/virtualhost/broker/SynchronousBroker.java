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

import lombok.NonNull;

/** A synchronous broker of messages and parcels.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
public interface SynchronousBroker {

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
     * @param obj Object send. Might be {@code null}
     * @param channelName Channel the object is send to. Must <i>not</i> be {@code null}.
     */
    public void send(Object obj, @NonNull String channelName);
}
