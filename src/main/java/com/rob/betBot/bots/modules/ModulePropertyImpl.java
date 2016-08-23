package com.rob.betBot.bots.modules;


public class ModulePropertyImpl implements ModuleProperty {

    private String name;
    private String displayName;
    private ModulePropertyType type;
    private String value;

    protected ModulePropertyImpl() {
    }

    public ModulePropertyImpl(String name, ModulePropertyType type, String displayName) {
        this.name = name;
        this.displayName = displayName;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public ModulePropertyType getType() {
        return type;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    public boolean isValid() {

        switch (type) {
            case BOOLEAN:
                return Boolean.TRUE.toString().equals(value) || Boolean.FALSE.toString().equals(value);
            case NUMBER:
                try {
                    Double.parseDouble(value);
                    return true;
                } catch (Exception ex) {
                    return false;
                }
            case STRING:
                return value != null;
            default:
                return false;
        }
    }

    @Override
    public String toString() {
        return new StringBuilder("name=").append(name)
            .append(",displayName=").append(displayName)
            .append(",type=").append(type)
            .append(",value=").append(value).toString();
    }
}
