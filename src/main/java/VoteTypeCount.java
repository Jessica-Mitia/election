import java.util.Objects;

public class VoteTypeCount {
    private VoteType voteType;
    private int count;

    public VoteTypeCount() {}

    public VoteType getVoteType() {
        return voteType;
    }

    public void setVoteType(VoteType voteType) {
        this.voteType = voteType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VoteTypeCount that = (VoteTypeCount) o;
        return count == that.count && voteType == that.voteType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(voteType, count);
    }

    @Override
    public String toString() {
        return "(VoteTypeCount(" +
                "voteType=" + voteType +
                ", count=" + count +
                ')';
    }
}
