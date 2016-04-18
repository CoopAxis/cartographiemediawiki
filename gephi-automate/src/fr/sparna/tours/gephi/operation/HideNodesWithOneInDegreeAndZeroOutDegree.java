package fr.sparna.tours.gephi.operation;

import org.gephi.filters.api.FilterController;
import org.gephi.filters.api.Query;
import org.gephi.filters.api.Range;
import org.gephi.filters.plugin.graph.InDegreeRangeBuilder.InDegreeRangeFilter;
import org.gephi.filters.plugin.graph.OutDegreeRangeBuilder.OutDegreeRangeFilter;
import org.gephi.filters.plugin.operator.INTERSECTIONBuilder.IntersectionOperator;
import org.gephi.filters.plugin.operator.UNIONBuilder.UnionOperator;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
import org.openide.util.Lookup;

public class HideNodesWithOneInDegreeAndZeroOutDegree implements GraphOperationIfc {
	
	@Override
	public String getName() {
		return HideNodesWithOneInDegreeAndZeroOutDegree.class.getSimpleName();
	}

	@Override
	public void apply(Graph graph, GraphModel graphModel) {		
		FilterController filterController = Lookup.getDefault().lookup(FilterController.class);
		
		// ou bien qui ont au moins un lien SORTANT...
		OutDegreeRangeFilter outDegree = new OutDegreeRangeFilter();
		outDegree.init(graph);
		// superieur strict
		outDegree.setRange(new Range(0, Integer.MAX_VALUE, false, true));
		
		// ou bien qui ont plus d'un lien ENTRANT
		InDegreeRangeFilter inDegree = new InDegreeRangeFilter();
		inDegree.init(graph);
		// superieur strict
		inDegree.setRange(new Range(1, Integer.MAX_VALUE, false, true));	
				
		
		// Query andQuery = filterController.createQuery(new IntersectionOperator());
		Query orQuery = filterController.createQuery(new UnionOperator());
		filterController.setSubQuery(orQuery, filterController.createQuery(inDegree));
		filterController.setSubQuery(orQuery, filterController.createQuery(outDegree));
		
		
		GraphView view = filterController.filter(orQuery);
		// Set the filter result as the visible view
		graphModel.setVisibleView(view);
	}

}
