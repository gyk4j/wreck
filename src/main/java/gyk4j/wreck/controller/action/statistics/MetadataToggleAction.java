package gyk4j.wreck.controller.action.statistics;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import gyk4j.wreck.model.SampleTableModel;
import gyk4j.wreck.util.ResourceLoader;
import gyk4j.wreck.view.preview.body.statistics.PreviewStatisticsGraphStack;

public class MetadataToggleAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	private final PreviewStatisticsGraphStack stack;
	private final SampleTableModel<?> model;

	public MetadataToggleAction(PreviewStatisticsGraphStack stack, SampleTableModel<?> model) {
		super(null, ResourceLoader.getIcon("metadata.png"));
		this.putValue(Action.SHORT_DESCRIPTION, "Show metadata data");
		this.putValue(Action.LONG_DESCRIPTION, "Show metadata data");
		this.stack = stack;
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		stack.getRawTable().getScrollTable().getTable().setModel(model);
		ChartActions.updateChartsMetadata(stack.getBarChart(), stack.getPieChart());
	}

}
