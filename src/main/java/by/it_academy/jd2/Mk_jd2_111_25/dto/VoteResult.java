package by.it_academy.jd2.Mk_jd2_111_25.dto;

import java.util.Map;

public class VoteResult {
    private final Map<String, Integer> singers;
    private final Map<String, Integer> genres;
    private final String commentsHtml;

    public VoteResult(Map<String, Integer> singers, Map<String, Integer> genres, String commentsHtml) {
        this.singers = singers;
        this.genres = genres;
        this.commentsHtml = commentsHtml;
    }

    public Map<String, Integer> getSingers() {
        return singers;
    }

    public Map<String, Integer> getGenres() {
        return genres;
    }

    public String getCommentsHtml() {
        return commentsHtml;
    }
}
