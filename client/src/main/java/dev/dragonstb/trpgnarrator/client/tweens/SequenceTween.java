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

import java.util.List;
import lombok.NonNull;

/** An action tween that progresses a sequence of other action tweens one after another.
 *
 * @since 0.0.1
 * @author Dragonstb
 */
public final class SequenceTween extends ActionTween{

    private final ActionTween[] tweens;
    private ActionTween current;
    private int idx = 0;

    public SequenceTween(@NonNull List<ActionTween> tweens) {
        this(argToArray(tweens));
    }

    public SequenceTween(@NonNull ActionTween... tweens) {
        super(getTotalLength(tweens));
        this.tweens = tweens;
        idx = 0;
        current = this.tweens[idx];
    }

    @Override
    public void internalAction(float dt) {
        boolean done;
        float step = dt;
        do{
            done = current.progress(step);
            if(done){
                ++idx;
                // check if there is a next one
                if( idx < tweens.length ){
                    // spill over some time, that is how far the next one is progressed in this frame (in the next iteration of the loop)
                    step = current.getTime() - current.getLength();
                    current = tweens[idx];
                }
                else {
                    break;
                }
            }
        } while(done);
    }

    public int getIndex() {
        return idx < tweens.length ? idx : tweens.length - 1;
    }

    public ActionTween getCurrentActionTween() {
        return tweens[getIndex()];
    }

    private static float getTotalLength(ActionTween[] tweens) {
        float length = 0;
        for (ActionTween tween : tweens)
            length += tween.getLength();

        return length;
    }

    private static ActionTween[] argToArray(List<ActionTween> tweens) {
        ActionTween[] arr = new ActionTween[tweens.size()];
        tweens.toArray(arr);
        return arr;
    }
}
