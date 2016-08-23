package com.rob.betBot.mvc.model;

import java.util.Collection;

import com.rob.betBot.bots.Module;
import com.rob.betBot.bots.modules.ModuleProperty;

public class AddedModule {

    private String id;
    private String name;
    private AddedModuleProperty[] properties;

    public AddedModule() {
    }

    public AddedModule(Module module) {

        id = module.getId();
        name = module.getName();

        Collection<ModuleProperty> moduleProperties = module.getProperties();
        properties = new AddedModuleProperty[moduleProperties.size()];

        int idx = 0;
        for (ModuleProperty p : moduleProperties) {
            properties[idx++] = new AddedModuleProperty(p);
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AddedModuleProperty[] getProperties() {
        return properties;
    }

    public void setProperties(AddedModuleProperty[] properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return new StringBuilder('[')
            .append("id=").append(id)
            .append(",name=").append(name)
            .append(']').toString();
    }
}
