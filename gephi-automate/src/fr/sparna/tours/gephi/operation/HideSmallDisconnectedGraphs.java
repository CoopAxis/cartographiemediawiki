package fr.sparna.tours.gephi.operation;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.partition.api.Part;
import org.gephi.partition.api.Partition;
import org.gephi.partition.api.PartitionController;
import org.gephi.partition.plugin.NodeColorTransformer;
import org.gephi.statistics.plugin.Modularity;
import org.openide.util.Lookup;

public class HideSmallDisconnectedGraphs implements GraphOperationIfc {

	@Override
	public String getName() {
		return HideSmallDisconnectedGraphs.class.getSimpleName();
	}

	@Override
	public void apply(Graph graph, GraphModel graphModel) {
		AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
		PartitionController partitionController = Lookup.getDefault().lookup(PartitionController.class);
		
		// Run modularity algorithm - community detection
		Modularity modularity = new Modularity();
		modularity.setResolution(20d);
        modularity.execute(graphModel, attributeModel);
        // Change node and edge color according to modularity
        AttributeColumn modColumn = attributeModel.getNodeTable().getColumn(Modularity.MODULARITY_CLASS);
        Partition p2 = partitionController.buildPartition(modColumn, graph);
        System.out.println(p2.getPartsCount() + " modularity partitions found");
        
        for (Part aPart : p2.getParts()) {
        	System.out.println(aPart.getPercentage()+" / "+aPart.getObjects().length);
        	if(aPart.getPercentage() < 0.05d) {
        		for (Object aNode : aPart.getObjects()) {
        			// remove nodes and edges
            		graphModel.getDirectedGraph().removeNode((Node)aNode);
				}
        	}
        	// org.gephi.graph.api.Node aNode = (org.gephi.graph.api.Node)aPart.getObjects()[0];
        	// System.out.println(aNode);
		}
	}

}
