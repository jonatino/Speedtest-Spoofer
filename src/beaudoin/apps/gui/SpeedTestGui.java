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
package beaudoin.apps.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import beaudoin.apps.gui.children.input.NumericalInput;
import beaudoin.apps.http.SpeedTestRequest;

/**
 * 
 * @author Jonathan
 * @since March 14th, 2014
 */
public class SpeedTestGui extends JFrame {

	private static final long serialVersionUID = -5007568166176969267L;

	/**
	 * Variables used throughout the class
	 */
	private JButton requestButton;
	private NumericalInput download, upload, ping;

	/**
	 * Main construction of the GUI
	 */
	public SpeedTestGui() {
		setSize(282, 218);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		JLabel lblDownloadSpeed = new JLabel("Download Speed:");
		lblDownloadSpeed.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblDownloadSpeed.setBounds(10, 11, 129, 27);
		getContentPane().add(lblDownloadSpeed);

		JLabel lblUploadSpeed = new JLabel("Upload Speed:");
		lblUploadSpeed.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblUploadSpeed.setBounds(10, 50, 129, 27);
		getContentPane().add(lblUploadSpeed);

		JLabel lblPing = new JLabel("Ping (ms):");
		lblPing.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblPing.setBounds(10, 88, 129, 27);
		getContentPane().add(lblPing);

		download = new NumericalInput(this, true);
		download.setBounds(137, 16, 129, 20);
		getContentPane().add(download);
		download.setColumns(10);

		upload = new NumericalInput(this, true);
		upload.setColumns(10);
		upload.setBounds(137, 55, 129, 20);
		getContentPane().add(upload);

		ping = new NumericalInput(this, false);
		ping.setColumns(10);
		ping.setBounds(137, 93, 129, 20);
		getContentPane().add(ping);

		requestButton = new JButton("Request Fake Speedtest");
		requestButton.setBounds(10, 140, 256, 27);
		requestButton.setEnabled(false);
		getContentPane().add(requestButton);

		requestButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String dText = download.getText();
				String uText = upload.getText();
				String pingText = ping.getText();
				if (Double.valueOf(dText) > NumericalInput.MAX_VALUE || Double.valueOf(uText) > NumericalInput.MAX_VALUE) {
					JOptionPane.showConfirmDialog(SpeedTestGui.this, "Due to limitations of www.speedtest.net, the max value for download/upload can not exceed 999.999.", "Max value reached!", JOptionPane.PLAIN_MESSAGE, JOptionPane.ERROR_MESSAGE);
					return;
				}
				SpeedTestRequest request = new SpeedTestRequest(dText, uText, pingText);
				request.displayResult();
			}
		});
	}

	/**
	 * Checks to see if the formats are proper, and enables the 
	 * request button if it passes the test(s)
	 */
	public void checkForActivation() {
		NumericalInput[] numericalInputs = { download, upload, ping };
		boolean b = true;
		for (NumericalInput input : numericalInputs) {
			if (input.getText().isEmpty() || input.getText().endsWith(".")) {
				b = false;
				break;
			}
		}
		requestButton.setEnabled(b);
	}

}