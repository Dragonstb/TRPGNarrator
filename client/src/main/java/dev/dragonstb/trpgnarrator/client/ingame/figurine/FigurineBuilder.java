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

package dev.dragonstb.trpgnarrator.client.ingame.figurine;

import com.jme3.math.ColorRGBA;
import dev.dragonstb.trpgnarrator.client.error.ClientErrorCodes;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *
 * @author Dragonstb
 * @since
 */
@NoArgsConstructor
@Accessors(chain = true)
@Setter()
public final class FigurineBuilder {

    /** The color. */
    private ColorRGBA color = null;
    /** The id of the new figurine. */
    private String id = null;

    /** Creates a FigurineBuilder with the given {@code id} already set.
     *
     * @author Dragonstb
     * @since 0.0.1
     * @param id Id of the soon-to-be figurine.
     * @return Builder.
     */
    public static FigurineBuilder ofId(@NonNull String id) {
        FigurineBuilder builder = new FigurineBuilder();
        builder.setId(id);
        return builder;
    }

    /** Builds the figurine.
     * @since 0.0.1
     * @author Dragonstb
     * @return A figurine.
     */
    public Figurine build() {
        String errCode = ClientErrorCodes.C83663;
        if(color == null) {
            String msg = "Color must be defined, but is null.";
            String use = ClientErrorCodes.assembleCodedMsg(msg, errCode);
            throw new RuntimeException(use);
        }
        if(id == null) {
            String msg = "Id must be defined, but is null.";
            String use = ClientErrorCodes.assembleCodedMsg(msg, errCode);
            throw new RuntimeException(use);
        }

        Figurine fig = new SimpleFigurine(id, color);
        return fig;
    }

}
