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

package dev.dragonstb.trpgnarrator.virtualhost.error;

import java.text.MessageFormat;

/** Collection of error codes used by the virtual host.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
public final class VHostErrorCodes {

    public static final String V03107 = "V03107";

    public static final String V11349 = "V11349";
    public static final String V14985 = "V14985";
    public static final String V16231 = "V16231";

    public static final String V29882 = "V29882";

    public static final String V31251 = "V31251";

    public static final String V43108 = "V43108";
    public static final String V42664 = "V42664";
    public static final String V45601 = "V45601";
    public static final String V46199 = "V46199";
    public static final String V48135 = "V48135";

    /** This code V62000 is used in a temporary, prototype-like part of the code and prone to be unused. */
    public static final String V62000 = "V62000-deprecated";
    public static final String V78642 = "V78642";

    public static final String V85159 = "V85159";

    public static final String V94039 = "V94039";
    public static final String V97498 = "V97498";
    public static final String V98915 = "V98915";

    /** Assembles the given message and the code to a single message that includes the code.
     *
     * @since 0.0.1
     * @author Dragonstb
     * @param msg Error message without error code.
     * @param code Error code.
     * @return Message with notification of the error code appended.
     */
    public static final String assembleCodedMsg(String msg, String code) {
        // TODO: this method exists in the same form in several components now. Possibliy shift it into a new "common" subproject
        String useMsg = msg != null ? msg : "no error message defined";
        String useCode = code != null ? code : "undefined";
        return MessageFormat.format("{0} (on vhost, error code: {1})", useMsg, useCode);
    }

}
