package ciss.in.jena;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import ciss.in.Application;
import ciss.in.jena.owl.Schemaorg;
import ciss.in.models.User;
 
public class RDFStream extends Object {
 
	public OutputStream createRDF(User userNow) {
		
		// create an empty model
		Model model = ModelFactory.createDefaultModel();

		model.setNsPrefix("schemaorg", "http://schema.org/");

    	String personURL    = "http://" + Application.xmppConfig.getDomainHost() + "/" + userNow.getUsername();

    	Resource talha = model.createResource(personURL, Schemaorg.Person);
    	
    	Property name = model.createProperty("http://schema.org/Person/name");
        Property email = model.createProperty("http://schema.org/Person/email");

        talha.addProperty(name, userNow.getUsername());
        talha.addProperty(email,  userNow.getEmail());
        
    	Property makesOffer = model.createProperty("http://schema.org/Person/makesOffer");
        Property price = model.createProperty("http://schema.org/Offer/price");
        Property priceCurrency = model.createProperty("http://schema.org/Offer/priceCurrency");
        Property itemOfferedName = model.createProperty("http://schema.org/Product/name");
        Property itemOfferedCategory = model.createProperty("http://schema.org/Product/category");
        Property itemOfferedImage = model.createProperty("http://schema.org/Product/image");
    	
    	Property seeks = model.createProperty("http://schema.org/Person/seeks");

    	Resource buyDemandSpec = model.createResource(Schemaorg.PriceSpecification);
    	Resource wantDemandSpec = model.createResource(Schemaorg.PriceSpecification);
    	Resource x2freeDemandSpec = model.createResource(Schemaorg.PriceSpecification);
    	Resource torent1DemandSpec = model.createResource(Schemaorg.PriceSpecification);
    	Resource torent2DemandSpec = model.createResource(Schemaorg.PriceSpecification);
    	Resource toown1DemandSpec = model.createResource(Schemaorg.PriceSpecification);
    	Resource toown2DemandSpec = model.createResource(Schemaorg.PriceSpecification);
        
        Property demandprice = model.createProperty("http://schema.org/PriceSpecification/price");
        Property demandpriceCurrency = model.createProperty("http://schema.org/PriceSpecification/priceCurrency");
        
    	Resource sellOffer = model.createResource(Schemaorg.Offer);
        Resource x1freeOffer = model.createResource(Schemaorg.Offer);
        Resource lendOffer = model.createResource(Schemaorg.Offer);
        Resource giveOffer = model.createResource(Schemaorg.Offer);
        Resource rentOffer = model.createResource(Schemaorg.Offer);
        Resource buyDemand = model.createResource(Schemaorg.Demand);
        Resource wantDemand = model.createResource(Schemaorg.Demand);
        Resource x2freeDemand = model.createResource(Schemaorg.Demand);
        Resource torent1Demand = model.createResource(Schemaorg.Demand);
        Resource torent2Demand = model.createResource(Schemaorg.Demand);
        Resource toown1Demand = model.createResource(Schemaorg.Demand);
        Resource toown2Demand = model.createResource(Schemaorg.Demand);

    	sellOffer.addProperty(price, (String) userNow.getMakesOffer().get(0).getPrice());
    	sellOffer.addProperty(priceCurrency, (String) userNow.getMakesOffer().get(0).getPriceCurrency());
    	x1freeOffer.addProperty(price, (String) "");
    	x1freeOffer.addProperty(priceCurrency, (String) "");
    	lendOffer.addProperty(price, (String) "");
    	lendOffer.addProperty(priceCurrency, (String) "");
    	giveOffer.addProperty(price, (String) "");
    	giveOffer.addProperty(priceCurrency, (String) "");
    	rentOffer.addProperty(price, (String) userNow.getMakesOffer().get(4).getPrice());
    	rentOffer.addProperty(priceCurrency, (String) userNow.getMakesOffer().get(4).getPriceCurrency());
    	
    	talha.addProperty(makesOffer, sellOffer);
    	talha.addProperty(makesOffer, x1freeOffer);
    	talha.addProperty(makesOffer, lendOffer);
    	talha.addProperty(makesOffer, giveOffer);
    	talha.addProperty(makesOffer, rentOffer);
    	talha.addProperty(seeks, buyDemand);
    	talha.addProperty(seeks, wantDemand);
    	talha.addProperty(seeks, x2freeDemand);
    	talha.addProperty(seeks, torent1Demand);
    	talha.addProperty(seeks, torent2Demand);
    	talha.addProperty(seeks, toown1Demand);
    	talha.addProperty(seeks, toown2Demand);
    	
    	Property sellitemOffered = model.createProperty("http://schema.org/Product/itemOffered");
    	Property x1freeitemOffered = model.createProperty("http://schema.org/Product/itemOffered");
    	Property lenditemOffered = model.createProperty("http://schema.org/Product/itemOffered");
    	Property giveitemOffered = model.createProperty("http://schema.org/Product/itemOffered");
    	Property rentitemOffered = model.createProperty("http://schema.org/Product/itemOffered");
    	Property buyitemOffered = model.createProperty("http://schema.org/Product/itemOffered");
    	Property wantitemOffered = model.createProperty("http://schema.org/Product/itemOffered");
    	Property x2freeitemOffered = model.createProperty("http://schema.org/Product/itemOffered");
    	Property torent1itemOffered = model.createProperty("http://schema.org/Product/itemOffered");
    	Property torent2itemOffered = model.createProperty("http://schema.org/Product/itemOffered");
    	Property toown1itemOffered = model.createProperty("http://schema.org/Product/itemOffered");
    	Property toown2itemOffered = model.createProperty("http://schema.org/Product/itemOffered");

    	Property buyitemPriceSpec = model.createProperty("http://schema.org/Demand/priceSpecification");
    	Property wantitemPriceSpec = model.createProperty("http://schema.org/Demand/priceSpecification");
    	Property x2freeitemPriceSpec = model.createProperty("http://schema.org/Demand/priceSpecification");
    	Property torent1itemPriceSpec = model.createProperty("http://schema.org/Demand/priceSpecification");
    	Property torent2itemPriceSpec = model.createProperty("http://schema.org/Demand/priceSpecification");
    	Property toown1itemPriceSpec = model.createProperty("http://schema.org/Demand/priceSpecification");
    	Property toown2itemPriceSpec = model.createProperty("http://schema.org/Demand/priceSpecification");

    	Resource sellProduct = model.createResource(Schemaorg.Product);
        Resource x1freeProduct = model.createResource(Schemaorg.Product);
        Resource lendProduct = model.createResource(Schemaorg.Product);
        Resource giveProduct = model.createResource(Schemaorg.Product);
        Resource rentProduct = model.createResource(Schemaorg.Product);
        Resource buyProduct = model.createResource(Schemaorg.Product);
        Resource wantProduct = model.createResource(Schemaorg.Product);
        Resource x2freeProduct = model.createResource(Schemaorg.Product);
        Resource torent1Product = model.createResource(Schemaorg.Product);
        Resource torent2Product = model.createResource(Schemaorg.Product);
        Resource toown1Product = model.createResource(Schemaorg.Product);
        Resource toown2Product = model.createResource(Schemaorg.Product);

        sellProduct.addProperty(itemOfferedName, (String) userNow.getMakesOffer().get(0).getItemOffered().getName());
        sellProduct.addProperty(itemOfferedCategory, (String) userNow.getMakesOffer().get(0).getItemOffered().getCategory());
        sellProduct.addProperty(itemOfferedImage, userNow.getMakesOffer().get(0).getItemOffered().getImage().getHref());
        sellOffer.addProperty(sellitemOffered, sellProduct);    	

        x1freeProduct.addProperty(itemOfferedName, (String) userNow.getMakesOffer().get(1).getItemOffered().getName());
        x1freeProduct.addProperty(itemOfferedCategory, (String) userNow.getMakesOffer().get(1).getItemOffered().getCategory());
        x1freeProduct.addProperty(itemOfferedImage, userNow.getMakesOffer().get(1).getItemOffered().getImage().getHref());
        x1freeOffer.addProperty(x1freeitemOffered, x1freeProduct);    	

        lendProduct.addProperty(itemOfferedName, (String) userNow.getMakesOffer().get(2).getItemOffered().getName());
        lendProduct.addProperty(itemOfferedCategory, (String) userNow.getMakesOffer().get(2).getItemOffered().getCategory());
        lendProduct.addProperty(itemOfferedImage, userNow.getMakesOffer().get(2).getItemOffered().getImage().getHref());
        lendOffer.addProperty(lenditemOffered, lendProduct);    	
        
        giveProduct.addProperty(itemOfferedName, (String) userNow.getMakesOffer().get(3).getItemOffered().getName());
        giveProduct.addProperty(itemOfferedCategory, (String) userNow.getMakesOffer().get(3).getItemOffered().getCategory());
        giveProduct.addProperty(itemOfferedImage, userNow.getMakesOffer().get(3).getItemOffered().getImage().getHref());
        giveOffer.addProperty(giveitemOffered, giveProduct);    	
        
        rentProduct.addProperty(itemOfferedName, (String) userNow.getMakesOffer().get(4).getItemOffered().getName());
        rentProduct.addProperty(itemOfferedCategory, (String) userNow.getMakesOffer().get(4).getItemOffered().getCategory());
        rentProduct.addProperty(itemOfferedImage, userNow.getMakesOffer().get(4).getItemOffered().getImage().getHref());
        rentOffer.addProperty(rentitemOffered, rentProduct);    	
        
    	buyDemandSpec.addProperty(demandprice, (String) userNow.getSeeks().get(0).getPriceSpecification().getPrice());
    	buyDemandSpec.addProperty(demandpriceCurrency, (String) userNow.getSeeks().get(0).getPriceSpecification().getPriceCurrency());
    	buyProduct.addProperty(itemOfferedName, (String) userNow.getSeeks().get(0).getItemOffered().getName());
    	buyProduct.addProperty(itemOfferedCategory, (String) userNow.getSeeks().get(0).getItemOffered().getCategory());
    	buyProduct.addProperty(itemOfferedImage, (String) "");
    	buyDemand.addProperty(buyitemPriceSpec, buyDemandSpec);    	
    	buyDemand.addProperty(buyitemOffered, buyProduct);    	

    	wantDemandSpec.addProperty(demandprice, (String) "");
    	wantDemandSpec.addProperty(demandpriceCurrency, (String) "");
    	wantProduct.addProperty(itemOfferedName, (String) userNow.getSeeks().get(1).getItemOffered().getName());
    	wantProduct.addProperty(itemOfferedCategory, (String) userNow.getSeeks().get(1).getItemOffered().getCategory());
    	wantProduct.addProperty(itemOfferedImage, (String) "");
    	wantDemand.addProperty(wantitemPriceSpec, wantDemandSpec);    	
    	wantDemand.addProperty(wantitemOffered, wantProduct);    	

    	x2freeDemandSpec.addProperty(demandprice, (String) "");
    	x2freeDemandSpec.addProperty(demandpriceCurrency, (String) "");
    	x2freeProduct.addProperty(itemOfferedName, (String) userNow.getSeeks().get(2).getItemOffered().getName());
    	x2freeProduct.addProperty(itemOfferedCategory, (String) userNow.getSeeks().get(2).getItemOffered().getCategory());
    	x2freeProduct.addProperty(itemOfferedImage, (String) userNow.getSeeks().get(2).getItemOffered().getImage().getHref());
    	x2freeDemand.addProperty(x2freeitemPriceSpec, x2freeDemandSpec);    	
    	x2freeDemand.addProperty(x2freeitemOffered, x2freeProduct);    	

    	torent1DemandSpec.addProperty(demandprice, (String) "");
    	torent1DemandSpec.addProperty(demandpriceCurrency, (String) "");
    	torent1Product.addProperty(itemOfferedName, (String) "");
    	torent1Product.addProperty(itemOfferedCategory, (String) userNow.getSeeks().get(3).getItemOffered().getCategory());
    	torent1Product.addProperty(itemOfferedImage, (String) "");
    	torent1Demand.addProperty(torent1itemPriceSpec, torent1DemandSpec);    	
    	torent1Demand.addProperty(torent1itemOffered, torent1Product);    	

    	torent2DemandSpec.addProperty(demandprice, (String) "");
    	torent2DemandSpec.addProperty(demandpriceCurrency, (String) "");
    	torent2Product.addProperty(itemOfferedName, (String) "");
    	torent2Product.addProperty(itemOfferedCategory, (String) userNow.getSeeks().get(4).getItemOffered().getCategory());
    	torent2Product.addProperty(itemOfferedImage, (String) "");
    	torent2Demand.addProperty(torent2itemPriceSpec, torent2DemandSpec);    	
    	torent2Demand.addProperty(torent2itemOffered, torent2Product);    	

    	toown1DemandSpec.addProperty(demandprice, (String) "");
    	toown1DemandSpec.addProperty(demandpriceCurrency, (String) "");
    	toown1Product.addProperty(itemOfferedName, (String) "");
    	toown1Product.addProperty(itemOfferedCategory, (String) userNow.getSeeks().get(5).getItemOffered().getCategory());
    	toown1Product.addProperty(itemOfferedImage, (String) "");
    	toown1Demand.addProperty(toown1itemPriceSpec, toown1DemandSpec);    	
    	toown1Demand.addProperty(toown1itemOffered, toown1Product);    	

    	toown2DemandSpec.addProperty(demandprice, (String) "");
    	toown2DemandSpec.addProperty(demandpriceCurrency, (String) "");
    	toown2Product.addProperty(itemOfferedName, (String) "");
    	toown2Product.addProperty(itemOfferedCategory, (String) userNow.getSeeks().get(6).getItemOffered().getCategory());
    	toown2Product.addProperty(itemOfferedImage, (String) "");
    	toown2Demand.addProperty(toown2itemPriceSpec, toown2DemandSpec);    	
    	toown2Demand.addProperty(toown2itemOffered, toown2Product);    	

    	// write the model in RDF/XML
		 //model.write(System.out, "RDF/XML");
		 OutputStream out = new ByteArrayOutputStream();
		 
		 RDFDataMgr.write(out, model, Lang.RDFXML);
		 //System.out.println(out.toString());
		 return out;
	}
}