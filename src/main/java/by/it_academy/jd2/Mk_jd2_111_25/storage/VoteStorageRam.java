package by.it_academy.jd2.Mk_jd2_111_25.storage;

import by.it_academy.jd2.Mk_jd2_111_25.dto.CommentRecord;
import by.it_academy.jd2.Mk_jd2_111_25.storage.api.IVoteStorage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class VoteStorageRam implements IVoteStorage {
    private final Map<String, AtomicInteger> singers = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> genres = new ConcurrentHashMap<>();
    private final List<CommentRecord> comments = new CopyOnWriteArrayList<>();

    @Override
    public void addSingerVote(String name) {
        singers.computeIfAbsent(name, k -> new AtomicInteger(0)).incrementAndGet();
    }

    @Override
    public void addGenreVote(String genre) {
        genres.computeIfAbsent(genre, k -> new AtomicInteger(0)).incrementAndGet();
    }

    @Override
    public void addComment(CommentRecord comment) {
        if (comment != null) {
            comments.add(comment);
        }
    }

    @Override
    public Map<String, AtomicInteger> getSingers(){
        return singers;
    }

    @Override
    public Map<String, AtomicInteger> getGenres(){
        return genres;
    }

    @Override
    public List<CommentRecord> getComments(){
        return comments;
    }
}
