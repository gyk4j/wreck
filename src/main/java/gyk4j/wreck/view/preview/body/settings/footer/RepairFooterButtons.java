package gyk4j.wreck.view.preview.body.settings.footer;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class RepairFooterButtons extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final int GAP = 8;
	
	private final JButton ok;
	private final JButton cancel;

	public RepairFooterButtons() {
		super();
		GridLayout gridLayout = new GridLayout(1, 0);
        gridLayout.setHgap(GAP);
        gridLayout.setVgap(GAP);
        setLayout(gridLayout);
        
        ok = new JButton("OK");
        ok.setSelected(true);
        ok.setDefaultCapable(true);
        
        cancel = new JButton("Cancel");
        
        add(ok);
        add(cancel);
	}

	public JButton getOk() {
		return ok;
	}

	public JButton getCancel() {
		return cancel;
	}
}
