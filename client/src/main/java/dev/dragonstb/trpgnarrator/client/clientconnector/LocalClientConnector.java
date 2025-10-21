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
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.VirtualHost;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.VHCommand;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.VHCommands;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.BoardDataDTO;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.FigurineDTO;
import java.util.List;
import lombok.NonNull;

/** A client connector for use in local single player. In local mode, the serialization/deserialization is not necessary.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
final class LocalClientConnector implements LocalClientForApp {

    /** The connected virtual host. */
    private VirtualHost host = null;

    /** Sends the command to the virtual host.
     *
     * @since 0.0.2
     * @param command Command sent.
     * @return Response. Can be {@code null}.
     */
    private Object sendCommand(@NonNull VHCommand command) {
        return host.dealRequest(command);
    }

    /** This method just throws an exception when the connection to the host is not ready and does nothing elsewise.
     *
     * @since 0.0.2
     * @author Dragonstb.
     * @param msg Message for the exception
     * @param code Error code to be used.
     * @throws HostConnectionNotReadyException When the connection has not been established yet.
     */
    private void checkConnectionReadiness(@NonNull String msg, @NonNull String code) throws HostConnectionNotReadyException {
        if(host == null) {
            String use = ClientErrorCodes.assembleCodedMsg(msg, code);
            throw new HostConnectionNotReadyException(use);
        }
    }

    @Override
    public BoardDataDTO getBoardData() throws RuntimeException, NullPointerException, HostConnectionNotReadyException{
        String code = ClientErrorCodes.C30737;
        checkConnectionReadiness("Cannot fetch board data: client connection has yet not been established.", code);

        VHCommand command = new VHCommand(VHCommands.fetchBoard);
        Object obj = sendCommand(command);
        if(obj == null) {
            String msg = "Cannot fetch board data: response from host is null.";
            String use = ClientErrorCodes.assembleCodedMsg(msg, code);
            throw new NullPointerException(use);
        }

        if(!(obj instanceof BoardDataDTO)) {
            String msg = "Cannot fetch board data: response from host is invalid.";
            String use = ClientErrorCodes.assembleCodedMsg(msg, code);
            throw new ClassCastException(use);
        }

        BoardDataDTO dto = (BoardDataDTO)obj;
        return dto;
    }

    @Override
    public List<FigurineDTO> getFigurinesList() {
        String code = ClientErrorCodes.C05317;
        checkConnectionReadiness("Cannot fetch figurines list: connection has yet not been established.", code);

        VHCommand command = new VHCommand(VHCommands.fetchFigurines);
        Object obj = sendCommand(command);
        // TODO: the following kind of check happens often here. Write a piece of code for this task that can beshared.
        if(obj == null) {
            String msg = "Cannot fetch figurine list: response from host is null.";
            String use = ClientErrorCodes.assembleCodedMsg(msg, code);
            throw new NullPointerException(use);
        }

        if(!(obj instanceof List<?>)) {
            String msg = "Cannot fetch figurine list: response from host is invalid.";
            String use = ClientErrorCodes.assembleCodedMsg(msg, code);
            throw new ClassCastException(use);
        }

        List<FigurineDTO> list = (List<FigurineDTO>)obj;
        return list;
    }


    @Override
    public void connectToVirtualHost(@NonNull VirtualHost host) {
        if(this.host == null) {
            this.host = host;
            // TODO: register client connector to host connector as sink for host-to-client data flow
        }
    }

}
