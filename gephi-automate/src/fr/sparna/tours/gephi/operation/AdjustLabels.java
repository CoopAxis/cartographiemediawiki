package fr.sparna.tours.gephi.operation;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Field;

import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.layout.plugin.labelAdjust.LabelAdjust;
import org.gephi.layout.plugin.labelAdjust.LabelAdjustBuilder;
import org.gephi.layout.plugin.scale.Expand;
import org.gephi.layout.plugin.scale.ScaleLayout;
import org.gephi.visualization.impl.TextDataImpl;
import org.gephi.visualization.impl.TextDataImpl.TextLine;

public class AdjustLabels implements GraphOperationIfc {

	@Override
	public String getName() {
		return AdjustLabels.class.getSimpleName();
	}

	@Override
	public void apply(Graph graph, GraphModel graphModel) {
		// @see https://github.com/gephi/gephi/issues/564
		Node[] nodes = graphModel.getGraph().getNodes().toArray();
		for (int i = 0; i < nodes.length; i++)
		{
			//Get the TextDataImpl object
			TextDataImpl td=(TextDataImpl) nodes[i].getNodeData().getTextData();
			String labelText=nodes[i].getNodeData().getLabel();
			td.setText(labelText);
			// Could perhaps used getFontMetrics here to be more accurate but
			// this heuristic seems to work for me:
			// Rectangle2D bounds=new Rectangle(labelText.length()*10,20);
			Rectangle2D bounds=new Rectangle(labelText.length()*5,20);
			
			try {
				// Use reflection to set the protected Bounds data to non-zero sizes.
				Field protectedLineField = TextDataImpl.class.getDeclaredField("line");
				protectedLineField.setAccessible(true);        
				TextLine line = (TextLine) protectedLineField.get(td);
				line.setBounds(bounds);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		LabelAdjust ladjustLayout = new LabelAdjust(new LabelAdjustBuilder());
		ladjustLayout.setGraphModel(graphModel);
		
		ladjustLayout.initAlgo();		
		for (int i = 0; i < 10 && ladjustLayout.canAlgo(); i++) {
			ladjustLayout.goAlgo();
		}
		ladjustLayout.endAlgo();
		System.out.println("End label adjust layout");
	}

}
