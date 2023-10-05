package gyk4j.wreck.controller.action.settings;

import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SettingsSourceChangeListener implements ChangeListener {
	
	private static final Logger LOG = LoggerFactory.getLogger(SettingsSourceChangeListener.class);

	@Override
	public void stateChanged(ChangeEvent e) {
		
		JToggleButton.ToggleButtonModel m = (JToggleButton.ToggleButtonModel) e.getSource();
		LOG.trace("{} = {}", m.getActionCommand(), m.isSelected());
	}

}
