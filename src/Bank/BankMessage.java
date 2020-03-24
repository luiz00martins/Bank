package Bank;

import java.io.Serializable;

public class BankMessage implements Serializable {
    private final String message;
    private final SensitiveAccount accOwned;
    private final Password ownedPass;
    private final Balance amount;
    private final Account accDest;
    private final Exception expt;

    public BankMessage(String message, SensitiveAccount accOwned, Password ownedPass, Balance amount, Account accDest, Exception expt) {
        this.message = message;
        this.accOwned = accOwned;
        this.ownedPass = ownedPass;
        this.amount = amount;
        this.accDest = accDest;
        this.expt = expt;
    }

    public String getMessage(){
        return message;
    }

    public SensitiveAccount getOwned(){
        return accOwned;
    }

    public Password getOwnedPass() {
        return ownedPass;
    }

    public Balance getAmount() {
        return amount;
    }

    public Account getDest() {
        return accDest;
    }

    public Exception getExpt() {
        return expt;
    }
}
