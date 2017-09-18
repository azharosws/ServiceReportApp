package com.azhar.servicereportapp.networkUtil;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by azhar-sarps on 20-Jun-17.
 */

public class WebserviceAPI {

    Context context;


    public static final String MAIN_URL="http://www.grapemundo.com";
    public static final String REG_URL="http://www.grapemundo.com/vactech/sr/api/register.php";
    public static final String LOGIN_URL=MAIN_URL+"/vactech/sr/api/login.php";
    public static final String HISTORY_URL=MAIN_URL+"/vactech/sr/api/complaints.php";
    public static final String PROFILEUPDATE_URL=MAIN_URL+"/vactech/sr/api/uProfile.php";
    public static final String IMGPRODUCT_URL=MAIN_URL+"/vactech/sr/p/";
    public static final String IMGCPRODUCT_URL=MAIN_URL+"/vactech/sr/cmp/";


    public WebserviceAPI(Context context) {
        this.context = context;
    }

    public String getImie(){
        TelephonyManager mngr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return mngr.getDeviceId();
    }




}
