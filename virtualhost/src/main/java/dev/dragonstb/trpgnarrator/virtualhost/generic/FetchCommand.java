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

package dev.dragonstb.trpgnarrator.virtualhost.generic;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/** A command sent over the fetch pipeline of the {@link dev.dragonstb.trpgnarrator.virtualhost.broker.SynchronousBroker internal broker}.
 *
 * @author Dragonstb
 * @since 0.0.2
 */
@Getter
@AllArgsConstructor
public final class FetchCommand {

    /** Specifies the actual command. */
    @NonNull private final String command;
    /** A parametrization of the command. */
    private final Object parms;

    /** Generates without parameters. Is is a short for {@code FetchCommand(command, null)}.
     *
     * @since 0.0.2
     * @param command Specifies the actual command.
     */
    public FetchCommand(String command) {
        this(command, null);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof FetchCommand)) {
            return false;
        }
        else if(this == obj) {
            return true;
        }

        FetchCommand other = (FetchCommand)obj;
        boolean parmsEqual = (parms != null && other.parms != null && parms.equals(other.getParms()))
                || (parms == null && other.getParms() == null);
        return command.equals(other.getCommand()) && parmsEqual;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.command);
        hash = 43 * hash + Objects.hashCode(this.parms);
        return hash;
    }

}
