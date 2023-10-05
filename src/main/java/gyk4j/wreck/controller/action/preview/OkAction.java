package gyk4j.wreck.controller.action.preview;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import gyk4j.wreck.controller.PreviewController;
import gyk4j.wreck.controller.action.AbstractCommonAction;
import gyk4j.wreck.view.preview.PreviewDialog;

public class OkAction extends AbstractCommonAction {
	
	private static final long serialVersionUID = 1L;
	
	private final PreviewController controller;
	
	public OkAction(String text, String desc, Integer mnemonic, KeyStroke accelerator, PreviewController controller) {
		super(text, desc, mnemonic, accelerator);
		this.controller = controller;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int option = JOptionPane.showConfirmDialog(
				controller.getView().getPreviewDialog(), 
				"Reset the file attribute timestamps?",
				"Warning: Change file timestamps?",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE);
		
		if(option == JOptionPane.OK_OPTION) {
			controller.repair();
			
			PreviewDialog pd = controller.getView().getPreviewDialog();
			WindowEvent windowClosing = new WindowEvent(pd, WindowEvent.WINDOW_CLOSING);
			pd.dispatchEvent(windowClosing);
		}
	}
}
