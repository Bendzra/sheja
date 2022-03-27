package jsp;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Found
{
    // одна пара = (key, value), где:
    // key -> drop,
    // value -> найденный результат = обрезанная строка
    //
    // pair = match = (пара1, пара2) = Pair<Pair, Pair>

    private List<Pair<Pair<Drop, String>, Pair<Drop, String>>> pairs = new ArrayList<>();

    public List<Pair<Pair<Drop, String>, Pair<Drop, String>>> getPairs()
    {
        return pairs;
    }

    public void setMatches(List<Pair<Pair<Drop, String>, Pair<Drop, String>>> pairs)
    {
        this.pairs = pairs;
    }

    public boolean addPairs(List<Pair<Pair<Drop, String>, Pair<Drop, String>>> pairs)
    {
        return this.pairs.addAll(pairs);
    }

    public boolean addPair(Pair<Pair<Drop, String>, Pair<Drop, String>> pair)
    {
        return this.pairs.add(pair);
    }
}

