package jndi.helper;

public enum ConfigEnum {
    PORT("port"),
    HOST("host"),
    GROUP("group");

    private String key;
    private ConfigEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
