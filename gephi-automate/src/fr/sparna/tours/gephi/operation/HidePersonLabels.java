package fr.sparna.tours.gephi.operation;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Field;

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
import org.gephi.layout.plugin.labelAdjust.LabelAdjust;
import org.gephi.layout.plugin.labelAdjust.LabelAdjustBuilder;
import org.gephi.visualization.impl.TextDataImpl;
import org.gephi.visualization.impl.TextDataImpl.TextLine;
import org.openide.util.Lookup;

public class HidePersonLabels implements GraphOperationIfc {
	
	private static final String SCHEMA_ORG_PERSON = "http://schema.org/Person";
	
	@Override
	public String getName() {
		return HidePersonLabels.class.getSimpleName();
	}

	@Override
	public void apply(Graph graph, GraphModel graphModel) {		
		NodeIterable nodeIterable = graphModel.getDirectedGraph().getNodes();
		NodeIterator iterator = nodeIterable.iterator();
		while(iterator.hasNext()) {
			Node aNode = iterator.next();
			String type = (String)aNode.getAttributes().getValue("type");
			if(type != null && type.equals(SCHEMA_ORG_PERSON)) {
				aNode.getNodeData().getTextData().setVisible(false);
			}
		}
		
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
	}

}
