package parsers;

import jsp.Edition;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EditionsParserHandler extends DefaultHandler
{
    // по-простому, без setter'ов и getter'ов
    public List<Edition> editions = new ArrayList<>();
    public String ul = "book";
    public boolean checked = false;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        if (qName.equals("editions")) {
            for (int i = 0; i < attributes.getLength(); i++) {
                try {
                    // считаем, что названия полей - ul и checked - по договоренности,
                    // совпадают с названиями атрибутов в теге <editions...>
                    Field field = this.getClass().getDeclaredField(attributes.getQName(i));
                    String val = attributes.getValue(i);
                    if(field.getName().equals("checked")) field.set(this, Boolean.parseBoolean(val));
                    else field.set(this, val);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (qName.equals("edition")) {
            Edition edition = newEdition(attributes);
            editions.add(edition);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if (qName.equals("editions")) {
            throw new DoneParsingException();
        }
    }

    // TODO: synchronized
    static Edition newEdition(Attributes attributes)
    {
        Edition edition = new Edition();
        for (int i = 0; i < attributes.getLength(); i++) {
            try {
                // считаем, что названия полей в моделе (классе) должны
                // (по договоренности) совпадать с названиями атрибутов в теге xml
                Field field = edition.getClass().getDeclaredField(attributes.getQName(i));
                field.setAccessible(true);
                field.set(edition, attributes.getValue(i));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return edition;
    }
}
