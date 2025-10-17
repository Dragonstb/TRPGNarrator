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

package dev.dragonstb.trpgnarrator.client.error;

/** List of error codes and error message assembly. The idea is that each code is used in exactly one place in the
 * code of the client. Thus, when an error occurs, you can easily find the code line by searching for the code.
 *
 * This class lists the error codes, so that you can easily check if a code is already in use when you need a new one.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
public final class ClientErrorCodes {

    public static final String C04278 = "C04278";
    public static final String C17679 = "C17679";
    public static final String C28331 = "C28331";
    public static final String C38587 = "C38587";
    public static final String C63749 = "C63749";
    public static final String C83663 = "C83663";
    public static final String C97700 = "C97700";

    /** Assembles the given message and the code to a single message that includes the code.
     *
     * @since 0.0.1
     * @author Dragonstb
     * @param msg Error message without error code.
     * @param code Error code.
     * @return Message with notification of the error code appended.
     */
    public static final String assembleCodedMsg(String msg, String code) {
        String useMsg = msg != null ? msg : "no error message defined";
        String useCode = code != null ? code : "undefined";
        return useMsg+" (on client, error code:"+useCode+")";
    }

}
