package fr.sparna.tours.gephi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.spi.CharacterExporter;
import org.gephi.io.exporter.spi.Exporter;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

public class GephiGraphmlExporter implements GephiExporterIfc {

	@Override
	public String getExtension() {
		return "graphml";
	}

	@Override
	public void export(Workspace w, File f) throws IOException {
		
		// make sure output dir exists
		f.getParentFile().mkdirs();
		
		ExportController ec = Lookup.getDefault().lookup(ExportController.class);
		Exporter exporterGraphML = ec.getExporter("graphml"); //Get GraphML exporter
		exporterGraphML.setWorkspace(w);
		ec.exportWriter(new FileWriter(f), (CharacterExporter) exporterGraphML);
	}

}
