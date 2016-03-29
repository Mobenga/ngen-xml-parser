package com.mobenga.ngen.xml.parser.example;

import com.mobenga.ngen.xml.parser.AttributeMapping;
import com.mobenga.ngen.xml.parser.ElementParserSettings;
import com.mobenga.ngen.xml.parser.ElementTextMapping;
import com.mobenga.ngen.xml.parser.Mappings;
import com.mobenga.ngen.xml.parser.example.model.Event;
import com.mobenga.ngen.xml.parser.example.model.Market;
import com.mobenga.ngen.xml.util.MappingUtil;

public class EventMapperExample1 implements Mappings {

    @Override
    public ElementParserSettings getSettings() {
        return event();
    }

    private ElementParserSettings event() {

        ElementParserSettings settings = new ElementParserSettings("event");

        settings.setElementStartProcessor(objectBranch -> {
            Event event = new Event();
            objectBranch.put(Event.class, event);
        });

        settings.setAttributeMappings(
                new AttributeMapping<>(Event.class, Event::setId, MappingUtil::getFirst, "id"),
                new AttributeMapping<>(Event.class, Event::setName, MappingUtil::getFirst, "name")
        );

        settings.setSubElementParsers(market());

        settings.setElementTextMappings(new ElementTextMapping<>(Event.class, Event::setMainMarket, MappingUtil::getElementText));

        return settings;
    }

    private ElementParserSettings market() {
        ElementParserSettings settings = new ElementParserSettings("market");

        settings.setElementStartProcessor(objectBranch -> objectBranch.put(Market.class, new Market()));

        settings.setAttributeMappings(
                new AttributeMapping<>(Market.class, Market::setId, MappingUtil::getFirst, "id"),
                new AttributeMapping<>(Market.class, Market::setName, MappingUtil::getFirst, "name")
        );

        settings.setElementEndProcessor(objectBranch -> {
            Event event = objectBranch.getInstance(Event.class);
            Market market = objectBranch.pop(Market.class);
            event.getMarkets().add(market);
        });
        return settings;
    }
}
