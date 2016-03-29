package com.mobenga.ngen.xml.parser.example.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This is only a Test Event used to document XML Parser
 */
public class Event {
    private String id;
    private String name;
    private String mainMarket;
    private String sportName;
    private List<Market> markets = new ArrayList<>();

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

    public String getMainMarket() {
        return mainMarket;
    }

    public void setMainMarket(String mainMarket) {
        this.mainMarket = mainMarket;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public List<Market> getMarkets() {
        return markets;
    }

    public void setMarkets(List<Market> markets) {
        this.markets = markets;
    }
}
