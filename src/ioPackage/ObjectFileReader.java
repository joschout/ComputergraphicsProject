package ioPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import math.Point;
import math.Transformation;
import math.Vector;
import shape.MeshTriangle;
import shape.TriangleMesh;

public class ObjectFileReader {
	/**
	 * Geeft een opbject van de klasse Grid terug
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public TriangleMesh mesh;
	
	
	public TriangleMesh readFile(String filename) 
	{
		
		mesh = new TriangleMesh(Transformation.createIdentity());
		
		
		//Het inlezen van de data en het in een lijst van strings steken
		//Elk element van de lijst is een rij van de file 
		BufferedReader reader = null;

		try {
		    File file = new File(filename);
		    if (! file.exists() || file.isDirectory()) {
				file = new File(filename + ".obj");
			}
			if (! file.exists() || file.isDirectory()) {
				//throw new IOException(filename + " is geen geldig bestand.");
				System.out.println(filename + " is geen geldig bestand.");
			}
		    reader = new BufferedReader(new FileReader(file));

		    String line;
		    while ((line = reader.readLine()) != null) {
		        parseLine(line);
		    } 
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		        reader.close();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		return mesh;
		
	}


	/**
	 * Splits one line in to tokens, using the whitespace as the delimiter.
	 * 
	 * Lines are formatted as:  
	 * 		v x y z
	 * 		vt u v
	 * 		vn x y z
	 * 		f v1/vt1/vn1 v2/vt2/vn2 v3/vt3/vn3
	 * 
	 * @param input
	 * @throws UnsupportedEncodingException
	 */
	private void parseLine(String input) throws UnsupportedEncodingException {
		String[] tokens = input.split(" ");
		tokenParser(tokens);
	}
	
	/**
	 * Decides which type of parsing should happen based on the first string in the given array.
	 * 
	 * Lines are formatted as:  
	 * 		v x y z
	 * 		vt u v
	 * 		vn x y z
	 * 		f v1/vt1/vn1 v2/vt2/vn2 v3/vt3/vn3
	 * 
	 * @param input
	 * @throws UnsupportedEncodingException
	 */
	private void tokenParser(String[] tokens) throws UnsupportedEncodingException{
		String lineType = tokens[0];
		switch (lineType) {
		case "v":
			parseVertex(tokens);
			break;
		case "vt":
			parseTextureCoordinate(tokens);
			break;
		case "vn":
			parseVertexNormal(tokens);
			break;
		case "f":
			parseTriangle(tokens);
			break;
		default:
			throw new UnsupportedEncodingException("The obj file contains a line "
					+ "starting with the following unsupported keyword: "
					+ tokens[0]);
		}
	}


	/**
	 * Parses a line defining a triangle.
	 * A line can be written in the following formats:
	 * 		f v1/vt1/vn1 v2/vt2/vn2 v3/vt3/vn3
	 * 		f v1//vn1 v2//vn2 v3//vn3
	 * 		f v1/vt1 v2/vt2 v3/vt3
	 * with x, y and z strings that will be parsed to double values.
	 * 
	 * The line is given as argument, already split in 4 substrings.
	 *  "f" == token[0]
	 *  v1/vt1/vn1 == token[1]
	 *  v2/vt2/vn2 == token[2]
	 *  v3/vt3/vn3 == token[3]
	 * 
	 * @param tokens
	 * @throws UnsupportedEncodingException
	 */
	private void parseTriangle(String[] tokens) throws UnsupportedEncodingException {
		if(tokens.length != 4){
			throw new UnsupportedEncodingException("This triangle line is wrong: "
					+ tokens.toString());
		}
		
		MeshTriangle tri = new MeshTriangle(mesh);
		
		for(int i = 1; i <= 3; i++){
			String[] vertexSubtokens = tokens[i].split("/");
			triangleVertixParser(tri, vertexSubtokens, i);
		}
		
		mesh.addTriangle(tri);
	}


