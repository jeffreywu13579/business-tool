import java.util.*;

public class RedditListing {
    private final String[] STATES = new String[]{"OPEN", "PENDING", "CLOSED", "SOLD"};
    private String _title;
    private String _text;
    private Item _item;
    private double _askingPrice;
    private double _soldPrice;
    private Date _listDate;
    private String _state;
    private String _url;
    private List<String> _itemIDs;
    private Map<String, String> _itemIDToTracking;

    /** Constructor for a Reddit listing */
    public RedditListing(String title, String text, Item item, double askingPrice, Date listDate, String url) {
        _title = title;
        _text = text;
        _item = item;
        _askingPrice = askingPrice;
        _soldPrice = -1;
        _listDate = listDate;
        _state = STATES[0];
        _url = url;
        _itemIDs = new ArrayList<>();
        _itemIDToTracking = new HashMap<>();
    }

    /** Returns the title of the listing. */
    public String getTitle() {
        return _title;
    }

    /** Returns the text of the listing. */
    public String getText() {
        return _text;
    }

    /** Returns the item associated with the listing. */
    public Item getItem() {
        return _item;
    }

    /** Returns the asking price of the listing. */
    public double getAskingPrice() {
        return _askingPrice;
    }

    /** Returns the listing date. */
    public Date getListDate() {
        return _listDate;
    }

    /** Returns the state of the listing (as listed in STATES array). */
    public String getState() {
        return _state;
    }

    public String getURL() {
        return _url;
    }

    /** Marks the listing as closed. */
    public void close() {
        _state = STATES[2];
    }

    /** Marks the listing as sold, with the final selling price. */
    public void sold(double soldPrice) {
        _state = STATES[3];
        _soldPrice = soldPrice;
    }
}
