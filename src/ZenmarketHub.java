import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ZenmarketHub {
    public File PARCEL_FOLDER;
    private MainHub _mainHub;
    private List<Integer> _allParcels;

    public ZenmarketHub(File parcelFolder, MainHub mainHub) {
        PARCEL_FOLDER = parcelFolder;
        _mainHub = mainHub;
        _allParcels = new ArrayList<>();
    }

    /** Creates a Zenmarket parcel with given parcelNum. Returns true
     * if the creation successful. Returns false if parcelNum in use already. */
    public boolean createParcel(int parcelNum) {
        if (_allParcels.contains(parcelNum)) {
            return false;
        }
        Parcel newParcel = new Parcel(parcelNum);
        File parcelFile = Utils.join(PARCEL_FOLDER, Integer.toString(parcelNum));
        try {
            Utils.writeObject(parcelFile, newParcel);
        } catch (IOException e) {
            e.printStackTrace();
        }
        _allParcels.add(parcelNum);
        return true;
    }

    /** Attempts to add an auctionID to a parcel. If auction already belongs to said parcel,
     * returns false. Otherwise, returns true. */
    public boolean addAuctionToParcel(int parcelNum, String auctionID) {
        Parcel parcel = getParcelObject(parcelNum);
        if (parcel.getAuctionIDs().contains(auctionID)) {
            return false;
        }
        parcel.getAuctionIDs().add(auctionID); /*
        System.out.println(parcel.getAuctionIDs().toString());
        System.out.println(parcel.hashCode());
        System.out.println("length is " + parcel.getParcelNum() + " " + parcel.getAuctionIDs().size());
        System.out.println("ADDED"); */
        YahooAuctionListing yal = _mainHub.getYahooHub().getYahooListingObject(auctionID);
        yal.setParcelNum(parcelNum);
        File parcelFile = Utils.join(PARCEL_FOLDER, Integer.toString(parcelNum));
        try {
            Utils.writeObject(parcelFile, parcel);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TO FINISH
        return true;

    }

    /** Returns the parcel object given the parcelNum. Returns null if not found. */
    public Parcel getParcelObject(int parcelNum) {
        if (_allParcels.contains(parcelNum)) {
            return Utils.readObject(Utils.join(PARCEL_FOLDER,
                    Integer.toString(parcelNum)), Parcel.class);
        }
        return null;
    }

    /** Returns the list of all parcel objects. */
    public List<Parcel> getAllParcels() {
        List<Parcel> parcelList = new ArrayList<>();
        for (int parcelNum: _allParcels) {
            parcelList.add(getParcelObject(parcelNum));
        }
        return parcelList;
    }

}
