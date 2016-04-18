package fr.sparna.tours.gephi.operation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.partition.api.Part;
import org.gephi.partition.api.Partition;
import org.gephi.partition.api.PartitionController;
import org.gephi.partition.plugin.NodeColorTransformer;
import org.gephi.statistics.plugin.Modularity;
import org.openide.util.Lookup;

public class ColorByPartition implements GraphOperationIfc {

	private static List<Color> COLORS = new ArrayList<Color>(Arrays.asList(new Color[] {
			// bleu
			new Color(0x0000FF),
			// rouge
			new Color(0xFF0000),
			// vert
			new Color(0x00FF00),
			// jaune
			new Color(0xFFFF00),
			// turquoise
			new Color(0x00FFFF),
			// violet
			new Color(0x00FF00),
			// default to grey
			new Color(0x999999)
	}));
	
	
	@Override
	public String getName() {
		return ColorByPartition.class.getSimpleName();
	}

	@Override
	public void apply(Graph graph, GraphModel graphModel) {
		AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
		PartitionController partitionController = Lookup.getDefault().lookup(PartitionController.class);
		
		// Run modularity algorithm - community detection
		Modularity modularity = new Modularity();
		modularity.setUseWeight(true);
		modularity.setResolution(1.2d);
		modularity.setRandom(true);
		
        modularity.execute(graphModel, attributeModel);
        // Change node and edge color according to modularity
        AttributeColumn modColumn = attributeModel.getNodeTable().getColumn(Modularity.MODULARITY_CLASS);
        Partition p2 = partitionController.buildPartition(modColumn, graph);
        System.out.println(p2.getPartsCount() + " modularity partitions found");
        
        
        
        
        NodeColorTransformer nodeColorTransformer = new NodeColorTransformer();
        
        // set colors - always use the same colors
        // nodeColorTransformer.randomizeColors(p2);
        if(p2.getPartsCount() > COLORS.size()) {
    		System.out.println("Warning, number of partitions ("+p2.getPartsCount()+") exceeds palette size ("+COLORS.size()+")");
    	}
        
        List<Part> parts = new ArrayList<Part>(Arrays.asList(p2.getParts()));
        Collections.sort(parts, new Comparator<Part>() {
			@Override
			public int compare(Part o1, Part o2) {
				if((o2.getPercentage() - o1.getPercentage()) < 0) {
					return -1;
				}
				else {
					return 1;
				}
			}        	
        });
        
        for(int i = 0; i < parts.size(); i++) {  
        	System.out.println(parts.get(i).getPercentage());
        	// p2.getParts()[i].setColor(COLORS.get(i));
        	nodeColorTransformer.getMap().put(parts.get(i).getValue(), COLORS.get(Math.min(i, COLORS.size()-1)));
        }
        
        partitionController.transform(p2, nodeColorTransformer);
	}

}
