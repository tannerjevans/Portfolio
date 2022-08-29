import org.json.JSONArray;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * AuctionHouse is the main class for the AuctionHouse application. It can be
 * started with command-line arguments detailing the IP address and the
 * connection port of the Bank application.
 *
 * The only non-private member is Main. The remaining members are for
 * exclusive use of this class. The Agent and its subcomponents, like
 * the other components in this project, were modeled after the actor model.
 *
 * @author  Tanner J. Evans
 * @version %I%, %G%
 * @since   11.0.8
 */

public class Agent {

    private static String            name          = "Agent";
    private static Integer           accountNumber = null;
    private static ConnectionHandler bankConnectionHandler;
    private static DisplayWriter     displayWriter = new DisplayWriter();
    private static InputReader       inputReader;

    private static final KeyList<Integer, ConnectionHandler> VENDORS_CONN = new KeyList<>();
    private static final KeyList<Integer, VendorSummary>     VENDORS_DISP = new KeyList<>();

    private static final MessageQueue<Message> QUEUE = new MessageQueue<>();
    private static final LinkedList<Item>      ITEMS = new LinkedList<>();

    private static int   bidsOpen = 0;


    /**
     * This main method calls a method to start the bank connection and spin
     * it off into its own thread, and to start an asynchronous displayWriter
     * and inputReader.
     *
     * @param args args[0] = Bank address, args[1] = Bank port
     * @throws UnknownHostException
     */
    public static void main(String[] args) {
        String bankAddress = args[0];
        String portNumber  = args[1];

        initialize(null, portNumber);
    }

    private static void processMessage(Message message) {
        Code code = message.getCode();

        switch (code) {
        case ACCOUNT_ID:
            accountNumber = message.getAccountID();
            name += " " + accountNumber;
            break;
        case NEW_VENDOR:
            ConnectionHandler connection =
                    connect(null/*message.getAddress()*/, message.getPort());
            VENDORS_CONN.put(message.getOtherAccount(), connection);
            Message handshake = new Message();
            handshake.setCode(Code.HANDSHAKE).setAccountID(accountNumber);
            connection.send(handshake);
            VendorSummary vendor = new VendorSummary(message.getName());
            VENDORS_DISP.put(message.getOtherAccount(), vendor);
            break;
        case ITEM_SUMMARY:
            VENDORS_DISP.get(message.getAccountID()).updateItems(message);
            displayWriter.refresh();
            break;
        case OUTBID:
            displayWriter.printToUser("Bid for item " + message.getItemNumber()+
                                      " from auction house " +
                                      message.getAccountID() +
                                      " was outbid.");
            bidsOpen--;
            break;
        case BID_TOO_LOW:
            displayWriter.printToUser("Bid for item " + message.getItemNumber()+
                                      " from auction house " +
                                      message.getAccountID() +
                                      " was too low.");
            bidsOpen--;
            break;
        case INSUFFICIENT_FUNDS:
            displayWriter.printToUser("Bid for item " + message.getItemNumber()+
                                      " from auction house " +
                                      message.getAccountID() +
                                      " was rejected for insufficient funds.");
            bidsOpen--;
            break;
        case BID_PLACED:
            displayWriter.printToUser("Bid for item " + message.getItemNumber()+
                                      " from auction house " +
                                      message.getAccountID() +
                                      " was placed!");
            break;
        case ITEM_TRANSFER:
            displayWriter.printToUser("Sale complete! Item " +
                                      message.getItemNumber() +
                                      " from auction house " +
                                      message.getAccountID() +
                                      " is now yours!");
            ITEMS.add(message.getItem());
            bidsOpen--;
            break;
        case ITEM_SOLD:
            displayWriter.printToUser("Bid for item " + message.getItemNumber()+
                                      " from auction house " +
                                      message.getAccountID() +
                                      " was rejected: it has been sold " +
                                      "already");
            bidsOpen--;
            break;
        case ACCOUNT_SUMMARY:
            String summary = "Account Summary: " +
                             "  Name: " + message.getName() +
                             "  ID: " + accountNumber +
                             "  Balance: $" + message.getTotalBalance() +
                             "  Available Funds: $" +
                             message.getAvailableFunds();
            displayWriter.printToUser(summary);
            break;
        }
    }

    private static class VendorSummary {
        protected final String VENDOR_NAME;
        protected final KeyList<Integer, Item> VENDOR_ITEMS = new KeyList();

