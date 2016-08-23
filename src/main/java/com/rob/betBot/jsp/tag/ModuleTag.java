package com.rob.betBot.jsp.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.rob.betBot.bots.modules.Module;

public class ModuleTag extends SimpleTagSupport {

    private Module module;
    private boolean addable;

    @Override
    public void doTag()
        throws JspException, IOException {

        PageContext context = (PageContext) getJspContext();
        context.getOut().print(module.getName());
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public void setAddable(String addableString) {
        addable = Boolean.parseBoolean(addableString);
    }
}