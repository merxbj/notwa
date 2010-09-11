/*
 * MainClass
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

package notwa.application;

import notwa.gui.MainWindow;
import notwa.logger.LoggingFacade;
import notwa.common.Config;

/**
 * The Class representin the entry point to the application. It parses the input
 * command line arguments, set up and run the application as user desired.
 *
 * @author Jaroslav Merxbauer
 * @version %I% %G%
 */
public class MainClass {

    /**
     * The entry point to the application.
     * 
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        CommandLine cl = CommandLine.parse(args);

        if (!cl.isValid()) {
            System.out.println("Invalid arguments provided.");
            return; // TODO: Some kind of help text should be displayed here
        }

        configureConfig(cl);
        configureLogging(cl);

        new MainWindow();
    }

    /**
     * Configures the logging according to the given <code>CommandLine</code>.
     *
     * @param cl    The <code>CommandLine</code> containng the information to 
     *              configure the logging.
     */
    private static void configureLogging(CommandLine cl) {
        LoggingFacade lf = LoggingFacade.getInstance();

        lf.enableFileLogging(cl.getLogFile());
        if (cl.isDebug()) {
            lf.enableWindowLogging();
        }
    }
    
    /**
     * Configures the config file according to the given <code>CommandLine</code>.
     *
     * @param cl    The <code>CommandLine</code> containng the information to 
     *              configure the config file.
     */
    private static void configureConfig(CommandLine cl) {
        Config.setConfigFilePath(cl.getConfigFile());
    }
}
