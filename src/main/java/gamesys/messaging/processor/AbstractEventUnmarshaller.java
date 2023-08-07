package gamesys.messaging.processor;

import gamesys.messaging.api.Event;
import gamesys.messaging.api.Header;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.Map;

public abstract class AbstractEventUnmarshaller implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String payload = exchange.getIn().getBody(String.class);
        String eventName = exchange.getIn().getHeader(Header.EVENT_NAME.getReadableValue(), String.class);
        Event event = getConvertedObject(payload, eventName);
        if (event == null) {
            String errMsg = String.format("Unable to create object for the event[%s] with payload:\n %s ", eventName, payload);
            exchange.setException(new RuntimeException(errMsg));
            return;
        }
        exchange.getOut().setHeaders(exchange.getIn().getHeaders());
        exchange.getOut().setBody(event);
    }

    private Event getConvertedObject(String payload, String eventName) {
        Map<String, JsonConverter<? extends Event>> converterMap = getConverterMap();
        JsonConverter<? extends Event> converter = converterMap.get(eventName);
        if (converter != null) {
            return converter.unmarshal(payload);
        } else {
            throw new IllegalStateException("Cannot find a converter for event of name " + eventName  + ". Listed converters are " + converterMap.keySet());
        }

    }

    public abstract Map<String, JsonConverter<? extends Event>> getConverterMap();

}