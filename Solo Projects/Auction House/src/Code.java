/**
 * Code provides project-wide support for passing information. It is a
 * required part of any Message object, (there are as-of-yet unfulfilled
 * plans to enforce the passing of a Code enum upon Message creation), and
 * all applications within the project rely on the presence of a Code to
 * carry out the processing of a Message.
 *
 * Most Codes serve as primary selection keys, but some are utilized only in
 * secondary code positions where a series of Messages would otherwise have
 * been required.
 *
 * @author  Tanner J. Evans
 * @version %I%, %G%
 * @since   11.0.8
 */

public enum Code {
    REGISTER,
    DEREGISTER,
    BLOCK,
    UNBLOCK,
    TRANSFER,
    ACCOUNT_SUMMARY,
    ITEM_SUMMARY,
    PLACE_BID,
    BID_TOO_LOW,
    ITEM_SOLD,
    CLOSE_SALE,
    NEW_VENDOR,
    OUTBID,
    SUCCESS,
    INSUFFICIENT_FUNDS,
    BLOCK_RESPONSE,
    ITEM_TRANSFER,
    BID_PLACED,
    ACCOUNT_ID,
    HANDSHAKE
}
