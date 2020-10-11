import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class YahooAuctionListing implements Serializable {
    private String _productID;
    private String _productName;
    private int _priceInYen;
    private int _shippingInYen;
    private Date _endTime;
    private boolean _isClosed;
    private boolean _purchased;
    private List<String> _itemIDs;
    private int _parcelNum;

    /** Constructs an Yahoo Auction Listing item. */
    public YahooAuctionListing(String productID, String productName, int priceInYen,
                               Date endTime, boolean isClosed) {
        _productID = productID;
        _productName = productName;
        _priceInYen = priceInYen;
        _endTime = endTime;
        _isClosed = isClosed;
        _purchased = false;
        _itemIDs = new ArrayList<>();
        _shippingInYen = 0;
        _parcelNum = -1;
    }

    /** Gets productID of item. */
    public String getProductID() {
        return _productID;
    }

    /** Gets productName of item. */
    public String getProductName() {
        return _productName;
    }

    /** Gets priceInYen of item. */
    public int getPriceInYen() {
        return _priceInYen;
    }

    /** Gets endTime of item. */
    public Date getEndTime() {
        return _endTime;
    }

    /** Returns whether item has closed. */
    public boolean isClosed() {
        return _isClosed;
    }

    /** Returns the cost of shipping for item. */
    public int getShipping() {
        return _shippingInYen;
    }

    /** Returns the parcel number of the item, or -1 if not in parcel. */
    public int getParcelNum() {
        return _parcelNum;
    }

    /** Sets the parcel number of the item. */
    public void setParcelNum(int num) {
        _parcelNum = num;
    }

    /** Sets an auction as having been purchased. */
    public void setPurchased(boolean status) {
        _purchased = status;
    }

    /** Sets the shipping cost. */
    public void setShipping(int shipping) {
        _shippingInYen = shipping;
    }

    /** Returns the list of item IDs of the auction. */
    public List<String> getItemIDs() {
        return _itemIDs;
    }
}
