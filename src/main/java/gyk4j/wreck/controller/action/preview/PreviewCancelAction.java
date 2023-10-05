package gyk4j.wreck.controller.action.preview;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.KeyStroke;

import gyk4j.wreck.controller.PreviewController;
import gyk4j.wreck.controller.action.AbstractCommonAction;
import gyk4j.wreck.view.preview.PreviewDialog;

public class PreviewCancelAction extends AbstractCommonAction {

	private static final long serialVersionUID = 1L;
	
	private final PreviewController controller;
	
	public PreviewCancelAction(String text, String desc, Integer mnemonic, KeyStroke accelerator, PreviewController controller) {
		super(text, desc, mnemonic, accelerator);
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {		
		PreviewDialog pd = controller.getView().getPreviewDialog();
		WindowEvent windowClosing = new WindowEvent(pd, WindowEvent.WINDOW_CLOSING);
		pd.dispatchEvent(windowClosing);
	}
}
