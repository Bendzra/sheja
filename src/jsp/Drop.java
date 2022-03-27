package jsp;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Drop
{
    private int id;
    private int fuseId;
    private Edition edition;
    private StringBuilder text = new StringBuilder(500);
    private List<Integer> starts = new ArrayList<>(); // начала вхождения searchSting в text
    private List<Integer> ends = new ArrayList<>(); // концы вхождения searchSting в text
    private List<Pair<Integer, String>> chapters = new ArrayList<>();

    public static class SearchOptions
    {
        private static boolean fMatchCase;
        private static boolean fMatchWholeWord;
        private static boolean fRegEx;
        private static String searchSting;
        private static Pattern pattern;
        private static boolean fWylie;

        public static void apply(boolean matchCase,
                                 boolean wholeWord,
                                 boolean wylie,
                                 boolean regExp,
                                 String toFind)
        {
            fMatchCase = matchCase;
            fMatchWholeWord = wholeWord;
            fWylie = wylie;
            fRegEx = regExp;
            searchSting = toFind;

            int flags = Pattern.UNICODE_CHARACTER_CLASS | Pattern.UNICODE_CASE;
            if (!fMatchCase) flags |= Pattern.CASE_INSENSITIVE;

            if (!fRegEx) {
                // spec chars: \.[]{}()<>*+-=!?^$|
                String[] rx = new String[]{
                        "\\\\", "\\.", "\\[", "\\]", "\\{", "\\}", "\\(", "\\)", "\\<", "\\>",
                        "\\*", "\\+", "\\-", "\\=", "\\!", "\\?", "\\^", "\\$", "\\|"
                };
                String[] rt = new String[]{
                        "\\\\\\\\", "\\\\.", "\\\\[", "\\\\]", "\\\\{", "\\\\}", "\\\\(", "\\\\)", "\\\\<", "\\\\>",
                        "\\\\*", "\\\\+", "\\\\-", "\\\\=", "\\\\!", "\\\\?", "\\\\^", "\\\\$", "\\\\|"
                };
                for (int i = 0; i < rx.length; i++) {
                    searchSting = searchSting.replaceAll(rx[i], rt[i]);
                }
            }

            if (fMatchWholeWord) {
                if (fWylie) {
                    searchSting = "(?<=^|[\\s\")(_/!*\\[\\]{},:@#])"
                            + searchSting + "(?=[\\s\")(_/!*\\[\\]{},:@#]|$)";

                } else {
                    searchSting = "\\b" + searchSting + "\\b";
                }
            }
            pattern = Pattern.compile(searchSting, flags);
        }
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getEditionId()
    {
        return edition.getId();
    }

    public Edition getEdition()
    {
        return edition;
    }

    public void setEdition(Edition edition)
    {
        this.edition = edition;
    }

    public StringBuilder getText()
    {
        return text;
    }

    public void setText(StringBuilder text)
    {
        this.text = text;
    }

    public List<Integer> getStarts()
    {
        return starts;
    }

    public void setStarts(List<Integer> starts)
    {
        this.starts = starts;
    }

    public List<Integer> getEnds()
    {
        return ends;
    }

    public void setEnds(List<Integer> ends)
    {
        this.ends = ends;
    }

    public int getFuseId()
    {
        return fuseId;
    }

    public void setFuseId(int fuseId)
    {
        this.fuseId = fuseId;
    }

    @Override
    public String toString()
    {
        return "Drop{" +
                "edition=" + edition +
                ", text=" + text +
                '}';
    }

    public List<Integer> findIndexes()
    {
        Matcher matcher = SearchOptions.pattern.matcher(text);
        while (matcher.find()) {
            starts.add(matcher.start());
            ends.add(matcher.end());
        }
        return starts;
    }

    public List<Pair<Integer, String>> getChapters()
    {
        return chapters;
    }

    public boolean addChapters(List<Pair<Integer, String>> chapters)
    {
        return this.chapters.addAll(chapters);
    }
}