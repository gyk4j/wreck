package gyk4j.wreck.view.preview.footer;

import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PreviewFooter extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final PreviewFooterButtons buttons;

	public PreviewFooter() {
		super();
		
		setLayout(new FlowLayout(SwingConstants.RIGHT));
        
        buttons = new PreviewFooterButtons();
        add(buttons);
	}

	public PreviewFooterButtons getButtons() {
		return buttons;
	}
}
