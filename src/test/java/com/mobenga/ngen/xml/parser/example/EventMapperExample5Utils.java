package com.mobenga.ngen.xml.parser.example;

import com.mobenga.ngen.xml.parser.example.model.Event;
import com.mobenga.ngen.xml.parser.ProtectedClassMap;

import java.util.Map;

public class EventMapperExample5Utils {

    protected static String getMarketNameInclEventName(Map<String, String> values, ProtectedClassMap objectBranch){
        Event event = objectBranch.getInstance(Event.class);
        String marketOriginalName = values.get("name");
        String eventName = event.getName();
        return marketOriginalName + " (" + eventName + ")";
    }
}
