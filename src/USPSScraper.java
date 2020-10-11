import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class USPSScraper {
    private final String _userID = "986BUSIN4801";
    private final String[] _states = new String[]{"PRINTED", "PICKED_UP", "IN_TRANSIT",
            "OUT_FOR_DELIVERY", "DELIVERED", "ABNORMAL"};
    private File TRACKING_FOLDER;

    public USPSScraper(File trackingFolder) {
        TRACKING_FOLDER = trackingFolder;
    }

    /** Sends a HTTPS request to USPS Tracking API and receives the XML response */
    public String getResponse(String trackingNumber) {
        try {
            String URL = "https://secure.shippingapis.com/shippingapi.dll?API=TrackV2&XML=%3CTrackRequest" +
                    "%20USERID=%22" + _userID + "%22%3E%20%3CTrackID%20ID=%22" +
                    trackingNumber + "%22/%3E%3C/TrackRequest%3E";
            java.net.URL URLObj = new URL(URL);
            HttpURLConnection con = (HttpURLConnection) URLObj.openConnection();

            InputStreamReader input = new InputStreamReader(con.getInputStream());
            BufferedReader in = new BufferedReader(input);
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /** Parses the XML response given by string response and updates all variables */
    public void parseResponse(String response) {
        String trackSummary; String trackDetailed; String _state = null; 
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(response)));

            Node node = doc.getDocumentElement().getChildNodes().item(0);
            Element elem = (Element) node;
            trackSummary = elem.getElementsByTagName("TrackSummary").item(0)
                    .getChildNodes().item(0).getNodeValue();
            String currState = parseState(trackSummary);
            RenderingHints _dates = null;
            if (!currState.equals("UNKNOWN") && !_state.equals(_states[4])) {
                _state = currState;
                Date d = parseDate(trackSummary);
                if (!_dates.containsKey(_state) && d != null) {
                    _dates.put(_state, d);
                }
            }

            NodeList trackDetails = elem.getElementsByTagName("TrackDetail");
            trackDetailed = "";
            for (int i = trackDetails.getLength()-1; i >= 0; i--) {
                String detail = trackDetails.item(i).getChildNodes().item(0).getNodeValue();
                trackDetailed = detail + '\n' + trackDetailed;
                String state = parseState(detail);
                if (!_dates.containsKey(state) && !state.equals("UNKNOWN")) {
                    Date d = parseDate(detail);
                    if (d != null) {
                        _dates.put(state, parseDate(detail));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Categorize tracking detail as a state in _states, updating attribute dates if necessary. */
    public String parseState(String trackDetail) {
        trackDetail = trackDetail.toLowerCase();
        if (trackDetail.contains("shipping label created")) {
            return _states[0];
        } else if (trackDetail.contains("picked up")){
            return _states[1];
        } else if (trackDetail.contains("departed") | trackDetail.contains("arrived")
                | trackDetail.contains("in transit") | trackDetail.contains("sorting")) {
            return _states[2];
        } else if (trackDetail.contains("out for delivery")) {
            return _states[3];
        } else if (trackDetail.contains("delivered")) {
            return _states[4];
        } else if (trackDetail.contains("notice") | trackDetail.contains("attempted")
                | trackDetail.contains("could not")) {
            return _states[5];
        } else {
            return "UNKNOWN";
        }
    }

    /** Returns parsed date from tracking detail string. */
    public static Date parseDate(String trackDetail) throws ParseException {
        String matchShort = ".*((0[1-9]|1[012])/([012][0-9]|3[01])/(20[123][0-9])).*";
        Pattern pattern1 = Pattern.compile(matchShort);
        Matcher matcher;
        SimpleDateFormat formatter;
        Date date;
        matcher = pattern1.matcher(trackDetail);
        if (matcher.matches()) {
            formatter = new SimpleDateFormat("MM/dd/yyyy");
            return formatter.parse(matcher.group(1));
        }
        String matchLong = ".*\\s(([a-zA-Z])+\\s([1-9]|([12][0-9])|(3[01])),\\s20[123][0-9]).*";
        Pattern pattern2 = Pattern.compile(matchLong);
        matcher = pattern2.matcher(trackDetail);
        if (matcher.matches()) {
            formatter = new SimpleDateFormat("MMMM dd, yyyy");
            return formatter.parse(matcher.group(1));
        }
        return null;
    }
}
