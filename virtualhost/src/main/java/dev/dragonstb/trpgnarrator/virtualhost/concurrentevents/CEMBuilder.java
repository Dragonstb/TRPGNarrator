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

import dev.dragonstb.trpgnarrator.virtualhost.broker.SynchronousBroker;
import dev.dragonstb.trpgnarrator.virtualhost.error.VHostErrorCodes;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.Clock;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import lombok.Setter;
import lombok.experimental.Accessors;

/** Builds a Concurrent Event Manager.
 *
 * @author Dragonstb
 * @since 0.0.2
 */
@Setter
@Accessors(chain = true)
public final class CEMBuilder {

    private SynchronousBroker broker;
    private Clock clock;
    private ScheduledThreadPoolExecutor executor;

    /** Builds a concurrent event manager.
     *
     * @since 0.0.2
     * @author Dragonstb
     * @return The manager.
     * @throws NullPointerException When at least one of the broker, the clock, or the executor is missing.
     */
    public ConcurrentEventManager build() {
        String errCode = VHostErrorCodes.V31251;

        if(broker == null) {
            String msg = "Broker must be defined, but is null.";
            String use = VHostErrorCodes.assembleCodedMsg(msg, errCode);
            throw new NullPointerException(use);
        }
        if(clock == null) {
            String msg = "Clock must be defined, but is null.";
            String use = VHostErrorCodes.assembleCodedMsg(msg, errCode);
            throw new NullPointerException(use);
        }
        if(executor == null) {
            String msg = "Scheduled Thread Pool Executor must be defined, but is null.";
            String use = VHostErrorCodes.assembleCodedMsg(msg, errCode);
            throw new NullPointerException(use);
        }

        CEManagement cem = new CEManagement(clock, broker, executor); // cem signs itself up to the broker
        clock.setReceiver(cem);

        return cem;
    }

}
