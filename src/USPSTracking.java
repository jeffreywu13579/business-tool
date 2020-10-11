import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class USPSTracking {
    private final String _userID = "986BUSIN4801";
    private final String[] _states = new String[]{"PRINTED", "PICKED_UP", "IN_TRANSIT",
                                        "OUT_FOR_DELIVERY", "DELIVERED", "ABNORMAL"};
    private String _trackingNumber;
    private boolean _isDelivered;
    private String _state;
    private String _trackSummary;
    private String _trackDetails;
    private HashMap<String, Date> _dates;

    public static void main(String ...args) {
        String num1 = "9400128206335190333075";
        String num2 = "9400128206335210922357";
        USPSTracking ex = new USPSTracking(num2);
        System.out.println("--------------");
        for (String d: ex._dates.keySet()) {
            System.out.println(d + " " + ex._dates.get(d));
        }

    }

    public USPSTracking(String trackingNumber) {
        _trackingNumber = trackingNumber;
        _isDelivered = false;
        _state = _states[0];
        _dates = new HashMap<>();
        update();
    }

    /** Updates the tracking status. If item has been updated as delivered before, do nothing.
     * Else, get the latest tracking details from USPS and updates variables if necessary */
    public void update() {
        if (_isDelivered) {
            return;
        }
        String response = getResponse();
        parseResponse(response);
    }

    /** Sends a HTTPS request to USPS Tracking API and receives the XML response */
    public String getResponse() {
        try {
            String URL = "https://secure.shippingapis.com/shippingapi.dll?API=TrackV2&XML=%3CTrackRequest" +
                    "%20USERID=%22" + _userID + "%22%3E%20%3CTrackID%20ID=%22" +
                    _trackingNumber + "%22/%3E%3C/TrackRequest%3E";
            URL URLObj = new URL(URL);
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
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(response)));

            Node node = doc.getDocumentElement().getChildNodes().item(0);
            Element elem = (Element) node;
            _trackSummary = elem.getElementsByTagName("TrackSummary").item(0)
                    .getChildNodes().item(0).getNodeValue();
            String currState = parseState(_trackSummary);
            if (!currState.equals("UNKNOWN") && !_state.equals(_states[4])) {
                _state = currState;
                Date d = parseDate(_trackSummary);
                if (!_dates.containsKey(_state) && d != null) {
                    _dates.put(_state, d);
                }
            }

            NodeList trackDetails = elem.getElementsByTagName("TrackDetail");
            _trackDetails = "";
            for (int i = trackDetails.getLength()-1; i >= 0; i--) {
                String detail = trackDetails.item(i).getChildNodes().item(0).getNodeValue();
                _trackDetails = detail + '\n' + _trackDetails;
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

    /** Returns whether the package has been delivered. */
    public boolean isDelivered() {
        return _isDelivered;
    }

    /** Returns number of days the package has been in transit. */
    public int daysInTransit() {
        return 0;
    }

    /** Returns a String representation of complete tracking details of item. */
    public String getDetails() {
        return _trackDetails;
    }

    /** Returns a String representation of tracking summary of item. */
    public String getSummary() {
        return _trackSummary;
    }

    /** Returns whether item is maybe lost (defined as more than 9 days in transit). */
    public boolean isAbnormal() {
        if (isDelivered()) {
            return false;
        }
        return _state.equals(_states[5]);
    }

}
