package jsp;

import org.xml.sax.SAXException;
import parsers.DoneParsingException;
import parsers.EditionsParserHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Book
{
    private int id;
    private String xmlPath;
    private String ul = "";
    private boolean checked = false;

    private List<Edition> editions = new ArrayList<>();

    public String getXmlPath()
    {
        return xmlPath;
    }

    public void setXmlPath(String xmlPath)
    {
        this.xmlPath = xmlPath;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public List<Edition> getEditions()
    {
        return editions;
    }

    public boolean addEdition(Edition edition)
    {
        return this.editions.add(edition);
    }

    // по абсолютному пути парсит xml и запоминает все editions
    public void parseEditionsXML()
    {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        EditionsParserHandler handler = new EditionsParserHandler();
        SAXParser parser = null;

        try {
            parser = factory.newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

        try {
            File file = new File(xmlPath);
            parser.parse(file, handler);
        } catch (DoneParsingException e) {
            this.editions = handler.editions;
            this.ul = handler.ul;
            this.checked = handler.checked;
            System.out.println("parseEditionsXML: OK");
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public String getUL()
    {
        return ul;
    }

    public void setUL(String ul)
    {
        this.ul = ul;
    }

    public boolean isChecked()
    {
        return checked;
    }

    public void setChecked(boolean checked)
    {
        this.checked = checked;
    }

    @Override
    public String toString()
    {
        return "Book{" +
                "id=" + id +
                ", xmlPath='" + xmlPath + '\'' +
                ", ul='" + ul + '\'' +
                ", checked=" + checked +
                ", editions=" + editions +
                '}';
    }
}


