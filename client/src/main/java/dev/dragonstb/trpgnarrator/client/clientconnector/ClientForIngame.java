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

import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.BoardDataDTO;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.FigurineDTO;
import java.util.List;

/** The interface the client connector exposes towards the {@link dev.dragonstb.trpgnarrator.client.ingame.IngameAppState IngameAppState}.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
public interface ClientForIngame {

    /** Fetches the board data from the virtual host.
     *
     * TODO: Once the board becomes split into patches we likely have to specify the requested patches some how.
     *
     * @since 0.0.1
     * @return The board data.
     */
    public BoardDataDTO getBoardData();

    /** Fetches the list of figurines from the virtual host.
     *
     * @since 0.0.2
     * @return List of all figurines.
     */
    public List<FigurineDTO> getFigurinesList();
}
