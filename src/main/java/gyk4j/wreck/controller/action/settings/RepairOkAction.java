package gyk4j.wreck.controller.action.settings;

import java.awt.event.ActionEvent;

import javax.swing.KeyStroke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gyk4j.wreck.controller.action.AbstractCommonAction;
import gyk4j.wreck.view.preview.body.settings.SettingsDialog;

public class RepairOkAction extends AbstractCommonAction {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = LoggerFactory.getLogger(RepairOkAction.class);
	
	private final SettingsDialog repairActionDialog;
	
	public RepairOkAction(String text, String desc, Integer mnemonic, KeyStroke accelerator, SettingsDialog repairActionDialog) {
		super(text, desc, mnemonic, accelerator);
		this.repairActionDialog = repairActionDialog;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		LOG.trace("Pretending to do something");
		repairActionDialog.setVisible(false);
	}
}
