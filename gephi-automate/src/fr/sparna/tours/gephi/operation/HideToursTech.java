package fr.sparna.tours.gephi.operation;

import org.gephi.filters.api.FilterController;
import org.gephi.filters.api.Query;
import org.gephi.filters.api.Range;
import org.gephi.filters.plugin.graph.DegreeRangeBuilder.DegreeRangeFilter;
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

public class HideToursTech implements GraphOperationIfc {
	
	private static final String TOURS_TECH = "http://smw.coopaxis.fr/id/Tours_Tech";
	
	@Override
	public String getName() {
		return HideToursTech.class.getSimpleName();
	}

	@Override
	public void apply(Graph graph, GraphModel graphModel) {		

		Node toDelete = null;
		Node[] nodes = graphModel.getDirectedGraph().getNodes().toArray();

		for(int j=0;j<nodes.length;j++) {
			Node aNode = nodes[j];
			for(int i=0;i<aNode.getNodeData().getAttributes().countValues();i++) {
				if(aNode.getNodeData().getAttributes().getValue(i).equals(TOURS_TECH)) {
					toDelete = aNode;
					break;
				}
			}
			if(toDelete != null) {
				break;
			}
		}
		
		if(toDelete != null) {
			System.out.println("Removing Tours Tech : "+toDelete.getId());
			graphModel.getDirectedGraph().removeNode(toDelete);
		}
	}

}
