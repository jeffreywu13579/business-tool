import java.io.File;
import java.io.IOException;
import java.util.List;

public class RedditHub {
    public File LISTING_FOLDER;
    public File IMAGE_FOLDER;
    private List<String> _completed;
    private List<String> _sold;
    private List<String> _open;
    private MainHub _mainHub;

    public RedditHub(File listingFolder, File imageFolder, MainHub mainHub) {
        LISTING_FOLDER = listingFolder;
        IMAGE_FOLDER = imageFolder;
        _mainHub = mainHub;
    }

    /** Adds a Reddit listing to an item. */
    public void addListing(String URL, String itemID) {
        // TO-DO
    }

    /** Removes a Reddit listing from an item. */
    public void removeListing(String listingID, String itemID) {
        if (_completed.contains(listingID)) {
            _completed.remove(listingID);
        } else if (_sold.contains(listingID)) {
            _sold.remove(listingID);
        } else if (_open.contains(listingID)) {
            _open.remove(listingID);
        }
        Utils.join(LISTING_FOLDER, listingID).delete();
        Utils.join(IMAGE_FOLDER, listingID).delete();

        File itemFile = Utils.join(_mainHub.getYahooHub().ITEM_FOLDER, itemID);
        Item item = Utils.readObject(itemFile, Item.class);
        item.getRedditListings().remove(listingID);
        try {
            Utils.writeObject(itemFile, item);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTrackingToListing(String listingID, String trackingNum) {

    }

    public void removeTrackingFromListing(String listingID, String trackingNum) {

    }


}
