import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AuctionDetailController implements Initializable {
    private MainHub _mainHub;
    private YahooAuctionListing _listing;
    private Stage _stage;
    private int _imageNum = 0;
    private int _numImages;
    @FXML private VBox vbox;
    @FXML private Label titleLabel;
    @FXML private ImageView imageView;
    @FXML private Label auctionID;
    @FXML private Label price;
    @FXML private Label endTime;
    @FXML private Label isClosed;
    @FXML private Label purchased;
    @FXML private Label shippingCost;
    @FXML private Label parcelNum;
    @FXML private Button leftButton;
    @FXML private Button rightButton;
    @FXML private Label imageDisplayed;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /** Sets the attributes, the MainHub and the auction in question,
     * and builds the content to display */
    public void setAttributes(MainHub mainHub, YahooAuctionListing yal, Stage stage) {
        _mainHub = mainHub; _listing = yal; _stage = stage;
        _numImages = Utils.join(_mainHub.getYahooHub().IMAGE_FOLDER, yal.getProductID()).listFiles().length;

        titleLabel.setText(yal.getProductName());
        auctionID.setText("Auction ID: " + yal.getProductID());
        price.setText("Price: " + Integer.toString(yal.getPriceInYen()) + " Yen");
        endTime.setText("End Date: " + yal.getEndTime().toString());
        isClosed.setText("Closed: " + yal.isClosed());
        String parcelNumText = yal.getParcelNum()!=-1 ? Integer.toString(yal.getParcelNum()) : "N/A";
        parcelNum.setText("Parcel Number: " + parcelNumText);

        BufferedImage auctionImg = _mainHub.getYahooHub().getNthImage(yal.getProductID(), 0);
        imageView.setFitWidth(300);
        imageView.setPreserveRatio(true);
        if (auctionImg != null) {
            imageView.setImage(SwingFXUtils.toFXImage(auctionImg, null));
            imageDisplayed.setText("1/" + _numImages);
        }

        leftButton.setOnMouseClicked((e) -> {
            if (_numImages == 0 || _imageNum == 0) return;
            _imageNum = (_imageNum - 1);
            BufferedImage img = _mainHub.getYahooHub().getNthImage(yal.getProductID(), _imageNum);
            imageView.setImage(SwingFXUtils.toFXImage(img, null));
            imageDisplayed.setText((_imageNum+1) + "/" + _numImages);
        });
        rightButton.setOnMouseClicked((e) -> {
            if (_numImages == 0 || _imageNum == _numImages-1) return;
            _imageNum = (_imageNum + 1);
            BufferedImage img = _mainHub.getYahooHub().getNthImage(yal.getProductID(), _imageNum);
            imageView.setImage(SwingFXUtils.toFXImage(img, null));
            imageDisplayed.setText((_imageNum+1) + "/" + _numImages);
        });

    }
}
