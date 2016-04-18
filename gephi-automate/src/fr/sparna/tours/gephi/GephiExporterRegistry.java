package fr.sparna.tours.gephi;

import java.util.ArrayList;
import java.util.List;

public class GephiExporterRegistry {

	private List<GephiExporterIfc> exporters = new ArrayList<GephiExporterIfc>();
	
	public GephiExporterIfc getExporterByExtension(String extension) {
		for (GephiExporterIfc anExporter : exporters) {
			if(anExporter.getExtension().equalsIgnoreCase(extension)) {
				return anExporter;
			}
		}
		
		return null;
	}

	public List<GephiExporterIfc> getExporters() {
		return exporters;
	}

	public void setExporters(List<GephiExporterIfc> exporters) {
		this.exporters = exporters;
	}
	
}
