package controllers;

import jsp.Drops;
import org.xml.sax.SAXException;
import parsers.OutlineParserHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

import static controllers.ShejaController.bookShelf;

@WebServlet(name = "OutlineController", value = "/outline")
public class OutlineController extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        RequestDispatcher requestDispatcher;
        // по полученным book_id и crumb_id (=branch_id) парсим xml и получаем outline.
        // forward'им и drop_id

        int book_id = 1;
        int crumb_id = 1;
        int drop_id = 1;

        try {
            book_id = Integer.parseInt(req.getParameter("book_id"));
            crumb_id = Integer.parseInt(req.getParameter("crumb_id"));
            drop_id = Integer.parseInt(req.getParameter("drop_id"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        String path = bookShelf.getBooks().get(book_id - 1).getXmlPath();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        OutlineParserHandler handler = new OutlineParserHandler(bookShelf.getBooks().get(book_id - 1), drop_id, crumb_id);
        SAXParser parser = null;

        try {
            parser = factory.newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

        try {
            File file = new File(path);
            parser.parse(file, handler);
            System.out.println("parseBranches: OK");
            Drops result = new Drops();
            result.drops = handler.getDrops();
            req.setAttribute("drops", result);
            req.setAttribute("book", bookShelf.getBooks().get(book_id - 1));
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }

        requestDispatcher = req.getRequestDispatcher("/jsp/OutlineView.jsp");
        requestDispatcher.forward(req, resp);

    }
}
