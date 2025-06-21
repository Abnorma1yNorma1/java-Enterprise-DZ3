package by.it_academy.jd2.Mk_jd2_111_25.storage.api;

import by.it_academy.jd2.Mk_jd2_111_25.dto.CommentRecord;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public interface IVoteStorage {
    void addSingerVote(String name);
    void addGenreVote(String genre);
    void addComment(CommentRecord comment);
    Map<String, AtomicInteger> getSingers();
    Map<String, AtomicInteger> getGenres();
    List<CommentRecord> getComments();
}
