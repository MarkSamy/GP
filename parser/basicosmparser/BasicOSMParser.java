package controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import model.Element;
import model.Node;
import model.Relation;
import model.Way;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * OSMParser parses XML file (OSM database extracts) and creates corresponding
 * Java objects.
 * 
 * @author Adrien PAVIE
 */
public class OSMParser extends DefaultHandler {

	// ATTRIBUTES
	/** The current read element **/
	private Element current;
	private StringBuilder csvNodesBuild;
	private StringBuilder csvWaysBuild;
	private StringBuilder csvRelsBuild;
	private File nodesFile, waysFile, relsFile, edgesFile;
	private PrintWriter nodesWriter, waysWriter, relsWriter,edgesWriter;
	private File nodesFile_Two;
	private PrintWriter nodesWriter_Two;
	private File dataFile;
	private PrintWriter dataWriter;
	private int nbNodes = 0, nbWays = 0, nbRels = 0, nbEdges = 0;
	private static final int FLUSH_AMOUNT = 10000;
	private static double minLat = 2e9, maxLat = -2e9, minLong = 2e9, maxLong = -2e9;

	// CONSTRUCTOR
	public OSMParser() {
		super();
	}

	// OTHER METHODS
	/**
	 * Parses a XML file and creates OSM Java objects
	 * 
	 * @param f
	 *            The OSM database extract, in XML format, as a file
	 * @return
	 * @return The corresponding OSM objects as a Map. Keys are elements ID, and
	 *         values are OSM elements objects.
	 * @throws IOException
	 *             If an error occurs during file reading
	 * @throws SAXException
	 *             If an error occurs during parsing
	 */
	public Object parse(File f) throws IOException, SAXException {

		csvNodesBuild = null;
		csvWaysBuild = null;
		csvRelsBuild = null;

		nodesFile = new File("nodes.txt");
		nodesFile_Two = new File("nodesv2.txt");
		waysFile = new File("ways.txt");
		relsFile = new File("rels.txt");
		edgesFile = new File("edges.txt");
		
		nodesWriter = new PrintWriter(new FileWriter(nodesFile));
		waysWriter = new PrintWriter(new FileWriter(waysFile));
		relsWriter = new PrintWriter(new FileWriter(relsFile));
		edgesWriter = new PrintWriter(new FileWriter(edgesFile));
		nodesWriter_Two = new PrintWriter(new FileWriter(nodesFile_Two));

		dataFile = new File("data.txt");
		dataWriter = new PrintWriter(new FileWriter(dataFile));

		// File check
		if (!f.exists() || !f.isFile()) {
			throw new FileNotFoundException();
		}

		if (!f.canRead()) {
			throw new IOException("Can't read file");
		}

		return parse(new InputSource(new FileReader(f)));
	}

	/**
	 * Parses a XML file and creates OSM Java objects
	 * 
	 * @param s
	 *            The OSM database extract, in XML format, as a String
	 * @return
	 * @return The corresponding OSM objects as a Map. Keys are elements ID, and
	 *         values are OSM elements objects.
	 * @throws IOException
	 *             If an error occurs during file reading
	 * @throws SAXException
	 *             If an error occurs during parsing
	 */
	public Object parse(String s) throws SAXException, IOException {
		return parse(new InputSource(new ByteArrayInputStream(s.getBytes("UTF-8"))));
	}

	/**
	 * Parses a XML input and creates OSM Java objects
	 * 
	 * @param input
	 *            The OSM database extract, in XML format, as an InputSource
	 * @return The corresponding OSM objects as a Map. Keys are elements ID, and
	 *         values are OSM elements objects.
	 * @throws IOException
	 *             If an error occurs during reading
	 * @throws SAXException
	 *             If an error occurs during parsing
	 */
	public Object parse(InputSource input) throws SAXException, IOException {
		// Start parsing
		XMLReader xr = XMLReaderFactory.createXMLReader();
		xr.setContentHandler(this);
		xr.setErrorHandler(this);
		xr.parse(input);

		System.out.println(nbNodes + " " + nbWays + " " + nbRels + " " + nbEdges);

		dataWriter.println(minLat + " " + maxLat + " " + minLong + " " + maxLong);

		nodesWriter.close();
		relsWriter.close();
		waysWriter.close();
		edgesWriter.close();
		nodesWriter_Two.close();
		dataWriter.close();
		return null;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);

