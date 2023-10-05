package gyk4j.wreck.view.preview.footer;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import gyk4j.wreck.resources.R;

public class PreviewFooterButtons extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final int GAP = R.integer.BORDER_8;
	
	private final JButton ok;
	private final JButton cancel;
	private final JButton apply;

	public PreviewFooterButtons() {
		super();
		GridLayout gridLayout = new GridLayout(1, 0);
        gridLayout.setHgap(GAP);
        gridLayout.setVgap(GAP);
        setLayout(gridLayout);
        
        ok = new JButton("OK");
        ok.setSelected(true);
        ok.setDefaultCapable(true);
        
        cancel = new JButton("Cancel");
        apply = new JButton("Apply");
        
        add(ok);
        add(cancel);
        add(apply);
	}

	public JButton getOk() {
		return ok;
	}

	public JButton getCancel() {
		return cancel;
	}

	public JButton getApply() {
		return apply;
	}

}
