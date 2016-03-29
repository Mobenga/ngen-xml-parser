package com.mobenga.ngen.xml.parser.example;

import com.mobenga.ngen.xml.parser.example.model.Event;
import com.mobenga.ngen.xml.parser.example.model.Market;
import com.mobenga.ngen.xml.parser.example.model.Outcome;
import com.mobenga.ngen.xml.parser.DocumentParser;
import com.mobenga.ngen.xml.parser.ProtectedClassMap;
import com.mobenga.ngen.xml.parser.XmlParser;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class EventMapperExampleTest {
    private String xmlStringEx1Ex4Ex5 = "<event id=\"1\" name =\"Foo\">\n" +
            " Some Description Text\n" +
            " <market id=\"1\" name=\"Mkt Foo\">\n" +
            " </market>\n" +
            " <market id=\"2\" name=\"Mkt Bar\">\n" +
            " </market>\n" +
            "</event>";

    @Test
    public void testEventMapperExample1() throws IllegalAccessException, XMLStreamException, InstantiationException {

        InputStream xmlStream = new ByteArrayInputStream(this.xmlStringEx1Ex4Ex5.getBytes());

        Event event = new XmlParser().parseXmlUnsafe(xmlStream, new DocumentParser(new EventMapperExample1()), Event.class);
        assertNotNull(event);
        assertThat(event.getId(), is("1"));
        assertThat(event.getName(), is("Foo"));
        List<Market> markets = event.getMarkets();
        assertNotNull(markets);
        assertThat(markets.size(), is(2));
        Market market = markets.get(0);
        assertThat(market.getId(), is("1"));
        assertThat(market.getName(), is("Mkt Foo"));
        market = markets.get(1);
        assertThat(market.getId(), is("2"));
        assertThat(market.getName(), is("Mkt Bar"));
    }

    @Test
    public void testEventMapperExample2() throws IllegalAccessException, XMLStreamException, InstantiationException {
        String xmlString = "<sport name=\"Football\">\n" +
                " <event id=\"1\" name =\"Foo\">\n" +
                "  <market id=\"1\" name=\"Mkt Foo\">\n" +
                "  </market>\n" +
                " </event>\n" +
                "</sport>";
        InputStream xmlStream = new ByteArrayInputStream(xmlString.getBytes());

        Event event = new XmlParser().parseXmlUnsafe(xmlStream, new DocumentParser(new EventMapperExample2()), Event.class);
        assertNotNull(event);
        assertThat(event.getId(), is("1"));
        assertThat(event.getName(), is("Foo"));
        assertThat(event.getSportName(), is("Football"));
        List<Market> markets = event.getMarkets();
        assertNotNull(markets);
        assertThat(markets.size(), is(1));
        Market market = markets.get(0);
        assertThat(market.getId(), is("1"));
        assertThat(market.getName(), is("Mkt Foo"));
    }

    @Test
    public void testEventMapperExample3() throws IllegalAccessException, XMLStreamException, InstantiationException {
        String xmlString = "<event id=\"1\" name =\"Foo\">\n" +
                " <market id=\"1\" name=\"Mkt Foo\">\n" +
                "  <selection id=\"1\" name=\"Sel Foo\">\n" +
                "   <price odds_frac=\"1/3\" odds_dec=\"1.33\"/>\n" +
                "  </selection>\n" +
                " </market>" +
                "</event>";
        InputStream xmlStream = new ByteArrayInputStream(xmlString.getBytes());

        Event event = new XmlParser().parseXmlUnsafe(xmlStream, new DocumentParser(new EventMapperExample3()), Event.class);
        assertNotNull(event);
        assertThat(event.getId(), is("1"));
        assertThat(event.getName(), is("Foo"));
        List<Market> markets = event.getMarkets();
        assertNotNull(markets);
        assertThat(markets.size(), is(1));
        Market market = markets.get(0);
        assertThat(market.getId(), is("1"));
        assertThat(market.getName(), is("Mkt Foo"));
        List<Outcome> outcomes = market.getOutcomes();
        assertThat(outcomes.size(), is(1));
        Outcome outcome = outcomes.get(0);
        assertThat(outcome.getName(), is("Sel Foo"));
        assertThat(outcome.getId(), is("1"));
        assertThat(outcome.getOdds(), is("1/3"));
        assertThat(outcome.getOddsDecimal(), is("1.33"));
    }

    @Test
    public void testEventMapperExample4() throws IllegalAccessException, XMLStreamException, InstantiationException {
        String xmlString = "<event id=\"1\">\n" +
                " <market id=\"1\">\n" +
                "  <selection id=\"1\" name=\"Sel Foo\" price=\"7/5\"/>\n" +
                "  <selection id=\"2\" name=\"Sel Bar\" price=\"5/3\"/>\n" +
                "  <selection id=\"3\" name=\"Sel Mitza\" price=\"3/1\"/>\n" +
                " </market>\n" +
                "</event>";

        InputStream xmlStream = new ByteArrayInputStream(this.xmlStringEx1Ex4Ex5.getBytes());
        Event event = new XmlParser().parseXmlUnsafe(xmlStream, new DocumentParser(new EventMapperExample1()), Event.class);
        assertNotNull(event);
        assertThat(event.getId(), is("1"));
        assertThat(event.getName(), is("Foo"));
        List<Market> markets = event.getMarkets();
        assertNotNull(markets);
        assertThat(markets.size(), is(2));

        Market market = markets.get(0);
        assertThat(market.getId(), is("1"));
        assertThat(market.getName(), is("Mkt Foo"));
        assertNotNull(market.getOutcomes());
        assertThat(market.getOutcomes().size(), is(0));

        market = markets.get(1);
        assertThat(market.getId(), is("2"));
        assertThat(market.getName(), is("Mkt Bar"));
        assertNotNull(market.getOutcomes());
        assertThat(market.getOutcomes().size(), is(0));

        ProtectedClassMap objectBranch = new ProtectedClassMap();
        objectBranch.put(Event.class, event);
        xmlStream = new ByteArrayInputStream(xmlString.getBytes());
        Event upgradedEvent = new XmlParser().parseXmlUnsafe(xmlStream, new DocumentParser(new EventMapperExample4(), objectBranch), Event.class);

        assertNotNull(upgradedEvent);
        assertThat(upgradedEvent.getId(), is("1"));
        assertThat(upgradedEvent.getName(), is("Foo"));
        markets = upgradedEvent.getMarkets();
        assertNotNull(markets);
        assertThat(markets.size(), is(2));

        market = markets.get(0);
        assertThat(market.getId(), is("1"));
        assertThat(market.getName(), is("Mkt Foo"));
        assertNotNull(market.getOutcomes());
        assertThat(market.getOutcomes().size(), is(3));
        assertThat(market.getOutcomes().get(0).getId(), is("1"));
        assertThat(market.getOutcomes().get(1).getName(), is("Sel Bar"));
        assertThat(market.getOutcomes().get(2).getOdds(), is("3/1"));

        market = markets.get(1);
        assertThat(market.getId(), is("2"));
        assertThat(market.getName(), is("Mkt Bar"));
        assertNotNull(market.getOutcomes());
        assertThat(market.getOutcomes().size(), is(0));
    }

    @Test
    public void testEventMapperExample5() throws IllegalAccessException, XMLStreamException, InstantiationException {

        InputStream xmlStream = new ByteArrayInputStream(this.xmlStringEx1Ex4Ex5.getBytes());

        Event event = new XmlParser().parseXmlUnsafe(xmlStream, new DocumentParser(new EventMapperExample5()), Event.class);
        assertNotNull(event);
        assertThat(event.getId(), is("1"));
        assertThat(event.getName(), is("Foo"));
        List<Market> markets = event.getMarkets();
        assertNotNull(markets);
        assertThat(markets.size(), is(2));
        Market market = markets.get(0);
        assertThat(market.getId(), is("1"));
        assertThat(market.getName(), is("Mkt Foo (Foo)"));
        market = markets.get(1);
        assertThat(market.getId(), is("2"));
        assertThat(market.getName(), is("Mkt Bar (Foo)"));
    }
}
