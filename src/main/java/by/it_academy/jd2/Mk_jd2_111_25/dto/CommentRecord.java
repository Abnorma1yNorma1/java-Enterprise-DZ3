package by.it_academy.jd2.Mk_jd2_111_25.dto;

import java.time.LocalDateTime;

public class CommentRecord {


    private final LocalDateTime timestamp;
    private final String content;

    public CommentRecord(LocalDateTime timestamp, String content) {
        this.timestamp = timestamp;
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getContent() {
        return content;
    }
}
