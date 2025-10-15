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

package dev.dragonstb.trpgnarrator.virtualhost.board;

import dev.dragonstb.trpgnarrator.virtualhost.error.VHostErrorCodes;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import dev.dragonstb.trpgnarrator.virtualhost.broker.SynchronousBroker;

/**
 *
 * @author Dragonstb
 * @since
 */
@NoArgsConstructor
@Accessors(chain=true)
@Setter
public final class BoardBuilder {

    private SynchronousBroker broker = null;

    @NonNull
    public Board build() {
        String errCode = VHostErrorCodes.V48135;
        if(broker == null) {
            String msg = "Broker must be defined, but is null.";
            String use = VHostErrorCodes.assembleCodedMsg(msg, errCode);
            throw new NullPointerException(use);
        }

        BoardData board = new BoardData(broker);
        return board;
    }

}
