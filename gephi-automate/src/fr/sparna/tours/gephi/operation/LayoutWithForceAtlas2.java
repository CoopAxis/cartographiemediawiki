package fr.sparna.tours.gephi.operation;

import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.layout.plugin.forceAtlas2.ForceAtlas2;
import org.gephi.layout.plugin.forceAtlas2.ForceAtlas2Builder;

public class LayoutWithForceAtlas2 implements GraphOperationIfc {

	@Override
	public String getName() {
		return LayoutWithForceAtlas2.class.getSimpleName();
	}

	@Override
	public void apply(Graph graph, GraphModel graphModel) {
		// run layout for 100 passes
		ForceAtlas2Builder builder = new ForceAtlas2Builder();
		ForceAtlas2 fa2Layout = new ForceAtlas2(builder);
		fa2Layout.setGraphModel(graphModel);
		fa2Layout.resetPropertiesValues();
		fa2Layout.setEdgeWeightInfluence(1.0);
		fa2Layout.setGravity(1.0);
		fa2Layout.setScalingRatio(2.0);
		fa2Layout.setBarnesHutTheta(1.2);
		fa2Layout.setJitterTolerance(0.1);
		fa2Layout.initAlgo();
		
		
		for (int i = 0; i < 200 && fa2Layout.canAlgo(); i++) {
			fa2Layout.goAlgo();
		}
		fa2Layout.endAlgo();
		
		System.out.println("End forceAtlas layout");
	}

}
