package gyk4j.wreck.controller.action.settings;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JSpinner;
import javax.swing.JToggleButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gyk4j.wreck.resources.SourceEnum;

public class SettingsSourceCustomItemListener implements ItemListener {
	
	private static final Logger LOG = LoggerFactory.getLogger(SettingsSourceCustomItemListener.class);
	
	private final JSpinner customDateTime;

	public SettingsSourceCustomItemListener(JSpinner customDateTime) {
		this.customDateTime = customDateTime;
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		JToggleButton.ToggleButtonModel m = (JToggleButton.ToggleButtonModel) e.getItem();
//		LOG.trace("{} = {}", m.getActionCommand(), (e.getStateChange()==ItemEvent.SELECTED)? "SELECTED": "DESELECTED");
		if(SourceEnum.CUSTOM == SourceEnum.valueOf(m.getActionCommand())) {
//			LOG.trace("Is CUSTOM");
			if(e.getStateChange()==ItemEvent.SELECTED) {
				LOG.trace("CUSTOM = SELECTED");
				customDateTime.setEnabled(true);
				customDateTime.getEditor().setEnabled(true);
			}
			else if(e.getStateChange()==ItemEvent.DESELECTED) {
				LOG.trace("CUSTOM = DESELECTED");
				customDateTime.setEnabled(false);
				customDateTime.getEditor().setEnabled(false);
			}
		}
		else {
			LOG.trace("Not CUSTOM");
		}
	}

}
