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

import dev.dragonstb.trpgnarrator.virtualhost.generic.converter.ExtractorOfFirst;
import dev.dragonstb.trpgnarrator.virtualhost.broker.ChannelNames;
import dev.dragonstb.trpgnarrator.virtualhost.broker.SynchronousBroker;
import dev.dragonstb.trpgnarrator.virtualhost.error.VHostErrorCodes;
import dev.dragonstb.trpgnarrator.virtualhost.generic.FetchCodes;
import dev.dragonstb.trpgnarrator.virtualhost.generic.FetchCommand;
import dev.dragonstb.trpgnarrator.virtualhost.generic.Message;
import dev.dragonstb.trpgnarrator.virtualhost.generic.MessageHeadlines;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.VHCommand;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.VHCommands;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.VirtualHost;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.BoardDataDTO;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.FigurinesListDTO;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.vhcommandparms.FindPathForFigurineParms;
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
     * @param fetch A fetch command. Who ever receives the request has to know what it means.
     * @param skipEmpties If true, empty optionals from receivers of the request are ignored not not included into the result.
     * @return List with the answers from the receivers of the request. May be empty.
     */
    @NonNull
    List<Optional<Object>> request(@NonNull String channelName, @NonNull FetchCommand fetch, boolean skipEmpties) {
        return broker.request(channelName, fetch, skipEmpties);
    }

    @Override
    public Object dealRequest(@NonNull VHCommand command) {
        VHCommands com = command.getCommand();
        Object obj = switch(com) {
            case fetchBoard -> {yield getBoardData();}
            case fetchFigurines -> {yield getFigurineList();}
            case setPathForFigurine -> {sendFindPathForFigurine(command.getParms()); yield true;}
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

    @NonNull
    abstract FigurinesListDTO getFigurineList();

    abstract void sendFindPathForFigurine(Object parms);

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
        FetchCommand fetchCommand = new FetchCommand(FetchCodes.BOARD_DATA, null);
        List<Optional<Object>> list = request(channelName, fetchCommand, true);

        BoardDataDTO dto = new ExtractorOfFirst(errCode)
                .setEmptyListMsg("BoardDataDTO output validation failed: No board data elements.")
                .setEmptyOptionalMsg("BoardDataDTO output validation failed: Missing board data.")
                .setWrongTypeMsg("BoardDataDTO output validation failed: Expected a {1}, but got an instance of {0} instead.")
                .extractFirst(list, BoardDataDTO.class);

        return dto;
    }

    @NonNull
    FigurinesListDTO doGetFigurineList() {
        String errCode = VHostErrorCodes.V45601;
        String channelName = ChannelNames.GET_FIGURINE_DATA;
        FetchCommand fetchCommand = new FetchCommand(FetchCodes.FIGURINE_FULL_LIST, null);
        List<Optional<Object>> list = request(channelName, fetchCommand, true);

        FigurinesListDTO result = new ExtractorOfFirst(errCode)
                .setEmptyListMsg("Figurine List output validation failed: No figurines.")
                .setEmptyOptionalMsg("Figurine List output validation failed: Missing figurine data.")
                .setWrongTypeMsg("Figurine List output validation failed: Expected a {1}, but got an instance of {0} instead.")
                .extractFirst(list, FigurinesListDTO.class);
        return result;
    }

    /** Sends the parameter object of finding a path for a figurine to the correct channel.
     *
     * @since 0.0.2
     * @author Dragonstb
     * @param parms Parameters of the request.
     */
    void doSendFindPathForFigurine(@NonNull FindPathForFigurineParms parms) {
        String errCode = VHostErrorCodes.V29882;

        // parms.figurineId is @NonNull => no check needed TODO: think of limiting the length of ids of figurines.
        // parms.toFieldId is an int anyway => no check needed

        Message msg = new Message(MessageHeadlines.PLEASE_FIND_PATH, parms);
        broker.send(msg, ChannelNames.GET_FIGURINE_DATA);
    }

}
