package beaudoin.apps.gui.children.input;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import beaudoin.apps.gui.SpeedTestGui;
import beaudoin.apps.utils.Utils;

public class NumericalInput extends JTextField {

	private static final long serialVersionUID = -2902369580587727916L;

	public static final double MAX_VALUE = 999.999;

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

	public boolean checkDot(char c, boolean allowDot) {
		return (c == '.' && allowDot) && (c == '.' && Utils.getCountOf(getText(), ".") < 1) && (c == '.' && !getText().isEmpty());
	}

}
