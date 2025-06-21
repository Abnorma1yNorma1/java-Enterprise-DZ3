package by.it_academy.jd2.Mk_jd2_111_25.service.api;

import by.it_academy.jd2.Mk_jd2_111_25.dto.VoteResult;

public interface IVoteService {
    boolean processVote(String singer, String[] genres, String comment);
    VoteResult getResults();

}
