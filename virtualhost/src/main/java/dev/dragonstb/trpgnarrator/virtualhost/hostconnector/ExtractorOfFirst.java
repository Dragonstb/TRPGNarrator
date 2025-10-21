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
import dev.dragonstb.trpgnarrator.virtualhost.generic.Globals;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.BoardDataDTO;
import java.text.MessageFormat;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *
 * @author Dragonstb
 * @since
 */
@Setter
@NoArgsConstructor
@Accessors(chain = true)
final class ExtractorOfFirst {

    private String errCode = Globals.EMPTY_STRING;
    private String emptyListMsg = "List is empty";
    private String emptyOptionalMsg = "Optional is empty";
    /** Message for exception thrown when the first optional in the list is of an unwanted type. Can contain a {0} as a placeholder which
     * becomes automatically filled with the class of the object in the optional, and a {1} for the expected class. */
    private String wrongTypeMsg = "Wrong type, got {0}";

    ExtractorOfFirst(String errCode) {
        this.errCode = errCode;
    }

    <T extends Object> T extractFirst(List<Optional<Object>> list, @NonNull Class<T> type) throws NoSuchElementException {
        if(errCode == null) {
            errCode = "undefined";
        }

        if(list == null || list.isEmpty()) {
            String use = VHostErrorCodes.assembleCodedMsg(emptyListMsg, errCode);
            throw new NoSuchElementException(use);
        }

        Optional<Object> opt = list.getFirst();
        if(opt.isEmpty()) {
            String use = VHostErrorCodes.assembleCodedMsg(emptyOptionalMsg, errCode);
            throw new NoSuchElementException(use);
        }

        Object obj = opt.get();
        if(obj == null || !type.isInstance(obj)) {
            String msg = MessageFormat.format(wrongTypeMsg, (obj != null ? obj.getClass().getSimpleName() : "null"), type.getSimpleName());
            String use = VHostErrorCodes.assembleCodedMsg(msg, errCode);
            throw new ClassCastException(use);
        }

        return (T)obj;
    }

}
