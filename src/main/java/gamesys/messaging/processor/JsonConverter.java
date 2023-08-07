package gamesys.messaging.processor;

public interface JsonConverter<T> {

    String marshal(T payload);

    T unmarshal(String jsonString);

}
