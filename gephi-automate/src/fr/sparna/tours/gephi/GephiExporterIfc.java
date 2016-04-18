package fr.sparna.tours.gephi;

import java.io.File;
import java.io.IOException;

import org.gephi.io.exporter.api.ExportController;
import org.gephi.project.api.Workspace;

public interface GephiExporterIfc {

	public String getExtension();
	
	public void export(Workspace w, File f) throws IOException;
	
}
