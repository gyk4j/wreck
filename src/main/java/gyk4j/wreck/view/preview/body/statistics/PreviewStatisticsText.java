package gyk4j.wreck.view.preview.body.statistics;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import gyk4j.wreck.view.preview.ScrollTable;

public class PreviewStatisticsText extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final int BORDER = 8;
	
	private final JTabbedPane statistics = new JTabbedPane();
	private final ScrollTable files;
	private final ScrollTable metadata;
	private final ScrollTable extension;
	
	public PreviewStatisticsText() {
		super();
		
		files = new ScrollTable(BORDER);
		metadata = new ScrollTable(BORDER);
		extension = new ScrollTable(BORDER);
		
		statistics.addTab("File", files);
		statistics.addTab("Metadata", metadata);
		statistics.addTab("Extension", extension);
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(statistics);
	}

	public ScrollTable getExtension() {
		return extension;
	}

	public ScrollTable getFiles() {
		return files;
	}

	public ScrollTable getMetadata() {
		return metadata;
	}

	public JTabbedPane getStatistics() {
		return statistics;
	}
}
