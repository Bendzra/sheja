package jsp;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class CrumbsKeeper
{
    private Edition edition;
    private List<Pair<Integer, String>> crumbs = new ArrayList<>();

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

    public List<Pair<Integer, String>> getCrumbs()
    {
        return crumbs;
    }

    public boolean addAll(List<Pair<Integer, String>> crumbs)
    {
        return this.crumbs.addAll(crumbs);
    }

    public boolean add(Pair<Integer, String> crumb)
    {
        return this.crumbs.add(crumb);
    }
}
