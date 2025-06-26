package by.it_academy.jd2.Mk_jd2_111_25.storage;

import by.it_academy.jd2.Mk_jd2_111_25.dto.CommentRecord;
import by.it_academy.jd2.Mk_jd2_111_25.storage.api.IVoteStorage;

import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class VoteStorageLocalDB implements IVoteStorage {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/local_votes";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";
    private static final String DB_SSL = "false";


    private Properties getProperties() {
        Properties props = new Properties();
        props.setProperty("user", DB_USER);
        props.setProperty("password", DB_PASSWORD);
        props.setProperty("ssl", DB_SSL);
        return props;
    }

    @Override
    public void addSingerVote(String name) {
        try (Connection conn = DriverManager.getConnection(DB_URL, getProperties())
        ){
            PreparedStatement select = conn.prepareStatement(
                    "SELECT votes FROM vote_app.singers WHERE name = ?");
            select.setString(1,name);
            ResultSet rs = select.executeQuery();
            if (rs.next()){
                int currentVotes = rs.getInt("votes");
                PreparedStatement update = conn.prepareStatement(
                    "UPDATE vote_app.singers SET votes = ? WHERE name = ?");
                update.setInt(1, currentVotes + 1);
                update.setString(2, name);
                update.executeUpdate();
            } else {
                PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO vote_app.singers(name, votes) VALUES(?, ?)"
                );
                insert.setString(1, name);
                insert.setInt(2, 1);
                insert.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add singer vote",e);
        }
    }

    @Override
    public void addGenreVote(String genre) {

        try (Connection conn = DriverManager.getConnection(DB_URL, getProperties())) {
            PreparedStatement select = conn.prepareStatement(
                "SELECT votes FROM vote_app.genres WHERE name = ?");
            select.setString(1, genre);
            ResultSet rs = select.executeQuery();
            if (rs.next()) {
                int currentVotes = rs.getInt("votes");
                PreparedStatement update = conn.prepareStatement(
                        "UPDATE vote_app.genres SET votes = ? WHERE name = ?");
                update.setInt(1, currentVotes + 1);
                update.setString(2, genre);
                update.executeUpdate();
            } else {
                PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO vote_app.genres (name, votes) VALUES (?, ?)");
                insert.setString(1, genre);
                insert.setInt(2, 1);
                insert.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add genre vote", e);
        }
    }

    @Override
    public void addComment(CommentRecord comment) {
        if (comment == null) return;
        try (Connection conn = DriverManager.getConnection(DB_URL, getProperties())){
            PreparedStatement insert = conn.prepareStatement(
                    "INSERT INTO vote_app.comments (dt_create, content) VALUES (?, ?)");
            insert.setTimestamp(1, Timestamp.valueOf(comment.getTimestamp()));
            insert.setString(2, comment.getContent());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add comment", e);
        }
    }

    @Override
    public Map<String, AtomicInteger> getSingers() {
        Map<String, AtomicInteger> result = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, getProperties());
             PreparedStatement statement = conn.prepareStatement("SELECT name, votes FROM vote_app.singers");
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                result.put(rs.getString("name"), new AtomicInteger(rs.getInt("votes")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get singers", e);
        }
        return result;
    }

    @Override
    public Map<String, AtomicInteger> getGenres() {
        Map<String, AtomicInteger> result = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, getProperties());
             PreparedStatement statement = conn.prepareStatement("SELECT name, votes FROM vote_app.genres");
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                result.put(rs.getString("name"), new AtomicInteger(rs.getInt("votes")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get genres", e);
        }
        return result;
    }

    @Override
    public List<CommentRecord> getComments() {
        List<CommentRecord> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, getProperties());
             PreparedStatement statement = conn.prepareStatement(
                "SELECT dt_create, content FROM vote_app.comments ORDER BY dt_create");
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                list.add(new CommentRecord(
                        rs.getTimestamp("dt_create").toLocalDateTime(),
                        rs.getString("content")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get comments", e);
        }
        return list;
    }
}
