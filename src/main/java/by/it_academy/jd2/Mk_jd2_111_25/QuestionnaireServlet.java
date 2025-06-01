package by.it_academy.jd2.Mk_jd2_111_25;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
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

        String singer = req.getParameter("singer");
        String[] genres = req.getParameterValues("genre");
        String comment = req.getParameter("about");

        if (singer == null || singer.isBlank() || genres == null || genres.length < 3 || genres.length > 5) {
            try (PrintWriter out = resp.getWriter()) {
                out.println("<html><head><title>Ошибка</title></head><body>");
                out.println("<h2 style='color:red;'>Ошибка: необходимо выбрать исполнителя и от 3 до 5 поджанров.</h2>");
                out.println("</body></html>");
                return;
            }
        } else {
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
        }

        try (PrintWriter out = resp.getWriter()) {
            out.println("<html><head><title>Результаты голосования</title></head><body>");

            out.println("<h1>Результаты голосования за исполнителей</h1>");
            out.println("<ul>");
            singerVotes.entrySet()
                    .stream()
                    .sorted((e1, e2) -> Integer.compare(e2.getValue().get(), e1.getValue().get()))
                    .forEach(entry -> out.println(
                            "<li>" + escapeHtml(entry.getKey()) + ": " + entry.getValue() + "</li>")
                    );
            out.println("</ul>");

            out.println("<h1>Результаты голосования за поджанры</h1>");
            out.println("<ul>");
            genreVotes.entrySet()
                    .stream()
                    .sorted((e1, e2) -> Integer.compare(e2.getValue().get(), e1.getValue().get()))
                    .forEach(entry -> out.println(
                            "<li>" + escapeHtml(entry.getKey()) + ": " + entry.getValue() + "</li>")
                    );
            out.println("</ul>");

            out.println("<h2>Комментарии:</h2>");
            synchronized (comments) {
                out.print(comments.toString());
            }

            out.println("</body></html>");
        }

    }

    private String escapeHtml(String input) {
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

}