	/**
	 * Parses a the information for one vertex on a line defining a triangle.
	 * A line can be written in the following formats:
	 * 		f v1/vt1/vn1 v2/vt2/vn2 v3/vt3/vn3
	 * 		f v1//vn1 v2//vn2 v3//vn3
	 * 		f v1/vt1 v2/vt2 v3/vt3
	 * with x, y and z strings that will be parsed to double values.
	 * 
	 * So the information for one triangle can have the following formats:
	 * 		v1/vt1/vn1
	 * 		v1//vn1
	 * 		v1/vt1
	 * This info is given in the arguments as an array of strings already split with "/" as a delimiter.
	 * Also given are the triangle to which this information must be added
	 * and the number of the vertex this information is about.
	 *  If 		vertexSubtokens.length = 2
	 *  Than 	vertexSubtokens[0] == v1
	 *  		vertexSubtokens[1] == vt1
	 *  Else if vertexSubtokens.length = 3
	 *  Than 	vertexSubtokens[0] == v1
	 *  		vertexSubtokens[1] ==  "" || vertexSubtokens[1] ==  vt1
	 *  		vertexSubtokens[2] == vn1
	 * 
	 * @param tokens
	 * @throws UnsupportedEncodingException
	 */
	private void triangleVertixParser(MeshTriangle tri, String[] vertexSubtokens, int vertexNumber) {
		if(vertexSubtokens.length == 2){
			int vertexIndex = Integer.parseInt(vertexSubtokens[0]) - 1;
			tri.vertices[vertexNumber-1] = vertexIndex;
			
			int textureIndex = Integer.parseInt(vertexSubtokens[1]) - 1;
			tri.texCoords[vertexNumber - 1] = textureIndex;
		}
		else if(vertexSubtokens.length == 3){
			int vertexIndex = Integer.parseInt(vertexSubtokens[0])- 1;
			tri.vertices[vertexNumber-1] = vertexIndex;
			
			if(!vertexSubtokens[1].equals("")){
				int textureIndex = Integer.parseInt(vertexSubtokens[1]) - 1;
				tri.texCoords[vertexNumber - 1] = textureIndex;
			}
			
			int normalIndex = Integer.parseInt(vertexSubtokens[2])- 1;
			tri.normals[vertexNumber-1] = normalIndex;
		}	
	}


	/**
	 * Parses a line defining a normal.
	 * A line is formatted as follows:
	 * 		vn x y z
	 * with x, y and z strings that will be parsed to double values.
	 * 
	 * The line is given as argument, already split in 4 substrings.
	 *  "vn" == token[0]
	 *  x == token[1]
	 *  y == token[2]
	 *  z == token[3]
	 * 
	 * @param tokens
	 * @throws UnsupportedEncodingException
	 */
	private void parseVertexNormal(String[] tokens) throws UnsupportedEncodingException {
		if(tokens.length != 4){
			throw new UnsupportedEncodingException("This normal line is wrong: "
					+ tokens.toString());
		}
		double x = Double.parseDouble(tokens[1]);
		double y = Double.parseDouble(tokens[2]);
		double z = Double.parseDouble(tokens[3]);
		Vector normal = new Vector(x, y, z);
		mesh.addNormal(normal);
	}


	
	/**
	 * Parses a line defining texture coordinates
	 * A line is formatted as follows:
	 * 		vt u v
	 * with u and y strings that will be parsed to double values.
	 * 
	 * The line is given as argument, already split in 3 substrings.
	 *  "v" == token[0]
	 *  u == token[1]
	 *  v == token[2] 
	 * 
	 * @param tokens
	 * @throws UnsupportedEncodingException
	 */
	private void parseTextureCoordinate(String[] tokens) throws UnsupportedEncodingException {
		if(tokens.length != 3){
			throw new UnsupportedEncodingException("This texture coordinate line is wrong: "
					+ tokens[0] + " " + tokens[1] + " " + tokens[2]);
		}
		double u = Double.parseDouble(tokens[1]);
		double v = Double.parseDouble(tokens[2]);
		Point point = new Point(u,v,0);
		mesh.addTextureCoordinates(point);
	}

	/**
	 * Parses a line defining a vertex.
	 * A line is formatted as follows:
	 * 		v x y z
	 * with x, y and z strings that will be parsed to double values.
	 * 
	 * The line is given as argument, already split in 4 substrings.
	 *  "v" == token[0]
	 *  x == token[1]
	 *  y == token[2]
	 *  z == token[3]
	 * 
	 * @param tokens
	 * @throws UnsupportedEncodingException
	 */
	private void parseVertex(String[] tokens) throws UnsupportedEncodingException {
		if(tokens.length != 4){
			throw new UnsupportedEncodingException("This vertex line is wrong: "
					+ tokens.toString());
		}
		double x = Double.parseDouble(tokens[1]);
		double y = Double.parseDouble(tokens[2]);
		double z = Double.parseDouble(tokens[3]);
		Point vertex = new Point(x,y,z);
		mesh.addVertex(vertex);
	}
	
	
}