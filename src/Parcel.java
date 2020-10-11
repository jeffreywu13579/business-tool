import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Parcel implements Serializable {
    private List<String> _auctionIDs;
    private MainHub _mainHub;
    private int _parcelNum;

    public Parcel(int parcelNum) {
        _auctionIDs = new ArrayList<>();
        _parcelNum = parcelNum;
    }

    /** Returns IDs of all Yahoo Auctions in the parcel. */
    public List<String> getAuctionIDs() {
        return _auctionIDs;
    }

    /** Returns the number of the parcel. */
    public int getParcelNum() {
        return _parcelNum;
    }


}
