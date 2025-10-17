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
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.HostType;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/** Creates a client connector.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
@NoArgsConstructor
@Accessors(chain = true)
@Setter
public final class ClientConnectorBuilder {

    /** Type of the virtual host. */
    private HostType type;

    public ClientConnectorBuilder(HostType type) {
        this.type = type;
    }

    /** Creates a client connector;
     *
     * @author Dragonstb
     * @since 0.0.1
     * @return Connector
     * @throws UnsupportedOperationException Some host times have not become supported yet.
     * @throws NullPointerException Missing a required setting.
     */
    public ClientForIngame build() throws UnsupportedOperationException, NullPointerException {
        String errCode = ClientErrorCodes.C97700;
        if(type == null) {
            String msg = "The type of the virtual host must be specified, but it is not.";
            String use = ClientErrorCodes.assembleCodedMsg(msg, errCode);
            throw new NullPointerException(use);
        }

        ClientForIngame client = switch(type) {
            case local -> {yield new LocalClientConnector();}
            case web -> {
                String msg = "Web hosts have not become supported yet.";
                String use = ClientErrorCodes.assembleCodedMsg(msg, errCode);
                throw new UnsupportedOperationException(use);
            }
            case lan -> {
                String msg = "LAN hosts have not become supported yet.";
                String use = ClientErrorCodes.assembleCodedMsg(msg, errCode);
                throw new UnsupportedOperationException(use);
            }
        };

        return client;
    }

}
