package gyk4j.wreck.view.preview.body.settings;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SettingsActionDescription extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final int BORDER = 8;
	
	private final JLabel description;
	
	public SettingsActionDescription() {
		super();
		
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Description"), 
				BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER, BORDER)));
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		description = new JLabel();
		add(description);
		add(Box.createGlue());
	}

	public JLabel getDescription() {
		return description;
	}
}
