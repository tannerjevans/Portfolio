import java.io.*;
import java.net.*;
import java.util.Random;


/**
 * Bank is the main class for the AuctionHouse application. It can be
 * started with a command-line argument detailing the desired Port to start on.
 *
 * The only non-private member is Main. The remaining members are for
 * exclusive use of this class. The Bank and its subcomponents, like
 * the other components in this project, were modeled after the actor model.
 *
 * @author  Tanner J. Evans
 * @version %I%, %G%
 * @since   11.0.8
 */

public class Bank {

    public static final boolean DEBUG = false;

    private static final String SERVER_NAME = "Bank";
    private static ServerSocket serverSocket;
    private static String       serverAddress;
    private static int          portNumber;
    private static int          idCounter;
    private static int          transactionCounter;
    private static final MessageQueue<Message>               QUEUE         = new MessageQueue<>();
    private static final KeyList<Integer, ConnectionHandler> CONNECTIONS   = new KeyList();
    private static final KeyList<Integer, BankAccount>       BANK_ACCOUNTS = new KeyList<>();

    /**
     * This main method sets some semi-random starting values for flavor, and
     * starts a server to accept incoming connections and spin off new
     * threads for message-handling from these connections.
     *
     * @param args args[0] = Bank address, args[1] = Bank port
     * @throws UnknownHostException
     */
    public static void main(String[] args) throws IOException {
        portNumber = Integer.parseInt(args[0]);

        randomizeStartingValues();
        startServer();
    }

    private static void processMessage(Message message) {
        Code code = message.getCode();
        int accountID = message.getAccountID();
        Message response = new Message();
        response.setAccountID(accountID);
        BankAccount account = BANK_ACCOUNTS.get(accountID);

        switch (code) {
        case REGISTER:
            account = new BankAccount(message, CONNECTIONS.remove(accountID));
            BANK_ACCOUNTS.put(accountID, account);
            switch (account.CLIENT_TYPE) {
            case VENDOR:
                Message newVendorNotice = new Message();
                newVendorNotice.setCode(Code.NEW_VENDOR)
                               .setName(message.getName())
                               .setAddress(account.ADDRESS)
                               .setPort(account.PORT)
                               .setOtherAccount(accountID);
                notifyAllAgents(newVendorNotice);
                break;
            case AGENT:
                notifyNewAgentOfVendors(accountID);
                break;
            }
            break;
        case DEREGISTER:
            BANK_ACCOUNTS.remove(accountID);
            break;
        case BLOCK:
            if (account.CLIENT_TYPE == Message.ClientType.VENDOR) {
                message.setTransactionID(++transactionCounter).setTransactionType(
                        Message.TransactionType.BLOCK);
                account = BANK_ACCOUNTS.get(message.getTargetAccount());
                Code code1 = account.block(message);
                response.setResult(code1)
                        .setCode(Code.BLOCK_RESPONSE)
                        .setTransactionID(transactionCounter)
                        .setItemNumber(message.getItemNumber())
                        .setAmount(message.getAmount())
                        .setName(message.getName())
                        .setTargetAccount(message.getTargetAccount());
                sendMessage(response);
            }
            break;
        case UNBLOCK:
            if (account.CLIENT_TYPE == Message.ClientType.VENDOR) {
                BANK_ACCOUNTS.get(message.getTargetAccount()).unblock(message, message.getTransactionID());
                sendMessage(response);
            }
            break;
        case TRANSFER:
            int target = message.getTargetAccount();
            int oldTransactionID = message.getTransactionID();
            message.setTransactionID(++transactionCounter);
            BANK_ACCOUNTS.get(target).unblock(message, oldTransactionID);
            BANK_ACCOUNTS.get(target).withdraw(message);
            BANK_ACCOUNTS.get(message.getOtherAccount()).deposit(message);
            break;
        case ACCOUNT_SUMMARY:
            response = account.getAccountSummary();
            sendMessage(response);
            break;
        }

        if (DEBUG) {
            System.out.println("Request:\n" + message.toString());
            System.out.println("Account States:");
            for (BankAccount bankAccount : BANK_ACCOUNTS.values()) {
                System.out.println(bankAccount.getAccountSummary().toString());
            }
        }

    }

    private static void notifyAllAgents(Message message) {
        for (BankAccount account : BANK_ACCOUNTS.values()) {
            if (account.CLIENT_TYPE.equals(Message.ClientType.AGENT)) {
                message.setAccountID(account.ACCOUNT_ID);
                account.CONNECTION.send(message);
            }
        }
    }

    private static void notifyNewAgentOfVendors(int agentID) {
        Message message = new Message();
        message.setAccountID(agentID).setCode(Code.NEW_VENDOR);
        for (BankAccount account : BANK_ACCOUNTS.values()) {
            if (account.CLIENT_TYPE.equals(Message.ClientType.VENDOR)) {
                message.setAddress(account.ADDRESS).setPort(account.PORT)
                       .setOtherAccount(account.ACCOUNT_ID)
                       .setName(account.CLIENT_NAME);
                BANK_ACCOUNTS.get(agentID).CONNECTION.send(message);
            }
        }
    }

    private static void sendMessage(Message message) {
        BANK_ACCOUNTS.get(message.getAccountID()).CONNECTION.send(message);
    }

    private static void startServer()
            throws UnknownHostException {

        String address = "";
        try
        {
            URL url_name = new URL("http://checkip.amazonaws.com");

            BufferedReader sc =
                    new BufferedReader(new InputStreamReader(url_name.openStream()));

            // reads system IPAddress
            address = sc.readLine().trim();
        }
        catch (Exception e)
        {
            address = "Cannot Execute Properly";
        }

        serverAddress = address;

        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException serverErr) {
            System.out.println("Unable to establish server. Error:");
            System.out.println(serverErr);
            System.exit(-1);
        }

        System.out.println(
                SERVER_NAME +
                " server established. Clients can connect using:");
        System.out.println("Address: " + serverAddress);
        System.out.println("Port: " + portNumber);

        Server server = new Server();
        MessageProcessor messageProcessor = new MessageProcessor();
        Thread messageProcessorThread = new Thread(messageProcessor);
        Thread serverThread = new Thread(server);
        serverThread.start();
        messageProcessorThread.start();
    }

    private static class Server implements Runnable {
        @Override
        public void run() {
            while (true) {

                try {
                    Socket socket = serverSocket.accept();
                    ConnectionHandler connectionHandler = new ConnectionHandler(socket, QUEUE);
                    Message accountID  = new Message();
                    accountID.setCode(Code.ACCOUNT_ID)
                             .setAccountID(++idCounter);
                    connectionHandler.send(accountID);
                    CONNECTIONS.put(idCounter, connectionHandler);
                    Thread connection = new Thread(connectionHandler);
                    connection.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class MessageProcessor implements Runnable {
        @Override
        public void run() {
            do {
                try {
                    Message message = QUEUE.take();
                    processMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    private static void randomizeStartingValues(){
        Random random = new Random();
        idCounter = random.nextInt(200) + 54;
        transactionCounter = random.nextInt(400) + 175;
    }

}