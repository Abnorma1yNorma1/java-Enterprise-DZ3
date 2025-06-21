package by.it_academy.jd2.Mk_jd2_111_25.controller;

import by.it_academy.jd2.Mk_jd2_111_25.dto.VoteResult;
import by.it_academy.jd2.Mk_jd2_111_25.service.VoteService;
import by.it_academy.jd2.Mk_jd2_111_25.service.api.IVoteService;
import by.it_academy.jd2.Mk_jd2_111_25.storage.VoteStorageRam;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/vote")
public class QuestionnaireServlet extends HttpServlet {
    private final IVoteService service = new VoteService(new VoteStorageRam());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String singer = req.getParameter("singer");
        String[] genres = req.getParameterValues("genre");
        String comment = req.getParameter("about");

        boolean success = service.processVote(singer, genres, comment);
        if (!success) {
            req.setAttribute("error", "Ошибка: выберите исполнителя и от 3 до 5 поджанров.");
        } else {
            req.setAttribute("submitted", true);
            VoteResult results = service.getResults();
            req.setAttribute("singerVotes", results.getSingers());
            req.setAttribute("genreVotes", results.getGenres());
            req.setAttribute("comments", results.getCommentsHtml());
        }
        req.getRequestDispatcher("/vote_page.jsp").forward(req, resp);
    }

}
