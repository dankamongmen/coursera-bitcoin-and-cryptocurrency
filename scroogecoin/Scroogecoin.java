public class Scroogecoin {

public static void main(String [] args){
	UTXOPool opool = new UTXOPool();
	int len = 1;
	TxHandler txh = new TxHandler(opool);
	Transaction possible[] = new Transaction[len];
	System.out.println("here we go");
	for(int i = 0 ; i < len ; ++i){
		possible[i] = new Transaction();
		if(txh.isValidTx(possible[i])){
			System.out.println("TX " + i + " validates");
		}else{
			System.out.println("TX " + i + " fails");
		}
	}
	txh.handleTxs(possible);
}

}
