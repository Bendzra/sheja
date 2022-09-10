package parsers;

// находит в xml первый match или не находит

import jsp.Drop;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TestParserHandler extends DefaultHandler
{
    private String currentTag = null;
    private Drop drop = new Drop();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        currentTag = qName;
        if (qName.equals("d")) {
            drop.setText(new StringBuilder());
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if (qName.equals("d")) {
            if (drop.findIndexes().size() > 0) {
                throw new DoneParsingException();
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        if (currentTag != null && currentTag.equals("d")) {
            drop.getText().append(ch, start, length);
        }
    }
}
