package com.interswitchng.mobilesdk;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.interswitchng.iswmobilesdk.IswMobileSdk;
import com.interswitchng.iswmobilesdk.shared.models.core.IswPaymentInfo;
import com.interswitchng.iswmobilesdk.shared.models.core.IswPaymentResult;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements IswMobileSdk.IswPaymentCallback {


    private LinearLayout resultContainer;

    private TextView resultTitle,
            responseCode,
            responseDescription,
            paymentAmount,
            channel,
            isSuccessful;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        resultContainer = findViewById(R.id.resultContainer);

        resultTitle = findViewById(R.id.resultTitle);
        responseCode = findViewById(R.id.responseCode);
        responseDescription = findViewById(R.id.responseDescription);
        paymentAmount = findViewById(R.id.paymentAmount);
        channel = findViewById(R.id.channel);
        isSuccessful = findViewById(R.id.isSuccessful);

        final EditText etAmount = findViewById(R.id.amount);

        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                resultContainer.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        Button btn = findViewById(R.id.btnPay);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get customer details
                String customerId = "your+customer+id",
                        customerName = "James Emmanuel",
                        customerEmail = "kenneth.ngedo@gmail.com",
                        customerMobile = "08031149929",
                        reference = "your-unique-ref" + new Date().getTime();

                EditText etAmount = findViewById(R.id.amount);


                long amount;
                String amtString = etAmount.getText().toString();

                // initialize amount
                if (amtString.isEmpty()) amount = 2500 * 100;
                else amount = Integer.parseInt(amtString) * 100;

                // create payment info
                IswPaymentInfo iswPaymentInfo = new IswPaymentInfo(
                        customerId,
                        customerName,
                        customerEmail,
                        customerMobile,
                        reference,
                        amount);


                // trigger payment
                IswMobileSdk.getInstance().pay(iswPaymentInfo, MainActivity.this);
            }
        });
    }


    @Override
    public void onUserCancel() {
        String title = "You cancelled payment";
        showResult(title, null);

        toast("You cancelled payment, please try again.");
    }


    @Override
    public void onPaymentCompleted(@NonNull IswPaymentResult result) {
        String title = "Payment Result";
        showResult(title, result);


        if (result.isSuccessful())
            toast("your payment was successful, using: " + result.getChannel().name());
        else toast("unable to complete payment at the moment, try again.");
    }


    private void showResult(String title, IswPaymentResult result) {
        resultContainer.setVisibility(View.VISIBLE);

        // show result
        resultTitle.setText(title);
        boolean hasValue = result != null;
        int primaryColor = ContextCompat.getColor(this, R.color.colorPrimary);
        resultTitle.setTextColor(!hasValue ? Color.RED: primaryColor);

        int visibility = hasValue ? View.VISIBLE : View.INVISIBLE;
        paymentAmount.setVisibility(visibility);
        responseCode.setVisibility(visibility);
        responseDescription.setVisibility(visibility);
        isSuccessful.setVisibility(visibility);
        channel.setVisibility(visibility);

        if (!hasValue) return;

        paymentAmount.setText("" + (result.getAmount() / 100));
        responseCode.setText(result.getResponseCode());
        responseDescription.setText(result.getResponseDescription());
        isSuccessful.setText("" + result.isSuccessful());
        channel.setText(result.getChannel().name());
    }


    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
