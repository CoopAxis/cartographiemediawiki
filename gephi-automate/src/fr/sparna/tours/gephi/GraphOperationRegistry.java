package fr.sparna.tours.gephi;

import java.util.ArrayList;
import java.util.List;

import fr.sparna.tours.gephi.operation.GraphOperationIfc;

public class GraphOperationRegistry {

	private List<GraphOperationIfc> operations = new ArrayList<GraphOperationIfc>();
	
	public GraphOperationIfc getOperationByName(String name) {
		for (GraphOperationIfc anOperation : operations) {
			if(anOperation.getName().equalsIgnoreCase(name)) {
				return anOperation;
			}
		}
		
		return null;
	}

	public List<GraphOperationIfc> getOperations() {
		return operations;
	}

	public void setOperations(List<GraphOperationIfc> operations) {
		this.operations = operations;
	}
	
}
