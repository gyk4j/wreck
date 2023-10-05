package gyk4j.wreck.view.preview.body.statistics;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import gyk4j.wreck.resources.R;

public class PreviewTabStatistics extends JPanel {

	private static final long serialVersionUID = 1L;
	
//	private final PreviewStatisticsText text;
	private final PreviewStatisticsGraph graph;
	
	public PreviewTabStatistics() {
		super();
		
//		this.text = new PreviewStatisticsText();
		this.graph = new PreviewStatisticsGraph();
		
		setBorder(R.style.BORDER_EMPTY_8);
		setLayout(new BorderLayout());
		add(graph, BorderLayout.CENTER);
//		add(text, BorderLayout.LINE_END);
	}

//	public PreviewStatisticsText getText() {
//		return text;
//	}

	public PreviewStatisticsGraph getGraph() {
		return graph;
	}	
}
