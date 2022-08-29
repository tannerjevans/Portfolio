import java.util.concurrent.LinkedBlockingQueue;

/**
 * Once again, MessageQueue is a wrapper for a class whose name I just found
 * to be too unwieldy. I also felt it sensible to abstract the implementation
 * away from the central code.
 *
 * @param <Message>
 */

public class MessageQueue<Message> extends LinkedBlockingQueue<Message> {
}
