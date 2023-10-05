package gyk4j.wreck.controller.action;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public abstract class AbstractCommonAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public AbstractCommonAction(String text, String desc, Integer mnemonic, KeyStroke accelerator) {
		
		super(text);
		putValue(SHORT_DESCRIPTION, desc);
		putValue(LONG_DESCRIPTION, desc);
		putValue(MNEMONIC_KEY, mnemonic);
		putValue(ACCELERATOR_KEY, accelerator);
	}
}
