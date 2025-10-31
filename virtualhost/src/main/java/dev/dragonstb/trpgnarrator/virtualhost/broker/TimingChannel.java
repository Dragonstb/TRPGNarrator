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

import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.ClockReceiver;
import java.util.HashSet;
import java.util.Set;
import lombok.NonNull;

/** A special channel for distributing the time signal.
 *
 * @author Dragonstb
 * @since 0.0.2
 */
final class TimingChannel {

    private final Set<ClockReceiver> receivers = new HashSet<>();

    void addReceiver(@NonNull ClockReceiver receiver) {
        if(!receivers.contains(receiver)) {
            receivers.add(receiver);
        }
    }

    void removeReceiver(@NonNull ClockReceiver receiver) {
        receivers.remove(receiver);
    }

    void update(float tpf) {
        receivers.forEach( rec -> rec.update(tpf) );
    }

}
