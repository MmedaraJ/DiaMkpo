package com.example.diamkpo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diamkpo.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SearchDeliveryAddressActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "DELIVERY_ADDRESS_SEARCH";
    private String apiKey = "AIzaSyDv9fXs48ylYlgIKOhB-ht7okZj7mL0PPU";

    private FirebaseFirestore firebaseFirestore;

    private GoogleSignInAccount account;

    private TextView currentLocationAddress;

    private ImageView backIconDeliveryAddressActivity;

    private AutocompleteSupportFragment autocompleteFragment;

    private Button updateDeliveryLocationButton;

    private String currentUserid;
    private String addressName;
    private String deliveryProvince;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_delivery_address);

        firebaseFirestore = FirebaseFirestore.getInstance();
        account = GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null) currentUserid = account.getId();

        currentLocationAddress = findViewById(R.id.currentLocationAddress);
        updateDeliveryLocationButton = findViewById(R.id.updateDeliveryLocationButton);
        backIconDeliveryAddressActivity = findViewById(R.id.backIconDeliveryAddressActivity);

        firebaseFirestore
                .collection("Users")
                .document(currentUserid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            String deliveryAddress = documentSnapshot.getString("deliveryAddress");
                            currentLocationAddress.setText(deliveryAddress);
                        }
                    }
                });

        Places.initialize(getApplicationContext(), apiKey);

        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                addressName = place.getName();
                deliveryProvince = place.getAddress();
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        setOnCLickListeners();
    }

    public void setOnCLickListeners(){
        updateDeliveryLocationButton.setOnClickListener(this);
        backIconDeliveryAddressActivity.setOnClickListener(this);
    }

    private void updateDeliveryLocation(){
        if(addressName != null) {
            currentLocationAddress.setText(addressName);
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("deliveryAddress", addressName);
            userInfo.put("deliveryProvince", deliveryProvince);
            firebaseFirestore
                    .collection("Users")
                    .document(currentUserid)
                    .update(userInfo);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.updateDeliveryLocationButton:
                updateDeliveryLocation();
                break;
            case R.id.backIconDeliveryAddressActivity:
                //Navigation.findNavController(this, R.id.cus)
                break;
        }
    }
}