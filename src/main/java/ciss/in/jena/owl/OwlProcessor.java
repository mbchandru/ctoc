package ciss.in.jena.owl;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

public class OwlProcessor {
	OntModel model;
	URL url;
	
	public OwlProcessor() {
		model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        url = OwlProcessor.class.getResource("all.rdf");
        model.read(url.toString(), "RDF/XML");
	}
	
	public void readPersonSchemaOrgFromOwl() {		
		try {
	    	Resource personRes = Schemaorg.Person;
	    	String urlperson = personRes.getURI();
	    	
	        OntClass person = model.getOntClass(urlperson);
	        Iterator<OntProperty> props = person.listDeclaredProperties();
	        while (props.hasNext()) {
	            OntProperty p = props.next();
	            if (p.isDatatypeProperty())
	            	System.out.println("p:" + p.getLocalName());
	            else
	            	System.out.println("pURI " + p.getLocalName());		            	
	        }
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public void readProductSchemaOrgFromOwl() {
		try {
	    	Resource productRes = Schemaorg.Product;
	    	String urlproduct = productRes.getURI();

	    	OntClass product = model.getOntClass(urlproduct);
	        Iterator<OntProperty> props = product.listDeclaredProperties();
	        while (props.hasNext()) {
	            OntProperty p = props.next();
            	//System.out.println("p:" + p.getLocalName());
	            if (p.isDatatypeProperty())
	            	System.out.println("p:" + p.getLocalName());
	            else
	            	System.out.println("pURI " + p.getLocalName());		            	
	        }
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public void readLocalBusinessSchemaOrgFromOwl() {
		try {
	    	Resource localBusi = Schemaorg.LocalBusiness;
	    	String urlBusi = localBusi.getURI();
	    	
	        OntClass localbusiness = model.getOntClass(urlBusi);	        
	        Iterator<OntProperty> props1 = localbusiness.listDeclaredProperties(true);
	        Map<String, Class<?>> classProperties = null;
	        while (props1.hasNext()) {
	            OntProperty p = props1.next();
            	System.out.println("pdirect: " + p.getLocalName());
        		classProperties = new HashMap<String, Class<?>>();
        		classProperties.put(p.getLocalName(), String.class);
	        }

	        Iterator<OntClass> i = localbusiness.listSuperClasses();
	        localbusiness.listSubClasses();
	        Map<String, Class<?>> superClassProperties;
	        while (i.hasNext()) {
	        	OntClass c = i.next();
		        String superClassName = c.getLocalName();

		        System.out.println("class: " + superClassName);
	        	Iterator<OntProperty> props = c.listDeclaredProperties(true);
		        while (props.hasNext()) {
		            OntProperty p = props.next();
	            	System.out.println("p: " + p.getLocalName());
	            	superClassProperties = new HashMap<String, Class<?>>();
	            	superClassProperties.put(p.getLocalName(), String.class);
		        }
	        }
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public Map<String, Class<?>> createProperties() {
		Map<String, Class<?>> props = new HashMap<String, Class<?>>();
		props.put("bar", String.class);
		return props;
	}	
}