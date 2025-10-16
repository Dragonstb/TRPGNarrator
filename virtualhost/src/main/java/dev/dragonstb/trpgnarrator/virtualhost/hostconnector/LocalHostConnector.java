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
import dev.dragonstb.trpgnarrator.virtualhost.error.VHostErrorCodes;
import dev.dragonstb.trpgnarrator.virtualhost.generic.FetchCodes;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.LocalVirtualHost;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.BoardDataDTO;
import java.util.List;
import java.util.Optional;

/** Host connector that runs on the same machine as the client, with this client being the only client connected to.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
final class LocalHostConnector extends AbstractHostConnector implements LocalVirtualHost {

    @Override
    public BoardDataDTO getBoardData() {
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
                    + (obj == null ? "null" : obj.getClass().getSimpleName())
                    + " instead.";
            String use = VHostErrorCodes.assembleCodedMsg(msg, errCode);
            throw new RuntimeException(use);
        }

        return (BoardDataDTO)obj;
    }


}
