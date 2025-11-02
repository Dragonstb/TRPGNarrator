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

import dev.dragonstb.trpgnarrator.virtualhost.error.VHostErrorCodes;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.BoardDataDTO;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.FigurinesListDTO;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.vhcommandparms.FindPathForFigurineParms;
import lombok.NonNull;

/** Host connector that runs on the same machine as the client, with this client being the only client connected to.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
final class LocalHostConnector extends AbstractHostConnector {

    @NonNull
    @Override
    public BoardDataDTO getBoardData() {
        return super.doGetBoardData();
    }

    @NonNull
    @Override
    FigurinesListDTO getFigurineList() {
        return super.doGetFigurineList();
    }

    @Override
    void sendFindPathForFigurine(Object parms) {
        String errCode = VHostErrorCodes.V85159;

        if(parms == null) {
            String msg = "No parameters in request for finding a path.";
            String use = VHostErrorCodes.assembleCodedMsg(msg, errCode);
            throw new NullPointerException(use);
        }

        if(!(parms instanceof FindPathForFigurineParms)) {
            String msg = "Expected parameter to be of class FindPathForFigurineParms, but got an instance of "
                    +parms.getClass().getSimpleName()+" instead.";
            String use = VHostErrorCodes.assembleCodedMsg(msg, errCode);
            throw new NullPointerException(use);
        }

        FindPathForFigurineParms arg = (FindPathForFigurineParms)parms;
        super.doSendFindPathForFigurine(arg);
    }

}
