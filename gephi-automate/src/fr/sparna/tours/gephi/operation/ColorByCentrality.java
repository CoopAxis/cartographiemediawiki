package fr.sparna.tours.gephi.operation;

import java.awt.Color;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.ranking.api.Ranking;
import org.gephi.ranking.api.RankingController;
import org.gephi.ranking.api.Transformer;
import org.gephi.ranking.plugin.transformer.AbstractColorTransformer;
import org.gephi.statistics.plugin.GraphDistance;
import org.openide.util.Lookup;

public class ColorByCentrality implements GraphOperationIfc {

	@Override
	public String getName() {
		return ColorByCentrality.class.getSimpleName();
	}

	@Override
	public void apply(Graph graph, GraphModel graphModel) {
		RankingController rankingController = Lookup.getDefault().lookup(RankingController.class);
		AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
		
		//Rank color by Degree
		AttributeColumn centralityColumn = attributeModel.getNodeTable().getColumn(GraphDistance.BETWEENNESS);
		Ranking centralityRanking = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, centralityColumn.getId());
		AbstractColorTransformer colorTransformer = (AbstractColorTransformer) rankingController.getModel().getTransformer(Ranking.NODE_ELEMENT, Transformer.RENDERABLE_COLOR);
		// du beige au rouge :
		// colorTransformer.setColors(new Color[]{new Color(0xFEF0D9), new Color(0xB30000)});
		// du bleu pale au rouge profond :
		colorTransformer.setColors(new Color[]{new Color(0xD0E1FF), new Color(0xB30000)});
		// rouge plus sombre
		// colorTransformer.setColors(new Color[]{new Color(0xD0E1FF), new Color(0x8F0000)});
		rankingController.transform(centralityRanking,colorTransformer);
	}

}
