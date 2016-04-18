package fr.sparna.tours.gephi.operation;

import org.gephi.filters.api.FilterController;
import org.gephi.filters.api.Query;
import org.gephi.filters.api.Range;
import org.gephi.filters.plugin.graph.DegreeRangeBuilder.DegreeRangeFilter;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
import org.openide.util.Lookup;

//Filter      
//DegreeRangeFilter degreeFilter = new DegreeRangeFilter();
//degreeFilter.init(graph);
//degreeFilter.setRange(new Range(30, Integer.MAX_VALUE));     //Remove nodes with degree < 30
//Query query = filterController.createQuery(degreeFilter);
//GraphView view = filterController.filter(query);
//graphModel.setVisibleView(view);    //Set the filter result as the visible view

public class HideNodesWithDegreeOne implements GraphOperationIfc {
	
	@Override
	public String getName() {
		return HideNodesWithDegreeOne.class.getSimpleName();
	}

	@Override
	public void apply(Graph graph, GraphModel graphModel) {		
		FilterController filterController = Lookup.getDefault().lookup(FilterController.class);
		
		DegreeRangeFilter degreeFilter = new DegreeRangeFilter();
		degreeFilter.init(graph);
		// lower bound exclusive
		degreeFilter.setRange(new Range(1, Integer.MAX_VALUE, false, true));
		Query query = filterController.createQuery(degreeFilter);
		
		
		GraphView view = filterController.filter(query);
		// Set the filter result as the visible view
		graphModel.setVisibleView(view);
	}

}
