package com.interswitchng.mobilesdk;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.interswitchng.iswmobilesdk.IswMobileSdk;
import com.interswitchng.iswmobilesdk.shared.models.core.IswPaymentInfo;
import com.interswitchng.iswmobilesdk.shared.models.core.IswPaymentResult;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.btnPay);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get customer details
                String customerId = "your+customer+id",
                        customerName = "James Emmanuel",
                        customerEmail = "kenneth.ngedo@gmail.com",
                        customerMobile = "08031149929",

                        // txn details
                        currencyCode = "566",
                        reference = "your-unique-ref" + new Date().getTime();

                EditText etAmount = findViewById(R.id.amount);


                int amount;
                String amtString = etAmount.getText().toString();

                // initialize amount
                if (amtString.isEmpty()) amount = 2500 * 100;
                else amount = Integer.parseInt(amtString) * 100;

                // create payment info
                IswPaymentInfo iswPaymentInfo = new IswPaymentInfo(customerId,
                        customerName, customerEmail, customerMobile,
                        currencyCode, reference, amount);


                // trigger payment
                IswMobileSdk.getInstance().pay(MainActivity.this, iswPaymentInfo);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == IswMobileSdk.CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_CANCELED) {
                toast("You cancelled payment, please try again.");
            } else if (resultCode == Activity.RESULT_OK) {
                IswPaymentResult result = IswMobileSdk.getResult(data);
                if (result.isSuccessful)
                    toast("your payment was successful, using: " + result.channel.name());
                else toast("unable to complete payment at the moment, try again later");
            }
        }

    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
