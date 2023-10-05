package gyk4j.wreck.controller.action.settings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gyk4j.wreck.resources.SourceEnum;
import gyk4j.wreck.view.preview.body.settings.SettingsSource;

public class SettingsSourceAction implements ActionListener {
	
	private final SettingsSource actionButtons; 
	private final SourceEnum repair;
	
	public SettingsSourceAction(SettingsSource actionButtons, SourceEnum repair) {
		this.actionButtons = actionButtons;
		this.repair = repair;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		actionButtons.getCustomDateTime().getDateTime().setEnabled(SourceEnum.CUSTOM == repair);
	}

}
