import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class USPSUnitTests {

    @Test
    public void testParseDate() throws ParseException {
        String dateShort = "In Transit to Next Facility, 05/22/2020";
        String dateLong = "Shipping Label Created, USPS Awaiting Item, May 20, 2020, 4:15 am, PLEASANTON, CA 94566";
        System.out.println(USPSTracking.parseDate(dateShort));
        System.out.println(USPSTracking.parseDate(dateLong));
    }
}
