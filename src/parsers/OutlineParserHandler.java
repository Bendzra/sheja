package parsers;

import jsp.Book;
import jsp.Drop;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class OutlineParserHandler extends DefaultHandler
{
    private Book book;
    private int drop_id;    // порядковый номер (1.....)
    private int drop_count = 0;
    private int branch_id;  // порядковый номер (1.....)
    private int branch_count = 0;
    private int edition_index;
    private String currentTag = null;
    private Drop drop = null;
    private boolean dropFlag = false;
    private boolean crambFlag = false;
    private List<Drop> drops = new ArrayList<>();

    public List<Drop> getDrops()
    {
        return drops;
    }

    public OutlineParserHandler(Book book, int drop_id, int branch_id)
    {
        this.book = book;
        this.drop_id = drop_id;
        this.branch_id = branch_id;
        edition_index = (drop_id - 1) % book.getEditions().size();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        currentTag = qName;
        if (qName.equals("drop")) {
            drop_count++;
            for (int i = 0; i < attributes.getLength(); i++) {
                if (attributes.getQName(i).equals("edition_id")) {
                    if (Integer.parseInt(attributes.getValue(i)) == edition_index + 1) {
                        dropFlag = true;
                        break;
                    }
                }
            }

            if (dropFlag) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    if (attributes.getQName(i).equals("crumb")) {
                        crambFlag = true;
                        break;
                    }
                }
            }

            if (dropFlag && crambFlag) {
                branch_count++;
                drop = new Drop();
                drop.setId(drop_count);
                if(branch_count == branch_id) {
                    drop.getText().append("<ul>\r\n<li><a class='text-danger font-weight-bold' href='#'>");
                } else {
                    drop.getText().append("<ul>\r\n<li><a href='#'>");
                }
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if (qName.equals("drop")) {
            if (dropFlag && crambFlag) {
                drop.getText().append("</a></li>");
                drops.add(drop);
            }
            dropFlag = false;
            crambFlag = false;
        } else if (qName.equals("b")) {
            drops.get(drops.size() - 1).getText().append("\r\n</ul>");
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        if (currentTag != null && currentTag.equals("drop") && dropFlag && crambFlag) {
            drop.getText().append(ch, start, length);
        }
    }
}
