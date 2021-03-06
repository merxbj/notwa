/*
 * CommandLine
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

/**
 * Class representing the input provided by the command line arguments. It allows
 * a simplified access to the parameters provided and a encapsulated way how to
 * parse them.
 * 
 * @author Jaroslav Merxbauer
 * @version %I% %G%
 */
public class CommandLine {

    private boolean valid;
    private String configFile;

    private static final String defaultConfigFile = "./user.config";

    /**
     * Let us hide the constructor. The new CommandLine instances are going to be
     * returned by {@link #parse(args)} call.
     */
    private CommandLine() {
        valid = false;
        configFile = defaultConfigFile;
    }

    /**
     * Parses the given array of <code>String</code>s usualy obtained as command
     * line arguments.
     *
     * @param args The arguments.
     * @return The new instance of <code>CommandLine</code> containing all the
     *         valid parsed command line arguments.
     */
    public static CommandLine parse(String[] args) {
        CommandLine cl = new CommandLine();

        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("/config")) {
                cl.configFile = args[i + 1];
            }
        }
        cl.valid = true;

        return cl;
    }

    /**
     * Gets whether the command line arguments were valid.
     *
     * @return  <code>true</code> if the arguments were valid, <code>false</code>
     *          otherwise.
     */
    public boolean isValid() {
        return valid;
    }
    
    /**
     * Gets the config file customized path.
     * 
     * @return  The path to the config file where the user want to store the configuration
     *          information.
     */
    public String getConfigFile() {
        return configFile;
    }
}
