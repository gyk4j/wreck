package gyk4j.wreck.controller.action.statistics;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import gyk4j.wreck.util.ResourceLoader;
import gyk4j.wreck.view.preview.body.statistics.PreviewStatisticsGraphStack;

public class BarChartToggleAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	private final PreviewStatisticsGraphStack stack;
	
	public BarChartToggleAction(PreviewStatisticsGraphStack stack) {
		super(null, ResourceLoader.getIcon("bar-graph.png"));
		this.putValue(Action.SHORT_DESCRIPTION, "Show bar chart");
		this.putValue(Action.LONG_DESCRIPTION, "Show bar chart");
		this.stack = stack;
	}

	@Override
	public void actionPerformed(ActionEvent e) {		
		CardLayout cl = (CardLayout) stack.getLayout();
		cl.show(stack, PreviewStatisticsGraphStack.CARD_BAR);
	}

}
