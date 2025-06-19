package by.it_academy.jd2.Mk_jd2_111_25;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@WebServlet(urlPatterns = "/vote")
public class QuestionnaireServlet extends HttpServlet {

    private final Map<String, AtomicInteger> singerVotes = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> genreVotes = new ConcurrentHashMap<>();

    private final StringBuilder comments = new StringBuilder();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        String singer = req.getParameter("singer");
        String[] genres = req.getParameterValues("genre");
        String comment = req.getParameter("about");

        if (singer == null || genres == null || genres.length < 3 || genres.length > 5) {
            req.setAttribute("error", "Ошибка: выберите исполнителя и от 3 до 5 поджанров.");
            req.getRequestDispatcher("/vote_page.jsp").forward(req, resp);
            return;
        }
        singerVotes.computeIfAbsent(singer, k -> new AtomicInteger(0))
                .incrementAndGet();
        for (String genre: genres){
            if (genre != null && !genre.isBlank()){
                genreVotes
                        .computeIfAbsent(genre, k -> new AtomicInteger(0))
                        .incrementAndGet();
            }
        }
        synchronized (comments) {
            comments.append("<p><b>")
                    .append(java.time.LocalDateTime.now()) // время
                    .append(":</b> ")
                    .append(escapeHtml(comment == null ? "" : comment))
                    .append("</p>\n");
        }
        req.setAttribute("submitted", true);
        req.setAttribute("singerVotes", singerVotes.entrySet());
        req.setAttribute("genreVotes", genreVotes.entrySet());
        req.setAttribute("comments", comments.toString());

        req.getRequestDispatcher("/vote_page.jsp").forward(req, resp);

    }

    private String escapeHtml(String input) {
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

}
