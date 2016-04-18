package fr.sparna.tours.gephi.operation;

import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.layout.plugin.scale.Expand;
import org.gephi.layout.plugin.scale.ScaleLayout;

public class Scale implements GraphOperationIfc {

	@Override
	public String getName() {
		return Scale.class.getSimpleName();
	}

	@Override
	public void apply(Graph graph, GraphModel graphModel) {
		ScaleLayout scaleLayout = new ScaleLayout(new Expand(), 4d);
		scaleLayout.setGraphModel(graphModel);
		
		scaleLayout.initAlgo();		
		for (int i = 0; i < 10 && scaleLayout.canAlgo(); i++) {
			scaleLayout.goAlgo();
		}
		scaleLayout.endAlgo();
		System.out.println("End Scale layout");
	}

}
