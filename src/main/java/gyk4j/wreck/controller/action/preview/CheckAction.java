package gyk4j.wreck.controller.action.preview;

import java.awt.event.ActionEvent;

import javax.swing.KeyStroke;

import gyk4j.wreck.controller.PreviewController;
import gyk4j.wreck.controller.action.AbstractCommonAction;

public class CheckAction extends AbstractCommonAction {

	private static final long serialVersionUID = 1L;
	
	private final PreviewController previewController;

	public CheckAction(String text, String desc, Integer mnemonic, KeyStroke accelerator, PreviewController controller) {
		super(text, desc, mnemonic, accelerator);
		previewController = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		previewController.analyze();
	}

}
