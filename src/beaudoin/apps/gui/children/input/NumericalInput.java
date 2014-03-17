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
package beaudoin.apps.gui.children.input;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import beaudoin.apps.gui.SpeedTestGui;
import beaudoin.apps.utils.Utils;

/**
 * 
 * @author Jonathan
 * @since March 14th, 2014
 */
public class NumericalInput extends JTextField {

	private static final long serialVersionUID = -2902369580587727916L;

	/**
	 * Used to specify the maximum value for the input
	 */
	public static final double MAX_VALUE = 999.999;

	/**
	 * Initiates a new NumericalInput instance, and adds the appropriate filters
	 * 
	 * @param parent
	 * @param allowDot
	 */
	public NumericalInput(final SpeedTestGui parent, final boolean allowDot) {
		((AbstractDocument) getDocument()).setDocumentFilter(new DocumentFilter() {

			@Override
			public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
				super.remove(fb, offset, length);
				parent.checkForActivation();
			}

			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
				for (char c : text.toCharArray()) {
					if (!checkDot(c, allowDot) && !Character.isDigit(c)) {
						return;
					}
				}
				super.replace(fb, offset, length, text, attrs);
				parent.checkForActivation();
			}

		});

	}

	/**
	 * Used to check to see if a decimal is in proper format in the textbox
	 * 
	 * @param c
	 * @param allowDot
	 * @return if valid, or not
	 */
	public boolean checkDot(char c, boolean allowDot) {
		return (c == '.' && allowDot) && (c == '.' && Utils.getCountOf(getText(), ".") < 1) && (c == '.' && !getText().isEmpty());
	}

}
