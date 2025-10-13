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

package dev.dragonstb.trpgnarrator.client.tweens;

import lombok.NonNull;

/** An action tween that progresses several tweens in parallel. Child tweens that end earlier than other child tweens are not longer
 * progressed once they are done.
 *
 * It is not possible for passing an empty list of action tweens, as this would result in an invalid tween length of 0.
 *
 * @since 0.0.1
 * @author Dragonstb
 */
public class ParallelTween extends ActionTween{

    private final ActionTween[] tweens;

    public ParallelTween(@NonNull ActionTween... tweens) {
        super( getMaximumLength(tweens) );
        this.tweens = tweens;
    }

    @Override
    public void internalAction(float dt) {
        for (ActionTween tween : tweens) {
            if(!tween.isDone()){
                tween.progress(dt);
            }
        }
    }

    private static float getMaximumLength(ActionTween... tweens) {
        float length = 0;
        for (ActionTween tween : tweens){
            length = Math.max(length, tween.getLength());
        }

        return length;
    }

}
