
# Interswitch Payment SDK

This library aids in processing payment through the following channels
- [x] Card
- [x] Verve Wallet
- [x] QR Code
- [x] USSD


# Usage
There are three steps you would have to complete to set up the SDK and perform transaction
 - Install the SDK as a dependency
 - Configure the SDK with Merchant Information
 - Initiate payment with customer details



#### Installation

To install the project, add the following to your root project's `build.gradle`

```groovy

    allprojects {
        repositories {
            google()
            jcenter()
            maven {
             url = uri("https://maven.pkg.github.com/techquest/isw-android-payment-packages")
             credentials {
                 username = "techquest"
                 password = "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"
             }
           }
        }
        
        //... other stuff
    }

```

Then add the following to the your app project's `build.gradle`, check for the [latest version](https://github.com/techquest/isw-mobile-payment-sdk-android/releases)

```groovy
    
    dependencies {
        def versionName = '1.0.4'
        implementation "com.interswitchng:isw-mobile-payment-sdk:$versionName"
    }
```

now build the project.


#### Configuration
You would also need to configure the project with your merchant credentials, and if you would like to customize the colors you would need to override the resource values.

```java

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // configure sdk
        configureSDK();
    }

    public void configureSDK() {
        // use provided configuration for your merchant account
        String clientId = "<your clientId>"; 
        String merchantCode = "<your merchantCode>";
        String clientSecret = "<your clientSecret>";

        // create sdk configuration
        IswSdkConfig config = new IswSdkConfig(clientId, 
                        clientSecre, merchantCode, "566");

        // uncomment to set environment, default is Environment.TEST
        // config.setEnv(Environment.SANDBOX);
        
        // initialize sdk at boot of application
        IswMobileSdk.initialize(this, config);
    }
}
```
Once the SDK has been initialized, you can then perform transactions.


#### Performing Transactions
You can perform a transaction, once the SDK is configured, like so:

1. First you would need to have your activity implement the `IswMobileSdk.IswPaymentCallback` interface


```java
    public class PurchaseActivity extends AppCompatActivity implements IswMobileSdk.IswPaymentCallback {

        // ... other methods
        
        // user cancelled payment with out completion
        @Override
        public void onUserCancel() {
            // handle cancellation
        }

        // user completed the payment
        @Override
        public void onPaymentCompleted(@NonNull IswPaymentResult result) {
            // handle payment result
        }
    }

```
2.  Once you have the implemented the interface, you can trigger payments

```java
    public class PurchaseActivity extends AppCompatActivity implements IswMobileSdk.IswPaymentCallback {
        
        @Override
        protected void onCreate() {
            //.. other stuff
            payButton.setOnclickListener((v) -> {
                initiatePayment();
            });
        }
        
        private void initiatePayment() {
            // set customer info
            String customerId = "<customer-id>",
                    customerName = "<customer-name>",
                    customerEmail = "<customer.email@domain.com>",
                    customerMobile = "<customer-phone>",
                    // generate a unique random
                    // reference for each transaction
                    reference = "<your-unique-ref>";
                        
            // amount in kobo e.g. "N500.00" -> 50000
            int amount = providedAmount; // e.g. 50000
                
            // create payment info
            IswPaymentInfo iswPaymentInfo = new IswPaymentInfo(
                customerId,
                customerName, 
                customerEmail,
                customerMobile,
                currencyCode,
                reference,
                amount
            );

            // trigger payment
            // parameters
            // -- paymentInfo: the payment information to be processed
            // -- activityCallback: the IswPaymentCallback that receives the result
            IswMobileSdk.getInstance().paypay(
                iswPaymentInfo, 
                this
            );
        }
        
    }

```
#### Handling Result
To process the result received `onPaymentCompleted` callback, here are the fields' attributes of the `IswPaymentResult`

| Field                 | Type          | meaning  |   
|-----------------------|---------------|----------|
| responseCode          | String        | txn response code  |
| responseDescription   | String        | txn response code description |
| isSuccessful          | boolean       | flag indicates if txn is successful  |
| transactionReference  | String        | reference for txn  |
| amount                | int           | txn amount  |
| channel               | PaymentChannel| channel used to make payment: one of `CARD`, `WALLET`, `QR`, or `USSD` |


And that is it you can start processing payment in your android app.


# Proguard
If you are using proguard, add the following to your code

```ruby

    # ISW classes
    -keep public class com.interswitchng.iswmobilesdk.IswMobileSdk {
      public protected *;
    }
    
    -keep public interface com.interswitchng.iswmobilesdk.IswMobileSdk$IswPaymentCallback {*;}
    
    -keep public class com.interswitchng.iswmobilesdk.shared.models.core.** {
        public protected *;
        !transient <fields>;
    }
    -keep public class com.interswitchng.iswmobilesdk.shared.models.payment.** {
        public protected *;
        !transient <fields>;
    }

    # SC provider
    -keep class org.spongycastle.**
    -dontwarn org.spongycastle.jce.provider.X509LDAPCertStoreSpi
    -dontwarn org.spongycastle.x509.util.LDAPStoreHelper

```
