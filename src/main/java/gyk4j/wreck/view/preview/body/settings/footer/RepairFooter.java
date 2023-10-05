package gyk4j.wreck.view.preview.body.settings.footer;

import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class RepairFooter extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final RepairFooterButtons buttons;

	public RepairFooter() {
		super();
		
		setLayout(new FlowLayout(SwingConstants.RIGHT));
        
        buttons = new RepairFooterButtons();
        add(buttons);
	}

	public RepairFooterButtons getButtons() {
		return buttons;
	}
}
