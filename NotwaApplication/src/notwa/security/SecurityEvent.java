/*
 * SecurityEvent
 *
 * Copyright (C) 2010  Jaroslav Merxbauer
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package notwa.security;

import notwa.common.Event;

/**
 * Event fired during any security operation informing about the security operation
 * progress or status, such as successful user login.
 *
 * @author Jaroslav Merxbauer
 * @version %I% %G%
 */
public class SecurityEvent extends Event<SecurityEventParams> {

    /**
     * The sole constructor providing the <code>SecurityEventParams</code>.
     * @param params The event parameters.
     */
    public SecurityEvent(SecurityEventParams params) {
        super(params);
    }

}
