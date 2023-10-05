package gyk4j.wreck.view.scanning;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import gyk4j.wreck.resources.R;
import gyk4j.wreck.util.ResourceLoader;

public class ScanningDialog extends JDialog {

	private static final String SCANNING = "Scanning";

	private static final long serialVersionUID = 1L;
	
	private final JLabel icon;
	private final JLabel action;
	private final JProgressBar progress;
	private final JButton cancel;

	public ScanningDialog(Frame owner) {
		super(owner);
		this.setAlwaysOnTop(false);
		this.setAutoRequestFocus(true);
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.setPreferredSize(new Dimension(400, 160));
//		this.setModalityType(ModalityType.APPLICATION_MODAL);
		
		getContentPane().setLayout(new BorderLayout());
		
		Icon iconScan = ResourceLoader.getIcon("scan.png");
		icon = new JLabel(iconScan, SwingConstants.LEFT);
		icon.setBorder(R.style.BORDER_EMPTY_8);
//		icon.setVerticalAlignment(SwingConstants.CENTER);
//		icon.setVerticalTextPosition(SwingConstants.CENTER);
		action = new JLabel();
		cancel = new JButton("Cancel");
		
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		p.setBorder(R.style.BORDER_EMPTY_8);
		progress = new JProgressBar();
		progress.setIndeterminate(false);
//		progress.setStringPainted(true);
		
		p.add(Box.createVerticalStrut(R.integer.BORDER_16));
		p.add(progress);
		p.add(Box.createVerticalStrut(R.integer.BORDER_8));
		p.add(action);
		
		JPanel footer = new JPanel();
		footer.setLayout(new FlowLayout(FlowLayout.TRAILING));
//		footer.setBorder(R.style.BORDER_EMPTY_8);
		footer.add(cancel);
		
		setTitle(SCANNING);
		getContentPane().add(icon, BorderLayout.LINE_START);
//		getContentPane().add(action, BorderLayout.CENTER);
		getContentPane().add(p, BorderLayout.CENTER);
		getContentPane().add(footer, BorderLayout.PAGE_END);
		
		pack();
        setLocationRelativeTo(null);
	}

	public void open() {
		//Display the window.
		setTitle(R.string.STARTING);
		getAction().setText(R.string.STARTING);
        setVisible(true);
        getCancel().setEnabled(true);
	}
	
	public void close() {
		getAction().setText(R.string.EMPTY);
		setVisible(false);
		getCancel().setEnabled(false);
	}
	
	public JProgressBar getProgress() {
		return progress;
	}

	public JLabel getAction() {
		return action;
	}
	
	public JButton getCancel() {
		return cancel;
	}
	
	public void setProgress(int progress) {
		StringBuilder sb = new StringBuilder();
		sb.append(SCANNING);
		sb.append(" - ");
		sb.append(progress);
		sb.append("%...");
		setTitle(sb.toString());
	}
}