        public VendorSummary (String vendorName) {
            VENDOR_NAME = vendorName;
        }

        protected void updateItems(Message itemSummary) {
            JSONArray jsonArray = itemSummary.getItemList();
            for (Object object : jsonArray) {
                String itemString = object.toString();
                Message itemMessage = new Message(itemString);
                Item item = new Item(itemMessage.getName(),
                                     itemMessage.getDescription(),
                                     itemMessage.getStartingPrice(),
                                     itemMessage.getItemNumber());
                item.setHighestBid(itemMessage.getHighestBid());
                item.setItemState(itemMessage.getItemState());
                item.setHighestBidder(itemMessage.getHighestBidder(),
                                      itemMessage.getHighestBidderID());
                VENDOR_ITEMS.put(itemMessage.getItemNumber(), item);
            }
        }
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
                } catch (InterruptedException ignored) {}
            }
        }
        return null;
    }

    private static Message getUserInfo() {
        //todo: get user input
        Message registrationRequest = new Message();
        registrationRequest.setCode(Code.REGISTER).setName(name)
                           .setAddress("n/a").setPort("n/a")
                           .setClientType(Message.ClientType.AGENT)
                           .setAmount(300.2).setAccountID(accountNumber);
        return registrationRequest;
    }

    private static void initialize(String address, String port) {
        MessageProcessor messageProcessor = new MessageProcessor();
        Thread processorThread = new Thread(messageProcessor);
        processorThread.start();
        bankConnectionHandler = connect(address, port);
        if (bankConnectionHandler == null) {
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
        bankConnectionHandler.send(getUserInfo());
        displayWriter = new DisplayWriter();
        inputReader = new InputReader();
        Thread readerThread = new Thread(inputReader);
        readerThread.start();
    }

    /**
     * DisplayWriter serves as the thoroughfare through which output to the
     * user runs. It works in tandem with InputReader to provide sequential
     * data placement of relevant information.
     */
    private static class DisplayWriter {

        protected void printToUser(String string) {
            System.out.println(string);
        }

        protected void putAccountStatus() {
            Message accountStatusRequest = new Message();
            accountStatusRequest.setCode(Code.ACCOUNT_SUMMARY)
                                .setAccountID(accountNumber);
            bankConnectionHandler.send(accountStatusRequest);
        }

        protected void refresh() {
            System.out.println("Auction house items updated:");
            for (Integer key : VENDORS_DISP.keySet()) {
                System.out.println("Auction House [" + key + "]:");
                VendorSummary vendor = VENDORS_DISP.get(key);
                for (Integer it : vendor.VENDOR_ITEMS.keySet()) {
                    Item item = vendor.VENDOR_ITEMS.get(it);
                    System.out.print("Item [" + it + "]: ");
                    System.out.print(item.NAME + ": " + item.DESCRIPTION);
                    System.out.print("  Highest bid: " + item.getHighestBid());
                    System.out.print("  Highest bidder: " +
                                     item.getHighestBidder());
                    System.out.print("  Item State: " +
                                     item.getItemState().toString());
                    System.out.println();
                }
            }
            if (!ITEMS.isEmpty()) {
                System.out.println("Your purchased items:");
                for (Item item : ITEMS) {
                    System.out.println(item.NAME +  " purchased for " +
                                       item.getHighestBid());
                }
            }
            System.out.println(inputReader.getCurrentPrompt());
        }

        protected boolean validateVendor(int selection) {
            VendorSummary vendorSummary = VENDORS_DISP.get(selection);
            return vendorSummary != null;
        }

        protected boolean validateItem(int vendor, int item) {
            VendorSummary vendorSummary = VENDORS_DISP.get(vendor);
            Item item1 = vendorSummary.VENDOR_ITEMS.get(item);
            return item1 != null;
        }

        protected void sendBid(int vendor, int item, double amount) {
            Message bid = new Message();
            bid.setAccountID(accountNumber).setCode(Code.PLACE_BID)
               .setItemNumber(item).setAmount(amount).setName(name);
            VENDORS_CONN.get(vendor).send(bid);
            if (VENDORS_CONN.get(vendor).isClosed()) {
                System.out.println("Auction House " + vendor + " has closed.");
                VENDORS_CONN.remove(vendor);
                VENDORS_DISP.remove(vendor);
                displayWriter.refresh();
            } else {
                bidsOpen++;
            }
        }
    }

    /**
     * InputReader is a private nested Runnable class that functions in
     * tandem with the displayWriter to enable the program to maintain
     * control flow through state changes, while providing immediate updating
     * of important messages and values received. It enforces a strict state
     * progression to promote stability.
     */
    private static class InputReader implements Runnable {
        private BufferedReader reader;
        private InputState inputState = InputState.OPEN;
        private int vendorSelection;
        private int itemSelection;

        protected String getCurrentPrompt() {
            return currentPrompt;
        }

        protected final String OPEN_PROMPT =
                "Enter command: [0] Shut down | [1] Place a bid | " +
                "[2] Check bank account";
        protected final String VENDOR_PROMPT =
                "Enter the auction house number. (0 cancels)";
        protected final String ITEM_PROMPT =
                "Enter the item number. (0 cancels)";
        protected final String AMOUNT_PROMPT =
                "Enter the amount to bid. (0 or less cancels)";
        protected final String CLOSE_NOTICE =
                "Closing (waiting for bids to resolve)...";
        private String currentPrompt = OPEN_PROMPT;

        public InputReader() {
            reader = new BufferedReader(new InputStreamReader(System.in));
        }

        @Override
        public void run() {
            displayWriter.printToUser("Waiting for vendor connections...");
            while (inputState != InputState.CLOSING) {
                try {
                    String userInput = reader.readLine();
                    processInput(userInput);
                } catch (IOException ignored) {}
            }
        }

        protected enum InputState {
            OPEN, VENDOR_SELECT, ITEM_SELECT, AMOUNT_SELECT, CLOSING
        }

        private void processInput(String input) {
            int selection = 0;
            double amount = 0;
            try {
                if (inputState == InputState.AMOUNT_SELECT) {
                    amount = Double.parseDouble(input);
                } else {
                    selection = Integer.parseInt(input);
                }
            } catch (NumberFormatException e) {
                displayWriter.printToUser("Entry invalid.");
                return;
            }
            switch (inputState) {
            case OPEN:
                if (selection == 0) {
                    inputState = InputState.CLOSING;
                    displayWriter.printToUser(CLOSE_NOTICE);
                    currentPrompt = CLOSE_NOTICE;
                    while (bidsOpen > 0) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(500);
                        } catch (InterruptedException ignored) {}
                    }
                    Message closeMessage = new Message();
                    closeMessage.setCode(Code.DEREGISTER)
                                .setAccountID(accountNumber);
                    bankConnectionHandler.send(closeMessage);
                    for (ConnectionHandler conn : VENDORS_CONN.values()) {
                        conn.send(closeMessage);
                    }
                    System.exit(1);
                } else if (selection < 1 || selection > 2) {
                    displayWriter.printToUser("Entry invalid.");
                } else if (selection == 1) {
                    inputState = InputState.VENDOR_SELECT;
                    currentPrompt = VENDOR_PROMPT;
                    displayWriter.printToUser(VENDOR_PROMPT);
                } else {
                    displayWriter.putAccountStatus();
                }
                break;
            case VENDOR_SELECT:
                if (selection == 0) {
                    inputState = InputState.OPEN;
                    displayWriter.printToUser(OPEN_PROMPT);
                    displayWriter.printToUser("Cancelled.");
                } else if (!displayWriter.validateVendor(selection)) {
                    displayWriter.printToUser("Entry invalid.");
                } else {
                    vendorSelection = selection;
                    inputState = InputState.ITEM_SELECT;
                    displayWriter.printToUser(ITEM_PROMPT);
                    currentPrompt = ITEM_PROMPT;
                }
                break;
            case ITEM_SELECT:
                if (selection == 0) {
                    inputState = InputState.OPEN;
                    displayWriter.printToUser(OPEN_PROMPT);
                    displayWriter.printToUser("Cancelled.");
                } else if (!displayWriter.validateItem(vendorSelection,
                                                       selection)) {
                    displayWriter.printToUser("Entry invalid.");
                } else {
                    itemSelection = selection;
                    inputState = InputState.AMOUNT_SELECT;
                    displayWriter.printToUser(AMOUNT_PROMPT);
                    currentPrompt = AMOUNT_PROMPT;
                }
                break;
            case AMOUNT_SELECT:
                if (amount <= 0) {
                    displayWriter.printToUser("Cancelled.");
                } else {
                    displayWriter.sendBid(vendorSelection, itemSelection,
                                          amount);
                }
                inputState = InputState.OPEN;
                displayWriter.printToUser(OPEN_PROMPT);
                currentPrompt = OPEN_PROMPT;
                break;
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