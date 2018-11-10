import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/* CompliantNode refers to a node that follows the rules (not malicious)*/
public class CompliantNode implements Node {

    public CompliantNode(double p_graph, double p_malicious, double p_txDistribution, int numRounds) {
        // IMPLEMENT THIS
    }

    public void setFollowees(boolean[] followees) {
        // IMPLEMENT THIS
    }

    public void setPendingTransaction(Set<Transaction> pendingTransactions) {
        // IMPLEMENT THIS
    }

    public Set<Transaction> sendToFollowers() {
	Set<Transaction> s = new HashSet<Transaction>();
        // IMPLEMENT THIS
	return s;
    }

    public void receiveFromFollowees(Set<Candidate> candidates) {
        // IMPLEMENT THIS
    }
}
