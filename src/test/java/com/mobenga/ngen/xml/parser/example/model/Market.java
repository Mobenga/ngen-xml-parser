package com.mobenga.ngen.xml.parser.example.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This is only a Test Market used to document XML Parser
 */
public class Market {
    private String id;
    private String name;
    private List<Outcome> outcomes = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Outcome> getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(List<Outcome> outcomes) {
        this.outcomes = outcomes;
    }
}
