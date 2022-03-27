package parsers;

import javafx.util.Pair;
import jsp.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class MatchesParserHandler extends DefaultHandler
{
    private List<Edition> editions = new ArrayList<>();
    private List<CrumbsKeeper> spreadsheet = new ArrayList<>(); // Текущие списки crumbs для всех всех editions
    private int stepsAway = 0;  // как далеко отошли по крошкам (= stack.size)
    private int branchID = 0;   // <b>...</b>
    private int fuseID = 0;     // <fuse>...</fuse>
    private int dropID = 0;     // <drop>...</drop>
    private String currentTag = null;
    private Drop drop = null;
    private Fuse fuse = null;
    private boolean found = false;
    private Found results = new Found();

    private final int MAX_PAIRS = 402;

    private static int count = 0;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        currentTag = qName;
        if (qName.equals("edition")) {
            Edition edition = EditionsParserHandler.newEdition(attributes);
            editions.add(edition);
            CrumbsKeeper editionCrumbs = new CrumbsKeeper();
            editionCrumbs.setEdition(edition);
            spreadsheet.add(editionCrumbs);
        } else if (qName.equals("b")) {
            branchID++;
            stepsAway++;
            spreadsheet.forEach(crumbsKeeper -> crumbsKeeper.add(new Pair<>(-1, "_placeholder_")));
        } else if (qName.equals("fuse")) {
            fuse = new Fuse();
            fuse.setId(++fuseID);
            found = false;
        } else if (qName.equals("drop")) {
            drop = new Drop();
            drop.setId(++dropID);
            drop.setFuseId(fuseID);

            // добавляем edition
            for (int i = 0; i < attributes.getLength(); i++) {
                if (attributes.getQName(i).equals("edition_id")) {
                    for (Edition edition : editions) {
                        if (edition.getId().equals(attributes.getValue(i))) {
                            drop.setEdition(edition);
                            break;
                        }
                    }
                    break;
                }
            }

            // проверяем на crumb и добавляем в текущий список крошек, если есть
            for (int i = 0; i < attributes.getLength(); i++) {
                if (attributes.getQName(i).equals("crumb")) {
                    for (CrumbsKeeper crumbsKeeper : spreadsheet) {
                        if (crumbsKeeper.getEditionId().equals(drop.getEditionId())) {
                            crumbsKeeper.getCrumbs().set(stepsAway - 1, new Pair<>(branchID, attributes.getValue(i)));
                            break;
                        }
                    }
                    break;
                }
            }

            for (CrumbsKeeper crumbsKeeper : spreadsheet) {
                if (crumbsKeeper.getEditionId().equals(drop.getEditionId())) {
                    drop.addChapters(crumbsKeeper.getCrumbs());
                    break;
                }
            }
            fuse.addDrop(drop);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        if (currentTag != null && currentTag.equals("drop")) {
            drop.getText().append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if (results.getPairs().size() < MAX_PAIRS) {
            if (qName.equals("drop")) {

                List<Integer> integers = drop.findIndexes();
                int t = integers.size();
                if (t > 0) {
                    found = true;
                    count += t;
                    System.out.println(count);
                }
            } else if (qName.equals("fuse")) {
                if (found) {
                    fuse.entwine();
                    results.addPairs(fuse.getMatches().getPairs());

                    if (results.getPairs().size() >= MAX_PAIRS - 1) {
                        Pair<Drop, String> pair1 = results.getPairs().get(results.getPairs().size() -1).getKey();
                        Pair<Drop, String> pair2 = results.getPairs().get(results.getPairs().size() -1).getValue();
                        results.addPair(
                                new Pair(
                                        new Pair(pair1.getKey(), "<div class='p-3 mb-2 bg-info text-white'>limit exceeded</div>"),
                                        new Pair(pair2.getKey(), "<div class='p-3 mb-2 bg-info text-white'>the job to remove all restrictions is to be done although</div>")
                                )
                        );
                    }
                }
            } else if (qName.equals("b")) {
                stepsAway--;
                spreadsheet.forEach(crumbsKeeper -> crumbsKeeper.getCrumbs().remove(stepsAway));
            }
        }
        currentTag = null;
    }

    @Override
    public void endDocument() throws SAXException
    {
        count = 0;
    }

    public Found getResults()
    {
        return results;
    }
}
