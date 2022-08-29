/**
 * Item is a utility class for use by AuctionHouse and Agent. It provides a
 * framework for sharing item data between these classes, as well as some
 * utility methods for summarizing that data for serialization.
 *
 * @author  Tanner J. Evans
 * @version %I%, %G%
 * @since   11.0.8
 */

public class Item {
    protected final String NAME;
    protected final String DESCRIPTION;
    protected final double STARTING_PRICE;
    protected final int    ITEM_NUMBER;

    private State  itemState       = State.OPEN;
    private String highestBidder   = "n/a";
    private int    highestBidderID = 0;
    private double highestBid;
    private int    transactionID;

    protected Item(String name, String description, double startingPrice,
                   int itemNumber) {
        NAME           = name;
        DESCRIPTION    = description;
        STARTING_PRICE = startingPrice;
        highestBid     = startingPrice;
        ITEM_NUMBER    = itemNumber;
    }

    protected String toStr() {
        Message message = new Message();
        message.setName(NAME)
               .setDescription(DESCRIPTION)
               .setStartingPrice(STARTING_PRICE)
               .setItemState(itemState)
               .setHighestBidder(highestBidder)
               .setHighestBid(highestBid)
               .setItemNumber(ITEM_NUMBER)
               .setHighestBidderID(highestBidderID);
        return message.toString();
    }

    protected double getHighestBid() {
        return highestBid;
    }

    protected String getHighestBidder() {
        return highestBidder;
    }

    protected State getItemState() {
        return itemState;
    }

    protected void setItemState(State itemState) {
        this.itemState = itemState;
    }

    protected void setHighestBidder(String highestBidder, int highestBidderID) {
        this.highestBidder   = highestBidder;
        this.highestBidderID = highestBidderID;
    }

    protected void setHighestBid(double highestBid) {
        this.highestBid = highestBid;
    }

    protected int getHighestBidderID() {
        return highestBidderID;
    }

    protected void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    protected int getTransactionID() {
        return transactionID;
    }
}
