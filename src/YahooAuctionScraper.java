import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YahooAuctionScraper {
    private File IMAGE_FOLDER;
    private File AUCTION_FOLDER;

    // test URL
    public static void main(String ...args) {
        //YahooAuctionScraper scraper = new YahooAuctionScraper();
        //YahooAuctionListing yl = scraper.constructListing("https://page.auctions.yahoo.co.jp/jp/auction/k469577055");
        //System.out.println(yl.getProductName());
        System.out.println("AIYA");
    }

    /** Constructs an Yahoo Auction Scraper, specifying folder to store scraped images. */
    public YahooAuctionScraper(File imageFolder, File auctionFolder) {
        IMAGE_FOLDER = imageFolder;
        AUCTION_FOLDER = auctionFolder;
    }

    /** Constructs a YahooAuctionListing object from URL, as well as
     * calling storePhotos function to download photos from the listing. */
    public YahooAuctionListing constructListing(String URL) {
        try {
            Document doc = Jsoup.connect(URL).get();

            // gets pageData string and construct item object
            String pageData = "";
            Elements scripts = doc.select("script[type=\"text/javascript\"]");
            for (Element el: scripts) {
                String elData = el.data();
                if (elData.contains("pageData")) {
                    pageData = elData;
                    break;
                }
            }
            YahooAuctionListing itemListing = processPageData(pageData);
            if (itemListing == null) return null;
            Utils.writeObject(Utils.join(AUCTION_FOLDER, itemListing.getProductID()), itemListing);

            // list of imageURLs
            List<String> imageURLs = new ArrayList<String>();
            Elements images = doc.select(".ProductImage__thumbnail img");
            for (Element el: images) {
                imageURLs.add(el.absUrl("src"));
            }
            //System.out.println(imageURLs);
            storePhotos(imageURLs, itemListing.getProductID());
            return itemListing;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /** Returns Yahoo Auction Listing object with attributes in pageData.
     * Returns null if pageData missing any required information. */
    public YahooAuctionListing processPageData(String pageData) {

        // patterns and corresponding matchers
        Pattern productIDPattern = Pattern.compile("(?s).*\"productID\":\\s\"(.*?)\"(?s).*");
        Matcher m1 = productIDPattern.matcher(pageData);
        Pattern productNamePattern = Pattern.compile("(?s).*\"productName\":\\s\"(.*?)\"(?s).*");
        Matcher m2 = productNamePattern.matcher(pageData);
        Pattern pricePattern = Pattern.compile("(?s).*\"price\":\\s\"(.*?)\"(?s).*");
        Matcher m3 = pricePattern.matcher(pageData);
        Pattern endTimePattern = Pattern.compile("(?s).*\"endtime\":\\s\"(.*?)\"(?s).*");
        Matcher m4 = endTimePattern.matcher(pageData);
        Pattern isClosedPattern = Pattern.compile("(?s).*\"isClosed\"\\s:\\s\"(.*?)\"(?s).*");
        Matcher m5 = isClosedPattern.matcher(pageData);

        // constructs and returns the item object (or null)
        if (!m1.matches() || !m2.matches() || !m3.matches() || !m4.matches() || !m5.matches()) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endDate;
        try {
            endDate = formatter.parse(m4.group(1));
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return new YahooAuctionListing(m1.group(1), m2.group(1), Integer.parseInt(m3.group(1)),
                                        endDate, m5.group(1).equals("1"));
    }

    /** Downloads photos from list of imageURLs into a newly-created
     * folder named folderName inside the IMAGE_PATH folder. */
    public void storePhotos(List<String> imageURLs, String folderName) {
        File photoFolder = Utils.join(IMAGE_FOLDER, folderName);
        if (photoFolder.exists()) {
            return;
        }
        photoFolder.mkdir();
        int count = 0;
        try {
            for (String url: imageURLs) {
                BufferedImage img = ImageIO.read(new URL(url));
                System.out.println(img.toString());
                System.out.println("_____________________");
                ImageIO.write(img, "jpg", Utils.join(photoFolder, folderName + "img" + count + ".jpg"));
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
