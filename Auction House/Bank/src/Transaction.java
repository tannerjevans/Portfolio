/**
 * Transaction is a utility class for use by Bank and BankAccount, and
 * provides a framework for ensuring that certain data requirements are upheld.
 *
 * @author  Tanner J. Evans
 * @version %I%, %G%
 * @since   11.0.8
 */

public class Transaction {
    protected final int                     TRANSACTION_ID;
    protected final Message.TransactionType TRANSACTION_TYPE;
    protected final double                  AMOUNT;
    protected final int                     OTHER_ACCOUNT;

    protected Transaction(Message message) {
        TRANSACTION_ID   = message.getTransactionID();
        TRANSACTION_TYPE = message.getTransactionType();
        AMOUNT           = message.getAmount();
        OTHER_ACCOUNT    = message.getOtherAccount();
    }

    protected String toStr() {
        Message message = new Message();
        message.setTransactionID(TRANSACTION_ID)
               .setTransactionType(TRANSACTION_TYPE)
               .setAmount(AMOUNT)
               .setOtherAccount(OTHER_ACCOUNT);
        return message.toString();
    }
}
