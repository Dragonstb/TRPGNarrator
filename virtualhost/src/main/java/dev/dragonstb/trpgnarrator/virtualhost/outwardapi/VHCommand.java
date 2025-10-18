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

import lombok.Getter;
import lombok.NonNull;

/** A command from a client to a {@link VirtualHost virtual host}.
 *
 * @author Dragonstb
 * @since 0.0.2
 */
@Getter
public final class VHCommand {

    @NonNull private final VHCommands command;
    private final Object parms;

    /** Generates.
     *
     * @since 0.0.2
     * @param command The type of command.
     * @param parms The parameters of the command. For example, when a figurine wants to move from one field to another, these parameters
     * would be the figurine id and the id of the field of destination. Can be {@code null}.
     */
    public VHCommand(@NonNull VHCommands command, Object parms) {
        this.command = command;
        this.parms = parms;
    }

    /** Generates with no parameters.
     *
     * @since 0.0.2
     * @param command The type of command.
     */
    public VHCommand(@NonNull VHCommands command) {
        this(command, null);
    }

}
