package gyk4j.wreck.controller.action.settings;

import java.awt.event.ActionEvent;

import javax.swing.KeyStroke;

import gyk4j.wreck.controller.action.AbstractCommonAction;
import gyk4j.wreck.view.preview.body.settings.SettingsDialog;

public class RepairCancelAction extends AbstractCommonAction {

	private static final long serialVersionUID = 1L;
	
	private final SettingsDialog repairActionDialog;

	public RepairCancelAction(String text, String desc, Integer mnemonic, KeyStroke accelerator, SettingsDialog repairActionDialog) {
		super(text, desc, mnemonic, accelerator);
		this.repairActionDialog = repairActionDialog;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repairActionDialog.setVisible(false);
	}
}
