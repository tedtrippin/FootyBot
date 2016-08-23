package com.rob.betBot.bots.modules;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import com.rob.betBot.exception.InvalidDataException;
import com.rob.betBot.mvc.model.AddedModuleProperty;

public class TestModuleFactory {

    @Test(expected = InvalidDataException.class)
    public void testCreateFromIdAndProperties_invalidId()
        throws InvalidDataException {

        ModuleFactory moduleFactory = new ModuleFactory();
        moduleFactory.createFilter("asdf", null);
    }

    @Ignore // Filters commented out
    @Test
    public void testCreateFromIdAndProperties()
        throws InvalidDataException {

        AddedModuleProperty ageProperty = new AddedModuleProperty();
        ageProperty.setName(PropertyNames.AGE);
        ageProperty.setValue("21");

        ModuleFactory moduleFactory = new ModuleFactory();
        AgeOverModule module = (AgeOverModule) moduleFactory.createFilter("1", null);

        assertEquals("21", module.getAgeProperty());
    }
}
