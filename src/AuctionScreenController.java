import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AuctionScreenController implements Initializable {

    private List<YahooAuctionListing> _content;
    @FXML ScrollPane scrollpane;
    @FXML TilePane tilepane;
    private MainHub _mainHub;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("NEW SCENE");
        //System.out.println(textfield);
        //buildScrollPane();
        //buildScrollPane();
    }

    /** Sets the content to display in auction screen. */
    public void setContent(List<YahooAuctionListing> content) {
        _content = content;
        System.out.println("SET" + content);
        System.out.println(_content);
    }

    public void setMainHub(MainHub mainHub) {
        System.out.println("MAINHUB SET");
        _mainHub = mainHub;
        buildScrollPane();
    }

    /** Constructs the scroll pane of auctions. */
    public void buildScrollPane() {
        System.out.println(_mainHub);
        List<YahooAuctionListing> auctionList = _content;
        VBox tiles[] = new VBox[auctionList.size()];
        for (int i = 0; i < auctionList.size(); i++) {
            YahooAuctionListing yal = auctionList.get(i);
            tiles[i] = new VBox();
            Label auctionID = new Label(yal.getProductID());
            Label dateLabel = new Label("Date: " + yal.getEndTime().toString());
            Label finalPrice = new Label("Price: " + yal.getPriceInYen() + " Yen");

            BufferedImage auctionImg = _mainHub.getYahooHub().getNthImage(yal.getProductID(), 0);
            ImageView iv = new ImageView();
            iv.setFitWidth(100);
            iv.setPreserveRatio(true);
            iv.setImage(SwingFXUtils.toFXImage(auctionImg, null));

            tiles[i].getChildren().addAll(auctionID, iv, dateLabel, finalPrice);

            tiles[i].setStyle("-fx-border-color: black;");
            tiles[i].setPadding(new Insets(10));

            tiles[i].setOnMouseClicked((e) -> displayAuctionWindow(yal));
            tilepane.getChildren().add(tiles[i]);
        }
        System.out.println("____________________");
        System.out.println("BUILT");
        //tilepane.setMaxWidth(Region.USE_PREF_SIZE);
    }

    /** Shows a separate auction window when auction VBox clicked. */
    public void displayAuctionWindow(YahooAuctionListing yal) {
        System.out.println("CLICKED" + yal.getProductID());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AuctionDetail.fxml"));
            Parent root = loader.load();
            AuctionDetailController controller = loader.getController();


            Stage newWindow = new Stage();
            newWindow.setTitle(yal.getProductID());
            controller.setAttributes(_mainHub, yal, newWindow);
            newWindow.setScene(new Scene(root));
            newWindow.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
