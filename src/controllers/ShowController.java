package controllers;

import jsp.Drops;
import org.xml.sax.SAXException;
import parsers.BranchesParserHandler;
import parsers.DoneParsingException;

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

@WebServlet(name = "ShowController", value = "/show")
public class ShowController extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        RequestDispatcher requestDispatcher;

        // по полученным book_id и crumb_id (=branch_id) парсим xml и получаем всю ветку
        // выделяем drop_id

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
        BranchesParserHandler handler = new BranchesParserHandler(bookShelf.getBooks().get(book_id - 1), drop_id, crumb_id);
        SAXParser parser = null;

        try {
            parser = factory.newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

        try {
            File file = new File(path);
            parser.parse(file, handler);
        } catch (DoneParsingException e) {
            System.out.println("parseBranches: OK");
            Drops result = new Drops();
            result.drops = handler.getDrops();
            req.setAttribute("drops", result);


        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }

        requestDispatcher = req.getRequestDispatcher("/jsp/ShowView.jsp");
        requestDispatcher.forward(req, resp);
    }
}
