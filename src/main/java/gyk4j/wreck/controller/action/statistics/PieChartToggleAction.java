package gyk4j.wreck.controller.action.statistics;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import gyk4j.wreck.util.ResourceLoader;
import gyk4j.wreck.view.preview.body.statistics.PreviewStatisticsGraphStack;

public class PieChartToggleAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	private final PreviewStatisticsGraphStack stack;

	public PieChartToggleAction(PreviewStatisticsGraphStack stack) {
		super(null, ResourceLoader.getIcon("pie-chart.png"));
		this.putValue(Action.SHORT_DESCRIPTION, "Show pie chart");
		this.putValue(Action.LONG_DESCRIPTION, "Show pie chart");
		this.stack = stack;
	}

	@Override
	public void actionPerformed(ActionEvent e) {		
		CardLayout cl = (CardLayout) stack.getLayout();
		cl.show(stack, PreviewStatisticsGraphStack.CARD_PIE);
	}

}
