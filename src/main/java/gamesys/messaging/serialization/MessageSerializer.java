package gamesys.messaging.serialization;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

import java.io.IOException;

public final class MessageSerializer {

    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        initMapper(mapper);
    }

    public MessageSerializer() {
    }

    public static byte[] getJsonBytes(Object obj) {
        try {
            return mapper.writeValueAsBytes(obj);
        } catch (JsonGenerationException e) {
            rethrow(e);
        } catch (JsonMappingException e) {
            rethrow(e);
        } catch (IOException e) {
            rethrow(e);
        }

        return new byte[]{};
    }

    public static String getJson(Object obj) {
        String result = null;
        try {
            result = mapper.writeValueAsString(obj);
        } catch (JsonGenerationException e) {
            rethrow(e);
        } catch (JsonMappingException e) {
            rethrow(e);
        } catch (IOException e) {
            rethrow(e);
        }
        return result;
    }


    public static <T> T getObject(String str, Class<T> clazz) {
        T result = null;
        try {
            return mapper.readValue(str, clazz);
        } catch (JsonParseException e) {
            rethrow(e);
        } catch (JsonMappingException e) {
            rethrow(e);
        } catch (IOException e) {
            rethrow(e);
        }
        return result;
    }

    public static <T> T getObject(byte[] bytes, Class<T> clazz) {
        T result = null;
        try {
            result = mapper.readValue(bytes, 0, bytes.length, clazz);
        } catch (JsonParseException e) {
            rethrow(e);
        } catch (JsonMappingException e) {
            rethrow(e);
        } catch (IOException e) {
            rethrow(e);
        }
        return result;
    }

    private static void rethrow(Exception e) {
        throw new RuntimeException(e);
    }

    private static void initMapper(ObjectMapper mapper) {
        mapper.enable(DeserializationConfig.Feature.USE_BIG_INTEGER_FOR_INTS);
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector());
    }
}
