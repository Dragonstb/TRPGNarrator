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

/** Some global agreements made so that the components can work together.
 *
 * TODO: some constants are used by virtual host, client, and possibly even the server. These constant should be better defined once in
 * a new "common" subproject and artefact, rather than defining them in each subproject over and over again.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
public final class Globals {

    /* Radius of a hex field, in WU. Two oppsing corners of a hex field will be twice this distance apart. */
    public static final float FIELD_RADIUS = .5f;
    /** Diameter of a field, in WU. */
    public static final float FIELD_DIAMETER = 2 * FIELD_RADIUS;
}
