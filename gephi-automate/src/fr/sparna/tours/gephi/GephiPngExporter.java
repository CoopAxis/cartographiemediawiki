package fr.sparna.tours.gephi;

import java.io.File;
import java.io.IOException;

import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.preview.PNGExporter;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

public class GephiPngExporter implements GephiExporterIfc {

	@Override
	public String getExtension() {
		return "png";
	}

	@Override
	public void export(Workspace w, File f) throws IOException {
		
		// make sure output dir exists
		f.getParentFile().mkdirs();
		
		ExportController ec = Lookup.getDefault().lookup(ExportController.class);
		PNGExporter exporter = (PNGExporter) ec.getExporter("png");
	    exporter.setHeight(3072);
	    exporter.setWidth(3072);
	    // ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ec.exportFile(f, exporter);
	}

}
