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
import lombok.NoArgsConstructor;
import dev.dragonstb.trpgnarrator.virtualhost.broker.SynchronousBroker;
import dev.dragonstb.trpgnarrator.virtualhost.error.VHostErrorCodes;
import dev.dragonstb.trpgnarrator.virtualhost.hostconnector.HostConnector;
import dev.dragonstb.trpgnarrator.virtualhost.hostconnector.HostConnectorBuilder;
import dev.dragonstb.trpgnarrator.virtualhost.hostconnector.HostType;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

/** Assembles and returns a virtual host.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
@NoArgsConstructor
@Accessors(chain = true)
@Setter
public final class VirtualHostBuilder {

    /** Type of the virtual host. */
    private HostType type = null;

    /** Generates with the given type already set.
     *
     * @since 0.0.1
     * @param type
     */
    public VirtualHostBuilder(@NonNull HostType type) {
        this.type = type;
    }

    public VirtualHost build() throws NullPointerException, UnsupportedOperationException {
        String errCode = VHostErrorCodes.V46199;
        if(type == null) {
            String msg = "The type of the virtual host must be specified, but it is not.";
            String use = VHostErrorCodes.assembleCodedMsg(msg, errCode);
            throw new NullPointerException(use);
        }

        HostConnector connector;
        try {
            connector = new HostConnectorBuilder().build(type);
        } catch (UnsupportedOperationException e) {
            // types web and lan yet unsupported
            String use = VHostErrorCodes.assembleCodedMsg(e.getMessage(), errCode);
            throw new UnsupportedOperationException(use);
        }

        SynchronousBroker broker = SyncBrokerFactory.createBroker(connector);
        connector.linkBroker(broker);

        Board board = new BoardBuilder().setBroker(broker).build();
        return null;
    }

}
