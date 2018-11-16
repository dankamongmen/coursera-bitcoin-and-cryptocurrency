import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/* CompliantNode refers to a node that follows the rules (not malicious)*/
public class CompliantNode implements Node {

    private double graph;
    private double malicious;
    private double tXDistribution;
    private int numRounds;

    private boolean[] followees;

    private Set<Transaction> pendingTransactions;

    private boolean[] blackListed;

    public CompliantNode(double p_graph, double p_malicious, double p_txDistribution, int numRounds) {
        this.graph = p_graph;
        this.malicious = p_malicious;
        this.tXDistribution = p_txDistribution;
        this.numRounds = numRounds;
    }

    public void setFollowees(boolean[] followees) {
        this.followees = followees;
        this.blackListed = new boolean[followees.length];
    }

    public void setPendingTransaction(Set<Transaction> pendingTransactions) {
        this.pendingTransactions = pendingTransactions;
    }

    public Set<Transaction> sendToFollowers() {
        Set<Transaction> toSend = new HashSet<>(pendingTransactions);
        pendingTransactions.clear();
        return toSend;
    }

    public void receiveFromFollowees(Set<Candidate> candidates) {
        Set<Integer> senders = candidates.stream().map(c -> c.sender).collect(toSet());
        for (int i = 0; i < followees.length; i++) {
            if (followees[i] && !senders.contains(i))
                blackListed[i] = true;
        }
        for (Candidate c : candidates) {
            if (!blackListed[c.sender]) {
                pendingTransactions.add(c.tx);
            }
        }
    }
}
