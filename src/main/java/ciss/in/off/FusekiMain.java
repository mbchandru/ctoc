package ciss.in.off;

//import ciss.in.jena.owl.OwlProcessor;
//import ciss.in.models.UserInfo;

public class FusekiMain {
	static String serviceURI = "http://localhost:3030/foaf";
	static String query = "SELECT ?x WHERE { ?x  <http://www.w3.org/2001/vcard-rdf/3.0#FN>  \"Chandramouleeswaran\" }";
	
	
    static String updateString = 
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
            "SELECT ?name WHERE { " +
            "    ?person foaf:name ?name . " +
            "}";
    
/*    public static void main(String[] args) {

    	UserInfo newMsg = new UserInfo();
    	newMsg.setFamily_name("College");
    	OwlProcessor owlprocess = new OwlProcessor();
    	//owlprocess.readPersonSchemaOrgFromOwl();
    	//owlprocess.readProductSchemaOrgFromOwl();
    	owlprocess.readLocalBusinessSchemaOrgFromOwl();
    }*/
}