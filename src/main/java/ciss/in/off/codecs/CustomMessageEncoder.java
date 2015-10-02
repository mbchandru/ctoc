package ciss.in.off.codecs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kafka.serializer.Encoder;
import kafka.utils.VerifiableProperties;

public class CustomMessageEncoder implements Encoder<Object> {
	
    final Logger logger = LoggerFactory.getLogger(CustomMessageEncoder.class);
    
    public CustomMessageEncoder(VerifiableProperties verifiableProperties) {
        /* This constructor must be present for successful compile. */
    }

	@Override
	public byte[] toBytes(Object webMsg) {
		ObjectMapper objectMapper = new ObjectMapper();
		byte[] byteValue = null;
		try {
			byteValue = objectMapper.writeValueAsString(webMsg).getBytes();
		} catch (JsonProcessingException e) {

			e.printStackTrace();
		}
		return byteValue;
	}
}