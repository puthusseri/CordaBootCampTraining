package bootcamp;

import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;

/* Our contract, governing how our state will evolve over time.
 * See src/main/java/examples/ArtContract.java for an example. */
public class TokenContract implements Contract {
    public static String ID = "bootcamp.TokenContract";

    @Override
    public void verify(LedgerTransaction tx) throws IllegalArgumentException {
        if (tx.getInputs().size() !=0){
            throw new IllegalArgumentException("zero arguments should be there");
        }
        if(tx.getOutputs().size() != 1 ) {
            throw new IllegalArgumentException(".Invalid.There are two outputs.");
        }
        if(tx.getCommands().size() !=1){
            throw new IllegalArgumentException("Has two command. Illegal");
        }

        if(!(tx.getOutput(0) instanceof TokenState)) {
            throw new IllegalArgumentException("Output of tokenState expected");
        }

        TokenState tokenState = (TokenState)tx.getOutput(0);
        if(tokenState.getAmount() <1){
            throw new IllegalArgumentException("The amount should be positive");
        }

        //check if the cmd is issue cmd
        if(!(tx.getCommand(0).getValue() instanceof Commands.Issue)){
            throw new IllegalArgumentException("Issue cmd expected");
        }

        //validate by correct signer

        if(!(tx.getCommand(0).getSigners().contains(tokenState.getIssuer().getOwningKey()))){
            throw new IllegalArgumentException("The issuer must sign the contract.");

        }

    }



    public interface Commands extends CommandData {
        class Issue implements Commands { }
    }
}