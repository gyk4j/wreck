package gyk4j.wreck.controller.action.preview;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import gyk4j.wreck.controller.PreviewController;
import gyk4j.wreck.controller.action.AbstractCommonAction;

public class RestoreAction extends AbstractCommonAction {

	private static final long serialVersionUID = 1L;
	
	private final PreviewController controller;

	public RestoreAction(String text, String desc, Integer mnemonic, KeyStroke accelerator, PreviewController controller) {
		super(text, desc, mnemonic, accelerator);
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int option = JOptionPane.showConfirmDialog(
				controller.getView().getPreviewDialog(), 
				"Restore the file attribute timestamps to the original recorded timestamps?",
				"Warning: Restore file timestamps?",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE);
		
		if(option == JOptionPane.OK_OPTION) {
			controller.restore();
		}
		
	}

}
