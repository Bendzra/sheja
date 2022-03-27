package jsp;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Fuse
{
    private int id;
    private List<Drop> drops = new ArrayList<>(); // excerpts
    public static int MAX_CHARS = 0x7F;
    private Found matches = new Found();

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public List<Drop> getDrops()
    {
        return drops;
    }

    public boolean addDrop(Drop drop)
    {
        return this.drops.add(drop);
    }

    @Override
    public String toString()
    {
        return "Fuse{" +
                "drops=" + drops +
                '}';
    }

    public void entwine()
    {
        for (int i = 0; i < drops.size(); i++) {
            Drop d1 = drops.get(i);
            for (int j = 0; j < d1.getStarts().size(); j++) {
                int start1 = d1.getStarts().get(j);
                int end1 = d1.getEnds().get(j);
                String s1 = doleOut(d1, start1, end1, MAX_CHARS, true);
                for (int k = 0; k < drops.size(); k++) {
                    if (k == i) continue;
                    Drop d2 = drops.get(k);
                    float f = (float) d2.getText().length() / d1.getText().length();
                    int start2 = (int) (f * start1);
                    int end2 = (int) (f * end1);
                    int span = (int) (f * MAX_CHARS);
                    String s2 = doleOut(d2, start2, end2, Integer.max(span, MAX_CHARS), false);

                    matches.addPair(new Pair( new Pair(d1, s1), new Pair(d2, s2)));
                }
            }
        }
    }

    private int leftBranch(Drop drop, int start, int span)
    {
        int left = Integer.max(0, start - span);
        while (left > 0) {
            char ch = drop.getText().charAt(left);
            if (ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t') {
                left++;
                break;
            }
            left--;
        }
        return left;
    }

    private int rightBranch(Drop drop, int end, int span)
    {
        int right = Integer.min(end + span, drop.getText().length());
        while (right < drop.getText().length()) {
            char ch = drop.getText().charAt(right);
            if (ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t') {
//                right--;
                break;
            }
            right++;
        }
        return right;
    }

    private String doleOut(Drop drop, int start, int end, int span, boolean select)
    {
        int left = leftBranch(drop, start, span);
        int leftLeft = leftBranch(drop, left, 2 * span);
        int right = rightBranch(drop, end, span);
        int rightRight = rightBranch(drop, right, 2 * span);

        String overLeft = "<div class='middle'>";
        if (left > 0) {
            overLeft = "<div class='collapse hidden_'>"
                    + drop.getText().substring(leftLeft, left)
                    + "</div><div class='middle'><a class='btn btn-sm toggleLeft'><i class='fa fa-chevron-up'></i></a> ";
        }

        String overRight = "</div>";
        if (right < drop.getText().length()) {
            overRight = " <a class='btn btn-sm toggleRight'><i class='fa fa-chevron-down'></i></a></div><div class='collapse _hidden'>"
                    + drop.getText().substring(right, rightRight) + "</div>";
        }

        String middle = (select) ? drop.getText().substring(left, start)
                + "<b class='text-info'>" + drop.getText().substring(start, end) + "</b>"
                + drop.getText().substring(end, right) : drop.getText().substring(left, right);

        return overLeft + middle + overRight;
    }

    public Found getMatches()
    {
        return matches;
    }
}
