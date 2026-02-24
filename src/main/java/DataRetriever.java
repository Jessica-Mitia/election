import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    public long countAllVotes () throws SQLException {
        DBConnection db = new DBConnection();
        long count = 0;
        String sql = "SELECT count(vote.id) as total_votes FROM vote";

        try (Connection connection = db.getDBConnection()){
            try (PreparedStatement statement = connection.prepareStatement(sql)){
                ResultSet rs = statement.executeQuery();
                if (rs.next()){
                    count = rs.getLong(1);
                }
            }
        }
        return count;
    }

    public List<VoteTypeCount> countVotesByType () throws SQLException {
        DBConnection db = new DBConnection();
        List<VoteTypeCount> votes = new ArrayList<VoteTypeCount>();
        String sql = "SELECT vote_type, count(*) as total FROM vote GROUP BY vote_type";
        try (Connection connection = db.getDBConnection()){
            try (PreparedStatement statement = connection.prepareStatement(sql)){
                ResultSet rs = statement.executeQuery();
                while (rs.next()){
                    VoteTypeCount vote = new VoteTypeCount();
                    vote.setVoteType(VoteType.valueOf(rs.getString("vote_type")));
                    vote.setCount(rs.getInt("total"));

                    votes.add(vote);
                }
            }
        }
        return votes;
    }

    public List<CandidateVoteCount> countValidVotesByCandidate() throws SQLException {
        DBConnection db = new DBConnection();
        String sql = """
                SELECT c.name AS candidate_name,
                COUNT(CASE WHEN v.vote_type = 'VALID' THEN 1 END) AS valid_vote_count
                FROM candidate c
                LEFT JOIN vote v ON c.id = v.candidate_id
                GROUP BY c.name
                ORDER BY c.name;
                """;
        List<CandidateVoteCount> candidateVoteCounts = new ArrayList<>();

        try (Connection connection = db.getDBConnection()){
            try (PreparedStatement statement = connection.prepareStatement(sql)){
                ResultSet rs = statement.executeQuery();
                while (rs.next()){
                    CandidateVoteCount vote = new CandidateVoteCount();
                    vote.setCandidateName(rs.getString("candidate_name"));
                    vote.setValidVoteCount(rs.getInt("valid_vote_count"));

                    candidateVoteCounts.add(vote);
                }
            }
        }
        return candidateVoteCounts;
    }


    public VoteSummary computeVoteSummary () throws SQLException {
        DBConnection db = new DBConnection();
        VoteSummary summary = new VoteSummary();
        String sql = """
                    SELECT count( CASE WHEN vote.vote_type = 'VALID' then vote.voter_id END) as valid_count,
                           count( CASE WHEN vote.vote_type = 'BLANK' then vote.voter_id END) as blank_count,
                           count( CASE WHEN vote.vote_type = 'NULL' then vote.voter_id END) as null_count
                    FROM vote;
                """;

        try (Connection connection = db.getDBConnection()){
            try (PreparedStatement statement = connection.prepareStatement(sql)){
                ResultSet rs = statement.executeQuery();
                if (rs.next()){
                    summary.setValidCount(rs.getInt("valid_count"));
                    summary.setBlankCount(rs.getInt("blank_count"));
                    summary.setNullCount(rs.getInt("null_count"));
                }
            }
        }
        return summary;
    }


    public double computeTurnoutRate() throws SQLException {
        DBConnection db = new DBConnection();
        double turnout = 0.0;

        String sql = """
                    SELECT
                        (COUNT(DISTINCT vote.voter_id) * 100.0) /
                        (SELECT COUNT(*) FROM voter) AS turnout_rate
                    FROM vote;
                """;

        try (Connection connection = db.getDBConnection()){
            try (PreparedStatement statement = connection.prepareStatement(sql)){
                ResultSet rs = statement.executeQuery();
                if (rs.next()){
                    turnout += rs.getDouble("turnout_rate");
                }
            }
        }

        return turnout;
    }


    public ElectionResult findWinner() throws SQLException {
        DBConnection db = new DBConnection();
        ElectionResult result = new ElectionResult();
        String sql = """
                    SELECT c.name AS candidate_name,
                         COUNT(CASE WHEN v.vote_type = 'VALID' THEN 1 END) AS valid_vote_count
                         FROM candidate c
                         LEFT JOIN vote v ON c.id = v.candidate_id
                         GROUP BY c.name
                         ORDER BY valid_vote_count DESC
                         LIMIT 1;
                """;

        try (Connection connection = db.getDBConnection()){
            try (PreparedStatement statement = connection.prepareStatement(sql)){
                ResultSet rs = statement.executeQuery();
                if (rs.next()){
                    result.setCandidateName(rs.getString("candidate_name"));
                    result.setValidVoteCount(rs.getInt("valid_vote_count"));
                }
            }
        }
        return result;
    }
}
