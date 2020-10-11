import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Item implements Serializable {
    public static String[] CONDITIONS = new String[]{"A1", "A2", "B+", "B", "B-", "C+", "C", "C-", "D", "F"};
    public static String[] ITEM_TYPES = new String[]{"Fountain Pen", "Mechanical Pencil", "Ballpoint Pen", "Multi-Pen"};
    private String _brand;
    private String _model;
    private String _details;
    private String _itemType;
    private String _condition;
    private String _hash;
    private String _auctionID;
    private List<String> _redditListings;

    public Item(String brand, String model, String details, String itemType, String condition, String auctionID) {
        _brand = brand;
        _model = model;
        _details = details;
        _itemType = itemType;
        _condition = condition;
        _auctionID = auctionID;
        _redditListings = new ArrayList<String>();
        try {
            _hash = Utils.sha1(Utils.serialize(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Returns degree of similarity of item - 3 or 4 counts as similar */
    public int similarityScore(Item otherItem) {
        int similarity = 0;
        if (otherItem.getBrand().toLowerCase().equals(_brand.toLowerCase())) {
            similarity++;
        } if (_model.length() != 0 && otherItem.getModel().length() != 0) {
            String[] words = otherItem.getModel().split("\\W+");
            for (String word: words) {
                if (_model.toLowerCase().contains(word.toLowerCase())) {
                    similarity++; break;
                }
            }
        } if (_details.length() != 0 && otherItem.getDetails().length() != 0) {
            String[] words = otherItem.getDetails().split("\\W+");
            for (String word : words) {
                if (_details.toLowerCase().contains(word.toLowerCase())) {
                    similarity++;
                    break;
                }
            }
        } if (_itemType.equals(otherItem.getItemType())) {
            similarity++;
        }
        return similarity;
    }

    public String getBrand() {
        return _brand;
    }

    public String getModel() {
        return _model;
    }

    public String getDetails() {
        return _details;
    }

    public String getItemType() {
        return _itemType;
    }

    public String getCondition() {
        return _condition;
    }

    public String getHash() {
        return _hash;
    }

    public String getAuctionID() {
        return _auctionID;
    }

    public List<String> getRedditListings() {
        return _redditListings;
    }

    public void setBrand(String brand) {
        _brand = brand;
    }

    public void setModel(String model) {
        _model = model;
    }

    public void setDetails(String detail) {
        _details = detail;
    }

    public void setItemType(String itemType) {
        _itemType = itemType;
    }

    public void setCondition(String condition) {
        _condition = condition;
    }

}
