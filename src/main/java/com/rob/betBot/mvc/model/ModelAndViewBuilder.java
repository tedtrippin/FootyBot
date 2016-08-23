package com.rob.betBot.mvc.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

import com.rob.betBot.mvc.ModelKeys;

public class ModelAndViewBuilder {

    private String view;
    private final Map<String, Object> model = new HashMap<>();
    private final List<String> errors = new LinkedList<>();

    public ModelAndViewBuilder(String view) {
        this.view = view;
        model.put(ModelKeys.ERRORS, errors);
    }

    public void addError(String errorMessage) {
        errors.add(errorMessage);
    }

    public void add(String key, Object value) {
        model.put(key, value);
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public ModelAndView build() {
        return new ModelAndView(view, model);
    }
}
