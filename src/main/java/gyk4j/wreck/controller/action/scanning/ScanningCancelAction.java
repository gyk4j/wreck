package gyk4j.wreck.controller.action.scanning;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.KeyStroke;

import gyk4j.wreck.controller.PreviewController;
import gyk4j.wreck.controller.action.AbstractCommonAction;
import gyk4j.wreck.view.scanning.ScanningDialog;

public class ScanningCancelAction extends AbstractCommonAction {

	private static final long serialVersionUID = 1L;
	
	private final ScanningDialog scanningDialog;
	private final PreviewController previewController;
	

	public ScanningCancelAction(String text, String desc, Integer mnemonic, KeyStroke accelerator, ScanningDialog scanning, PreviewController controller) {
		super(text, desc, mnemonic, accelerator);
		scanningDialog = scanning;
		previewController = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton cancel = (JButton) e.getSource();
		cancel.setEnabled(false);
		
		if(previewController.cancelScanning()) {
			setEnabled(false);
			scanningDialog.dispatchEvent(new WindowEvent(scanningDialog, WindowEvent.WINDOW_CLOSING));
		}
	}

}
