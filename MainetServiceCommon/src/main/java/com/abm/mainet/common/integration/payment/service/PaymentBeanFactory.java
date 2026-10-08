/**
 *
 */
package com.abm.mainet.common.integration.payment.service;

import com.abm.mainet.common.constant.MainetConstants;

/**
 * @author umashanker.kanaujiya this class are used for object creation
 */
public class PaymentBeanFactory {

    public static PaymentStrategy getInstance(final String pgName) {
        PaymentStrategy paymentStrategy = null;
        if ((pgName != null) && pgName.equalsIgnoreCase(MainetConstants.PG_SHORTNAME.PAYU)) {
            paymentStrategy = new PayuPayment();
        } else if ((pgName != null) && pgName.equalsIgnoreCase(MainetConstants.PG_SHORTNAME.HDFC)) {
            paymentStrategy = new HDFCPayment();
        } else if ((pgName != null) && (pgName.equalsIgnoreCase(MainetConstants.PG_SHORTNAME.TECH_PROCESS)||pgName.equalsIgnoreCase(MainetConstants.PG_SHORTNAME.TECH_PROCESS_PNB))) {
            paymentStrategy = new TechProcessPayment();
        } else if ((pgName != null) && pgName.equalsIgnoreCase(MainetConstants.PG_SHORTNAME.MAHA_ONLINE)) {
            paymentStrategy = new MahaOnlinePayment();
        } else if ((pgName != null) && pgName.equalsIgnoreCase(MainetConstants.PG_SHORTNAME.CCA)) {
            paymentStrategy = new CCAvenue();
        } else if ((pgName != null) && pgName.equalsIgnoreCase(MainetConstants.PG_SHORTNAME.AWL)) {
            paymentStrategy = new Awlline();
        } else if ((pgName != null) && pgName.equalsIgnoreCase(MainetConstants.PG_SHORTNAME.EASYPAY)) {
            paymentStrategy = new EasyPay();
        } else if ((pgName != null) && pgName.equalsIgnoreCase(MainetConstants.PG_SHORTNAME.ICICI)) {
            paymentStrategy = new ICICIPayment();
        } else if ((pgName != null) && pgName.equalsIgnoreCase(MainetConstants.PG_SHORTNAME.BILLCLOUD)) {
            paymentStrategy = new BillCloud();
        } else if ((pgName != null) && pgName.equalsIgnoreCase(MainetConstants.PG_SHORTNAME.BILLDESK)) {
            paymentStrategy = new BillDesk();
        } else if ((pgName != null) && pgName.equalsIgnoreCase(MainetConstants.PG_SHORTNAME.RAZORPAY)) {
            paymentStrategy = new Razorpay();
        }
        else if ((pgName != null) && pgName.equalsIgnoreCase(MainetConstants.PG_SHORTNAME.ISGPAY)) {
            paymentStrategy = new ISGPay();
        }
        else if ((pgName != null) && pgName.equalsIgnoreCase(MainetConstants.PG_SHORTNAME.GETEPAY_IDBI)) {
            paymentStrategy = new GetPay();
        }
        return paymentStrategy;
    }

}