		if (localName.equals("node") || localName.equals("way") || localName.equals("relation")) {
			// Add element to list, and delete current
			if (current != null) {
				if (localName.equals("node") || (localName.equals("way") && ((Way) current).getNodes().size() >= 2)
						|| (localName.equals("relation") && ((Relation) current).getMembers().size() > 0)) {
					if (current instanceof Node) {
						csvNodesBuild = new StringBuilder();
						nbNodes++;
						// Node element (define latitude, longitude)
						Node currentNode = (Node) current;
						addInformations(csvNodesBuild, current);
						csvNodesBuild.append(";" + currentNode.getLat() + ";" + currentNode.getLon() + ";");
						minLat = Math.min(minLat, currentNode.getLat());
						maxLat = Math.max(maxLat, currentNode.getLat());
						minLong = Math.min(minLong, currentNode.getLon());
						maxLong = Math.max(maxLong, currentNode.getLon());
						addTags(csvNodesBuild, current);
						csvNodesBuild.append("\n");
						nodesWriter.print(csvNodesBuild);
						nodesWriter_Two.print(
								current.getId() + ";" + currentNode.getLat() + ";" + currentNode.getLon() + "\n");

						if (nbNodes % FLUSH_AMOUNT == 0) {
							nodesWriter.flush();
							nodesWriter_Two.flush();
						}
						csvNodesBuild = null;
					}

					else if (current instanceof Way) {
						csvWaysBuild = new StringBuilder();
						nbWays++;
						Way currentWay = (Way) current;
						addInformations(csvWaysBuild, current);
						csvWaysBuild.append(";\"[" + currentWay.getNodes().get(0));
						for (int i = 1; i < currentWay.getNodes().size(); i++) {
							csvWaysBuild.append("," + currentWay.getNodes().get(i));
							edgesWriter.println(currentWay.getNodes().get(i) + ";" + currentWay.getNodes().get(i-1));
							edgesWriter.println(currentWay.getNodes().get(i-1) + ";" + currentWay.getNodes().get(i));
							nbEdges+=2;
						}
						csvWaysBuild.append("]\";");
						addTags(csvWaysBuild, current);
						csvWaysBuild.append("\n");
						waysWriter.print(csvWaysBuild);
						if(nbEdges%FLUSH_AMOUNT == 0){
							edgesWriter.flush();
						}
						if (nbWays % FLUSH_AMOUNT == 0) {
							waysWriter.flush();
						}
						csvWaysBuild = null;
					} else if (current instanceof Relation) {
						csvRelsBuild = new StringBuilder();
						nbRels++;
						// Relation element (list members and roles)
						Relation currentRel = (Relation) current;
						addInformations(csvRelsBuild, current);
						csvRelsBuild.append(";\"[");
						// List members and roles

						for (int i = 0; i < currentRel.getMembers().size(); i++) {
							if (i > 0) {
								csvRelsBuild.append(",");
							}

							// Member
							csvRelsBuild.append(currentRel.getMembers().get(i) + "=");

							// Role
							String role = currentRel.getMemberRole(currentRel.getMembers().get(i));
							if (role.equals("")) {
								role = "null";
							}
							csvRelsBuild.append(role);
						}
						csvRelsBuild.append("]\";");
						addTags(csvRelsBuild, current);
						csvRelsBuild.append("\n");
						relsWriter.print(csvRelsBuild);
						if (nbRels % FLUSH_AMOUNT == 0) {
							relsWriter.flush();
						}
						csvRelsBuild = null;

					} else {
						throw new RuntimeException("Unexpected kind of Element: " + current.getClass().toString());
					}
				}
				current = null;
			}
		}
	}

	private void addTags(StringBuilder sb, Element elem) {
		// Start tags array
		sb.append("\"[");

		boolean firstTag = true;

		// Add each tag
		for (String key : elem.getTags().keySet()) {
			if (!firstTag) {
				sb.append(",");
			} else {
				firstTag = false;
			}

			sb.append(key + "=" + elem.getTags().get(key));
		}

		// End array
		sb.append("]\"");
	}

	private void addInformations(StringBuilder sb, Element elem) {
		sb.append(elem.getId() + ';' + elem.getUid() + ';' + elem.getTimestamp() + ';' + elem.isVisible() + ';'
				+ elem.getVersion() + ';' + elem.getChangeset());
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);

		// Case of node
		if (localName.equals("node")) {
			Node n = new Node(Long.parseLong(attributes.getValue("id")), Double.parseDouble(attributes.getValue("lat")),
					Double.parseDouble(attributes.getValue("lon")));
			n.setUser(attributes.getValue("user"));

			if (attributes.getValue("uid") != null) {
				n.setUid(Long.parseLong(attributes.getValue("uid")));
			}

			n.setVisible(Boolean.parseBoolean(attributes.getValue("visible")));

			if (attributes.getValue("version") != null) {
				n.setVersion(Integer.parseInt(attributes.getValue("version")));
			}

			if (attributes.getValue("changeset") != null) {
				n.setChangeset(Long.parseLong(attributes.getValue("changeset")));
			}

			n.setTimestamp(attributes.getValue("timestamp"));
			current = n;
		}
		// Case of way
		else if (localName.equals("way")) {
			Way w = new Way(Long.parseLong(attributes.getValue("id")));
			w.setUser(attributes.getValue("user"));

			if (attributes.getValue("uid") != null) {
				w.setUid(Long.parseLong(attributes.getValue("uid")));
			}

			w.setVisible(Boolean.parseBoolean(attributes.getValue("visible")));

			if (attributes.getValue("version") != null) {
				w.setVersion(Integer.parseInt(attributes.getValue("version")));
			}

			if (attributes.getValue("changeset") != null) {
				w.setChangeset(Long.parseLong(attributes.getValue("changeset")));
			}

			w.setTimestamp(attributes.getValue("timestamp"));
			current = w;
		}
		// Case of way node
		else if (localName.equals("nd")) {
			((Way) current).addNode(attributes.getValue("ref"));
		}
		// Case of relation
		else if (localName.equals("relation")) {
			Relation r = new Relation(Long.parseLong(attributes.getValue("id")));
			r.setUser(attributes.getValue("user"));

			if (attributes.getValue("uid") != null) {
				r.setUid(Long.parseLong(attributes.getValue("uid")));
			}

			r.setVisible(Boolean.parseBoolean(attributes.getValue("visible")));

			if (attributes.getValue("version") != null) {
				r.setVersion(Integer.parseInt(attributes.getValue("version")));
			}

			if (attributes.getValue("changeset") != null) {
				r.setChangeset(Long.parseLong(attributes.getValue("changeset")));
			}

			r.setTimestamp(attributes.getValue("timestamp"));
			current = r;
		}
		// Case of relation member
		else if (localName.equals("member")) {
			String type = attributes.getValue("type");
			String ref = attributes.getValue("ref");

			// //If member isn't contained in data, create stub object

			// Add member
			((Relation) current).addMember(attributes.getValue("role"), type, ref);
		}
		// Case of tag
		else if (localName.equals("tag")) {
			if (current != null) {
				current.addTag(attributes.getValue("k"), attributes.getValue("v"));
			}
		}
	}

}
