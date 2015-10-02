package ciss.in.off.codecs;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ciss.in.models.UserInfo;

import com.fasterxml.jackson.databind.ObjectMapper;

import kafka.serializer.Decoder;
import kafka.utils.VerifiableProperties;

public class CustomMessageDecoder implements Decoder<UserInfo> {
	
    final Logger logger = LoggerFactory.getLogger(CustomMessageDecoder.class);
    
    public CustomMessageDecoder(VerifiableProperties verifiableProperties) {
        /* This constructor must be present for successful compile. */
    }

	@Override
    public UserInfo fromBytes(byte[] bytes) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(bytes, UserInfo.class);
        } catch (IOException e) {
        	logger.error(String.format("Json processing failed for object: %s", bytes.toString()), e);
        }
        return null;
    }
}