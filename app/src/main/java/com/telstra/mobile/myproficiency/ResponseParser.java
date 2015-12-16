package com.telstra.mobile.myproficiency;

import com.telstra.mobile.model.JSONResponseModel;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseParser {



    private ObjectMapper objectMapper = null;
    private JsonFactory jsonFactory = null;

    public ResponseParser() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jsonFactory = new JsonFactory();
    }

    public Object ParserInputStreamReader(InputStream result, int requestType) {
        Object objOutput = null;
        JsonParser thisJsonParsor;
        try {
            thisJsonParsor = jsonFactory.createJsonParser(result);
            switch (requestType) {
                case 1:
                    objOutput = objectMapper.readValue(thisJsonParsor, JSONResponseModel.class);
                    break;
                default:
                    break;
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return objOutput;
    }
}
