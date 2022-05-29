package controllers;

import jsp.Book;
import jsp.BookShelf;
import jsp.Drop;

import jsp.Found;
import org.xml.sax.SAXException;
import parsers.DoneParsingException;
import parsers.MatchesParserHandler;
import parsers.TestParserHandler;

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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@WebServlet(name = "ShejaController", value = "/sheja")
public class ShejaController extends HttpServlet
{
    public static BookShelf bookShelf = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        int tristate = -2;  
		// -2 : the request is not from a form
        //  0 : invalid form request
        // -1 : valid form request

        String qSting = req.getQueryString();
        List<Integer> reqBookIds = null;

        if (qSting != null && qSting.length() > 0) {
            tristate = 0;

            qSting = req.getParameter("q"); // search query
            if ((reqBookIds = checkBooks(req, qSting)) != null) {
                tristate = -1;
            }
        }

        if (tristate == 0) {
            resp.sendRedirect("/found");
            // search query error:
            // do nothing
            return;
        }

        shelveNIO(req);

        if (tristate == -2) {
            // home page:
            req.setAttribute("bookShelf", bookShelf);
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/jsp/ShejaView.jsp");
            requestDispatcher.forward(req, resp);
            return;
        }

        // tristate == -1
        doSearch(req, resp, qSting, reqBookIds);
    }

    private synchronized List<Integer> checkBooks(HttpServletRequest req, String qSting)
    {
        boolean r = (qSting != null && qSting.trim().length() > 0);
        String[] bs = req.getParameterValues("book");
        r &= (bs != null);
        if (!r) return null;
        List<Integer> bookIDs = new ArrayList<>(bs.length);
        for (int i = 0; i < bs.length; i++) {
            r &= isInt(bs[i]);
            if (r) {
                bookIDs.add(Integer.parseInt(bs[i]));
            } else {
                return null;
            }
        }
        return bookIDs;
    }

    private synchronized void shelveNIO(HttpServletRequest req) throws IOException
    {
        if (bookShelf != null) return;

        String dir = req.getSession().getServletContext().getRealPath("/resources");
        try (Stream<Path> stream = Files.walk(Paths.get(dir))) {
            List<String> xmls = stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::toString)
                    .filter(str -> str.endsWith(".xml"))
                    .sorted()
                    .collect(Collectors.toList());
            bookShelf = new BookShelf();
            for (int i = 0; i < xmls.size(); i++) {
                Book book = new Book();
                book.setId(i + 1);
                book.setXmlPath(xmls.get(i));
                book.parseEditionsXML();
                bookShelf.addBook(book);
            }
        }
    }

    private synchronized void doSearch(HttpServletRequest req, HttpServletResponse resp, String searchSting, List<Integer> reqBookIds) throws ServletException, IOException
    {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = null;

        try {
            parser = factory.newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

		// although it should be the whole (doGet) method synchronized
		// TODO:  static Drop.SearchOptions is not good at all

		boolean fMatchCase = (req.getParameter("case") != null);
		boolean fMatchWholeWord = (req.getParameter("word") != null);
		boolean fWylie = (req.getParameter("wylie") != null);
		boolean fRegEx = (req.getParameter("regex") != null);
		Drop.SearchOptions.apply(fMatchCase, fMatchWholeWord, fWylie, fRegEx, searchSting);
	
		List<Integer> interestingIDs = null;
		BookShelf ofInterest = new BookShelf();
		String cb = req.getParameter("cb");
		int curBook = isInt(cb) ? Integer.parseInt(cb) : 0;

		// if there is no current book in the query, filter the list of all books
		if (curBook == 0) {
			interestingIDs = new ArrayList<>();
			for (Integer id : reqBookIds) {
				TestParserHandler testHandler = new TestParserHandler();
				int book_index = id - 1;
				String path = bookShelf.getBooks().get(book_index).getXmlPath();
				boolean flag = false;
				try {
					File file = new File(path);
					parser.parse(file, testHandler);
				} catch (DoneParsingException e) {
					flag = true;
				} catch (SAXException | IOException e) {
					e.printStackTrace();
				}

				if(flag) {
					interestingIDs.add(id);
					ofInterest.addBook(bookShelf.getBooks().get(book_index));
				}
			}
		} else {
			interestingIDs = reqBookIds;
			for (Integer id: interestingIDs) {
				ofInterest.addBook(bookShelf.getBooks().get(id - 1));
			}
		}

        // find the result
        RequestDispatcher requestDispatcher;

        if (interestingIDs.size() == 0) {
            req.setAttribute("results", new Found());
            req.setAttribute("book", bookShelf.getBooks().get(0));
        } else {

            int book_index = (curBook == 0) ? interestingIDs.get(0) - 1 : curBook - 1;

            String path = bookShelf.getBooks().get(book_index).getXmlPath();

            MatchesParserHandler handler = new MatchesParserHandler();
            // search result page:
            try {
                File file = new File(path);
                parser.parse(file, handler);
                Found results = handler.getResults();
                req.setAttribute("results", results);
                req.setAttribute("book", bookShelf.getBooks().get(book_index));
                req.setAttribute("ofInterest", ofInterest);
            } catch (SAXException | IOException e) {
                e.printStackTrace();
            }
        }
        requestDispatcher = req.getRequestDispatcher("/jsp/DeliveryView.jsp");
        requestDispatcher.forward(req, resp);
    }

    private boolean isInt(String strNum)
    {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
