/*
 * ServerConnectionInfo
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

package notwa.client.common;

import notwa.common.ConnectionInfo;
import org.w3c.dom.Node;

/**
 *
 * @author eTeR
 * @version %I% %G%
 */
public class ServerConnectionInfo extends ConnectionInfo {

    /**
     * Parses out all connection information from the provided <code>Node</code>
     * utilizing the {@link XPath}.
     *
     * @param rawCon The node containing all the connection information.
     * @return The instance of <code>DatabaseConnectionInfo</code>
     */
    public void parseFromConfig(Node node) {
        Node host = node.getAttributes().getNamedItem("host");
        Node port = node.getAttributes().getNamedItem("port");
        Node label = node.getAttributes().getNamedItem("label");

        super.setHost(host != null ? host.getNodeValue() : "");
        super.setPort(port != null ? port.getNodeValue() : "");
        super.setLabel(label != null ? label.getNodeValue() : "");
    }
}
