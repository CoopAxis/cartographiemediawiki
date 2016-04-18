package fr.sparna.tours.gephi;

import fr.sparna.tours.gephi.operation.ColorByCentrality;
import fr.sparna.tours.gephi.operation.ColorByDegree;
import fr.sparna.tours.gephi.operation.HidePersonLabels;
import fr.sparna.tours.gephi.operation.HideSmallDisconnectedGraphs;
import fr.sparna.tours.gephi.operation.HideSmallLabels;
import fr.sparna.tours.gephi.operation.HideToursTech;
import fr.sparna.tours.gephi.operation.LayoutWithYifanHu;
import fr.sparna.tours.gephi.operation.Scale;
import fr.sparna.tours.gephi.operation.LayoutWithForceAtlas2;
import fr.sparna.tours.gephi.operation.HideNodesWithDegreeOne;
import fr.sparna.tours.gephi.operation.HideNodesWithOneInDegreeAndZeroOutDegree;
import fr.sparna.tours.gephi.operation.HideNodesWithOneOutDegreeAndZeroInDegree;
import fr.sparna.tours.gephi.operation.AdjustLabels;
import fr.sparna.tours.gephi.operation.ColorByPartition;
import fr.sparna.tours.gephi.operation.SizeByCentrality;
import fr.sparna.tours.gephi.operation.SizeByDegree;

public class Config {

	private static Config instance;
	
	private GephiExporterRegistry exporterRegistry;
	private GraphOperationRegistry operationRegistry;
	
	private Config() {
		exporterRegistry = new GephiExporterRegistry();
		exporterRegistry.getExporters().add(new GephiPngExporter());
		exporterRegistry.getExporters().add(new GephiGexfExporter());
		exporterRegistry.getExporters().add(new GephiGraphmlExporter());
		
		operationRegistry = new GraphOperationRegistry();
		operationRegistry.getOperations().add(new HideNodesWithDegreeOne());
		operationRegistry.getOperations().add(new HideNodesWithOneOutDegreeAndZeroInDegree());
		operationRegistry.getOperations().add(new HideNodesWithOneInDegreeAndZeroOutDegree());
		operationRegistry.getOperations().add(new LayoutWithForceAtlas2());
		operationRegistry.getOperations().add(new Scale());
		operationRegistry.getOperations().add(new AdjustLabels());
		operationRegistry.getOperations().add(new ColorByPartition());
		operationRegistry.getOperations().add(new ColorByDegree());
		operationRegistry.getOperations().add(new ColorByCentrality());
		operationRegistry.getOperations().add(new SizeByCentrality());
		operationRegistry.getOperations().add(new SizeByDegree());
		operationRegistry.getOperations().add(new HideSmallDisconnectedGraphs());
		operationRegistry.getOperations().add(new HideSmallLabels());
		operationRegistry.getOperations().add(new HideToursTech());
		operationRegistry.getOperations().add(new LayoutWithYifanHu());
		operationRegistry.getOperations().add(new HidePersonLabels());		
	}
	
	public static Config getInstance() {
		if(instance == null) {
			instance = new Config();
		}
		
		return instance;
	}

	public GephiExporterRegistry getExporterRegistry() {
		return exporterRegistry;
	}

	public GraphOperationRegistry getOperationRegistry() {
		return operationRegistry;
	}
	
}
