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

import java.util.Optional;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import lombok.Setter;

/** Configuration for the virtual host.
 *
 * @author Dragonstb
 * @since 0.0.2
 */
@Setter
public final class Configuration {

    private Clock clock;

    private ScheduledThreadPoolExecutor executor;

    /** Gets the clock.
     *
     * @since 0.0.2
     * @author Dragonstb
     * @return Optional with the clock if present.
     */
    public Optional<Clock> getClock() {
        return Optional.ofNullable(clock);
    }

    /** Gets the executor.
     *
     * @since 0.0.2
     * @author Dragonstb
     * @return The executor if present.
     */
    public Optional<ScheduledThreadPoolExecutor> getExecutor() {
        return Optional.ofNullable(executor);
    }

}
