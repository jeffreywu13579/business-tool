import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainHub mainHub = new MainHub();

        // just to have data to play around with
        YahooAuctionHub hub = mainHub.getYahooHub();
        hub.processListing("https://page.auctions.yahoo.co.jp/jp/auction/b469505339", true);
        hub.processListing("https://page.auctions.yahoo.co.jp/jp/auction/470349095", true);
        hub.processListing("https://page.auctions.yahoo.co.jp/jp/auction/s738834370", true);
        hub.processListing("https://page.auctions.yahoo.co.jp/jp/auction/n425661355", true);
        hub.processListing("https://page.auctions.yahoo.co.jp/jp/auction/j656947308", false);
        ZenmarketHub zenHub = mainHub.getZenmarketHub();
        zenHub.createParcel(1);
        zenHub.createParcel(2);
        zenHub.addAuctionToParcel(1, "b469505339");
        zenHub.addAuctionToParcel(2, "n425661355");
        zenHub.addAuctionToParcel(2, "s738834370");
        System.out.println("debug" + zenHub.getParcelObject(1).getAuctionIDs().toString());
        System.out.println("debug" + zenHub.getParcelObject(2).getAuctionIDs().toString());







        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
        Parent root = loader.load();
        MainScreenController controller = loader.getController();
        controller.setMainHub(mainHub);
        Scene mainScene = new Scene(root);
        primaryStage.setTitle("Business Tool");
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }


}
