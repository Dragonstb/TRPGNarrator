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

import lombok.NonNull;

/** Where the server or the client connect to the virtual host.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
public interface VirtualHost {

    /** Makes a request to the virtual host.
     *
     * @since 0.0.2
     * @param command An object that parametrizes the request. The VHCommand is just a dumb parcel that does not care about the content.
     * The implementation if the virtual host knows how to deal with it.
     * @return An object or not. Can be {@code null}.
     */
    public Object dealRequest(@NonNull VHCommand command);

    /** Registers a stream receiver to the virtual host. This method does nothing in case of the given receiver being already registered.
     *
     * @since 0.0.2
     * @param receiver Receiver.
     */
    public void addStreamReceiver(@NonNull StreamReceiver receiver);
}
