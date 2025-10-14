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

package dev.dragonstb.trpgnarrator.virtualhost.outwardapi;

import dev.dragonstb.trpgnarrator.virtualhost.board.Board;
import dev.dragonstb.trpgnarrator.virtualhost.board.BoardBuilder;
import dev.dragonstb.trpgnarrator.virtualhost.broker.SyncBrokerFactory;
import dev.dragonstb.trpgnarrator.virtualhost.broker.SynchonousBroker;
import lombok.NoArgsConstructor;

/** Assembles and returns a virtual host.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
@NoArgsConstructor
public final class VirtualHostBuilder {

    private SynchonousBroker broker;
    private Board board;

    public VirtualHost build() {
        broker = SyncBrokerFactory.createBroker();
        board = new BoardBuilder().setBroker(broker).build();
        return null;
    }

}
