package fr.sparna.tours.gephi.operation;

import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;

public interface GraphOperationIfc {

	public String getName();
	
	public void apply(Graph graph, GraphModel graphModel);
	
}
