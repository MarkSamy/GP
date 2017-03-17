package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import database.Database;

public class DijkstraAlgorithm {

//	private final List<Edge> edges;
	private Set<String> settledNodes;
	private Set<String> unSettledNodes;
	private Map<String, String> predecessors;
	private Map<String, Double> distance;
	private Database db;
	
	public DijkstraAlgorithm() throws ClassNotFoundException, SQLException, IOException {
		db = new Database();
		db.loadWeightedEdges();
	}

	public void execute(String source) throws SQLException {
		settledNodes = new HashSet<String>();
		unSettledNodes = new HashSet<String>();
		distance = new HashMap<String, Double>();
		predecessors = new HashMap<String, String>();
		distance.put(source,  0D);
		unSettledNodes.add(source);
		while (unSettledNodes.size() > 0) {
			String node = getMinimum(unSettledNodes);
			settledNodes.add(node);
			unSettledNodes.remove(node);
			findMinimalDistances(node);
		}
	}

	private void findMinimalDistances(String node) throws SQLException {
		List<String> adjacentNodes = getNeighbors(node);
		for (String target : adjacentNodes) {
			if (getShortestDistance(target) > getShortestDistance(node) + getDistance(node, target)) {
				distance.put(target, getShortestDistance(node) + getDistance(node, target));
				predecessors.put(target, node);
				unSettledNodes.add(target);
			}
		}

	}

	private Double getDistance(String node, String target) throws SQLException {
		return db.getWeights(node, target);
	}

	private List<String> getNeighbors(String node) throws SQLException {
		List<String> neighbors = new ArrayList<String>();
		List<String> destinations = db.selectWithID(node);
		for (String dest : destinations) {
			if (!isSettled(dest)) {
				neighbors.add(dest);
			}
		}
		return neighbors;
	}

	private String getMinimum(Set<String> vertexes) {
		String minimum = null;
		for (String vertex : vertexes) {
			if (minimum == null) {
				minimum = vertex;
			} else {
				if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
					minimum = vertex;
				}
			}
		}
		return minimum;
	}

	private boolean isSettled(String vertex) {
		return settledNodes.contains(vertex);
	}

	public Double getShortestDistance(String destination) {
		Double d = distance.get(destination);
		if (d == null) {
			return Double.MAX_VALUE;
		} else {
			return d;
		}
	}

	/*
	 * This method returns the path from the source to the selected target and
	 * NULL if no path exists
	 */
	public LinkedList<String> getPath(String target) {
		LinkedList<String> path = new LinkedList<String>();
		String step = target;
		// check if a path exists
		if (predecessors.get(step) == null) {
			return null;
		}
		path.add(step);
		while (predecessors.get(step) != null) {
			step = predecessors.get(step);
			path.add(step);
		}
		// Put it into the correct order
		Collections.reverse(path);
		return path;
	}
 
}