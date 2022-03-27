package jsp;

public class Edition
{
    private String id = "";
    private String lang = "";
    private String title = "";
    private String author = "";
    private String translator = "";

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getLang()
    {
        return lang;
    }

    public void setLang(String lang)
    {
        this.lang = lang;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getTranslator()
    {
        return translator;
    }

    public void setTranslator(String translator)
    {
        this.translator = translator;
    }

    @Override
    public String toString()
    {
        return "Edition{" +
                "id='" + id + '\'' +
                ", lang='" + lang + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", translator='" + translator + '\'' +
                '}';
    }
}
