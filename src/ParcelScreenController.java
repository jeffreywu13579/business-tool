import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ParcelScreenController implements Initializable {
    private MainHub _mainHub;
    @FXML TilePane tilePane;
    @FXML BorderPane borderPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setMainHub(MainHub mainHub) {
        System.out.println("MAINHUB SET");
        _mainHub = mainHub;
        buildScrollPane();
    }

    public void passPane(BorderPane pane) {
        borderPane = pane;
    }

    /** Constructs the scroll pane of auctions. */
    public void buildScrollPane() {
        List<Parcel> parcelList = _mainHub.getZenmarketHub().getAllParcels();
        VBox tiles[] = new VBox[parcelList.size()];
        for (int i = 0; i < parcelList.size(); i++) {
            System.out.println("ARRIVED " + i);
            Parcel parcel = parcelList.get(i);
            tiles[i] = new VBox();
            Label parcelLabel = new Label("Parcel " + parcel.getParcelNum());
            parcelLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
            Label numLabel = new Label("Number of Auctions: " + parcel.getAuctionIDs().size() + " Auctions");
            // add a label for price of items in parcel

            tiles[i].getChildren().addAll(parcelLabel, numLabel);

            tiles[i].setStyle("-fx-border-color: black;");
            tiles[i].setPadding(new Insets(10));

            tiles[i].setOnMouseClicked((e) -> displayAuctionWindow(parcel));
            tilePane.getChildren().add(tiles[i]);
        }
        System.out.println("____________________");
        System.out.println("BUILT");
        //tilepane.setMaxWidth(Region.USE_PREF_SIZE);
    }

    /** Shows a separate window with all auctions in a parcel when parcel clicked. */
    public void displayAuctionWindow(Parcel parcel) {
        System.out.println("CLICKED " + parcel.getParcelNum());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AuctionScreen.fxml"));
            Parent root = loader.load();
            AuctionScreenController controller = loader.getController();

            List<YahooAuctionListing> lst = _mainHub.getYahooHub().getYahooListingObjectList(parcel.getAuctionIDs());
            System.out.println("l " + lst);
            controller.setContent(lst);
            controller.setMainHub(_mainHub);

            borderPane.setCenter(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
