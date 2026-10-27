package in.ameya.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class UppercaseStringDeserializer extends StdDeserializer<String> {

	/** Automatically make strings uppercase
	 * @author aakash
	 */
	private static final long serialVersionUID = 5728823365159434126L;

	public UppercaseStringDeserializer() { 
        this(null); 
    } 

    public UppercaseStringDeserializer(Class<?> vc) { 
        super(vc); 
    }

	@Override
	public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
		JsonNode node = jp.getCodec().readTree(jp);
		return node.asText().toUpperCase();
	}
}
