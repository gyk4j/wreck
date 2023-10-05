package gyk4j.wreck.view.preview.body.statistics;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class PreviewStatisticsGraph extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private static final int STRUTS = 8;
	
	private final PreviewStatisticsGraphChartType chartType;
	private final PreviewStatisticsGraphStack graphStack;
	
	public PreviewStatisticsGraph() {
		super();
		
		chartType = new PreviewStatisticsGraphChartType();
		graphStack = new PreviewStatisticsGraphStack();
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(chartType);
		add(Box.createVerticalStrut(STRUTS));
		add(graphStack);
	}
	
	public PreviewStatisticsGraphChartType getChartType() {
		return chartType;
	}

	public PreviewStatisticsGraphStack getGraphStack() {
		return graphStack;
	}
}
