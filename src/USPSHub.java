import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class USPSHub {
    private List<String> _printed;
    private List<String> _pickedUp;
    private List<String> _inTransit;
    private List<String> _outForDelivery;
    private List<String> _delivered;
    private List<String> _maybeLost;
    public File TRACKING_FOLDER;
    private MainHub _mainHub;

    public USPSHub(File trackingFolder, MainHub mainHub) {
        _printed = new ArrayList<String>();
        _pickedUp = new ArrayList<String>();
        _inTransit = new ArrayList<String>();
        _outForDelivery = new ArrayList<String>();
        _delivered = new ArrayList<String>();
        _maybeLost= new ArrayList<String>();
        TRACKING_FOLDER = trackingFolder;
        _mainHub = mainHub;
    }

    /** Processes a USPS Tracking Number. Returns true if successful. */
    public void processTracking(String tracking) {
        USPSScraper scraper = new USPSScraper(TRACKING_FOLDER);
    }

    /** Updates statuses of all tracking numbers in the hub */
    public void update() {
        // TO-DO
    }

}
