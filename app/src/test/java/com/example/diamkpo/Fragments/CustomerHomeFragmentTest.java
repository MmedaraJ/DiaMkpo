package com.example.diamkpo.Fragments;

import android.app.Application;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.diamkpo.Activities.CustomerActivity;
import com.example.diamkpo.BuildConfig;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
//@Config(constants = BuildConfig.class, sdk=21)
public class CustomerHomeFragmentTest {
    private CustomerActivity customerActivity;
    private CustomerHomeFragment customerHomeFragment;

    public CustomerHomeFragmentTest(){
        //super(Application.class);
    }

    @Before
    public void setUp() {
        customerActivity = Robolectric.setupActivity(CustomerActivity.class);
        customerHomeFragment = new CustomerHomeFragment();
        startFragment(customerHomeFragment);
    }

    @Test
    public void testMainActivity() {
        assertNotNull(customerActivity);
    }

    private void startFragment( Fragment fragment ) {
        FragmentManager fragmentManager = customerActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragment, null );
        fragmentTransaction.commit();
    }

    @After
    public void tearDown() {
    }
}