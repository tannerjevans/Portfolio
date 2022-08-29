import org.json.JSONException;
import java.io.*;
import java.net.Socket;
import java.util.LinkedHashMap;

/**
 * ConnectionHandler is probably the most important class in the entire
 * project. It is short and simple, but when it is used to produce connection
 * threads by a server, each ConnectionHandler acts as an Actor itself,
 * passing messages in an out of the socket. All messages received through
 * the socket during the run method are placed on the blocking queue that is
 * passed to it at creation. Those messages are processed sequentially, and
 * the results are assigned back out to the ConnectionHandlers from which
 * they came.
 *
 * Plans were made to have a ConnectionHandler enforce identification of its
 * messages by appending a JSON attribute to all incoming messages, but this
 * has not yet been implemented.
 *
 * @author  Tanner J. Evans
 * @version %I%, %G%
 * @since   11.0.8
 */

public class ConnectionHandler implements Runnable {
    private final  Socket                              SOCKET;
    private final  MessageQueue<Message>               MESSAGE_QUEUE;
    private final  PrintWriter                         TO_SOCKET;
    private final  BufferedReader                      FROM_SOCKET;
    private static KeyList<Integer, ConnectionHandler> AGENTS;

    /**
     * The constructor for the Agent and Bank classes. Initializes the
     * connection and its constituents.
     * @param socket
     * @param messageQueue
     * @throws IOException
     */

    public ConnectionHandler(Socket socket,
                             MessageQueue<Message> messageQueue)
        throws IOException {
        SOCKET        = socket;
        MESSAGE_QUEUE = messageQueue;
        AGENTS        = null;
        OutputStream outputStream = SOCKET.getOutputStream();
        InputStream  inputStream  = SOCKET.getInputStream();
        TO_SOCKET   = new PrintWriter(outputStream, true);
        FROM_SOCKET = new BufferedReader(new InputStreamReader(inputStream));
    }


    /**
     * The constructor for the AuctionHouse class. Initalizes all the same
     * items, but also passes in an AGENTS list from the AuctionHouse to
     * enable a simpler handshake protocol.
     * @param socket
     * @param messageQueue
     * @param agents
     * @throws IOException
     */
    public ConnectionHandler(Socket socket,
                             MessageQueue<Message> messageQueue,
                             KeyList<Integer, ConnectionHandler> agents)
        throws IOException {
        SOCKET        = socket;
        MESSAGE_QUEUE = messageQueue;
        AGENTS = agents;
        OutputStream outputStream = SOCKET.getOutputStream();
        InputStream inputStream = SOCKET.getInputStream();
        TO_SOCKET   = new PrintWriter(outputStream, true);
        FROM_SOCKET = new BufferedReader(new InputStreamReader(inputStream));
    }

    /**
     * The runtime logic of ConnectionHandler. Besides some handshake
     * protocol redirection, it reads a line in, converts it to a Message,
     * and drops it onto the BlockingQueue provided at construction.
     */
    @Override
    public void run() {
        String incomingPacket;
        do {
            try {
                incomingPacket = FROM_SOCKET.readLine();
                Message incomingMessage = new Message(incomingPacket);
                Code code;
                try {
                    code = incomingMessage.getCode();
                    if (code == Code.HANDSHAKE) {
                        AGENTS.put(incomingMessage.getAccountID(), this);
                    }
                    MESSAGE_QUEUE.put(incomingMessage);
                } catch (JSONException ignored) {}
            } catch (IOException | NullPointerException |
                    InterruptedException e) {
                incomingPacket = null;
            }
        } while (incomingPacket != null);
    }

    protected void send(Message message) {
        TO_SOCKET.println(message.toString());
    }

    protected boolean isClosed() {
        return TO_SOCKET.checkError();
    }
}
