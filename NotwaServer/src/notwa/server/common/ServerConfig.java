/*
 * ServerConfig
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

package notwa.server.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author eTeR
 * @version %I% %G%
 */
public class ServerConfig {
    private Set<NotwaDatabaseConnectionInfo> connections = new TreeSet<NotwaDatabaseConnectionInfo>();
    private final XPath xpath = XPathFactory.newInstance().newXPath();
    
    /**
     * Gets all connection information parsed from the configuration file.
     * Every connection information is wrapped into the <code>ConnectionInfo</code>
     * instance and all these instances are kept inside single <code>Collection</code>.
     *
     * @return <code>Collection</code> of all <code>ConnectionInfo</code>.
     */
    public Collection<NotwaDatabaseConnectionInfo> getConnecionStrings() {
        return connections;
    }
    
    public void setConnectionInfo(NotwaDatabaseConnectionInfo nci) {
        connections.add(nci);
    }
    
    /**
     * Parse the XML configuration document utilizing the DOM {@link Document}.
     *
     * @param configFile The file claimed to be the configuration XML file.
     * @throws Exception If the config file does not exist.
     */
    private void parse(File configFile) throws Exception {
        if (configFile.exists()) {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document dom = db.parse(configFile);
            
            for (Node node : getChildNodesByPath(dom.getDocumentElement(), "./*")) {
                if (node.getNodeName().equals("AvailableDatabases")) {
                    for (int i=0; i<node.getChildNodes().getLength(); i++) {
                        Node subNode = node.getChildNodes().item(i);
                        if (subNode.getNodeName().equals("Database")) {
                            NotwaDatabaseConnectionInfo nci = new NotwaDatabaseConnectionInfo();
                            nci.parseFromConfig(subNode);
                            connections.add(nci);
                        }
                    }
                }
            }
        } else {
            throw new Exception("Config file does not exists!");
        }
    }
    
    /**
     * TODO: <MERXBJ> Copy & Pasted from notwa.client.common.Config.java
     * Finds all the child elements of given parent matching the xpath provided.
     *
     * @param parent The Node where to start.
     * @param path The path to be evaluated.
     * @return The <code>List</code> of all the child nodes matching the given xpath.
     */
    private List<Node> getChildNodesByPath(Node parent, String path) {
        List<Node> childs = new ArrayList<Node>();
        try {
            NodeList rawChilds = (NodeList) xpath.evaluate(path, parent, XPathConstants.NODESET);
            for (int i = 0; i < rawChilds.getLength(); i++) {
                childs.add(rawChilds.item(i));
            }
        } catch (XPathExpressionException xpeex) {
            Logger.getLogger(this.getClass()).error("Error occured while executing an XPath expression!", xpeex);
        }

        return childs;
    }
}
