package id.ac.umn.simplymeal.ShoppingCart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PaymentMethodPump {
    public static HashMap getData() {
        HashMap expandableListDetail = new HashMap();

        List BCA = new ArrayList();
        BCA.add("1. Log in to your m-BCA account and enter your access code");
        BCA.add("2. Select m-Transfer > BCA Virtual Account");
        BCA.add("3. Enter 39358 followed by your mobile number (e.g. 39358 08xx xxxx xxxx)");
        BCA.add("4. Confirm payment");

        List Mandiri = new ArrayList();
        Mandiri.add("1. Log in to your m-Mandiri account and enter your access code");
        Mandiri.add("2. Select m-Transfer > Mandiri Virtual Account");
        Mandiri.add("3. Enter 39358 followed by your mobile number (e.g. 39358 08xx xxxx xxxx)");
        Mandiri.add("4. Confirm payment");

        List BRI = new ArrayList();
        BRI.add("1. Log in to your m-BRI account and enter your access code");
        BRI.add("2. Select m-Transfer > BRI Virtual Account");
        BRI.add("3. Enter 39358 followed by your mobile number (e.g. 39358 08xx xxxx xxxx)");
        BRI.add("4. Confirm payment");

        expandableListDetail.put("BCA (Bank Central Asia)", BCA);
        expandableListDetail.put("Mandiri", Mandiri);
        expandableListDetail.put("BRI (Bank Rakyat Indonesia)", BRI);
        return expandableListDetail;
    }
}
