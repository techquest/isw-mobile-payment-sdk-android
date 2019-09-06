# Interswitch Payment SDK

This library aids in processing payment through the following channels
- [x] Card
- [x] Verve Wallet
- [x] QR Code
- [ ] USSD (Coming soon)
- [ ] PAYCODE (Coming soon)


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
            maven { url "http://dl.bintray.com/techquest/maven-repo" }
        }
        
        //... other stuff
    }

```

Then add the following to the your app project's `build.gradle`

```groovy
    
    dependencies {
        def versionName = 'latest-version'
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
        String merchantId = "<your merchantId>"; 
        String merchantCode = "<your merchantCode>";
        String merchantKey = "<your merchantKey>";

        // create sdk configuration
        IswSdkConfig config = new IswSdkConfig(merchantId, 
                        merchantKey, merchantCode, "566");

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

```java
    public class PurchaseActivity extends AppCompatActivity {
        
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

                    // txn info
                    // Naira's currency code
                    currencyCode = "566",
                    // generate a unique random
                    // reference for each transaction
                    reference = "<your-unique-ref>";
                        
            // amount in kobo e.g. "N500.00" -> 50000
            int amount = providedAmount; // e.g. 50000
                
            // create payment info
            IswPaymentInfo iswPaymentInfo = new IswPaymentInfo(customerId,
                    customerName, customerEmail, customerMobile,
                    currencyCode, reference, amount);

            // trigger payment
            IswMobileSdk.getInstance().pay(this, iswPaymentInfo);
        }
        
    }

```
#### Handling Result
To handle result all you need to do is override on activity result and extract the intent sent back to generate `IswPaymentResult`

| Field                 | Type          | meaning  |   
|-----------------------|---------------|----------|
| responseCode          | String        | txn response code  |
| responseDescription   | String        | txn response code description |
| isSuccessful          | boolean       | flag indicates if txn is successful  |
| transactionReference  | String        | reference for txn  |
| amount                | int           | txn amount  |
| channel               | PaymentChannel| channel used to make payment: one of `CARD, QR, USSD, PAYCODE  |



```java

public class PurchaseActivity extends AppCompatActivity {
    // ... other stuff

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IswMobileSdk.CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_CANCELED) {
                toast("You cancelled payment, please try again.");
            } else if (resultCode == Activity.RESULT_OK) {
                IswPaymentResult result = IswMobileSdk.getResult(data);
                if (result.isSuccessful) toast("successful: " + result.channel.name());
                else toast("failed, try again later");
            }
        }
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}


```
And that is it you can start processing payment in your android app.
