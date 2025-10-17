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

package dev.dragonstb.trpgnarrator.client.clientconnector;

import dev.dragonstb.trpgnarrator.client.error.ClientErrorCodes;
import dev.dragonstb.trpgnarrator.client.error.HostConnectionNotReadyException;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.LocalVirtualHost;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.BoardDataDTO;
import lombok.NonNull;

/** A client connector for use in local single player. In local mode, the serialization/deserialization is not necessary.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
final class LocalClientConnector implements LocalClientForApp {

    /** The connected virtual host. */
    private LocalVirtualHost host = null;

    @Override
    public BoardDataDTO getBoardData() throws RuntimeException, NullPointerException, HostConnectionNotReadyException{
        if(host == null) {
            String msg = "Cannot fetch board data: client connection has yet not been established.";
            String code = ClientErrorCodes.C30737;
            String use = ClientErrorCodes.assembleCodedMsg(msg, code);
            throw new HostConnectionNotReadyException(use);
        }
        BoardDataDTO dto = host.getBoardData();
        return dto;
    }

    @Override
    public void connectToVirtualHost(@NonNull LocalVirtualHost host) {
        if(this.host == null) {
            this.host = host;
            // TODO: register client connector to host connector as sink for host-to-client data flow
        }
    }

}
