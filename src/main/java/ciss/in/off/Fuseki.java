package ciss.in.off;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;

public class Fuseki {
	
	public void createFusekiREST(String serviceURI) {

		String rdf = "E:/foaf.rdf";
		File rdfFile = new File(rdf);
		String base = "http://www.ldodds.com";
		FileInputStream in = null;
		Model m = ModelFactory.createDefaultModel();
		
		try {
			in = new FileInputStream(rdfFile);
			m.read(in, base, "RDF/XML");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
				
		DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(serviceURI);
		accessor.putModel(m);
		
		// Now the rdf will be available in Fuseki server in control panel at /foaf
		// Now SPARQL can be used to query the database using http-uris
	}
	
	public void execSelectAndPrint(String serviceURI, String query) {

		QueryExecution q = QueryExecutionFactory.sparqlService(serviceURI, query);
		ResultSet results = q.execSelect();

		ResultSetFormatter.out(System.out, results);

		while (results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			RDFNode x = soln.get("x");
			System.out.println(x);
		}
	}	
}