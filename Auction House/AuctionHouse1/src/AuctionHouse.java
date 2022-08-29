import org.json.JSONArray;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

/**
 * AuctionHouse is the main class for the AuctionHouse application. It can be
 * started with command-line arguments detailing the IP address and the
 * connection port of the Bank application.
 *
 * The only non-private member is Main. The remaining members are for
 * exclusive use of this class. The AuctionHouse and its subcomponents, like
 * the other components in this project, were modeled after the actor model.
 *
 * @author  Tanner J. Evans
 * @version %I%, %G%
 * @since   11.0.8
 */

public class AuctionHouse {

    private static ServerSocket serverSocket;
    private static String       name          = "Auction House ";
    private static String       serverAddress;
    private static int          serverPort;
    private static Integer      accountNumber = null;
    private static ConnectionHandler
                                bankConnection;

    private static final KeyList<Integer, ConnectionHandler> AGENTS    = new KeyList<>();
    private static final MessageQueue<Message>               QUEUE     = new MessageQueue<>();
    private static final KeyList<Integer, Item>              ITEMS     = new KeyList<>();
    private static final ScheduledExecutorService            scheduler = Executors.newScheduledThreadPool(4);

    private static ScheduledFuture<?>[] itemTimers = { null, null, null };

    /**
     * This main method calls methods to start the bank connection and spin
     * it off into its own thread, and to start a server thread that
     * automatically accepts incoming connections for Agents (this also spins
     * off its own threads.
     *
     * @param args args[0] = Bank address, args[1] = Bank port
     * @throws UnknownHostException
     */
    public static void main(String[] args) throws UnknownHostException {
        String bankAddress = args[0];
        String bankPort = args[1];
        serverPort = Integer.parseInt(bankPort) + 1;
        startServer();
        initialize(null, bankPort);
    }

    private static void processMessage(Message message) {
        Code code = message.getCode();
        int agentNumber = message.getAccountID();
        Item item;

        switch (code) {
        case ACCOUNT_ID:
            accountNumber = message.getAccountID();
            name += accountNumber.toString();
            break;
        case HANDSHAKE:
            Message itemSummary = new Message();
            itemSummary.setCode(Code.ITEM_SUMMARY).setVendor(name)
                       .setAccountID(accountNumber);
            JSONArray jsonArray = new JSONArray();
            for (Item item1 : ITEMS.values()) {
                jsonArray.put(item1.toStr());
            }
            itemSummary.setItemList(jsonArray);
            sendToAgent(agentNumber, itemSummary);
            break;
        case PLACE_BID:
            double bidAmount = message.getAmount();
            item = ITEMS.get(message.getItemNumber());
            if (item.getItemState().equals(State.SOLD)) {
                Message response = new Message();
                response.setCode(Code.ITEM_SOLD)
                        .setItemNumber(item.ITEM_NUMBER)
                        .setAccountID(accountNumber);
                sendToAgent(agentNumber, response);
            } else if (bidAmount < item.getHighestBid()) {
                Message response = new Message();
                response.setCode(Code.BID_TOO_LOW)
                        .setItemNumber(item.ITEM_NUMBER)
                        .setAccountID(accountNumber);
                sendToAgent(agentNumber, response);
            } else {
                Message blockFunds = new Message();
                blockFunds.setCode(Code.BLOCK)
                          .setTargetAccount(message.getAccountID())
                          .setAccountID(accountNumber)
                          .setOtherAccount(accountNumber)
                          .setAmount(message.getAmount())
                          .setItemNumber(item.ITEM_NUMBER)
                          .setName(message.getName());
                bankConnection.send(blockFunds);
            }
            break;
        case BLOCK_RESPONSE:
            Item item1 = ITEMS.get(message.getItemNumber());
            agentNumber = message.getTargetAccount();
            if (message.getResult().equals(Code.INSUFFICIENT_FUNDS)) {
                message.setCode(Code.INSUFFICIENT_FUNDS);
                message.setAccountID(accountNumber);
                sendToAgent(agentNumber, message);
            } else {
                if (itemTimers[item1.ITEM_NUMBER] != null) {
                    itemTimers[item1.ITEM_NUMBER].cancel(true);
                }
                Message outbidNotice = new Message();
                outbidNotice.setCode(Code.OUTBID)
                            .setItemNumber(item1.ITEM_NUMBER)
                            .setAccountID(accountNumber);
                if (item1.getHighestBidderID() != 0) {
                    int oldBidder = item1.getHighestBidderID();
                    sendToAgent(oldBidder, outbidNotice);
                    Message unblock = new Message();
                    unblock.setCode(Code.UNBLOCK).setAccountID(accountNumber)
                           .setTargetAccount(oldBidder)
                           .setTransactionID(item1.getTransactionID());
                    bankConnection.send(unblock);
                }
                item1.setItemState(State.PENDING);
                item1.setHighestBid(message.getAmount());
                item1.setHighestBidder(message.getName(), agentNumber);
                item1.setTransactionID(message.getTransactionID());
                BidTimer bidTimer = new BidTimer(item1.ITEM_NUMBER,
                                                 agentNumber);
                itemTimers[item1.ITEM_NUMBER] =
                        scheduler.schedule(bidTimer, 30, TimeUnit.SECONDS);
                Message bidPlaced = new Message();
                bidPlaced.setCode(Code.BID_PLACED)
                         .setItemNumber(item1.ITEM_NUMBER)
                         .setName(item1.NAME)
                         .setAccountID(accountNumber);
                sendToAgent(agentNumber, bidPlaced);
                sendAgentsItemUpdate();
            }
            break;
        case CLOSE_SALE:
            int itemNumber = message.getItemNumber();
            item = ITEMS.get(itemNumber);
            item.setItemState(State.SOLD);
            Message transferMessage = new Message();
            transferMessage.setCode(Code.TRANSFER).setAccountID(accountNumber)
                           .setTargetAccount(item.getHighestBidderID())
                           .setOtherAccount(accountNumber)
                           .setAmount(item.getHighestBid())
                           .setTransactionID(item.getTransactionID())
                           .setItemNumber(item.ITEM_NUMBER);
            bankConnection.send(transferMessage);
            Message itemTransfer = new Message();
            itemTransfer.setCode(Code.ITEM_TRANSFER).setItem(item)
                        .setItemNumber(item.ITEM_NUMBER)
                        .setAccountID(accountNumber);
            sendToAgent(item.getHighestBidderID(), itemTransfer);
            sendAgentsItemUpdate();
            break;
        case DEREGISTER:
            AGENTS.remove(message.getAccountID());
            break;
        }
    }

