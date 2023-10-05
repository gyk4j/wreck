package gyk4j.wreck.view.preview.body.statistics;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class PreviewStatisticsTextType extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final ButtonGroup buttonGroup;
	private final JToggleButton buttonFile;
	private final JToggleButton buttonMetadata;
	private final JToggleButton buttonExtension;
	
	public PreviewStatisticsTextType() {
		super();
		
		setLayout(new FlowLayout(FlowLayout.RIGHT));
		setBorder(BorderFactory.createLineBorder(Color.BLUE));
		
		buttonGroup = new ButtonGroup();
		
		buttonFile = new JToggleButton("File");
		buttonFile.setToolTipText("File");
		
		buttonMetadata = new JToggleButton("Metadata");
		buttonMetadata.setToolTipText("Metadata");
		
		buttonExtension = new JToggleButton("Extension");
		buttonExtension.setToolTipText("Extension");
		
		buttonGroup.add(buttonFile);
		buttonGroup.add(buttonMetadata);
		buttonGroup.add(buttonExtension);
		
		add(buttonFile);
		add(buttonMetadata);
		add(buttonExtension);
	}

	public JToggleButton getButtonFile() {
		return buttonFile;
	}

	public JToggleButton getButtonMetadata() {
		return buttonMetadata;
	}

	public JToggleButton getButtonExtension() {
		return buttonExtension;
	}
	
}
