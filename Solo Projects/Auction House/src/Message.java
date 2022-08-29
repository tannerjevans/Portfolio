import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Message is a wrapper of JSONObject with helper functions, intended to make
 * JSONObject manipulation simpler and more intuitive. It was intended to
 * promote stability by regularizing the keys used to represent values. In
 * practice, it often served to obfuscate and limit. Given more time, I would
 * develop Builders or Constructors for messages of certain types that would
 * enforce certain data inclusions. As it stands, it works, but it is rather
 * a mess. The fluent style was well-intentioned, but the method names grew
 * too long to make it any more sensible than separate function calls.
 *
 * @author  Tanner J. Evans
 * @version %I%, %G%
 * @since   11.0.8
 */

public class Message extends JSONObject {

    protected Message (String string) {
        super(string);
    }

    protected Message () {
        super();
    }


    protected Message setResult(Code result) {
        this.put("result", result.name());
        return this;
    }

    protected Code getResult() {
        String result = this.getString("result");
        return Code.valueOf(result);
    }

    protected Message setCode(Code code) {
        this.put("code", code.name());
        return this;
    }

    protected Message setAccountID(int accountID) {
        this.put("accountID", accountID);
        return this;
    }

    protected Message setName(String name) {
        this.put("name", name);
        return this;
    }

    protected Message setDescription(String description) {
        this.put("description", description);
        return this;
    }

    protected Message setStartingPrice(double startingPrice) {
        this.put("startingPrice", startingPrice);
        return this;
    }

    protected Message setItemState(State itemState) {
        this.put("itemState", itemState.name());
        return this;
    }

    protected Message setHighestBidder(String highestBidder) {
        this.put("highestBidder", highestBidder);
        return this;
    }

    protected Message setHighestBid(double highestBid) {
        this.put("highestBid", highestBid);
        return this;
    }

    protected Message setItemList(JSONArray itemList) {
        this.put("itemList", itemList);
        return this;
    }

    protected Message setAddress(String address) {
        this.put("address", address);
        return this;
    }

    protected Message setPort(String port) {
        this.put("port", port);
        return this;
    }

    protected Message setClientType(ClientType clientType) {
        this.put("clientType", clientType.name());
        return this;
    }

    protected Message setTotalBalance(double totalBalance) {
        this.put("totalBalance", totalBalance);
        return this;
    }

    protected Message setAvailableFunds(double availableFunds) {
        this.put("availableFunds", availableFunds);
        return this;
    }

    protected Message setTransactionID(int transactionID) {
        this.put("transactionID", transactionID);
        return this;
    }

    protected Message setTransactionType(TransactionType transactionType) {
        this.put("transactionType", transactionType.name());
        return this;
    }

    protected Message setTransactionList(JSONArray transactionList) {
        this.put("transactionList", transactionList);
        return this;
    }

    protected Message setAmount(double amount) {
        this.put("amount", amount);
        return this;
    }

    protected Message setTargetAccount(int targetAccount) {
        this.put("targetAccount", targetAccount);
        return this;
    }

    protected Message setOtherAccount(int otherAccount) {
        this.put("otherAccount", otherAccount);
        return this;
    }

    protected Message setVendor(String vendor) {
        this.put("vendor", vendor);
        return this;
    }

    protected Message setItemNumber(int itemNumber) {
        this.put("itemNumber", itemNumber);
        return this;
    }

    protected Message setItem(Item item) {
        this.put("item", item.toStr());
        return this;
    }

    protected Message setHighestBidderID(int highestBidderID) {
        this.put("highestBidderID", highestBidderID);
        return this;
    }


    protected int getHighestBidderID() {
        int highestBidderID = this.getInt("highestBidderID");
        return highestBidderID;
    }

    protected Item getItem() {
        String  itemString  = this.getString("item");
        Message itemMessage = new Message(itemString);
        Item item = new Item(itemMessage.getName(),
                             itemMessage.getDescription(),
                             itemMessage.getStartingPrice(),
                             itemMessage.getItemNumber());
        item.setHighestBid(itemMessage.getHighestBid());
        return item;
    }

    protected int getItemNumber() {
        int itemNumber = this.getInt("itemNumber");
        return itemNumber;
    }

    protected Code getCode() {
        String string = this.getString("code");
        return Code.valueOf(string);
    }

    protected int getAccountID() {
        return this.getInt("accountID");
    }

    protected String getName() {
        return this.getString("name");
    }

    protected String getDescription() {
        String description = this.getString("description");
        return description;
    }

    protected double getStartingPrice() {
        double startingPrice = this.getDouble("startingPrice");
        return startingPrice;
    }

    protected State getItemState() {
        State itemState = State.valueOf(this.getString("itemState"));
        return itemState;
    }

    protected String getHighestBidder() {
        String highestBidder = this.getString("highestBidder");
        return highestBidder;
    }

    protected double getHighestBid() {
        double highestBid = this.getDouble("highestBid");
        return highestBid;
    }

    protected JSONArray getItemList() {
        JSONArray jsonArray = this.getJSONArray("itemList");
        return jsonArray;
    }

    protected String getAddress() {
        return this.getString("address");
    }

    protected String getPort() {
        return this.getString("port");
    }

    protected ClientType getClientType() {
        String string = this.getString("clientType");
        return ClientType.valueOf(string);
    }

    protected double getTotalBalance() {
        return this.getDouble("totalBalance");
    }

    protected double getAvailableFunds() {
        return this.getDouble("availableFunds");
    }

    protected int getTransactionID() {
        return this.getInt("transactionID");
    }

    protected TransactionType getTransactionType() {
        String string = this.getString("transactionType");
        return TransactionType.valueOf(string);
    }

    protected double getAmount() {
        return this.getDouble("amount");
    }

    protected int getTargetAccount() {
        return this.getInt("targetAccount");
    }

    protected int getOtherAccount() {
        return this.getInt("otherAccount");
    }


    protected enum ClientType {
        AGENT,
        VENDOR
    }

    protected enum TransactionType {
        BLOCK,
        WITHDRAW,
        DEPOSIT
    }

}
