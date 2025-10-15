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

import lombok.NonNull;

/**
 *
 * @author Dragonstb
 * @since
 */
public final class HostConnectorBuilder {

    @NonNull
    public HostConnector build(@NonNull HostType type) {
        HostConnector connector = switch(type) {
            case local: yield new LocalHostConnectorBuilder().build();
            case web: throw new UnsupportedOperationException("web host yet not supported");
            case lan: throw new UnsupportedOperationException("lan host yet not supported");
        };
        return connector;
    }

}
