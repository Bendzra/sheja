package jsp;

import java.util.ArrayList;
import java.util.List;

public class BookShelf
{
    public void setBooks(List<Book> books)
    {
        this.books = books;
    }

    private List<Book> books = new ArrayList<>();

    public List<Book> getBooks()
    {
        return books;
    }

    public boolean addBook(Book book)
    {
        return this.books.add(book);
    }
}
