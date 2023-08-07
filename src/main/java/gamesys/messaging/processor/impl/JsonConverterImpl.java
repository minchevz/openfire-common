package gamesys.messaging.processor.impl;

import gamesys.messaging.api.Event;
import gamesys.messaging.processor.JsonConverter;
import gamesys.messaging.serialization.MessageSerializer;

public class JsonConverterImpl<T extends Event> implements JsonConverter<T> {


    private Class<T> eventType;

    public JsonConverterImpl(Class<T> eventType) {
        this.eventType = eventType;
    }

    @Override
    public T unmarshal(String jsonString) {
        return MessageSerializer.getObject(jsonString, eventType);
    }

    @Override
    public String marshal(T payload) {
        return MessageSerializer.getJson(payload);
    }

}
