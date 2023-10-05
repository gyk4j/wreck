package gyk4j.wreck.view.preview.body.settings;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;

import gyk4j.wreck.resources.R;
import gyk4j.wreck.util.ResourceLoader;
import gyk4j.wreck.view.preview.body.settings.footer.RepairFooter;

public class SettingsDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private final TabSettings actions;
	private final RepairFooter footer;

	public SettingsDialog(JFrame parent) {
		super(parent, ModalityType.APPLICATION_MODAL);
		
		//Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        ImageIcon icon = (ImageIcon) ResourceLoader.getIcon(R.icon.APP);
        this.setIconImage(icon.getImage());
        setPreferredSize(new Dimension(R.dimen.DIALOG_WIDTH, R.dimen.DIALOG_HEIGHT));
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        actions = new TabSettings();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(actions, BorderLayout.CENTER);
        
        footer = new RepairFooter();
        getContentPane().add(footer, BorderLayout.PAGE_END);
	}
	
	public void open() {
		//Display the window.
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
	}
	
	public TabSettings getActions() {
		return actions;
	}
	
	public RepairFooter getFooter() {
		return footer;
	}
}
