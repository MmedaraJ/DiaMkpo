/*
package com.example.diamkpo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class CheckoutActivity extends AppCompatActivity {
    private static final String BACKEND_URL = "https://example.com/";
    private static final String STRIPE_PUBLISHABLE_KEY = "pk_test_51JDvDkCJpsXV1wvDldQP6hRo6Wf60pnDs1ksf73qlN13nz0mAft9AlUKuJgvFAXbj1emUGRGtpceHbSYG8zQbCwL00kgAXJMfA";

    private PaymentSheet paymentSheet;

    private String paymentIntentClientSecret;
    private String customerId;
    private String ephemeralKeySecret;

    private Button payButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // instantiate view and buyButton

        payButton.setEnabled(false);

        PaymentConfiguration.init(this, STRIPE_PUBLISHABLE_KEY);

        paymentSheet = new PaymentSheet(this, result -> {
            onPaymentSheetResult(result);
        });

        payButton.setOnClickListener(v -> presentPaymentSheet());

        fetchInitData();
    }

    private void fetchInitData() {
        final String requestJson = "{}";
        final RequestBody requestBody = RequestBody.create(
                requestJson,
                MediaType.get("application/json; charset=utf-8")
        );

        final Request request = new Request
                .Builder()
                .url(BACKEND_URL + "payment-sheet")
                .post(requestBody)
                .build();

        new OkHttpClient()
                .newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        // Handle failure
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            // Handle failure
                        } else {
                            final JSONObject responseJson = parseResponse(response.body());

                            paymentIntentClientSecret = responseJson.optString("paymentIntent");
                            customerId = responseJson.optString("customer");
                            ephemeralKeySecret = responseJson.optString("ephemeralKey");

                            runOnUiThread(() -> payButton.setEnabled(true));
                        }
                    }
                });
    }

    private JSONObject parseResponse(ResponseBody responseBody) {
        if (responseBody != null) {
            try {
                return new JSONObject(responseBody.string());
            } catch (IOException | JSONException e) {
                Log.e("App", "Error parsing response", e);
            }
        }
        return new JSONObject();
    }

    private void presentPaymentSheet() {
        paymentSheet.presentWithPaymentIntent(
                paymentIntentClientSecret,
                new PaymentSheet.Configuration(
                        "Example, Inc.",
                        new PaymentSheet.CustomerConfiguration(
                                customerId,
                                ephemeralKeySecret
                        )
                )
        );
    }

    private void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Toast.makeText(
                    this,
                    "Payment Canceled",
                    Toast.LENGTH_LONG
            ).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Toast.makeText(
                    this,
                    "Payment Failed. See logcat for details.",
                    Toast.LENGTH_LONG
            ).show();

            Log.e("App", "Got error: ", ((PaymentSheetResult.Failed) paymentSheetResult).getError());
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Toast.makeText(
                    this,
                    "Payment Complete",
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}
*/
