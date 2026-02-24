import java.util.Objects;

public class CandidateVoteCount {
    private String candidateName;
    private int validVoteCount;

    public CandidateVoteCount() {}

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public int getValidVoteCount() {
        return validVoteCount;
    }

    public void setValidVoteCount(int validVoteCount) {
        this.validVoteCount = validVoteCount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CandidateVoteCount that = (CandidateVoteCount) o;
        return validVoteCount == that.validVoteCount && Objects.equals(candidateName, that.candidateName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(candidateName, validVoteCount);
    }

    @Override
    public String toString() {
        return  candidateName +
                " = " + validVoteCount;
    }
}
