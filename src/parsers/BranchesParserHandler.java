package parsers;

import jsp.Book;
import jsp.Drop;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class BranchesParserHandler extends DefaultHandler
{
//    private Book book;
    private int drop_id;    // порядковый номер (1.....)
    private int drop_count = 0;
    private int branch_id;  // порядковый номер (1.....)
    private int branch_count = 0;
    private int edition_index;
    private int branch_stack_size = -1;
    private String currentTag = null;
    private Drop drop = null;
    private boolean dropFlag = false;
    private boolean crambFlag = false;
	private boolean rootFlag = false;
    private List<Drop> drops = new ArrayList<>();

    public List<Drop> getDrops()
    {
        return drops;
    }

    public BranchesParserHandler(Book book, int drop_id, int branch_id)
    {
        super();
//        this.book = book;
        this.drop_id = drop_id;
        this.branch_id = branch_id;
        edition_index = (drop_id - 1) % book.getEditions().size();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        currentTag = qName;
        if (qName.equals("b")) {
            branch_count++;
            if (branch_count == branch_id) {
                branch_stack_size = 0;
            }

            if (branch_stack_size >= 0) // put
            {
                branch_stack_size++;
            }
        } else if (qName.equals("d")) {
            drop_count++;
            if (branch_stack_size > 0) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    if (attributes.getQName(i).equals("e")) {
                        if (Integer.parseInt(attributes.getValue(i)) == edition_index + 1) {
                            dropFlag = true;
                            drop = new Drop();
                            break;
                        }
                    }
                }

                if (dropFlag) {
                    if(drop_count == drop_id) {
                        drop.getText().append("<div class='text-primary vewable'>");
                    }

                    for (int i = 0; i < attributes.getLength(); i++) {
                        if (attributes.getQName(i).equals("crumb")) {
							int hn = Math.min(branch_stack_size, 6);
                            drop.getText().append("<h").append(hn).append(">");
                            crambFlag = true;
                            break;
                        } else if (attributes.getQName(i).equals("root")) {
                            drop.getText().append("<strong>");
                            rootFlag = true;
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if (qName.equals("b")) {
            if (branch_stack_size > 0) {  // pop
                branch_stack_size--;
            }

            if (branch_stack_size == 0) {
                throw new DoneParsingException();
            }
        } else if (qName.equals("d")) {
            if (crambFlag) {
				int hn = Math.min(branch_stack_size, 6);
                drop.getText().append("</h").append(hn).append(">");
            }
			if (rootFlag) drop.getText().append("</strong>");
            if (dropFlag && drop_count == drop_id) drop.getText().append("</div>");
            if (branch_stack_size > 0 && dropFlag) drops.add(drop);
            crambFlag = false;
			rootFlag = false;
            dropFlag = false;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        if (currentTag != null && currentTag.equals("d") && branch_stack_size > 0 && dropFlag) {
            for (int i = start; i < start + length; i++) {
				if(ch[i] == '\r') {
					continue;
				}
                else if (ch[i] == '\n') {
                    drop.getText().append("<br />\r");
                }
                drop.getText().append(ch[i]);
            }
        }
    }
}
