import java.util.Arrays;
import java.util.HashSet;
import java.util.ArrayList;
import java.security.PublicKey;

public class TxHandler {

	private UTXOPool upool;

    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    public TxHandler(UTXOPool utxoPool) {
	    upool = new UTXOPool(utxoPool);
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool, 
     * (2) the signatures on each input of {@code tx} are valid, 
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     *     values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
	    UTXOPool spentpool = new UTXOPool();
	    int innum = tx.numInputs();
	    int outnum = tx.numOutputs();
	    ArrayList<UTXO> utxo = upool.getAllUTXO();
	    int unum = utxo.size();
	    System.out.println("inputs: " + innum + " outputs: " + outnum + " upool: " + unum);
	    double intotal = 0;
	    double outtotal = 0;
	    for(int i = 0 ; i < innum ; ++i){
		Transaction.Input in = tx.getInput(i);
		UTXO unew = new UTXO(in.prevTxHash, in.outputIndex);
		Transaction.Output out = upool.getTxOutput(unew);
		if(out == null){
			System.out.println("upool doesn't contain UTXO for " + i);
			return false;
		}
		byte msg[] = tx.getRawDataToSign(i);
		byte txid[] = in.prevTxHash;
		PublicKey pubkey = out.address;
		if(!Crypto.verifySignature(pubkey, msg, in.signature)){
			System.out.println("bad signature for " + i);
			return false;
		}
		double value = out.value;
		intotal += value;
		System.out.println("verified " + i + " value: " + value);
		if(spentpool.contains(unew)){
			return false;
		}
		spentpool.addUTXO(unew, out);
		// breaks most validation
		// upool.removeUTXO(unew);
	    }
	    for(int i = 0 ; i < outnum ; ++i){
		    Transaction.Output out = tx.getOutput(i);
		    double value = out.value;
		    System.out.println("output " + i + " value " + value);
		    if(value < 0){
			    return false;
		    }
		    outtotal += value;
		    /*
		    UTXO u = new UTXO(tx.getHash(), i);
		    upool.addUTXO(u, out);
		    */
	    }
	    if(outtotal > intotal){
		    System.out.println("output " + outtotal + " > input " + intotal);
		    return false;
	    }
	    return true;
    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
	HashSet<Transaction> keep = new HashSet<>();
        for(Transaction tx : possibleTxs){
            if(isValidTx(tx)){
                keep.add(tx);
                for(Transaction.Input in : tx.getInputs()){
                    UTXO utxo = new UTXO(in.prevTxHash, in.outputIndex);
                    upool.removeUTXO(utxo);
                }
                for(int i = 0; i < tx.numOutputs(); i++){
                    Transaction.Output out = tx.getOutput(i);
                    UTXO utxo = new UTXO(tx.getHash(), i);
                    upool.addUTXO(utxo, out);
                }
            }
        }
        Transaction[] validTxArray = new Transaction[keep.size()];
        return keep.toArray(validTxArray);
    }

}
