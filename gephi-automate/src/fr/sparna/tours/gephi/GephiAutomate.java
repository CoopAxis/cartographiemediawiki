package fr.sparna.tours.gephi;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Properties;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.filters.api.FilterController;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDefault;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.plugin.forceAtlas2.ForceAtlas2;
import org.gephi.layout.plugin.forceAtlas2.ForceAtlas2Builder;
import org.gephi.layout.plugin.labelAdjust.LabelAdjust;
import org.gephi.layout.plugin.labelAdjust.LabelAdjustBuilder;
import org.gephi.partition.api.Partition;
import org.gephi.partition.api.PartitionController;
import org.gephi.partition.plugin.NodeColorTransformer;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.ranking.api.Ranking;
import org.gephi.ranking.api.RankingController;
import org.gephi.ranking.api.Transformer;
import org.gephi.ranking.plugin.transformer.AbstractColorTransformer;
import org.gephi.ranking.plugin.transformer.AbstractSizeTransformer;
import org.gephi.statistics.plugin.GraphDistance;
import org.gephi.statistics.plugin.Modularity;
import org.gephi.visualization.impl.TextDataImpl;
import org.gephi.visualization.impl.TextDataImpl.TextLine;
import org.openide.util.Lookup;

import com.beust.jcommander.JCommander;

import fr.inria.edelweiss.semantic.importer.SemanticWebImportParser;
import fr.inria.edelweiss.sparql.restdriver.SparqlRestEndPointDriver;
import fr.inria.edelweiss.sparql.restdriver.SparqlRestEndPointDriverParameters;
import fr.sparna.commons.io.ReadWriteTextFile;
import fr.sparna.tours.gephi.operation.GraphOperationIfc;

public class GephiAutomate {

	protected Parameters parameters;	

	public GephiAutomate(Parameters p) {
		super();
		this.parameters = p;
	}

