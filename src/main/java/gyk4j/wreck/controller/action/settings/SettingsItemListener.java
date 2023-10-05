package gyk4j.wreck.controller.action.settings;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SettingsItemListener implements ChangeListener {
	private final JCheckBox[] sources;
	private final JCheckBox[] actions;
	private final JButton btnOK;
	private final JButton btnApply;
	
	public SettingsItemListener(JCheckBox[] sources, JCheckBox[] actions, JButton ok, JButton apply) {
		this.sources = sources;
		this.actions = actions;
		this.btnOK = ok;
		this.btnApply = apply;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		boolean enabled = false;
		for(JCheckBox cb : sources) {
			if(cb.getModel().isSelected()) {
				enabled = true;
				break;
			}
		}
		
		if(enabled) {
			enabled = false;
			for(JCheckBox cb : actions) {
				if(cb.getModel().isSelected()) {
					enabled = true;
					break;
				}
			}
		}
		
		btnOK.setEnabled(enabled);
		btnApply.setEnabled(enabled);
	}

}
