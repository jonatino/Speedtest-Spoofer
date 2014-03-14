package beaudoin.apps.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import beaudoin.apps.utils.Utils;

import javax.swing.JLabel;

import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.JButton;

public class SpeedTestRequest {

	private String download;
	private String upload;
	private String ping;
	private int serverId;

	public SpeedTestRequest(String download, String upload, String ping) {
		this.download = download.replace(".", "");
		this.upload = upload.replace(".", "");
		this.ping = ping;

		ServerSelection ss = new ServerSelection();
		this.serverId = ss.getClosestServer();
	}

	public void displayResult() {
		JFrame frame = new JFrame("Your Speedtest result");
		frame.setSize(325, 235);
		frame.getContentPane().setLayout(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);

		final String url = getResultURL();
		final ImageIcon imageIcon = getResultImage();

		JLabel image = new JLabel(imageIcon);
		image.setBounds(10, 10, 300, 135);
		frame.getContentPane().add(image);

		JTextField urlField = new JTextField(url);
		urlField.setEditable(false);
		urlField.setBounds(10, 158, 201, 20);
		frame.getContentPane().add(urlField);
		urlField.setColumns(10);

		JButton copy = new JButton("Copy");
		copy.setBounds(221, 157, 89, 23);
		frame.getContentPane().add(copy);

		copy.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(url), null);
			}
		});

		frame.setVisible(true);
	}

	private ImageIcon getResultImage() {
		try {
			return new ImageIcon(new URL(getResultURL()));
		} catch (MalformedURLException e) {
			return null;
		}
	}

	private String getResultURL() {
		try {
			URL url = new URL("http://www.speedtest.net/api/api.php");
			Random r = new Random();

			String accuracy = String.valueOf(7 + r.nextInt(20));
			String hash = Utils.convertToMd5(ping + "-" + upload + "-" + download + "-297aae72");
			String data = String.format("startmode=recommendedselect&promo=&upload=%s&accuracy=%s&recommendedserverid=%s&serverid=%s&ping=%s&hash=%s&download=%s", upload, accuracy, serverId, serverId, ping, hash, download);

			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.addRequestProperty("Host", "www.speedtest.net");
			urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11");
			urlConnection.addRequestProperty("Referer", "http://c.speedtest.net/flash/speedtest.swf");
			urlConnection.addRequestProperty("Origin", "http://c.speedtest.net");
			urlConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			urlConnection.addRequestProperty("Content-Length", String.valueOf(data.length()));
			urlConnection.addRequestProperty("Connection", "Close");

			OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
			wr.write(data);
			wr.close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String result = reader.readLine();
			result = result.substring(result.indexOf("resultid") + 9, result.indexOf("&"));
			reader.close();
			return String.format("http://www.speedtest.net/result/%s.png", result);
		} catch (Exception e) {
			return null;
		}
	}

}
