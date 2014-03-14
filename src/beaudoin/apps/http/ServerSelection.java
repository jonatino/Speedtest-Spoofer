package beaudoin.apps.http;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ServerSelection {

	private Document servers, config;

	public ServerSelection() {
		init();
	}

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

	private double[] getCurrentLocation() {
		NodeList nodes = config.getDocumentElement().getChildNodes();
		Element node = (Element) nodes.item(1);
		return new double[] { Double.parseDouble(node.getAttribute("lat")), Double.parseDouble(node.getAttribute("lon")) };
	}

	private double getDistance(double[] origin, double[] destination) {
		double latitude = Math.toRadians(destination[0] - origin[0]);
		double longitude = Math.toRadians(destination[1] - origin[1]);
		double a = (Math.sin(latitude / 2) * Math.sin(latitude / 2) + Math.cos(Math.toRadians(origin[0])) * Math.cos(Math.toRadians(destination[0])) * Math.sin(longitude / 2) * Math.sin(longitude / 2));
		double b = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return b * 6371;
	}
}
