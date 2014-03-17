/**
 *  The MIT License (MIT)
 * 	
 *  Copyright (c) 2014 Jonathan Beaudoin
 *	
 *	Permission is hereby granted, free of charge, to any person obtaining a copy
 *	of this software and associated documentation files (the "Software"), to deal
 *	in the Software without restriction, including without limitation the rights
 *	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *	copies of the Software, and to permit persons to whom the Software is
 *	furnished to do so, subject to the following conditions:
 *	
 *	The above copyright notice and this permission notice shall be included in all
 *	copies or substantial portions of the Software.
 *	
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *	SOFTWARE.
 */
package beaudoin.apps.http;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Jonathan
 * @since March 14th, 2014
 */
public class ServerSelection {

	/**
	 * Store the documents for the config settings, and the servers
	 */
	private Document servers, config;

	/**
	 * When the class is initiated, the constructor is called
	 */
	public ServerSelection() {
		init();
	}

	/**
	 * Preload the documents before running any calculations
	 */
	private void init() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(false);
			factory.setValidating(false);
			DocumentBuilder builder = factory.newDocumentBuilder();
			servers = builder.parse(new URL("http://speedtest.net/speedtest-servers.php").openStream());
			config = builder.parse(new URL("http://www.speedtest.net/speedtest-config.php").openStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to go through the list of servers, and calculate the closest
	 * one using longitude and latitude
	 * 
	 * @return the closest server id
	 */
	public int getClosestServer() {
		NodeList nodes = servers.getDocumentElement().getChildNodes().item(1).getChildNodes();

		int serverId = -1;
		double lastDistance = Double.MAX_VALUE;
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);

			if (node instanceof Element) {
				Element child = (Element) node;
				double[] destination = { Double.parseDouble(child.getAttribute("lat")), Double.parseDouble(child.getAttribute("lon")) };
				double distance = getDistance(getCurrentLocation(), destination);

				if (distance < lastDistance) {
					lastDistance = distance;
					serverId = Integer.parseInt(child.getAttribute("id"));
				}
			}
		}
		return serverId;
	}

	/**
	 * Gets the users current location
	 * 
	 * @return longitude and latitude
	 */
	private double[] getCurrentLocation() {
		NodeList nodes = config.getDocumentElement().getChildNodes();
		Element node = (Element) nodes.item(1);
		return new double[] { Double.parseDouble(node.getAttribute("lat")), Double.parseDouble(node.getAttribute("lon")) };
	}

	/**
	 * Calculates distance from the users location, and the servers
	 * 
	 * @param origin
	 * @param destination
	 * @return distance
	 */
	private double getDistance(double[] origin, double[] destination) {
		double latitude = Math.toRadians(destination[0] - origin[0]);
		double longitude = Math.toRadians(destination[1] - origin[1]);
		double a = (Math.sin(latitude / 2) * Math.sin(latitude / 2) + Math.cos(Math.toRadians(origin[0])) * Math.cos(Math.toRadians(destination[0])) * Math.sin(longitude / 2) * Math.sin(longitude / 2));
		double b = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return b * 6371;
	}
}
