package fr.sparna.tours.gephi.operation;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.ranking.api.Ranking;
import org.gephi.ranking.api.RankingController;
import org.gephi.ranking.api.Transformer;
import org.gephi.ranking.plugin.transformer.AbstractSizeTransformer;
import org.gephi.statistics.plugin.GraphDistance;
import org.openide.util.Lookup;

public class SizeByCentrality implements GraphOperationIfc {

	@Override
	public String getName() {
		return SizeByCentrality.class.getSimpleName();
	}

	@Override
	public void apply(Graph graph, GraphModel graphModel) {
		AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
		RankingController rankingController = Lookup.getDefault().lookup(RankingController.class);
		
		// Rank size by centrality
		AttributeColumn centralityColumn = attributeModel.getNodeTable().getColumn(GraphDistance.BETWEENNESS);
		Ranking centralityRanking = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, centralityColumn.getId());
		AbstractSizeTransformer sizeTransformer = (AbstractSizeTransformer) rankingController.getModel().getTransformer(Ranking.NODE_ELEMENT, Transformer.RENDERABLE_SIZE);
		sizeTransformer.setMinSize(1);
		sizeTransformer.setMaxSize(6);
		rankingController.transform(centralityRanking,sizeTransformer);
	}

}