	public void run() throws Exception {
		//Init a project - and therefore a workspace
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();
		 
		//Get models and controllers for this new workspace - will be useful later
		AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
		PreviewModel model = Lookup.getDefault().lookup(PreviewController.class).getModel();
		ImportController importController = Lookup.getDefault().lookup(ImportController.class);
		FilterController filterController = Lookup.getDefault().lookup(FilterController.class);
		RankingController rankingController = Lookup.getDefault().lookup(RankingController.class);
		PartitionController partitionController = Lookup.getDefault().lookup(PartitionController.class);
		 
		
		if(parameters.getInput() != null) {
			System.out.println("Loading from "+parameters.getInput().getAbsolutePath());
			//Import file
			Container container;
			try {
				container = importController.importFile(parameters.getInput());
				// Force DIRECTED
				container.getLoader().setEdgeDefault(EdgeDefault.DIRECTED); 
			} catch (Exception ex) {
				ex.printStackTrace();
				return;
			}
			
			//Append imported data to GraphAPI
			importController.process(container, new DefaultProcessor(), workspace);
		} else {
			
			SparqlRestEndPointDriver driver = new SparqlRestEndPointDriver();
			SparqlRestEndPointDriverParameters driverParameters = new SparqlRestEndPointDriverParameters();
			driverParameters.setEndPointUrl(this.parameters.getEndpoint());
			driver.setParameters(driverParameters);
			
			System.out.println("Encodings : System file.encoding : "+System.getProperty("file.encoding")+", System default Charset : "+Charset.defaultCharset()+", default charset in use : "+getDefaultCharSet());
			System.out.println("Will run SPARQL against : "+this.parameters.getEndpoint());			
			System.out.println("Will execute SPARQL in "+this.parameters.getSparql()+" (file : "+this.parameters.getSparql().isFile()+" , dir : "+this.parameters.getSparql().isDirectory()+")");
			
			SemanticWebImportParser swParser = new SemanticWebImportParser();
			if(this.parameters.getSparql().isFile()) {
				/*
				 * LOAD FROM SPARQL ENDPOINT
				 */
				
				String sparql = ReadWriteTextFile.getContents(this.parameters.getSparql());
				System.out.println("Will execute SPARQL : "+"\n"+sparql);
				
		        swParser.getParameters().setRdfRequest(sparql);
		        swParser.populateRDFGraph(driver, new Properties(), null);
		        swParser.waitEndpopulateRDFGraph();
		        
		        /*
		         * END - LOAD FROM SPARQL ENDPOINT
		         */
			} else if(this.parameters.getSparql().isDirectory()) {
				for (File aFile : this.parameters.getSparql().listFiles()) {
					if(aFile.getName().endsWith("rq")) {
						String sparql = ReadWriteTextFile.getContents(aFile);
						System.out.println("Will execute SPARQL : "+"\n"+sparql);
						
				        swParser.getParameters().setRdfRequest(sparql);
				        swParser.populateRDFGraph(driver, new Properties(), null);
				        swParser.waitEndpopulateRDFGraph();
					}
				}
			}
		}
		 
		//See if graph is well imported
		DirectedGraph graph = graphModel.getDirectedGraph();
		System.out.println("Initial stats : nodes="+graph.getNodeCount()+"/edges="+graph.getEdgeCount());	 

		//Get Centrality
		GraphDistance distance = new GraphDistance();
		distance.setDirected(true);
		distance.execute(graphModel, attributeModel);
		
		// Apply filters
		if(this.parameters.getOperations() != null) {
			for (String aFilterName : this.parameters.getOperations()) {
				GraphOperationIfc anOperation = Config.getInstance().getOperationRegistry().getOperationByName(aFilterName);
				if(anOperation == null) {
					System.out.println("Warning, cannot find operation with name "+aFilterName);
				} else {
					anOperation.apply(graph, graphModel);
				}
				
				//See visible graph stats
				DirectedGraph graphVisible = graphModel.getDirectedGraphVisible();
				System.out.println("Stats after "+anOperation+" : nodes="+graphVisible.getNodeCount()+"/edges="+graphVisible.getEdgeCount());
			}
		}
		 
		// change edge color according to their type
//		Partition p = partitionController.buildPartition(attributeModel.getEdgeTable().getColumn("Label"), graph);
//		System.out.println(p.getPartsCount() + " edge partitions found");
//		EdgeColorTransformer edgeColorTransformer = new EdgeColorTransformer();
//		edgeColorTransformer.randomizeColors(p);
//		partitionController.transform(p, edgeColorTransformer);
//		
		
		//Preview
		model.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
		model.getProperties().putValue(PreviewProperty.EDGE_CURVED, Boolean.FALSE);
		// model.getProperties().putValue(PreviewProperty.EDGE_COLOR, new EdgeColor(Color.GRAY));
		model.getProperties().putValue(PreviewProperty.EDGE_THICKNESS, new Float(0.3f));
		model.getProperties().putValue(PreviewProperty.NODE_BORDER_WIDTH, new Float(0.2f));
		model.getProperties().putValue(PreviewProperty.NODE_LABEL_FONT, model.getProperties().getFontValue(PreviewProperty.NODE_LABEL_FONT).deriveFont(8));
		
		// now export
		for (String anOutput : this.parameters.getOutputs()) {
			String extension = anOutput.substring(anOutput.lastIndexOf('.')+1);
			GephiExporterIfc anExporter = Config.getInstance().getExporterRegistry().getExporterByExtension(extension);
			if(anExporter == null) {
				System.out.println("Cannot find exporter for file extension : "+extension+". Doing nothing.");
			} else {
				anExporter.export(workspace, new File(anOutput));
			}
		}
	}
	
	private static String getDefaultCharSet() {
    	OutputStreamWriter writer = new OutputStreamWriter(new ByteArrayOutputStream());
    	String enc = writer.getEncoding();
    	return enc;
    }
	
	/**
	 * args[0] : SPARQL endpoint
	 * args[1] : Path to SPARQL query file
	 * args[2] : PNG output file
	 * args[3] : (optional) : path to gexf output file
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String...args) throws Exception {
		Parameters p = new Parameters();
		new JCommander(p, args);
		System.out.println(p);
		GephiAutomate automate = new GephiAutomate(p);
		automate.run();
	}
	
}
