package com.interswitchng.mobilesdk;


import androidx.multidex.MultiDexApplication;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.interswitchng.iswmobilesdk.IswMobileSdk;
import com.interswitchng.iswmobilesdk.shared.models.core.Environment;
import com.interswitchng.iswmobilesdk.shared.models.core.IswSdkConfig;

public class MyApplication extends MultiDexApplication {


    @Override
    public void onCreate() {
        super.onCreate();

        // update play services if needed
        updateAndroidSecurityProvider();
        // configure sdk
        configureSDK();
    }

    public void configureSDK() {
        String merchantId = BuildConfig.MERCHANT_ID;
        String merchantCode = BuildConfig.MERCHANT_CODE;
        String merchantKey = BuildConfig.MERCHANT_KEY;

        IswSdkConfig config = new IswSdkConfig(merchantId, merchantKey, merchantCode, "566");

        // uncomment to set environment, default is Environment.TEST
        // config.setEnv(Environment.SANDBOX);

        IswMobileSdk.initialize(this, config);
    }

    // for devices below android lollipop
    private void updateAndroidSecurityProvider() {
        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (GooglePlayServicesRepairableException e) {
            // Thrown when Google Play Services is not installed, up-to-date, or enabled
            // Show dialog to allow users to install, update, or otherwise enable Google Play services.
            // GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), callingActivity, 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            // Log.e("SecurityException", "Google Play Services not available.");
        }
    }
}
