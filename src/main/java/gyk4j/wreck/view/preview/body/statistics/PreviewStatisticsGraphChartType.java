package gyk4j.wreck.view.preview.body.statistics;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class PreviewStatisticsGraphChartType extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final ButtonGroup dataGroup;
	private final JToggleButton dataFile;
	private final JToggleButton dataMetadata;
	private final JToggleButton dataExtension;
	
	private final ButtonGroup buttonGroup;
	private final JToggleButton buttonTable;
	private final JToggleButton buttonBar;
	private final JToggleButton buttonPie;
	
	public PreviewStatisticsGraphChartType() {
		super();
		dataGroup = new ButtonGroup();
		
		dataFile = new JToggleButton("File");
		dataFile.setToolTipText("File");
		dataGroup.add(dataFile);
		
		dataMetadata = new JToggleButton("Metadata");
		dataMetadata.setToolTipText("Metadata");
		dataGroup.add(dataMetadata);
		
		dataExtension = new JToggleButton("Extension");
		dataExtension.setToolTipText("Extension");
		dataGroup.add(dataExtension);
		
		buttonGroup = new ButtonGroup();
		
		buttonTable = new JToggleButton();
		buttonTable.setToolTipText("Table");
		
		buttonBar = new JToggleButton();
		buttonBar.setToolTipText("Bar");
		
		buttonPie = new JToggleButton();
		buttonPie.setToolTipText("Pie");
		
		buttonGroup.add(buttonTable);
		buttonGroup.add(buttonBar);
		buttonGroup.add(buttonPie);
		
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		add(dataFile);
		add(dataMetadata);
		add(dataExtension);
		add(Box.createHorizontalGlue());
		add(buttonTable);
		add(buttonBar);
		add(buttonPie);		
	}

	public JToggleButton getDataFile() {
		return dataFile;
	}

	public JToggleButton getDataMetadata() {
		return dataMetadata;
	}

	public JToggleButton getDataExtension() {
		return dataExtension;
	}
	
	public JToggleButton getButtonTable() {
		return buttonTable;
	}

	public JToggleButton getButtonBar() {
		return buttonBar;
	}

	public JToggleButton getButtonPie() {
		return buttonPie;
	}
}
