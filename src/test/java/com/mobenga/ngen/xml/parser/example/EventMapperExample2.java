package com.mobenga.ngen.xml.parser.example;

import com.mobenga.ngen.xml.parser.example.model.Event;
import com.mobenga.ngen.xml.parser.example.model.Market;
import com.mobenga.ngen.xml.parser.AttributeMapping;
import com.mobenga.ngen.xml.parser.ElementParserSettings;
import com.mobenga.ngen.xml.parser.Mappings;
import com.mobenga.ngen.xml.util.MappingUtil;

public class EventMapperExample2 implements Mappings {

    @Override
    public ElementParserSettings getSettings() {
        return sport();
    }

    private ElementParserSettings sport() {
        ElementParserSettings settings = new ElementParserSettings("sport");
        settings.setElementStartProcessor(objectBranch -> objectBranch.put(ProprietaryDataStorage.class, new ProprietaryDataStorage()));
        settings.setAttributeMappings(
                new AttributeMapping<>(ProprietaryDataStorage.class, ProprietaryDataStorage::setSportName, MappingUtil::getFirst, "name")
        );
        settings.setSubElementParsers(event());
        settings.setElementEndProcessor(objectBranch -> {objectBranch.pop(ProprietaryDataStorage.class);});

        return settings;
    }

    private ElementParserSettings event() {
        ElementParserSettings settings = new ElementParserSettings("event");

        settings.setElementStartProcessor(objectBranch -> {
            Event event = new Event();
            event.setSportName(objectBranch.getInstance(ProprietaryDataStorage.class).getSportName());
            objectBranch.put(Event.class, event);
        });

        settings.setAttributeMappings(
                new AttributeMapping<>(Event.class, Event::setId, MappingUtil::getFirst, "id"),
                new AttributeMapping<>(Event.class, Event::setName, MappingUtil::getFirst, "name")
        );

        settings.setSubElementParsers(market());
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
