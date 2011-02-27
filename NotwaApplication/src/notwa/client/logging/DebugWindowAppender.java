/*
 * DebugWindowAppender
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

package notwa.client.logging;

import notwa.client.gui.DebugWindow;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

/**
 *
 * @author eTeR
 * @version %I% %G%
 */
public class DebugWindowAppender extends AppenderSkeleton {

    private DebugWindow window;

    public DebugWindowAppender() {
        this.window = new DebugWindow();
        this.window.setVisible(true);
    }

    @Override
    protected void append(LoggingEvent event) {
        StringBuilder message = new StringBuilder(this.getLayout().format(event));
        if (this.layout.ignoresThrowable()) {
            String[] humanReadableThrowable = event.getThrowableStrRep();
            if (humanReadableThrowable != null) {
                for (String line : humanReadableThrowable) {
                    message.append(line);
                    message.append(Layout.LINE_SEP);
                }
            }
        }
        window.append(message.toString());
    }

    @Override
    public void close() {
    }

    @Override
    public boolean requiresLayout() {
        return true;
    }

}
