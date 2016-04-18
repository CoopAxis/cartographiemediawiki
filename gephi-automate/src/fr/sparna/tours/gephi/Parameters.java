package fr.sparna.tours.gephi;

import java.io.File;
import java.util.List;

import com.beust.jcommander.Parameter;

public class Parameters {

	@Parameter(
			names = { "--endpoint", "-e" },
			description = "URL of SPARQL endpoint"
	)
	private String endpoint;
	
	@Parameter(
			names = { "--sparql", "-s" },
			description = "Path to SPARQL query file"
	)
	private File sparql;
	
	@Parameter(
			names = { "--input", "-i" },
			description = "Path to input graph file"
	)
	private File input;
	
	@Parameter(
			names = { "--operation", "-op" },
			description = "Operation (filters or layout) names"
	)
	private List<String> operations;
	
	@Parameter(
			names = { "--output", "-o" },
			description = "Path to an output file",
			required = true
	)
	private List<String> outputs;
	
	public String getEndpoint() {
		return endpoint;
	}

	public File getSparql() {
		return sparql;
	}

	public List<String> getOutputs() {
		return outputs;
	}

	public List<String> getOperations() {
		return operations;
	}

	public File getInput() {
		return input;
	}	
	
}
