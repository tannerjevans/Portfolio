import org.json.JSONArray;

/**
 * BankAccount is a utility class for use by Bank, which has been extracted
 * only because it is non-static, its members are all protected, and it is
 * obnoxiously large for a nested class. It provides a framework for the
 * creation of BankAccount objects, as well as convenience methods for
 * accessing information and performing account status changes.
 *
 * @author  Tanner J. Evans
 * @version %I%, %G%
 * @since   11.0.8
 */

public class BankAccount {
    protected final  ConnectionHandler  CONNECTION;
    protected final  int                ACCOUNT_ID;
    protected final  String             CLIENT_NAME;
    protected final  String             ADDRESS;
    protected final  String             PORT;
    protected final  Message.ClientType CLIENT_TYPE;
    private   final  KeyList<Integer, Transaction>
                                        TRANSACTIONS = new KeyList<>();
    private   double totalBalance;
    private   double availableFunds;

    protected BankAccount(Message message, ConnectionHandler connection) {
        CONNECTION     = connection;
        ACCOUNT_ID     = message.getAccountID();
        CLIENT_NAME    = message.getName();
        ADDRESS        = message.getAddress();
        PORT           = message.getPort();
        CLIENT_TYPE    = message.getClientType();
        totalBalance   = message.getAmount();
        availableFunds = message.getAmount();
    }

    protected void withdraw(Message message) {
        double amount = message.getAmount();
        message.setTransactionType(Message.TransactionType.WITHDRAW);
        Transaction transaction = new Transaction(message);
        TRANSACTIONS.put(message.getTransactionID(), transaction);
        totalBalance   -= amount;
        availableFunds -= amount;
    }

    protected void deposit(Message message) {
        double amount = message.getAmount();
        message.setTransactionType(Message.TransactionType.DEPOSIT);
        int targetAccount = message.getTargetAccount();
        message.setTargetAccount(message.getOtherAccount());
        message.setOtherAccount(targetAccount);
        Transaction transaction = new Transaction(message);
        TRANSACTIONS.put(message.getTransactionID(), transaction);
        totalBalance   += amount;
        availableFunds += amount;
    }

    protected Code block(Message message) {
        double amount = message.getAmount();
        if (amount > availableFunds) return Code.INSUFFICIENT_FUNDS;
        Transaction transaction = new Transaction(message);
        TRANSACTIONS.put(message.getTransactionID(), transaction);
        availableFunds -= amount;
        return Code.SUCCESS;
    }

    protected void unblock(Message message, int id) {
        Transaction transaction = TRANSACTIONS.remove(id);
        System.out.println("Transaction amount: " + transaction.AMOUNT);
        System.out.println("AvailableFunds: " + availableFunds);
        availableFunds = availableFunds + transaction.AMOUNT;
        System.out.println("New AvailableFunds: " + availableFunds);
    }

    protected Message getAccountSummary() {
        Message accountSummary = new Message();
        accountSummary.setCode(Code.ACCOUNT_SUMMARY)
                      .setAvailableFunds(availableFunds)
                      .setTotalBalance(totalBalance)
                      .setAccountID(ACCOUNT_ID)
                      .setName(CLIENT_NAME);
        JSONArray jsonArray = new JSONArray();
        for (Transaction transaction : TRANSACTIONS.values()) {
            jsonArray.put(transaction.toStr());
        }
        accountSummary.setTransactionList(jsonArray);
        return accountSummary;
    }
}
