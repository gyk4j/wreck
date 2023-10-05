package gyk4j.wreck.view.preview.body;

import javax.swing.JTabbedPane;

import gyk4j.wreck.resources.R;
import gyk4j.wreck.view.preview.body.about.TabAbout;
import gyk4j.wreck.view.preview.body.files.TabFiles;
import gyk4j.wreck.view.preview.body.settings.TabSettings;
import gyk4j.wreck.view.preview.body.statistics.PreviewTabStatistics;

public class PreviewBody extends JTabbedPane {

	private static final long serialVersionUID = 1L;

	private final TabSettings settings;
	private final TabFiles files;
	private final PreviewTabStatistics statistics;
	private final TabAbout about;
	
	public PreviewBody() {
		super();
		
		setBorder(R.style.BORDER_EMPTY_4);

		settings = new TabSettings();
		files = new TabFiles();
		statistics = new PreviewTabStatistics();
		about = new TabAbout();
		
		this.addTab("Settings", settings);
		this.addTab("Files", files);
		this.addTab("Statistics", statistics);
		this.addTab("About", about);
	}
	
	public TabSettings getSettings() {
		return settings;
	}

	public TabFiles getFiles() {
		return files;
	}

	public PreviewTabStatistics getStatistics() {
		return statistics;
	}

	public TabAbout getAbout() {
		return about;
	}
}
