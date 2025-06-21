package by.it_academy.jd2.Mk_jd2_111_25.service;

import by.it_academy.jd2.Mk_jd2_111_25.dto.CommentRecord;
import by.it_academy.jd2.Mk_jd2_111_25.dto.VoteResult;
import by.it_academy.jd2.Mk_jd2_111_25.service.api.IVoteService;
import by.it_academy.jd2.Mk_jd2_111_25.storage.api.IVoteStorage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class VoteService implements IVoteService {

    private final IVoteStorage storage;

    public VoteService (IVoteStorage storage) {
        this.storage = storage;
    }

    @Override
    public boolean processVote(String singer, String[] genres, String comment) {
        if (singer == null || genres == null || genres.length < 3 || genres.length > 5) {
            return false;
        }
        storage.addSingerVote(singer);
        for (String genre : genres) {
            if (genre != null && !genre.isBlank()) {
                storage.addGenreVote(genre);
            }
        }
        String safeComment = escapeHtml(comment == null ? "" : comment);
        storage.addComment( new CommentRecord(LocalDateTime.now(), safeComment));

        return true;
    }

    @Override
    public VoteResult getResults() {
        Map<String, Integer> singersCopy = sortVotes(storage.getSingers());
        Map<String, Integer> genresCopy = sortVotes(storage.getGenres());
        String commentsHtml = sortAndFormatComments();
        return new VoteResult(singersCopy, genresCopy, commentsHtml);

    }

    private Map<String, Integer> sortVotes(Map<String, AtomicInteger> source){
        return source.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue().get(), e1.getValue().get()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().get(),
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    private String sortAndFormatComments(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return storage.getComments().stream()
                .sorted(Comparator.comparing(CommentRecord::getTimestamp))
                .map(c -> "<p><b>" + c.getTimestamp().format(formatter) + ":</b> " + c.getContent() + "</p>")
                .collect(Collectors.joining("\n"));
    }


    private String escapeHtml(String input) {
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

}
