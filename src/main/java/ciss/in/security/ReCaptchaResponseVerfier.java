package ciss.in.security;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ReCaptchaResponseVerfier {
	
	private final static String url = "https://www.google.com/recaptcha/api/siteverify";
	private final static String secret = "6LcXUwgTAAAAAFKE8QCuMgndwZzN-9hARfnczaqE";
	private final static String USER_AGENT = "Mozilla/5.0";
	private static final Logger LOGGER = LoggerFactory.getLogger(ReCaptchaResponseVerfier.class);

  public boolean verifyRecaptcha(String gRecaptchaResponse) throws IOException {
	  boolean returnVerify = false;
   
      if (gRecaptchaResponse == null || "".equals(gRecaptchaResponse)) {
          return false;
      }
       
      try {
      URL obj = new URL(url);
      HttpsURLConnection request = (HttpsURLConnection) obj.openConnection();

      // add request header
      request.setRequestMethod("POST");
      request.setRequestProperty("User-Agent", USER_AGENT);
      request.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
      
      String params = "secret=" + secret + "&response=" + gRecaptchaResponse;
      request.setDoOutput(true);
      DataOutputStream wr = new DataOutputStream(request.getOutputStream());
      wr.writeBytes(params);
      wr.flush();
      wr.close();
      
   // Get the response code
      int respCode = request.getResponseCode();
      LOGGER.debug("Response code: " + respCode);
      // Read in the response data
      BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
      String line;
      StringBuilder input = new StringBuilder();
      while ((line = in.readLine()) != null){
          input.append(line);
      }
      in.close();
      
      JsonParser jp = new JsonParser(); //from gson
      JsonElement root = jp.parse(input.toString());
      JsonObject rootobj = root.getAsJsonObject();
      returnVerify = rootobj.get("success").getAsBoolean() ;
      } catch (Exception e) {
      }
		return returnVerify;        	
  	}
}