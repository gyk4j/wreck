package gyk4j.wreck.view.preview.body.statistics;

import java.awt.Component;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gyk4j.wreck.controller.action.statistics.ChartActions;

public class StatisticsTabChangeAction implements ChangeListener {
	private static final Logger LOG = LoggerFactory.getLogger(StatisticsTabChangeAction.class);
	
	private final PreviewStatisticsGraphStack graphStack;
	private final PreviewStatisticsText text;
	
	public StatisticsTabChangeAction(PreviewStatisticsGraphStack graphStack, PreviewStatisticsText text) {
		super();
		this.graphStack = graphStack;
		this.text = text;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JTabbedPane statistics = (JTabbedPane) e.getSource();
//		LOG.trace("Tab: ({}) {}", statistics.getSelectedIndex(), statistics.getTitleAt(statistics.getSelectedIndex()));
		Component c = statistics.getSelectedComponent();
		
		if(c == null) {
			LOG.error("Tab component is null");
		}
		else if(text.getFiles().equals(c)) {
			ChartActions.updateChartsFiles(graphStack.getBarChart(), graphStack.getPieChart());
		}
		else if(text.getMetadata().equals(c)) {
			ChartActions.updateChartsMetadata(graphStack.getBarChart(), graphStack.getPieChart());
		}
		else if(text.getExtension().equals(c)) {
			ChartActions.updateChartsExtension(graphStack.getBarChart(), graphStack.getPieChart());
		}
		else {
			LOG.error("Unknown tab: ", c.toString());
		}

	}

}
