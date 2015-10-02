package ciss.in.off.rdf;

import java.io.InputStream;
import java.util.Iterator;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
/*import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;*/
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.tdb.TDBLoader;
import org.apache.jena.tdb.sys.TDBInternal;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;

public class RDFTransactions {
	Dataset dataset;
	InputStream in;
	Model model;
	
	public RDFTransactions() {
	}
	
	public void initializeRDF(String RDFFile) {
		
		
		dataset = TDBFactory.assembleDataset(RDFFile) ;		  
	}
	
	public void readRDF(String RDFFile) {        
       //Apply methods like getResource, getProperty, listStatements,listLiteralStatements ...
       //to your model to extract the information you want

		dataset.begin(ReadWrite.READ);
		
        dataset.begin(ReadWrite.READ);
	    try {
	        Iterator<Quad> iter = dataset.asDatasetGraph().find();
	        while ( iter.hasNext() ) {
	            Quad quad = iter.next();
	            System.out.println(quad);
	        }
	    } catch (Exception e) {
	    } finally {
	        dataset.end();
	    }
	    
		// Get model inside the transaction
/*        Resource person = model.getResource("http://test.example.com/MainPerson.rdf");
        Property firstName = model.createProperty("http://xmlns.com/foaf/0.1/firstName");
        String firstNameValue = person.getProperty(firstName).getString();
        System.out.println(firstNameValue);
		
		model = dataset.getDefaultModel();*/
		dataset.end();		  
	}
	
	public void updateRDF() {

        String updateString = 
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
                "UPDATE ?name WHERE { " +
                "    ?person foaf:mbox <mailto:alice@example.org> . " +
                "    ?person foaf:name ?name . " +
                "}";
        
		dataset.begin(ReadWrite.WRITE);
	    try {
	        TDBLoader.load(TDBInternal.getBaseDatasetGraphTDB(dataset.asDatasetGraph()), in, false);
	        
            UpdateRequest update = UpdateFactory.create(updateString);
            UpdateProcessor qexec = UpdateExecutionFactory.create(update, dataset);
            try {
                qexec.execute();
                dataset.commit();
		    } catch (Exception e) {
		        dataset.abort();
		    } finally {
		        dataset.end();
		    }
	
            dataset.begin(ReadWrite.READ);
		    try {
		        Iterator<Quad> iter = dataset.asDatasetGraph().find();
		        while ( iter.hasNext() ) {
		            Quad quad = iter.next();
		            System.out.println(quad);
		        }
		    } catch (Exception e) {
		    } finally {
		        dataset.end();
		    }
	    } finally {
	        dataset.end();
	    }
	}
	
	public void readRDF() {

        String queryString = 
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
                "SELECT ?name WHERE { " +
                "    ?person foaf:mbox <mailto:alice@example.org> . " +
                "    ?person foaf:name ?name . " +
                "}";
        
        dataset.begin(ReadWrite.READ);
        try {
            Query query = QueryFactory.create(queryString);
            QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
            try {
                ResultSet results = qexec.execSelect();
                while ( results.hasNext() ) {
                    QuerySolution soln = results.nextSolution();
                    Literal name = soln.getLiteral("name");
                    System.out.println(name);
                }
            } finally {
                qexec.close();
            }
        } finally {
            dataset.end();
        }
	}
	
	public void createRDF() {

		dataset.begin(ReadWrite.WRITE);
        try {
            TDBLoader.load(TDBInternal.getBaseDatasetGraphTDB(dataset.asDatasetGraph()), in, false);
            dataset.commit();
        } catch (Exception e) {
            dataset.abort();
        } finally {
            dataset.end();
        }

        dataset.begin(ReadWrite.READ);
        try {
            Iterator<Quad> iter = dataset.asDatasetGraph().find();
            while ( iter.hasNext() ) {
                Quad quad = iter.next();
                System.out.println(quad);
            }
        } finally {
            dataset.end();
        }
	}
	
	public void queryDbpedia() {
		String queryString=
				"PREFIX p: <http://dbpedia.org/property/>"+
				"PREFIX dbpedia: <http://dbpedia.org/resource/>"+
				"PREFIX category: <http://dbpedia.org/resource/Category:>"+
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
				"PREFIX skos: <http://www.w3.org/2004/02/skos/core#>"+
				"PREFIX geo: <http://www.georss.org/georss/>"+

				"SELECT DISTINCT ?m ?n ?p ?d"+
				"WHERE {"+
				" ?m rdfs:label ?n."+
				" ?m skos:subject ?c."+
				" ?c skos:broader category:Churches_in_Paris."+
				" ?m p:abstract ?d."+
				" ?m geo:point ?p"+
				//" FILTER ( lang(?n) = "fr" )"+
				//" FILTER ( lang(?d) = "fr" )"+
				" }";

		// now creating query object
		Query query = QueryFactory.create(queryString);
		// initializing queryExecution factory with remote service.
		// **this actually was the main problem I couldn't figure out.**
		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);

		//after it goes standard query execution and result processing which can
		// be found in almost any Jena/SPARQL tutorial.
		try {
		    ResultSet results = qexec.execSelect();
		    for (; results.hasNext();) {

		    // Result processing is done here.
		    }
		}
		finally {
		   qexec.close();
		}

	}
}