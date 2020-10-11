import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import net.sf.saxon.Configuration;

import javax.xml.transform.TransformerException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {
    @FXML private TreeView treeview;
    @FXML private Button todo;
    @FXML private BorderPane borderpane;
    private MainHub _mainHub;

    /** Initializes the TreeView on the main screen. */
    public void initializeTreeView() {
        TreeItem rootItem = new TreeItem("Root");
        treeview.setRoot(rootItem);
        rootItem.setExpanded(true);

        TreeItem section1 = new TreeItem("Purchases");
        TreeItem section2 = new TreeItem("Listings");
        rootItem.getChildren().addAll(section1, section2);
        section1.setExpanded(true);
        section2.setExpanded(true);

        TreeItem treeItem1 = new TreeItem("Yahoo Auctions");
        TreeItem treeItem2 = new TreeItem("Zenmarket");
        TreeItem treeItem3 = new TreeItem("Reddit Listings");
        TreeItem treeItem4 = new TreeItem("Shipping Labels");
        section1.getChildren().addAll(treeItem1, treeItem2);
        section2.getChildren().addAll(treeItem3, treeItem4);
        treeItem1.setExpanded(true);
        treeItem3.setExpanded(true);

        TreeItem child1 = new TreeItem("Completed");
        TreeItem child2 = new TreeItem("In Progress");
        TreeItem child3 = new TreeItem("Sold Listing");
        TreeItem child4 = new TreeItem("Closed Listing");
        TreeItem child5 = new TreeItem("Open Listing");
        treeItem1.getChildren().addAll(child1, child2);
        treeItem3.getChildren().addAll(child3, child4, child5);
    }

    /** Acknowledges a mouse click on the TreeView. */
    public void clickTreeView(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            TreeItem item = (TreeItem) treeview.getSelectionModel().getSelectedItem();
            String itemValue = (String) item.getValue();
            if (itemValue.equals("Listings") | itemValue.equals("Purchases")
                    | itemValue.equals("Reddit Listings") | itemValue.equals("Yahoo Auctions")) {
                return;
            }
            System.out.println(item.getValue());
            loadCenterPane((String) item.getValue());
        }
    }

    /** Switches the view of the center pane due to clicking on TreeView or on Button. */
    public void loadCenterPane(String ui) {
        String fxmlName = null;
        List<YahooAuctionListing> auctions = null;
        switch (ui) {
            case "Completed":
                fxmlName = "AuctionScreen";
                auctions = _mainHub.getYahooHub().getCompletedListings();
                break;
            case "In Progress":
                fxmlName = "AuctionScreen";
                auctions = _mainHub.getYahooHub().getInProgressListings();
                break;
            case "Zenmarket":
                fxmlName = "ParcelScreen";
                break;
            default:
                System.out.println("AIYA");
                return;
        }
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName + ".fxml"));
            root = loader.load();

            //System.out.println("Controller: " + controller);
            if (fxmlName.equals("AuctionScreen")) {
                AuctionScreenController controller = loader.getController();
                controller.setContent(auctions);
                controller.setMainHub(_mainHub);
            } else if (fxmlName.equals("ParcelScreen")) {
                ParcelScreenController controller = loader.getController();
                controller.passPane(borderpane);
                controller.setMainHub(_mainHub);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        borderpane.setCenter(root);
    }

    public void setMainHub(MainHub mainHub) {
        _mainHub = mainHub;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTreeView();
        System.out.println("HERE");
    }
}
