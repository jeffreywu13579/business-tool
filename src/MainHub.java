import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class MainHub implements Serializable {
    private YahooAuctionHub _yahooHub;
    private USPSHub _uspsHub;
    private ZenmarketHub _zenmarketHub;
    private RedditHub _redditHub;

    public MainHub() {
        File f = new File("MainHub");
        if (!f.exists()) {
            initialize();
        } else {
            MainHub hub = Utils.readObject(f, MainHub.class);
            _yahooHub = hub.getYahooHub();
            _uspsHub = hub.getUSPSHub();
            _zenmarketHub = hub.getZenmarketHub();
            _redditHub = hub.getRedditHub();
        }
    }

    /** Initializes the hub by creating folders for data storage. */
    public void initialize() {
        File yahooFolder = new File(".YahooFolder");
        yahooFolder.mkdir();
        File imageDir = Utils.join(yahooFolder, "images");
        File auctionDir = Utils.join(yahooFolder, "auctions");
        File itemDir = Utils.join(yahooFolder, "items");
        imageDir.mkdir();
        auctionDir.mkdir();
        itemDir.mkdir();
        _yahooHub = new YahooAuctionHub(imageDir, auctionDir, itemDir, this);

        File uspsFolder = new File(".USPSFolder");
        uspsFolder.mkdir();
        File trackingDir = Utils.join(uspsFolder, "tracking");
        trackingDir.mkdir();
        _uspsHub = new USPSHub(trackingDir, this);

        File zenmarketFolder = new File(".ZenmarketFolder");
        zenmarketFolder.mkdir();
        File parcelDir = Utils.join(zenmarketFolder, "parcels");
        parcelDir.mkdir();
        _zenmarketHub = new ZenmarketHub(parcelDir, this);

        File redditFolder = new File(".RedditFolder");
        redditFolder.mkdir();
        File listingDir = Utils.join(redditFolder, "listings");
        File imgurDir = Utils.join(redditFolder, "images");
        listingDir.mkdir();
        imgurDir.mkdir();
        _redditHub = new RedditHub(listingDir, imgurDir, this);
    }

    /** Refreshes the state of hubs that need to be updated. */
    public void updateAll() {
        _uspsHub.update();
        _yahooHub.update();
    }

    public YahooAuctionHub getYahooHub() {
        return _yahooHub;
    }

    public USPSHub getUSPSHub() {
        return _uspsHub;
    }

    public ZenmarketHub getZenmarketHub() {
        return _zenmarketHub;
    }

    public RedditHub getRedditHub() {
        return _redditHub;
    }

    /** Saves myself. */
    public void save() {
        try {
            Utils.writeObject(new File("MainHub"), this);
        } catch (Exception e){
            e.printStackTrace();
        }
    }




}
