package fr.sparna.tours.gephi.operation;

import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.layout.plugin.force.yifanHu.YifanHu;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;

public class LayoutWithYifanHu implements GraphOperationIfc {

	@Override
	public String getName() {
		return LayoutWithYifanHu.class.getSimpleName();
	}

	@Override
	public void apply(Graph graph, GraphModel graphModel) {
		// run layout for 100 passes
		YifanHu builder = new YifanHu();
		YifanHuLayout yifanHu = builder.buildLayout();
		yifanHu.setGraphModel(graphModel);		
		yifanHu.setBarnesHutTheta(1.2f);
		yifanHu.setQuadTreeMaxLevel(10);
		yifanHu.setOptimalDistance(100f);
		yifanHu.setRelativeStrength(0.2f);
		yifanHu.setInitialStep(20f);
		yifanHu.setStepRatio(0.95f);
		yifanHu.setAdaptiveCooling(true);
		yifanHu.setConvergenceThreshold(0.0001f);
		
		yifanHu.initAlgo();
		
		
		for (int i = 0; i < 100 && yifanHu.canAlgo(); i++) {
			yifanHu.goAlgo();
		}
		yifanHu.endAlgo();
		
		System.out.println("End yifanHu layout");
	}

}
