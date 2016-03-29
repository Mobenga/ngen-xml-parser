package com.mobenga.ngen.xml.parser.example;

import com.mobenga.ngen.xml.parser.example.model.Event;
import com.mobenga.ngen.xml.parser.example.model.Market;
import com.mobenga.ngen.xml.parser.example.model.Outcome;
import com.mobenga.ngen.xml.parser.AttributeMapping;
import com.mobenga.ngen.xml.parser.ElementParserSettings;
import com.mobenga.ngen.xml.parser.Mappings;
import com.mobenga.ngen.xml.util.MappingUtil;

public class EventMapperExample4 implements Mappings {

    @Override
    public ElementParserSettings getSettings() {
        return event();
    }

    private ElementParserSettings event() {
        ElementParserSettings settings = new ElementParserSettings("event");

        settings.setSubElementParsers(market());

        return settings;
    }

    private ElementParserSettings market() {
        ElementParserSettings settings = new ElementParserSettings("market");

        settings.setElementStartProcessor((objectBranch, id) -> {
            Event event = objectBranch.getInstance(Event.class);
            // Find market with correct id
            Market market = event.getMarkets().stream().filter(mkt -> (id.equals(mkt.getId()))).findFirst().get();
            objectBranch.put(Market.class, market);
        }, "id");

        settings.setSubElementParsers(selection());

        settings.setElementEndProcessor(objectBranch -> {objectBranch.pop(Market.class);});

        return settings;
    }

    private ElementParserSettings selection() {

        ElementParserSettings settings = new ElementParserSettings("selection");

        settings.setElementStartProcessor(objectBranch -> objectBranch.put(Outcome.class, new Outcome()));

        settings.setAttributeMappings(
                new AttributeMapping<>(Outcome.class, Outcome::setId, MappingUtil::getFirst, "id"),
                new AttributeMapping<>(Outcome.class, Outcome::setName, MappingUtil::getFirst, "name"),
                new AttributeMapping<>(Outcome.class, Outcome::setOdds, MappingUtil::getFirst, "price")
        );

        settings.setElementEndProcessor(objectBranch -> {
            Market market = objectBranch.getInstance(Market.class);
            Outcome outcome = objectBranch.pop(Outcome.class);
            market.getOutcomes().add(outcome);
        });

        return settings;
    }
}
