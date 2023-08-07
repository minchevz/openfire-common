package gamesys.messaging.api;

public enum Header {

    EVENT_NAME("eventName");

    private final String readableValue;

    private Header(String readableValue) {
        this.readableValue = readableValue;
    }

    public String getReadableValue() {
        return readableValue;
    }

}
