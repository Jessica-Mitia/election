import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        DataRetriever data = new DataRetriever();

        System.out.println(data.countAllVotes());
        System.out.println(data.countVotesByType());
        System.out.println(data.countValidVotesByCandidate());
        System.out.println(data.computeVoteSummary());
        System.out.println(data.findWinner());
        System.out.println(data.computeTurnoutRate());
    }
}