    private static void sendAgentsItemUpdate() {
        Message itemSummary = new Message();
        itemSummary.setCode(Code.ITEM_SUMMARY).setVendor(name)
                   .setAccountID(accountNumber);
        JSONArray jsonArray = new JSONArray();
        for (Item item1 : ITEMS.values()) {
            jsonArray.put(item1.toStr());
        }
        itemSummary.setItemList(jsonArray);
        for (Integer key : AGENTS.keySet()) {
            sendToAgent(key, itemSummary);
        }
    }


    private static class BidTimer implements Callable {
        private Message message = new Message();

        public BidTimer(int itemNumber, int agentNumber) {
            message.setItemNumber(itemNumber).setCode(Code.CLOSE_SALE)
                   .setAccountID(agentNumber);
        }

        @Override
        public Object call() throws Exception {
            System.out.println("Bid timer run");
            try {
                QUEUE.put(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static void sendToAgent(int id, Message message) {
        AGENTS.get(id).send(message);
    }

    private static ConnectionHandler connect(String address, String port){
        boolean connected = false;
        while (!connected) {
            try {
                Socket socket =
                        new Socket(address, Integer.parseInt(port));
                connected = true;
                ConnectionHandler connectionHandler =
                        new ConnectionHandler(socket, QUEUE);
                Thread  thread            = new Thread(connectionHandler);
                thread.start();
                return connectionHandler;
            } catch (IOException e) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException interruptedException) {}
            }
        }
        return null;
    }

    private static Message getUserInfo() {
        //todo: get user input
        String port = Integer.toString(serverPort);
        Message registrationRequest = new Message();
        registrationRequest.setCode(Code.REGISTER).setName(name)
                           .setAddress(serverAddress).setPort(port)
                           .setClientType(Message.ClientType.VENDOR)
                           .setAmount(300.2).setAccountID(accountNumber);
        return registrationRequest;
    }

    private static void initialize(String address, String port) {
        MessageProcessor messageProcessor = new MessageProcessor();
        Thread processorThread = new Thread(messageProcessor);
        processorThread.start();
        bankConnection = connect(address, port);
        if (bankConnection == null) {
            System.out.println("Unable to connect to bank. Exiting.");
            System.exit(-1);
        }
        while (accountNumber == null) {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        bankConnection.send(getUserInfo());

        for (int i = 1 ; i < 4 ; i++) {
            Item item = new Item(("item" + i), ("descrip" + i), i*10, i);
            ITEMS.put(i, item);
        }
    }

    private static void startServer() throws UnknownHostException {
        while (true) {
            try {
                serverSocket = new ServerSocket(serverPort);
                break;
            } catch (IOException serverErr) {
                serverPort++;
            }
        }

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

        System.out.println(
                name +
                " server established. Clients can connect using:");
        System.out.println(
                "Address: " + serverAddress);
        System.out.println("Port: " + serverPort);
        Server server = new Server();
        Thread serverThread = new Thread(server);
        serverThread.start();
    }

    private static class Server implements Runnable {
        @Override
        public void run() {
            while (true) {

                try {
                    Socket socket = serverSocket.accept();
                    ConnectionHandler connectionHandler = new
                            ConnectionHandler(socket, QUEUE, AGENTS);
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
                    processMessage(QUEUE.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }
}