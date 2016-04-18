package fr.sparna.tours.gephi;

import java.io.File;
import java.io.IOException;

import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.spi.GraphExporter;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

public class GephiGexfExporter implements GephiExporterIfc {

	@Override
	public String getExtension() {
		return "gexf";
	}

	@Override
	public void export(Workspace w, File f) throws IOException {

		// make sure output dir exists
		f.getParentFile().mkdirs();

		ExportController ec = Lookup.getDefault().lookup(ExportController.class);
		// get GEXF Exporter
		GraphExporter gexfExporter = (GraphExporter) ec.getExporter("gexf");
		// Only exports the visible (filtered) graph
		gexfExporter.setExportVisible(true);
		gexfExporter.setWorkspace(w);
		ec.exportFile(f, gexfExporter);
	}

}
