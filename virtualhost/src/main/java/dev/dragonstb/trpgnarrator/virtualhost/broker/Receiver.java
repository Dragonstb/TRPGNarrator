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
import java.util.Optional;
import lombok.NonNull;

/** Can receive messages from the {@link SynchonousBroker broker}.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
public interface Receiver {

    /** Delivers the object to the receiver. The receiver has to care about validation and interpretation of what it got. The broker is just
     * a dump pipe.
     *
     * @since 0.0.1
     * @param msg Object delivered. Might be {@code null}.
     */
    public void receive(Message msg);

    /** The origin of the requests fetches an object from the receiver.
     *
     * @param fetch A command object that specifies the requested object. The receiver must now how to interpret this.
     * @return Requested object.
     */
    public Optional<Object> request(@NonNull FetchCommand fetch);
}
