package fr.sparna.tours.gephi.operation;

import org.gephi.filters.api.FilterController;
import org.gephi.filters.api.Query;
import org.gephi.filters.api.Range;
import org.gephi.filters.plugin.graph.DegreeRangeBuilder.DegreeRangeFilter;
import org.gephi.graph.api.Attributable;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.NodeIterable;
import org.gephi.graph.api.NodeIterator;
import org.openide.util.Lookup;

//Filter      
//DegreeRangeFilter degreeFilter = new DegreeRangeFilter();
//degreeFilter.init(graph);
//degreeFilter.setRange(new Range(30, Integer.MAX_VALUE));     //Remove nodes with degree < 30
//Query query = filterController.createQuery(degreeFilter);
//GraphView view = filterController.filter(query);
//graphModel.setVisibleView(view);    //Set the filter result as the visible view

public class HideSmallLabels implements GraphOperationIfc {
	
	@Override
	public String getName() {
		return HideSmallLabels.class.getSimpleName();
	}

	@Override
	public void apply(Graph graph, GraphModel graphModel) {		
		NodeIterable nodeIterable = graphModel.getDirectedGraph().getNodes();
		NodeIterator iterator = nodeIterable.iterator();
		while(iterator.hasNext()) {
			Node aNode = iterator.next();
			System.out.println(aNode.getNodeData().getLabel()+" : "+aNode.getNodeData().getSize());
			if(aNode.getNodeData().getSize() == 4 || aNode.getNodeData().getSize() < 1.05) {
				System.out.println("Marking label invisible : '"+aNode.getNodeData().getLabel()+"'");
				aNode.getNodeData().getTextData().setVisible(false);
			}
		}
	}

}
