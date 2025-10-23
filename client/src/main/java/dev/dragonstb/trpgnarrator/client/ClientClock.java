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

package dev.dragonstb.trpgnarrator.client;

import com.jme3.app.state.AbstractAppState;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.Clock;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.ClockReceiver;

/** A {@link dev.dragonstb.trpgnarrator.virtualhost.outwardapi.Clock clock} for the virtual host that forwards the update loop of the engine
 * as a time signal.
 *
 * @author Dragonstb
 * @since 0.0.2
 */
final class ClientClock extends AbstractAppState implements Clock {

    private ClockReceiver receiver;

    @Override
    public void update(float tpf) {
        if(receiver != null) {
            receiver.update(tpf);
        }
    }

    @Override
    public void setReceiver(ClockReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void setPaused(boolean paused) {
        super.setEnabled(paused);
    }

}
