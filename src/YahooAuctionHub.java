import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YahooAuctionHub {
    private List<String> _completed;
    private List<String> _inProgress;
    public File IMAGE_FOLDER;
    public File AUCTION_FOLDER;
    public File ITEM_FOLDER;
    private MainHub _mainHub;

    public YahooAuctionHub(File imageFolder, File auctionFolder, File itemFolder, MainHub mainHub) {
        IMAGE_FOLDER = imageFolder;
        AUCTION_FOLDER = auctionFolder;
        ITEM_FOLDER = itemFolder;
        _completed = new ArrayList<>();
        _inProgress = new ArrayList<>();
        System.out.println("GOT HERE");
        _mainHub = mainHub;
    }

    /** Processes a Yahoo Auction Listing. Returns true if successful. */
    public boolean processListing(String URL, boolean purchased) {
        YahooAuctionScraper scraper = new YahooAuctionScraper(IMAGE_FOLDER, AUCTION_FOLDER);
        YahooAuctionListing listing = scraper.constructListing(URL);
        if (listing == null) {
            System.out.println("FUCKED UP");
            return false;
        } else {
            if (listing.isClosed()) {
                _completed.add(listing.getProductID());
            } else {
                _inProgress.add(listing.getProductID());
            }
            listing.setPurchased(purchased);
            return true;
        }
    }

    /** Returns a list of completed Yahoo Auction Listing items (reads the objects
     * from the AUCTION_FOLDER. */
    public List<YahooAuctionListing> getCompletedListings() {
        List<YahooAuctionListing> allListings = new ArrayList<>();
        System.out.println("allListings is " + allListings);
        for (String id: _completed) {
            allListings.add(Utils.readObject(Utils.join(AUCTION_FOLDER, id), YahooAuctionListing.class));
        }
        return allListings;
    }

    /** Returns a list of in progress Yahoo Auction Listing items (reads the objects
     * from the AUCTION_FOLDER. */
    public List<YahooAuctionListing> getInProgressListings() {
        List<YahooAuctionListing> allListings = new ArrayList<>();
        for (String id: _inProgress) {
            allListings.add(Utils.readObject(Utils.join(AUCTION_FOLDER, id), YahooAuctionListing.class));
        }
        return allListings;
    }

    /** Updates statuses of all auctions tracked by the hub. */
    public void update() {
        // TO-DO
    }

    /** Adds an item to the listing, serializing the item in the process. */
    public void addItemToAuction(YahooAuctionListing listing, Item item) {
        try {
            Utils.writeObject(Utils.join(ITEM_FOLDER, item.getHash()), item);
            listing.getItemIDs().add(item.getHash());
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /** Deletes an item from the listing, removing the serialization in the process. */
    public void deleteItemFromAuction(YahooAuctionListing listing, Item item) {
        String itemHash = item.getHash();
        if (listing.getItemIDs().contains(itemHash)) {
            listing.getItemIDs().remove(itemHash);
            try {
                Utils.writeObject(AUCTION_FOLDER, listing);
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (String listingID: item.getRedditListings()) {
                _mainHub.getRedditHub().removeListing(listingID, itemHash);
            }
            Utils.join(ITEM_FOLDER, itemHash).delete();
        }
    }

    /** Returns Yahoo Auction Listing object of corresponding listingID,
     * or null if not found. */
    public YahooAuctionListing getYahooListingObject(String listingID) {
        File listingFile = Utils.join(AUCTION_FOLDER, listingID);
        if (!listingFile.exists()) {
            return null;
        }
        return Utils.readObject(listingFile, YahooAuctionListing.class);
    }

    public List<YahooAuctionListing> getYahooListingObjectList(List<String> listingIDs) {
        List<YahooAuctionListing> lst = new ArrayList<>();
        for (String id: listingIDs) {
            YahooAuctionListing obj = getYahooListingObject(id);
            if (obj == null) {
                return null;
            }
            lst.add(obj);
        }
        return lst;
    }

    /** Returns the nth image of an auction, or null if image does not exist. */
    public BufferedImage getNthImage(String listingID, int n) {
        File imgFile = Utils.join(Utils.join(IMAGE_FOLDER, listingID),
                listingID + "img" + n + ".jpg");
        if (imgFile.exists()) {
            BufferedImage img;
            try {
                img = ImageIO.read(imgFile);
                return img;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

}
