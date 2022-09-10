package jsp;

import java.util.ArrayList;
import java.util.List;

public class BookShelf
{
    private List<Book> books = new ArrayList<>();
    public List<Integer> default_checked_ids = new ArrayList();

    public void setBooks(List<Book> books)
    {
        this.books = books;
    }

    public List<Book> getBooks()
    {
        return books;
    }

    public boolean addBook(Book book)
    {
        return this.books.add(book);
    }
}
