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

package dev.dragonstb.trpgnarrator.virtualhost.hostconnector;

import dev.dragonstb.trpgnarrator.virtualhost.broker.ChannelNames;
import dev.dragonstb.trpgnarrator.virtualhost.broker.SynchronousBroker;
import dev.dragonstb.trpgnarrator.virtualhost.error.VHostErrorCodes;
import dev.dragonstb.trpgnarrator.virtualhost.generic.FetchCodes;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.VHCommand;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.VHCommands;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.VirtualHost;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.BoardDataDTO;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;

/**
 *
 * @author Dragonstb
 * @since 0.0.1
 */
abstract class AbstractHostConnector implements HostConnector, VirtualHost {

    /** The broker the connector speaks with. */
    private SynchronousBroker broker = null;

    @Override
    public void linkBroker(@NonNull SynchronousBroker broker) {
        if(this.broker == null) {
            this.broker = broker;
        }
    }

    /** Convenience method of calling the request method of the broker. So you don't have to use getBroker.request(..).
     *
     * @since 0.0.1
     * @author Dragonstb
     * @param channelName Broker channel the request is sent to.
     * @param fetch A fetch code. Who ever receives the request has to know what it means.
     * @param skipEmpties If true, empty optionals from receivers of the request are ignored not not included into the result.
     * @return List with the answers from the receivers of the request. May be empty.
     */
    @NonNull
    List<Optional<Object>> request(@NonNull String channelName, @NonNull String fetch, boolean skipEmpties) {
        return broker.request(channelName, fetch, skipEmpties);
    }

    @Override
    public Object dealRequest(@NonNull VHCommand command) {
        VHCommands com = command.getCommand();
        Object obj = switch(com) {
            case fetchBoard -> {yield getBoardData();}
        };

        return obj;
    }

    // ____________________  methods called internally when the actual command is deciphered  ____________________

    /** Gets the board data.
     *
     * TODO: When the board becomes large and split into patches, limit do certain patches chosen by the method caller.
     * TODO: For multiplayer, an id of the client may become necessary.
     *
     * @since 0.0.1
     * @return
     */
    @NonNull
    abstract BoardDataDTO getBoardData();

    // ____________________  code shared among all types of virtual hosts  ____________________

    /** Gets the board data.
     *
     * TODO: When the board becomes large and split into patches, limit do certain patches chosen by the method caller.
     * TODO: For multiplayer, an id of the client may become necessary.
     *
     * @since 0.0.1
     * @return
     */
    @NonNull
    BoardDataDTO doGetBoardData() {
        String errCode = VHostErrorCodes.V16231;
        String channelName = ChannelNames.GET_BOARD_DATA;
        String fetchCode = FetchCodes.BOARD_DATA;
        List<Optional<Object>> list = request(channelName, fetchCode, true);
        if(list.isEmpty()) {
            String msg = "BoardDataDTO output validation failed: No board data elements.";
            String use = VHostErrorCodes.assembleCodedMsg(msg, errCode);
            throw new RuntimeException(use);
        }

        Optional<Object> opt = list.getFirst();
        if(opt.isEmpty()) {
            String msg = "BoardDataDTO output validation failed: Missing board data.";
            String use = VHostErrorCodes.assembleCodedMsg(msg, errCode);
            throw new RuntimeException(use);
        }

        Object obj = opt.get();
        if(!(obj instanceof BoardDataDTO)) {
            String msg = "BoardDataDTO output validation failed: Expected a BoardDataDTO, but got an instance of "
                    + (obj != null ? obj.getClass().getSimpleName() : "null")
                    + " instead.";
            String use = VHostErrorCodes.assembleCodedMsg(msg, errCode);
            throw new RuntimeException(use);
        }

        return (BoardDataDTO)obj;
    }

}
