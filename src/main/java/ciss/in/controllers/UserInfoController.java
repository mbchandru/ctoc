package ciss.in.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/*import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import ciss.in.xmpp.template.XmppUser;
*/
import org.schema.base.URL;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import javax.validation.Valid;

import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.schema.PriceSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.apache.camel.CamelContext;

//import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;

import ciss.in.Application;
import ciss.in.jena.RDFStream;
import ciss.in.models.Demand;
import ciss.in.models.EmailInfo;
import ciss.in.models.Offer;
import ciss.in.models.Person;
import ciss.in.models.Product;
import ciss.in.models.User;
import ciss.in.repositories.UserRepository;
import ciss.in.security.ReCaptchaResponseVerfier;
import ciss.in.service.UserService;
import ciss.in.utils.MailService;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class UserInfoController extends WebMvcConfigurerAdapter {
	@Autowired
	UserRepository userRepository;

	@Autowired
	CamelContext camelContext;
	
	static final Logger logger = LoggerFactory.getLogger(UserInfoController.class);
    
	String service;

	String prefix = 
    		
			"Prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + 
			"Prefix schemaorg: <http://schema.org/> " + 
			"Prefix j.1:   <http://schema.org/Person/> " + 
			"Prefix j.0:   <http://schema.org/Demand/> " + 
			"Prefix j.3:   <http://schema.org/Offer/> " + 
			"Prefix j.2:   <http://schema.org/Product/> " + 
			"Prefix j.4:   <http://schema.org/PriceSpecification/> "; 

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/user/register").setViewName("/user/register");
        registry.addViewController("/user/login").setViewName("/user/login");
        registry.addViewController("/user/data").setViewName("/user/data");
        registry.addViewController("/user/account").setViewName("/user/account");
        registry.addViewController("/user/buyorsell").setViewName("/user/buyorsell");
        registry.addViewController("/user/transact").setViewName("/user/transact");
        registry.addViewController("/user/response").setViewName("/user/response");
        registry.addViewController("/user/search").setViewName("/user/search");
        registry.addViewController("/user/search/{searchFor}").setViewName("/user/search/{searchFor}");
        registry.addViewController("/user/wishlist").setViewName("/user/wishlist");
        //registry.addViewController("/mails/mailSimple").setViewName("/mails/mailSimple");
    }
 
    @RequestMapping(value="/user/wishlist", method=RequestMethod.GET)
    public String showWishes(HttpServletRequest httpServletRequest,Model model) {
    	
		HttpSession session = httpServletRequest.getSession();
    	String username = (String) session.getAttribute("username");
		UserService userservice = new UserService(userRepository);    	    	    	
		ciss.in.models.User loggedInUser = userservice.read(username);		

    	ArrayList<String> transactionTypes = new ArrayList<String>();
    	
    	transactionTypes.add(0, "Rent it");
    	transactionTypes.add(1, "To Buy");
		
    	loggedInUser.setTransactionTypes(transactionTypes);

		model.addAttribute("transactions", transactionTypes);
    	String result = "";
		model.addAttribute("result", result);
    	
		model.addAttribute("loggedInUser", loggedInUser);
    	return "/user/wishlist";
	}
    
    @RequestMapping(value="/user/wishlist", method=RequestMethod.POST)
    public String wishProducts(HttpServletRequest httpServletRequest,
    		@ModelAttribute(value="loggedInUser") @Valid User loggedInUser, BindingResult bindingResultUser,
    		@ModelAttribute(value="transactions") @Valid ArrayList<String> transactionTypes, BindingResult bindingResultTransactionTypes,
			@ModelAttribute(value="buyProduct") @Valid Product buyProduct, BindingResult bindingResultBuyProduct,
			@ModelAttribute(value="rentProduct") @Valid Product rentProduct, BindingResult bindingResultRentProduct,

			@ModelAttribute(value="buypricespec") @Valid PriceSpecification buypricespec, BindingResult bindingResultRentBuyPriceSpec,
			@ModelAttribute(value="buydemand") @Valid Demand buydemand, BindingResult bindingResultBuyDemand,
			@ModelAttribute(value="rentdemand") @Valid Demand rentdemand, BindingResult bindingResultRentDemand,

			@ModelAttribute(value="seeks") @Valid ArrayList<Demand> seeks, BindingResult bindingResultRentManyDemand,
			
			@ModelAttribute(value="new1User") @Valid User new1User, BindingResult bindingResultNew1User,
    		
    		Model model) {

    	String result = null;

		HttpSession session = httpServletRequest.getSession();
    	String username = (String) session.getAttribute("username");
    	
		UserService userservice = new UserService(userRepository);    	
    	ciss.in.models.User userNow =  userservice.read(username);

    	
    	if (loggedInUser.getProductCategory().trim().equals("Rent it")) {
	    	if (userNow.getSeeks().get(3).getItemOffered().getName().equals("")) {
	    		userNow.getSeeks().get(3).getItemOffered().setName(loggedInUser.getProductSearch());
	    	}
	    	else if (userNow.getSeeks().get(4).getItemOffered().getName().isEmpty()) {
	    		userNow.getSeeks().get(4).getItemOffered().setName(loggedInUser.getProductSearch());
	    	}
	    	else result = "Can save only two 'To Rent' items";
    	}

    	if (loggedInUser.getProductCategory().trim().equals("To Buy")) {
	    	if (userNow.getSeeks().get(5).getItemOffered().getName().isEmpty()) {
	    		userNow.getSeeks().get(5).getItemOffered().setName(loggedInUser.getProductSearch());
	    	}
	    	else if (userNow.getSeeks().get(6).getItemOffered().getName().isEmpty()) {
	    		userNow.getSeeks().get(6).getItemOffered().setName(loggedInUser.getProductSearch());
	    	}
	    	else result = "Can save only two 'To Own' items";
    	}

		userservice.update(userNow);

		String transactType[] = {"ToRent1","ToRent2","ToOwn1","ToOwn2"};    		

    	Endpoint controller3 = camelContext.getEndpoint("direct:start-user");
    	producerTemplate = camelContext.createProducerTemplate();
    	producerTemplate.setDefaultEndpoint(controller3);

    	String selectClause = "productName";
		String whereClause = "j.2:name";   		
		String newProductNameValue[] = {
	    		"",
	    		"",
	    		"",
	    		"",
	    		"",

	    		"",
	    		"",
	    		"",
	    		userNow.getSeeks().get(3).getItemOffered().getName(),
	    		userNow.getSeeks().get(4).getItemOffered().getName(),
	    		userNow.getSeeks().get(5).getItemOffered().getName(),
	    		userNow.getSeeks().get(6).getItemOffered().getName(),
		};
		
		service = "http://"+ Application.xmppConfig.getFusekiHost() + ":3030/coaf/query";
		for ( int i1 = 0, i2= 8; i1 < transactType.length; i1++, i2++) {
			//int i2 = i1 + 8;
    		String query = update(selectClause, whereClause, username, service, prefix, newProductNameValue[i2], transactType[i1]);
    		//System.out.println("query " + query);
    		producerTemplate.sendBodyAndHeader(query,"proc","update");
		}
		
    	transactionTypes.add(0, "Rent it");
    	transactionTypes.add(1, "To Buy");
		
    	loggedInUser.setTransactionTypes(transactionTypes);

		model.addAttribute("transactions", transactionTypes);
		model.addAttribute("result", result);

		model.addAttribute("loggedInUser", loggedInUser);
    	return "/user/wishlist";
	}
    
    @RequestMapping(value="/user/search", method=RequestMethod.GET)
    public String showSearchPage(Model model) {
    	
    	ciss.in.models.User new1User = new User();
    	ArrayList<String> transactionTypes = new ArrayList<String>();
    	
    	transactionTypes.add(0, "For Sale");
    	transactionTypes.add(1, "To Give away for Free");
    	transactionTypes.add(2, "Lend it for free");
    	transactionTypes.add(3, "Rent it");
    	
    	transactionTypes.add(4, "To Buy");
    	transactionTypes.add(5, "Free Exchange for");
    	transactionTypes.add(6, "To get in Exchange for ");
    	transactionTypes.add(7, "Want it Free");
		
    	new1User.setTransactionTypes(transactionTypes);
    	new1User.setProductCategory("");
		model.addAttribute("new1User", new1User);
		model.addAttribute("transactions", transactionTypes);
    	String result = "";
		model.addAttribute("result", result);

		return "/user/search";
	}

    @Autowired
    private ProducerTemplate producerTemplate;
    
    @Autowired
    private ConsumerTemplate consumerTemplate;
      
    @RequestMapping(value="/user/search", method=RequestMethod.POST)
    public String searchProducts(HttpServletRequest httpServletRequest,
    		@ModelAttribute(value="new1User") @Valid User new1User, BindingResult bindingResultUser,
    		@ModelAttribute(value="transactions") @Valid ArrayList<String> transactionTypes, BindingResult bindingResultTransactionTypes,
    		Model model) throws Exception {
    	
    	transactionTypes = new ArrayList<String>();
    	
    	transactionTypes.add(0, "For Sale");
    	transactionTypes.add(1, "To Give away for Free");
    	transactionTypes.add(2, "Lend it for free");
    	transactionTypes.add(3, "Rent it");
    	
    	transactionTypes.add(4, "To Buy");
    	transactionTypes.add(5, "Free Exchange for");
    	transactionTypes.add(6, "To get in Exchange for ");
    	transactionTypes.add(7, "Want it Free");
    	
    	ArrayList<String> productCategory1 = new ArrayList<String>();
    	productCategory1.add(0, "Sell");
    	productCategory1.add(1, "GiveFree");
    	productCategory1.add(2, "Lend");
    	productCategory1.add(3, "Rent");

    	productCategory1.add(4, "Buy");
    	productCategory1.add(5, "ExchangeGive");
    	productCategory1.add(6, "ExchangeGet");
    	productCategory1.add(7, "WantFree");
    	
    	String searchFor = null;
    	for(int i = 0; i < transactionTypes.size(); i++)
    		if (transactionTypes.get(i).equals(new1User.getProductCategory())) {
    			searchFor = productCategory1.get(i);
    			break;
    		}

		HttpSession session = httpServletRequest.getSession();
    	String username = (String) session.getAttribute("username");

    	String queryString = prefix + 
    	
    			"SELECT ?itemOfferedName " +
    				" WHERE { " +
    					"?x j.1:name ?person . " +
    					"?y j.2:name ?itemOfferedName . " +
    					"?y j.2:category ?category " +
    					"FILTER (?person != " + "\"" + username + "\"" + ") " +
    					"FILTER regex(?category,\"" + searchFor + "\",\"i\"" + ") " +
    					"FILTER regex(?itemOfferedName,\"" + new1User.getProductSearch() + "\",\"i\"" + ") " +
		    		"}";

    	//Search

    	service = "http://"+ Application.xmppConfig.getFusekiHost() + ":3030/coaf/query";
    	Query q= QueryFactory.create(queryString);
    	QueryExecution qexec = QueryExecutionFactory.sparqlService(service, q);
    	ResultSet results = qexec.execSelect();

    	ArrayList<String> productValue = new ArrayList<String>();
    	for (;results.hasNext();) {
	    	QuerySolution binding = results.nextSolution();
	    	RDFNode val = binding.get("itemOfferedName");
	    	productValue.add(val.toString());
    	}
    	if (!productValue.isEmpty()) {
    		String result = "The searched item : " + productValue.get(0) + " is available. An email had been sent to you for further transactions";
    		model.addAttribute("result", result);
    	}
		model.addAttribute("new1User", new1User);
		model.addAttribute("transactions", transactionTypes);
    	return "/user/search";
	}
    
    @RequestMapping(value="/user/account", method=RequestMethod.GET)
    public String showAccount(HttpServletRequest httpServletRequest,Model model) {
    	
		HttpSession session = httpServletRequest.getSession();
    	String username = (String) session.getAttribute("username");
		UserService userservice = new UserService(userRepository);    	    	    	
		ciss.in.models.User loggedInUser = userservice.read(username);		

		model.addAttribute("loggedInUser", loggedInUser);
    	return "/user/account";
	}
    
    @RequestMapping(value="/user/account", method=RequestMethod.POST)
    public String updateAccount(HttpServletRequest httpServletRequest,
    		@ModelAttribute(value="loggedInUser") @Valid User loggedInUser, BindingResult bindingResultUser,
    		Model model) {
    	
		HttpSession session = httpServletRequest.getSession();
    	String username = (String) session.getAttribute("username");
		UserService userservice = new UserService(userRepository);    	    	    	
		ciss.in.models.User updateUser = userservice.read(username);
		updateUser.setGivenName(loggedInUser.getGivenName());
		updateUser.setFamilyName(loggedInUser.getFamilyName());
		updateUser.setTelephone(loggedInUser.getTelephone());

		userservice.update(updateUser);
		
		model.addAttribute("loggedInUser", updateUser);
    	return "/user/account";
	}
    
    @RequestMapping(value="/user/response", method=RequestMethod.GET)
    public String showResponseForm(HttpServletRequest httpServletRequest, Model model) {
    	
		HttpSession session = httpServletRequest.getSession();
    	String username = (String) session.getAttribute("username");
		UserService userservice = new UserService(userRepository);    	    	    	
		ArrayList<ciss.in.models.User> allUsers = userservice.list();		
		ciss.in.models.User loggedInUser = userservice.read(username);		
		
		ArrayList<Offer> makesOffer = new ArrayList<Offer>();

		if (allUsers.size() > 6) {
			allUsers.remove(loggedInUser);
			Random rand = new Random();
			Set<Integer> generated = new LinkedHashSet<Integer>();
			
			while (generated.size() < 7)
			{
				Integer next = rand.nextInt(allUsers.size());
			    generated.add(next);
			}
	
			Integer[] array = generated.toArray(new Integer[generated.size()]);
			
			ArrayList<ciss.in.models.User> SixUsersExceptLoggedIn = new ArrayList<ciss.in.models.User>();
	
			for (int i = 0; i < 6; i++) {
				SixUsersExceptLoggedIn.add(i,allUsers.get(array[i].intValue()));
			}
		
			for (int i = 0; i < 5; i++) {
				makesOffer.add(i, (Offer) SixUsersExceptLoggedIn.get(i).getMakesOffer().get(i));
			}
	    	model.addAttribute("makesOffer", makesOffer);
	    	return "/user/response";
		}
		else {
	    	return "redirect:/";
		}
    }

	@RequestMapping(value="/user/transact", method=RequestMethod.GET)
    public String showTransactForm(HttpServletRequest httpServletRequest, Model model) {

    	Product sellProduct = new Product();
    	Product buyProduct = new Product();
    	Product exchange1Product = new Product();
    	Product exchange2Product = new Product();
    	Product loanProduct = new Product();
    	Product wantfreeProduct = new Product();
    	Product givefreeProduct = new Product();
    	Product rentProduct = new Product();

    	Product torent1Product = new Product();
    	Product torent2Product = new Product();
    	Product toown1Product = new Product();
    	Product toown2Product = new Product();

    	torent1Product.setName("");
    	torent1Product.setCategory("");
    	torent1Product.setImage(new URL().to(""));

    	torent2Product.setName("");
    	torent2Product.setCategory("");
    	torent2Product.setImage(new URL().to(""));

    	toown1Product.setName("");
    	toown1Product.setCategory("");
    	toown1Product.setImage(new URL().to(""));

    	toown2Product.setName("");
    	toown2Product.setCategory("");
    	toown2Product.setImage(new URL().to(""));

    	Offer selloffer = new Offer();
    	Offer exchangeoffer = new Offer();
    	exchangeoffer.setPrice(new Integer(0));
    	Offer loanoffer = new Offer();
    	loanoffer.setPrice(new Integer(0));
    	Offer givefreeoffer = new Offer();
    	givefreeoffer.setPrice(new Integer(0));
    	Offer rentoffer = new Offer();
    	rentoffer.setPrice(new Integer(0));

    	selloffer.setItemOffered(sellProduct);
    	exchangeoffer.setItemOffered(exchange1Product);
    	loanoffer.setItemOffered(loanProduct);
    	givefreeoffer.setItemOffered(givefreeProduct);
    	rentoffer.setItemOffered(rentProduct);

    	ArrayList<Offer> makesOffer = new ArrayList<Offer>();
    	makesOffer.add(0, selloffer);
    	makesOffer.add(1, exchangeoffer);
    	makesOffer.add(2, loanoffer);
    	makesOffer.add(3, givefreeoffer);
    	makesOffer.add(4, rentoffer);
    	
    	PriceSpecification buypricespec = new PriceSpecification();
    	PriceSpecification wantpricespec = new PriceSpecification();
    	wantpricespec.setPrice(new Integer(0));

    	PriceSpecification torent1pricespec = new PriceSpecification();
    	torent1pricespec.setPrice(new Integer(0));
    	PriceSpecification torent2pricespec = new PriceSpecification();
    	torent2pricespec.setPrice(new Integer(0));
    	PriceSpecification toown1pricespec = new PriceSpecification();
    	toown1pricespec.setPrice(new Integer(0));
    	PriceSpecification toown2pricespec = new PriceSpecification();
    	toown2pricespec.setPrice(new Integer(0));

    	Demand exchangedemand = new Demand();
    	exchangedemand.setPriceSpecification(buypricespec);
    	exchangedemand.setItemOffered(exchange2Product);

    	Demand buydemand = new Demand();
    	buydemand.setPriceSpecification(buypricespec);
    	buydemand.setItemOffered(buyProduct);
    	
    	Demand wantfreedemand = new Demand();
    	wantfreedemand.setPriceSpecification(wantpricespec);
    	wantfreedemand.setItemOffered(wantfreeProduct);

    	Demand torent1demand = new Demand();
    	torent1demand.setPriceSpecification(torent1pricespec);
    	torent1demand.setItemOffered(torent1Product);

    	Demand torent2demand = new Demand();
    	torent2demand.setPriceSpecification(torent2pricespec);
    	torent2demand.setItemOffered(torent2Product);

    	Demand toown1demand = new Demand();
    	toown1demand.setPriceSpecification(toown1pricespec);
    	toown1demand.setItemOffered(toown1Product);

    	Demand toown2demand = new Demand();
    	toown2demand.setPriceSpecification(toown2pricespec);
    	toown2demand.setItemOffered(toown2Product);

    	ArrayList<Demand> seeks = new ArrayList<Demand>();
    	
    	seeks.add(0,buydemand);
    	seeks.add(1,exchangedemand);
    	seeks.add(2,wantfreedemand);
    	seeks.add(3,torent1demand);
    	seeks.add(4,torent2demand);
    	seeks.add(5,toown1demand);
    	seeks.add(6,toown2demand);
    	
    	model.addAttribute("sellProduct", sellProduct);
    	model.addAttribute("buyProduct", buyProduct);
    	model.addAttribute("exchange1Product", exchange1Product);
    	model.addAttribute("exchange2Product", exchange2Product);
    	model.addAttribute("loanProduct", loanProduct);
    	model.addAttribute("wantfreeProduct", wantfreeProduct);
    	model.addAttribute("givefreeProduct", givefreeProduct);
    	model.addAttribute("rentProduct", rentProduct);
    	model.addAttribute("torent1Product", torent1Product);
    	model.addAttribute("torent2Product", torent2Product);
    	model.addAttribute("toown1Product", toown1Product);
    	model.addAttribute("toown2Product", toown2Product);
    	
    	model.addAttribute("buypricespec", buypricespec);
    	model.addAttribute("wantpricespec", wantpricespec);
    	model.addAttribute("wantpricespec", wantpricespec);
    	model.addAttribute("torent1pricespec", torent1pricespec);
    	model.addAttribute("torent2pricespec", torent2pricespec);
    	model.addAttribute("toown1pricespec", toown1pricespec);
    	model.addAttribute("toown2pricespec", toown2pricespec);

    	model.addAttribute("buydemand", buydemand);
    	model.addAttribute("selloffer", selloffer);
    	model.addAttribute("exchangeoffer", exchangeoffer);
    	model.addAttribute("loanoffer", loanoffer);
    	model.addAttribute("exchangedemand", exchangedemand);
    	model.addAttribute("wantfreedemand", wantfreedemand);
    	model.addAttribute("torent1demand", torent1demand);
    	model.addAttribute("torent2demand", torent2demand);
    	model.addAttribute("toown1demand", toown1demand);
    	model.addAttribute("toown2demand", toown2demand);
    	model.addAttribute("givefreeoffer", givefreeoffer);
    	model.addAttribute("rentoffer", rentoffer);
    	    	
    	User new1User = new User(); 

    	HttpSession session = httpServletRequest.getSession();
    	String username = (String) session.getAttribute("username");
    	User userNow = userRepository.findByUsername(username);
    	
    	//if(userNow.equals(null)){
    		//new1User.setSeeks(seeks);
    		//new1User.setMakesOffer(makesOffer);
    		//System.out.println(" new1User" + makesOffer.get(0).getItemOffered().getName());
/*    	}
    	else {
*/    		new1User.setSeeks(userNow.getSeeks());
    		new1User.setMakesOffer(userNow.getMakesOffer());
    	//}

    	model.addAttribute("makesOffer", makesOffer);
    	model.addAttribute("seeks", seeks);

    	model.addAttribute("new1User", new1User);
    	
    	ArrayList<MultipartFile> imageFiles = new ArrayList<MultipartFile>();
    	new1User.setImageFiles(imageFiles);
    	model.addAttribute("imageFiles", imageFiles);

    	return "/user/transact";
    }
    
	@RequestMapping(value="/user/transact", method=RequestMethod.POST)
    public String transactUser(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale, Model model,
				@ModelAttribute(value="buyProduct") @Valid Product buyProduct, BindingResult bindingResultBuyProduct,
    			@ModelAttribute(value="sellProduct") @Valid Product sellProduct, BindingResult bindingResultSellProduct,
    			@ModelAttribute(value="exchange1Product") @Valid Product exchange1Product, BindingResult bindingResultExchange1Product,
    			@ModelAttribute(value="exchange2Product") @Valid Product exchange2Product, BindingResult bindingResultExchange2Product,
    			@ModelAttribute(value="loanProduct") @Valid Product loanProduct, BindingResult bindingResultLoanProduct,
    			@ModelAttribute(value="wantfreeProduct") @Valid Product wantfreeProduct, BindingResult bindingResultWantFreeProduct,
    			@ModelAttribute(value="givefreeProduct") @Valid Product givefreeProduct, BindingResult bindingResultGiveFreeProduct,
    			@ModelAttribute(value="rentProduct") @Valid Product rentProduct, BindingResult bindingResultRentProduct,
    			@ModelAttribute(value="torent1Product") @Valid Product torent1Product, BindingResult bindingResultToRent1Product,
    			@ModelAttribute(value="torent2Product") @Valid Product torent2Product, BindingResult bindingResultToRent2Product,
    			@ModelAttribute(value="toown1Product") @Valid Product toown1Product, BindingResult bindingResultToOwn1Product,
    			@ModelAttribute(value="toown2Product") @Valid Product toown2Product, BindingResult bindingResultToOwn2Product,

    			@ModelAttribute(value="buypricespec") @Valid PriceSpecification buypricespec, BindingResult bindingResultRentBuyPriceSpec,
    			@ModelAttribute(value="wantpricespec") @Valid PriceSpecification wantpricespec, BindingResult bindingResultWantPriceSpec,
    			@ModelAttribute(value="torent1pricespec") @Valid PriceSpecification torent1pricespec, BindingResult bindingResultToRent1PriceSpec,
    			@ModelAttribute(value="torent1pricespec") @Valid PriceSpecification torent2pricespec, BindingResult bindingResultToRent2PriceSpec,
    			@ModelAttribute(value="toown1pricespec") @Valid PriceSpecification toown1pricespec, BindingResult bindingResultToOwn1PriceSpec,
    			@ModelAttribute(value="toown2pricespec") @Valid PriceSpecification toown2pricespec, BindingResult bindingResultToOwn2PriceSpec,

    			@ModelAttribute(value="exchangedemand") @Valid Demand exchangedemand, BindingResult bindingResultExchangeDemand,
    			@ModelAttribute(value="buydemand") @Valid Demand buydemand, BindingResult bindingResultBuyDemand,
    			@ModelAttribute(value="selloffer") @Valid Offer selloffer, BindingResult bindingResultSellOffer,
    			@ModelAttribute(value="exchangeoffer") @Valid Offer exchangeoffer, BindingResult bindingResultExchangeOffer,
    			@ModelAttribute(value="loanoffer") @Valid Offer loanoffer, BindingResult bindingResultLoanOffer,
    			@ModelAttribute(value="wantfreedemand") @Valid Demand wantfreedemand, BindingResult bindingResultWantFreeDemand,
    			@ModelAttribute(value="torent1demand") @Valid Demand torent1demand, BindingResult bindingResultRent1Demand,
    			@ModelAttribute(value="torent2demand") @Valid Demand torent2demand, BindingResult bindingResultRent2Demand,
    			@ModelAttribute(value="toown1demand") @Valid Demand toown1demand, BindingResult bindingResultOwn1Demand,
    			@ModelAttribute(value="toown2demand") @Valid Demand toown2demand, BindingResult bindingResultOwn2Demand,
    			@ModelAttribute(value="givefreeoffer") @Valid Offer givefreeoffer, BindingResult bindingResultGiveFreeOffer,
    			@ModelAttribute(value="rentoffer") @Valid Offer rentoffer, BindingResult bindingResultRentOffer,

    			@ModelAttribute(value="makesOffer") @Valid ArrayList<Offer> makesOffer, BindingResult bindingResultRentManyOffer,
    			@ModelAttribute(value="seeks") @Valid ArrayList<Demand> seeks, BindingResult bindingResultRentManyDemand,
    			
    			@RequestParam("imageFiles") ArrayList<MultipartFile> imageFiles, BindingResult bindingResultImageFiles,
    			@ModelAttribute(value="new1User") @Valid User new1User, BindingResult bindingResultNew1User) throws MessagingException, IOException
	{
		if (bindingResultImageFiles.hasErrors()) {
			for (int i = 0; i < imageFiles.size(); i ++) {
				MultipartFile singleImage = imageFiles.get(i);
				BufferedImage image = ImageIO.read(singleImage.getInputStream());
				Integer width = image.getWidth();
				Integer height = image.getHeight();
				if (width.intValue() < 180 || height.intValue() < 180) {
					model.addAttribute("WelcomeMessage","Upload image files of 180px width and 180px height");      	
			    	return "/user/transact";
				}
			}
		}
		service= "http://"+ Application.xmppConfig.getFusekiHost() + ":3030/coaf/query";
		HttpSession session = httpServletRequest.getSession();
    	String username = (String) session.getAttribute("username");

		UserService userservice = new UserService(userRepository);    	
    	ciss.in.models.User userNow =  userservice.read(username);

		userNow.setSeeks(new1User.getSeeks());
		userNow.setMakesOffer(new1User.getMakesOffer());
		
		userNow.getMakesOffer().get(0).getItemOffered().setImage(new URL().to(""));
		userNow.getMakesOffer().get(1).getItemOffered().setImage(new URL().to(""));
		userNow.getMakesOffer().get(2).getItemOffered().setImage(new URL().to(""));
		userNow.getMakesOffer().get(3).getItemOffered().setImage(new URL().to(""));
		userNow.getMakesOffer().get(4).getItemOffered().setImage(new URL().to(""));
		userNow.getSeeks().get(0).getItemOffered().setImage(new URL().to(""));
		userNow.getSeeks().get(1).getItemOffered().setImage(new URL().to(""));
		userNow.getSeeks().get(2).getItemOffered().setImage(new URL().to(""));

    	torent1Product.setName("");
    	torent1Product.setCategory("ToRent1");
    	torent1Product.setImage(new URL().to(""));

    	torent2Product.setName("");
    	torent2Product.setCategory("ToRent2");
    	torent2Product.setImage(new URL().to(""));

    	toown1Product.setName("");
    	toown1Product.setCategory("ToOwn1");
    	toown1Product.setImage(new URL().to(""));

    	toown2Product.setName("");
    	toown2Product.setCategory("ToOwn2");
    	toown2Product.setImage(new URL().to(""));
    	
    	torent1pricespec.setPrice(new Integer(0));
    	torent2pricespec.setPrice(new Integer(0));
    	toown1pricespec.setPrice(new Integer(0));
    	toown2pricespec.setPrice(new Integer(0));
    	
		torent1demand.setItemOffered(torent1Product);
		torent2demand.setItemOffered(torent2Product);
		toown1demand.setItemOffered(toown1Product);
		toown2demand.setItemOffered(toown2Product);
		torent1demand.setPriceSpecification(torent1pricespec);
		torent2demand.setPriceSpecification(torent2pricespec);
		toown1demand.setPriceSpecification(toown1pricespec);
		toown2demand.setPriceSpecification(toown2pricespec);
		userNow.getSeeks().add(3, torent1demand);
		userNow.getSeeks().add(4, torent2demand);
		userNow.getSeeks().add(5, toown1demand);
		userNow.getSeeks().add(6, toown2demand);
    	
		//Index right
		userNow.getMakesOffer().get(0).getItemOffered().setCategory("Sell");
		userNow.getMakesOffer().get(1).getItemOffered().setCategory("ExchangeGive");
		userNow.getMakesOffer().get(2).getItemOffered().setCategory("Lend");
		userNow.getMakesOffer().get(3).getItemOffered().setCategory("GiveFree");
		userNow.getMakesOffer().get(4).getItemOffered().setCategory("Rent");

		userNow.getSeeks().get(0).getItemOffered().setCategory("Buy");
		userNow.getSeeks().get(1).getItemOffered().setCategory("WantFree");
		userNow.getSeeks().get(2).getItemOffered().setCategory("ExchangeGet");
		userNow.getSeeks().get(3).getItemOffered().setCategory("ToRent1");
		userNow.getSeeks().get(4).getItemOffered().setCategory("ToRent2");
		userNow.getSeeks().get(5).getItemOffered().setCategory("ToOwn1");
		userNow.getSeeks().get(6).getItemOffered().setCategory("ToOwn2");

    	PriceSpecification pricespec = new PriceSpecification();
    	pricespec.setPrice(new Integer(0));
    	
		userNow.getSeeks().get(1).setPriceSpecification(pricespec);
		userNow.getSeeks().get(2).setPriceSpecification(pricespec);
		
		List<MultipartFile> filesForUpload = imageFiles;
		String envVar = null;
		envVar = System.getenv("OPENSHIFT_DATA_DIR");
		if (envVar == null)
			envVar = "/";
		
		String relativeLoadPath = envVar;
		String absoluteLoadPath = httpServletRequest.getServletContext().getRealPath(relativeLoadPath);
		httpServletRequest.setAttribute("javax.servlet.context.tempdir", absoluteLoadPath);
		httpServletRequest.getServletContext().getAttribute("javax.servlet.context.tempdir");
		
		String relativeWebPath = "/store/";
		String absoluteFilePath = httpServletRequest.getServletContext().getRealPath(relativeWebPath);
		File theFile = new File(absoluteFilePath + "/" + username);
        theFile.mkdirs();

		int i = 0;
		
        if (null != filesForUpload && filesForUpload.size() > 0) {
            for (MultipartFile multipartFile : filesForUpload) {
 
                String fileName = multipartFile.getOriginalFilename();
                String filePath;
                File imageFilePath;
                File image;
                if (!"".equalsIgnoreCase(fileName)) {
                    // Handle file content - multipartFile.getInputStream()
                    try {
                    	String domainHost = Application.xmppConfig.getDomainHost();
                    	switch (i) {
                    	case 0:
                    		filePath  = theFile.getAbsolutePath() + "/exchange1/";
                    		imageFilePath = new File(filePath);
                    		imageFilePath.mkdirs();
                    		image = new File(imageFilePath.getAbsolutePath() + "/" + fileName);
    						multipartFile.transferTo(image);
    						userNow.getMakesOffer().get(1).getItemOffered().setImage(new URL().to("http://" + domainHost + "/store/" + username + "/exchange1/" + fileName));
                    		break;
                    	case 1: 
                    		filePath  = theFile.getAbsolutePath() + "/exchange2/";
                    		imageFilePath = new File(filePath);
                    		imageFilePath.mkdirs();
                    		image = new File(imageFilePath.getAbsolutePath() + "/" + fileName);
    						multipartFile.transferTo(image);
    						userNow.getSeeks().get(2).getItemOffered().setImage(new URL().to("http://" + domainHost + "/store/" + username + "/exchange2/" + fileName));
                    		break;
                    	case 2: 
                    		filePath  = theFile.getAbsolutePath() + "/sell/";
                    		imageFilePath = new File(filePath);
                    		imageFilePath.mkdirs();
                    		image = new File(imageFilePath.getAbsolutePath() + "/" + fileName);
    						multipartFile.transferTo(image);
    						userNow.getMakesOffer().get(0).getItemOffered().setImage(new URL().to("http://" + domainHost + "/store/" + username + "/sell/" + fileName));
                    		break;
                    	case 3: 
                    		filePath  = theFile.getAbsolutePath() + "/loan/";
                    		imageFilePath = new File(filePath);
                    		imageFilePath.mkdirs();
                    		image = new File(imageFilePath.getAbsolutePath() + "/" + fileName);
    						multipartFile.transferTo(image);
    						userNow.getMakesOffer().get(2).getItemOffered().setImage(new URL().to("http://" + domainHost + "/store/" + username + "/loan/" + fileName));
                    		break;
                    	case 4: 
                    		filePath  = theFile.getAbsolutePath() + "/give/";
                    		imageFilePath = new File(filePath);
                    		imageFilePath.mkdirs();
                    		image = new File(imageFilePath.getAbsolutePath() + "/" + fileName);
    						multipartFile.transferTo(image);
    						userNow.getMakesOffer().get(3).getItemOffered().setImage(new URL().to("http://" + domainHost + "/store/" + username + "/give/" + fileName));
                    		break;
                    	case 5: 
                    		filePath  = theFile.getAbsolutePath() + "/rent/";
                    		imageFilePath = new File(filePath);
                    		imageFilePath.mkdirs();
                    		image = new File(imageFilePath.getAbsolutePath() + "/" + fileName);
    						multipartFile.transferTo(image);
    						userNow.getMakesOffer().get(4).getItemOffered().setImage(new URL().to("http://" + domainHost + "/store/" + username + "/rent/" + fileName));
                    		break;
                    	}
					} catch (IllegalStateException | IOException e) {
						e.printStackTrace();
					}
                }
                i++;
            }
        }

		//buyProduct.setImage(new URL("http://" + domainHost + "/store/" + username + "buy/" + fileName));
    	//wantfreeProduct.setImage(new URL("http://" + domainHost + "/store/" + username + "want/" + fileName));
    			
        userservice.update(userNow);
        
        MailService mailserve = new MailService(javaMailSender);
        ServletContext context = httpServletRequest.getServletContext();

        ArrayList<String> products = new ArrayList<String>();
        ArrayList<String> transactionTypes = new ArrayList<String>();
        for (int i1 = 0; i1 < userNow.getMakesOffer().size(); i1 ++) {
        	products.add(i1, userNow.getMakesOffer().get(i1).getItemOffered().getName());
        	transactionTypes.add(i1, userNow.getMakesOffer().get(i1).getItemOffered().getCategory());
        }
        for (int i2 = 0; i2 < userNow.getSeeks().size(); i2 ++) {
        	products.add(i2, userNow.getSeeks().get(i2).getItemOffered().getName());
        	transactionTypes.add(i2, userNow.getSeeks().get(i2).getItemOffered().getCategory());
        }

        String[][] productTransactions = new String[21][21];
        for (int i3 = 0,i4 = 0; i3 < transactionTypes.size(); i3++) {
        		productTransactions[i3][i4] = transactionTypes.get(i3);
        }
        for (int i31 = 0, i5 = 1; i31 < products.size(); i31++) {
        	productTransactions[i31][i5] = products.get(i31);
        }
        EmailInfo emailForm = new EmailInfo();

    	emailForm.setProducts(products);
    	emailForm.setTransactionTypes(transactionTypes);
    	emailForm.setProductTransactions(productTransactions);
        mailserve.emailTemplate(emailForm, httpServletRequest, httpServletResponse, context, new Locale("en", "US"), "Chandra", "mbchandru@gmail.com");

    	RDFStream rdf = new RDFStream();
    	ByteArrayOutputStream out = (ByteArrayOutputStream) rdf.createRDF(userNow);

    	camelContext.getProperties().put(Exchange.LOG_DEBUG_BODY_MAX_CHARS, "6000");
		ProducerTemplate producerTemplate = camelContext.createProducerTemplate();

 /*   	Endpoint controller2 = camelContext.getEndpoint("direct:channel2");
    	producerTemplate.setDefaultEndpoint(controller2);
    	producerTemplate.sendBodyAndHeader(out, KafkaConstants.PARTITION_KEY, 1);*/
    	
    	Endpoint controller3 = camelContext.getEndpoint("direct:start-user");
    	producerTemplate = camelContext.createProducerTemplate();
    	producerTemplate.setDefaultEndpoint(controller3);

    	//Search
    	String queryString = prefix +
				
    			"SELECT ?person" +
    				" WHERE { " +
    					"?person j.1:name" + "\"" + username.toString() + "\"" +
		    		"}";

    	Query q= QueryFactory.create(queryString);
    	QueryExecution qexec = QueryExecutionFactory.sparqlService(service, q);
    	ResultSet results = qexec.execSelect();
    		
		if (!results.hasNext()) {
			producerTemplate.sendBodyAndHeader(out.toString(),"proc","load");
		}
        else {   		
    				
String transactType[] = {"Sell","ExchangeGive","Lend","GiveFree","Rent","Buy","WantFree","ExchangeGet"};    		

    		String selectClause = "productName";
    		String whereClause = "j.2:name";   		
    		String newProductNameValue[] = {
    	    		userNow.getMakesOffer().get(0).getItemOffered().getName(),
    	    		userNow.getMakesOffer().get(1).getItemOffered().getName(),
    	    		userNow.getMakesOffer().get(2).getItemOffered().getName(),
    	    		userNow.getMakesOffer().get(3).getItemOffered().getName(),
    	    		userNow.getMakesOffer().get(4).getItemOffered().getName(),

    	    		userNow.getSeeks().get(0).getItemOffered().getName(),
    	    		userNow.getSeeks().get(1).getItemOffered().getName(),
    	    		userNow.getSeeks().get(2).getItemOffered().getName()
    		};
    		
    		for ( int i1 = 0; i1 < transactType.length; i1++) {
	    		String query = update(selectClause, whereClause, username, service, prefix, newProductNameValue[i1], transactType[i1]);
	    		//System.out.println("query " + query);
	    		producerTemplate.sendBodyAndHeader(query,"proc","update");
    		}

    		selectClause = "productImageName";
    		whereClause = "j.2:image";   		
    		String newProductImageNameValue[] = {
    	    		userNow.getMakesOffer().get(0).getItemOffered().getImage().getHref(),
    	    		userNow.getMakesOffer().get(1).getItemOffered().getImage().getHref(),
    	    		userNow.getMakesOffer().get(2).getItemOffered().getImage().getHref(),
    	    		userNow.getMakesOffer().get(3).getItemOffered().getImage().getHref(),
    	    		userNow.getMakesOffer().get(4).getItemOffered().getImage().getHref(),
    	    		"",
    	    		"",
    	    		userNow.getSeeks().get(2).getItemOffered().getImage().getHref()
    		};
    		
    		for ( int i1 = 0; i1 < transactType.length; i1++) {
	    		String query = update(selectClause, whereClause, username, service, prefix, newProductImageNameValue[i1], transactType[i1]);
	    		//System.out.println("query " + query);
	    		producerTemplate.sendBodyAndHeader(query,"proc","update");
    		}
        
    		selectClause = "productPrice";
    		whereClause = "j.3:price";   		

    		String newProductPriceValue[] = {
    				(String) userNow.getMakesOffer().get(0).getPrice(),
    	    		"",
    	    		"",
    	    		"",
    				(String) userNow.getMakesOffer().get(4).getPrice(),
    	    		"",
    	    		"",
    	    		""
    		};
    		
    		for ( int i1 = 0; i1 < 5; i1++) {
	    		String query = updatePrice2(selectClause, whereClause, username, service, prefix, newProductPriceValue[i1], transactType[i1]);
	    		//System.out.println("query " + query);

	    		producerTemplate.sendBodyAndHeader(query,"proc","update");
    		}    		

    		
    		
    		selectClause = "productPriceCurrency";
    		whereClause = "j.3:priceCurrency";   		
    		String newProductPriceCurrencyValue[] = {
    				(String) userNow.getMakesOffer().get(0).getPriceCurrency(),
    	    		"",
    	    		"",
    	    		"",
    				(String) userNow.getMakesOffer().get(4).getPriceCurrency(),
    	    		"",
    	    		"",
    	    		""
    		};
    		
    		for ( int i1 = 0; i1 < 5; i1++) {
	    		String query = updatePriceCurrency2(selectClause, whereClause, username, service, prefix, newProductPriceCurrencyValue[i1], transactType[i1]);
	    		//System.out.println("query " + query);
	    		producerTemplate.sendBodyAndHeader(query,"proc","update");
    		}    		
        
    		selectClause = "productPrice";
    		whereClause = "j.4:price";   		
    		String newProduct4PriceValue[] = {
    	    		"",
    	    		"",
    	    		"",
    	    		"",
    	    		"",
    	    		(String) userNow.getSeeks().get(0).getPriceSpecification().getPrice(),
    	    		(String) userNow.getSeeks().get(1).getPriceSpecification().getPrice().toString(),
    	    		(String) userNow.getSeeks().get(2).getPriceSpecification().getPrice().toString()
    		};
    		
    		for ( int i1 = 5; i1 < transactType.length; i1++) {
	    		String query = updateProductPrice4(selectClause, whereClause, username, service, prefix, newProduct4PriceValue[i1], transactType[i1]);
	    		//System.out.println("query " + query);
	    		producerTemplate.sendBodyAndHeader(query,"proc","update");
    		}    		

    		selectClause = "productPriceCurrency";
    		whereClause = "j.4:priceCurrency";   		
    		String newProduct4PriceCurrencyValue[] = {
    	    		"",
    	    		"",
    	    		"",
    	    		"",
    	    		"",
    	    		(String) userNow.getSeeks().get(0).getPriceSpecification().getPriceCurrency(),
    	    		(String) userNow.getSeeks().get(1).getPriceSpecification().getPriceCurrency(),
    	    		(String) userNow.getSeeks().get(2).getPriceSpecification().getPriceCurrency()
    		};
    		
    		for ( int i1 = 5; i1 < transactType.length; i1++) {
	    		String query = updateProductPriceCurrency4(selectClause, whereClause, username, service, prefix, newProduct4PriceCurrencyValue[i1], transactType[i1]);
	    		//System.out.println("query " + query);
	    		producerTemplate.sendBodyAndHeader(query,"proc","update");
    		}
        }
       	Application.chatRoom.sendMessage("Hello All, " + username + " is offering " + "product1");

       	/*try {
    		Object obj = session.getAttribute("xmppUser");

        	XmppUser xmppUser = null;
           	if (obj instanceof XmppUser) {
           		xmppUser = (XmppUser) obj;
           	}

           	XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
           			.setHost(Application.xmppConfig.getHost())
    				.setPort(Application.xmppConfig.getListenPost())
    				.setServiceName(Application.xmppConfig.getHost())
    				.setSecurityMode(XMPPTCPConnectionConfiguration.SecurityMode.disabled)
    				.build();
           	AbstractXMPPConnection connection = new XMPPTCPConnection(config);
       		connection.setPacketReplyTimeout(10000);
       		Roster roster = Roster.getInstanceFor(connection);
            roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
            roster.setRosterLoadedAtLogin(false);
			connection.connect();
			
	       	connection.login(xmppUser.getUsername(), xmppUser.getPassword());
	       	ChatManager chatmanager = ChatManager.getInstanceFor(connection);
	       	
            Chat newChat = chatmanager.createChat(xmppUser.getUsername()+"@"+ Application.xmppConfig.getHost());
            newChat.getListeners();
            newChat.sendMessage("Hi CXCAdmin !, This is "+ xmppUser.getUsername());
            
            connection.disconnect();
		} catch (SmackException | XMPPException | IOException e) {
			e.printStackTrace();
		}*/
		return "/user/transact";
	}
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	public String update(String selectClause, String whereClause, String username, String service, String prefix, String newValue, String transactType ) {

    	String queryString2 = prefix +
    	
    			"SELECT ?" + selectClause +
				" WHERE { " +
					"?x " + whereClause + " ?" + selectClause + " . ?x j.2:category " + "\"" + transactType + "\"" + " . ?y j.1:name " + "\"" + username.toString() + "\"" +
	    		"}";   	
		//System.out.println("queryString2 " + queryString2);

    	Query q2 = QueryFactory.create(queryString2);
    	QueryExecution qexec2 = QueryExecutionFactory.sparqlService(service, q2);
    	ResultSet results2 = qexec2.execSelect();
    	QuerySolution binding = results2.nextSolution();
    	RDFNode val = binding.get(selectClause);
    	String oldValue = val.toString();

		String query1String = prefix +

				" DELETE { " +
		    				"?z " + whereClause + " " + "\"" + oldValue + "\"" +
		    			"}"	+
				" INSERT { " +
							"?z " + whereClause + " " + "\"" + newValue + "\"" +
						"}"	+
				" WHERE { " +
							"?z j.2:category " + "\"" + transactType + "\"" + " . ?y j.1:name " + "\"" + username.toString() + "\"" +
						"}";
		
		return query1String;
	}
	
	public String updatePrice2(String selectClause, String whereClause, String username, String service, String prefix, String newValue, String transactType ) {

    	String queryString2 = prefix +
    	
    			"SELECT ?" + selectClause +
				" WHERE { " +
				 " ?x j.1:name " + "\"" + username.toString() + "\"" + " ." +
		 		 " ?z j.1:makesOffer ?makesOffer . " +
				 " ?makesOffer	j.2:itemOffered ?itemOffered . " +
				 " ?itemOffered j.2:category " + "\"" + transactType + "\"" +  " ." +
				 " ?makesOffer j.3:price " + " ?" + selectClause + " ." +
		    		"}";
				
					
					//"?x " + whereClause + " ?" + selectClause + " . ?x j.1:category " + "\"" + transactType + "\"" + " . ?y j.0:name " + "\"" + username.toString() + "\"" +
		//System.out.println("queryString2 " + queryString2);



		
    	Query q2 = QueryFactory.create(queryString2);
    	QueryExecution qexec2 = QueryExecutionFactory.sparqlService(service, q2);
    	ResultSet results2 = qexec2.execSelect();
    	QuerySolution binding = results2.nextSolution();
    	RDFNode val = binding.get(selectClause);
    	String oldValue = val.toString();

		String query1String = prefix +

				" DELETE { " +
		    				"?makesOffer j.3:price " + "\"" + oldValue + "\"" +
		    			"}"	+
				" INSERT { " +
							"?makesOffer j.3:price " + "\"" + newValue + "\"" +
						"}"	+
				" WHERE { " +
						"?x j.1:name " + "\"" + username.toString() + "\"" + " . ?z j.1:makesOffer ?makesOffer . ?makesOffer j.2:itemOffered ?itemOffered . " +
						" ?itemOffered j.2:category " + "\"" + transactType + "\"" + " ." + 
						"}";  				
		  				
		return query1String;
	}
	
	public String updatePriceCurrency2(String selectClause, String whereClause, String username, String service, String prefix, String newValue, String transactType ) {

    	String queryString2 = prefix +
    	
    			"SELECT ?" + selectClause +
				" WHERE { " +
				 " ?x j.1:name " + "\"" + username.toString() + "\"" + " ." +
		 		 " ?z j.1:makesOffer ?makesOffer . " +
				 " ?makesOffer	j.2:itemOffered ?itemOffered . " +
				 " ?itemOffered j.2:category " + "\"" + transactType + "\"" +  " ." +
				 " ?makesOffer j.3:priceCurrency " + " ?" + selectClause + " ." +
		    		"}";
				
					
					//"?x " + whereClause + " ?" + selectClause + " . ?x j.1:category " + "\"" + transactType + "\"" + " . ?y j.0:name " + "\"" + username.toString() + "\"" +
		//System.out.println("queryString2 " + queryString2);



		
    	Query q2 = QueryFactory.create(queryString2);
    	QueryExecution qexec2 = QueryExecutionFactory.sparqlService(service, q2);
    	ResultSet results2 = qexec2.execSelect();
    	QuerySolution binding = results2.nextSolution();
    	RDFNode val = binding.get(selectClause);
    	String oldValue = val.toString();

		String query1String = prefix +

				" DELETE { " +
		    				"?makesOffer j.3:priceCurrency " + "\"" + oldValue + "\"" +
		    			"}"	+
				" INSERT { " +
							"?makesOffer j.3:priceCurrency " + "\"" + newValue + "\"" +
						"}"	+
				" WHERE { " +
						"?x j.1:name " + "\"" + username.toString() + "\"" + " . ?z j.1:makesOffer ?makesOffer . ?makesOffer j.2:itemOffered ?itemOffered . " +
						" ?itemOffered j.2:category " + "\"" + transactType + "\"" + " ." + 
						"}";  				
		  				
		return query1String;
	}
	
	public String updateProductPrice4(String selectClause, String whereClause, String username, String service, String prefix, String newValue, String transactType ) {

    	String queryString2 = prefix +
    	
    			"SELECT ?" + selectClause +
				" WHERE { " +
					" ?x j.1:name " + "\"" + username.toString() + "\"" + " ." +
			  		" ?z j.1:seeks ?seeks . " +
			  		" ?seeks	j.2:itemOffered ?itemOffered . " +
			  		" ?itemOffered j.2:category " + "\"" + transactType + "\"" +  " ." +
			  		" ?seeks	j.0:priceSpecification ?priceSpecification . " +
			    	" ?priceSpecification j.4:price " + " ?" + selectClause + " ." +
		    		"}";
		//System.out.println("queryString2 " + queryString2);

		Query q2 = QueryFactory.create(queryString2);
    	QueryExecution qexec2 = QueryExecutionFactory.sparqlService(service, q2);
    	ResultSet results2 = qexec2.execSelect();
    	QuerySolution binding = results2.nextSolution();
    	RDFNode val = binding.get(selectClause);
    	String oldValue = val.toString();

		String query1String = prefix +

				" DELETE { " +
		    				"?makesOffer j.3:price " + "\"" + oldValue + "\"" +
		    			"}"	+
				" INSERT { " +
							"?makesOffer j.3:price " + "\"" + newValue + "\"" +
						"}"	+
				" WHERE { " +
					" ?x j.1:name " + "\"" + username.toString() + "\"" + " ." +
			  		" ?z j.1:seeks ?seeks . " +
			  		" ?seeks	j.2:itemOffered ?itemOffered . " +
			  		" ?itemOffered j.2:category " + "\"" + transactType + "\"" +  " ." +
			  		" ?seeks	j.0:priceSpecification ?priceSpecification . " +
			    	" ?priceSpecification j.4:price " + " ?" + selectClause + " ." +
						"}";  				
		  				
		return query1String;
	}
	
	public String updateProductPriceCurrency4(String selectClause, String whereClause, String username, String service, String prefix, String newValue, String transactType ) {

    	String queryString2 = prefix +
    	
    			"SELECT ?" + selectClause +
				" WHERE { " +
					" ?x j.1:name " + "\"" + username.toString() + "\"" + " ." +
			  		" ?z j.1:seeks ?seeks . " +
			  		" ?seeks	j.2:itemOffered ?itemOffered . " +
			  		" ?itemOffered j.2:category " + "\"" + transactType + "\"" +  " ." +
			  		" ?seeks	j.0:priceSpecification ?priceSpecification . " +
			    	" ?priceSpecification j.4:priceCurrency " + " ?" + selectClause + " ." +
		    		"}";
		//System.out.println("queryString2 " + queryString2);

		Query q2 = QueryFactory.create(queryString2);
    	QueryExecution qexec2 = QueryExecutionFactory.sparqlService(service, q2);
    	ResultSet results2 = qexec2.execSelect();
    	QuerySolution binding = results2.nextSolution();
    	RDFNode val = binding.get(selectClause);
    	String oldValue = val.toString();

		String query1String = prefix +

				" DELETE { " +
		    				"?makesOffer j.3:priceCurrency " + "\"" + oldValue + "\"" +
		    			"}"	+
				" INSERT { " +
							"?makesOffer j.3:priceCurrency " + "\"" + newValue + "\"" +
						"}"	+
				" WHERE { " +
					" ?x j.1:name " + "\"" + username.toString() + "\"" + " ." +
			  		" ?z j.1:seeks ?seeks . " +
			  		" ?seeks	j.2:itemOffered ?itemOffered . " +
			  		" ?itemOffered j.2:category " + "\"" + transactType + "\"" +  " ." +
			  		" ?seeks	j.0:priceSpecification ?priceSpecification . " +
			    	" ?priceSpecification j.4:priceCurrency " + " ?" + selectClause + " ." +
						"}";  				
		  				
		return query1String;
	}
	
    @RequestMapping(value="/user/data", method=RequestMethod.GET)
    public String showMsgForm(Model model) {
    	User userMsg = new User();
    	model.addAttribute("usermsg", userMsg);
    	return "/user/data";
    }
    
	@RequestMapping(value="/user/data", method=RequestMethod.POST)
    public String sendMsgToTopic(HttpServletRequest httpServletRequest, @ModelAttribute(value="usermsg") @Valid Person user, BindingResult bindingResultUser, Model model) {

/*    	if (bindingResultUser.hasErrors()) { 	
        	model.addAttribute("WelcomeMessage","Enter valid User ID aand password");      	
        	return "/user/data";
        }*/
    	    	
		HttpSession session = httpServletRequest.getSession();
    	String username = (String) session.getAttribute("username");
    	ciss.in.models.User userNow = userRepository.findByUsername(username);
    	
    	//userNow.setUrl(user.getUrl());
    	userNow.setGivenName(user.getGivenName());
    	userNow.setFamilyName(user.getFamilyName());
    	//userNow.setJobTitle(user.getJobTitle());

		UserService userservice = new UserService(userRepository);
		userservice.update(userNow);
 
/*    	Endpoint controller2 = camelContext.getEndpoint("direct:channel2");
    	producerTemplate = camelContext.createProducerTemplate();
    	producerTemplate.setDefaultEndpoint(controller2);

    	RDFStream rdf = new RDFStream();
    	ByteArrayOutputStream out = (ByteArrayOutputStream) rdf.createRDF(userNow);
    	
    	producerTemplate.sendBodyAndHeader(out, KafkaConstants.PARTITION_KEY, 1);*/

    	
/*    	Endpoint controller3 = camelContext.getEndpoint("direct:start-user");
    	producerTemplate = camelContext.createProducerTemplate();
    	producerTemplate.setDefaultEndpoint(controller3);
        
    	String queryString =
 				"PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
    			"SELECT ?nick WHERE { " +
    		  	"	?nick a foaf:Person . " +
    			"}";
    	PREFIX foaf: <http://xmlns.com/foaf/0.1/>

    		SELECT ?nick
    		WHERE {
    		  ?nick a foaf:Person .
    		}
    	
        String queryString = 
                //"PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
        		//"INSERT DATA { " +
        		out.toString();// +
        		//" }";
    	producerTemplate.sendBody(queryString);*/
    	
		return "/user/data";
    }
	
    @RequestMapping(value="/user/login", method=RequestMethod.GET)
    public String doLoginForm(Model model) {
    	
    	String username;
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	if (principal instanceof UserDetails) {
    	  username = ((UserDetails)principal).getUsername();
    	} else {
    	  username = principal.toString();
    	}
    	
    	String returnValue = null;
    	if (username.equals("anonymousUser") || username.equals(null)) {
    		returnValue = "/user/login";
    	}
    	else return "redirect:/";
		return returnValue;
    }
    
	/* Register */
    @RequestMapping(value="/user/register", method=RequestMethod.GET)
    public String showForm(Model model) {
    	User user = new User();
    	
    	model.addAttribute(user);
 
    	String username;
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	if (principal instanceof UserDetails) {
    	  username = ((UserDetails)principal).getUsername();
    	} else {
    	  username = principal.toString();
    	}
    	
    	if (username.equals("anonymousUser") || username.equals(null)) {
    		return "/user/register";
    	}
    	else return "redirect:/";
    }

    @RequestMapping(value="/user/register", method=RequestMethod.POST)
    public String addUser(@ModelAttribute(value="user") @Valid User user, BindingResult bindingResultUser, 
    		Model model, HttpServletRequest httpServletRequest) {

    	UserService userservice = new UserService(userRepository);
    	String returnValue = null;

    	if (bindingResultUser.hasErrors()) { 	
        	model.addAttribute("WelcomeMessage","Enter valid User ID aand password");      	
        	return "/user/register";
        }
    	
    	User userNew = userRepository.findByUsername(user.getUsername());
    	//User usereMailNew = userRepository.findByEmail(user.getEmail());
    	
        if ((userNew != null)/* || usereMailNew != null*/) {
        	model.addAttribute("WelcomeMessage","This userId or email is already taken, try a new one");
        	return "/user/register";
        }
        else {
            boolean verify = false;
        	
          	String gRecaptchaResponse = (String) httpServletRequest.getParameter("g-recaptcha-response");
            ReCaptchaResponseVerfier veri = new ReCaptchaResponseVerfier();
			try {
				verify = veri.verifyRecaptcha(gRecaptchaResponse);
			} catch (IOException e) {
			}

        	if (verify) {
	        	userservice = new UserService(userRepository);
	        	user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
	        	user.setRole(2);
	        	user.setEmail(user.getEmail());
	        	user.setUserType("Ordinary User");
	        	userservice.create(user);
	        	model.addAttribute(user);
	        	model.addAttribute("WelcomeMessage","Hi Welcome to our site");
	        	returnValue = "redirect:/";
        	}
        	else returnValue = "/user/register";
        }
        return returnValue;
    } 
}