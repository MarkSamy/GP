/*
	Copyright 2014 Adrien PAVIE
	
	This file is part of BasicOSMParser.
	
	BasicOSMParser is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.
	
	BasicOSMParser is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with BasicOSMParser. If not, see <http://www.gnu.org/licenses/>.
 */

package model;

import java.util.ArrayList;
import java.util.List;

/**
 * A way is an OSM element, which combines several {@link Node}s to create a path.
 * @author Adrien PAVIE
 */
public class Way extends Element {
//ATTRIBUTES
	/** The list of nodes of the way **/
	private List<String> nodes;
	
//CONSTRUCTOR
	/**
	 * Default constructor
	 * @param id The object ID
	 * @param nodes Its nodes
	 */
	public Way(long id, List<String> nodes) {
		super(id);
		
		//Conditions on nodes
		if(nodes == null) {
			throw new NullPointerException("Nodes list can't be null");
		}
		if(nodes.size() < 2) {
			throw new RuntimeException("A way should have at least two nodes");
		}
		
		this.nodes = nodes;
	}
	
	/**
	 * Constructor without nodes, not safe to use !
	 * Don't forget to add at least two nodes
	 * @param id The object ID
	 */
	public Way(long id) {
		super(id);
		this.nodes = new ArrayList<String>();
	}

//ACCESSORS
	@Override
	public String getId() {
		return ""+id;
	}

	/**
	 * @return The list of nodes of the way
	 */
	public List<String> getNodes() {
		return nodes;
	}

//MODIFIERS
	/**
	 * @param n The node to add at the end of the way
	 */
//	public void addNode(Node n) {
//		nodes.add(n);
//	}
	
	/**
	 * @param n The node to add at the end of the way
	 */
	public void addNode(String n) {
		nodes.add(n);
	}
	
	/**
	 * @param index The index of the node to remove
	 */
	public void removeNode(int index) {
		if(nodes.size() == 2) {
			throw new RuntimeException("Can't remove node, only two remaining");
		}
		nodes.remove(index);
	}
}
