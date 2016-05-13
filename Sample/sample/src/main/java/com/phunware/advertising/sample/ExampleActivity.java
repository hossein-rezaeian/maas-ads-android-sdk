package com.phunware.advertising.sample;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.phunware.advertising.PwAdRequest;
import com.phunware.advertising.PwAdvertisingModule;
import com.phunware.advertising.PwBannerAdView;
import com.phunware.advertising.PwInterstitialAd;
import com.phunware.advertising.PwVideoInterstitialAd;
import com.phunware.core.PwCoreSession;
import com.phunware.core.PwLog;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ExampleActivity extends AppCompatActivity {
    private final static String TAG = "AdvertisingSample";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // enable debug logs during development
        PwLog.setShowLog(true);

        Resources r = getResources();

        // Initialize MaaS Core
        PwCoreSession.getInstance().registerKeys(this,
                r.getString(R.string.app_appid),
                r.getString(R.string.app_accesskey),
                r.getString(R.string.app_signaturekey),
                r.getString(R.string.app_encryptionkey));

        // Register MaaS Advertising module
        PwCoreSession.getInstance().installModules(PwAdvertisingModule.getInstance());

        // test that you've integrated properly
        // NOTE: remove this before your app goes live!
        PwAdvertisingModule.get().validateSetup(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*
         * Start Core Session
         */
        PwCoreSession.getInstance().activityStartSession(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        /*
         * Stop Core Session
         */
        PwCoreSession.getInstance().activityStopSession(this);
    }


    public void simpleInterstitialExample() {
        String zoneId = getResources().getString(R.string.intrs_zone_id);
        PwInterstitialAd interstitialAd = PwAdvertisingModule.get().getInterstitialAdForZone(this, zoneId);
        interstitialAd.show();
    }


    public void advancedInterstitialExample() {
        // generate a customized request
        String zoneId = getResources().getString(R.string.intrs_zone_id);

        PwAdRequest request = PwAdvertisingModule.get().getAdRequestBuilder(zoneId)
                // enable during the development phase
//                .setTestMode(true)
                .getPwAdRequest();

        // get an ad instance using request
        PwInterstitialAd interstitialAd = PwAdvertisingModule.get().getInterstitialAd(this, request);

        // register for ad lifecycle callbacks
        interstitialAd.setListener(new PwInterstitialAd.PwInterstitialAdListener() {
            @Override
            public void interstitialDidLoad(PwInterstitialAd ad) {
                // show ad as soon as it's loaded
                Log.d(TAG, "Interstitial Did Load");
                ad.show();
            }

            @Override
            public void interstitialDidClose(PwInterstitialAd ad) {
                Log.d(TAG, "Interstitial Did Close");
            }

            @Override
            public void interstitialDidFail(PwInterstitialAd ad, String error) {
                Log.d(TAG, "Interstitial Did Fail: " + error);
            }

            @Override
            public void interstitialActionWillLeaveApplication(PwInterstitialAd ad) {
                Log.d(TAG, "Interstitial Will Leave App");
            }
        });

        // load ad... we'll be notified when it's ready
        interstitialAd.load();
    }


    public void simpleVideoExample() {
        String zoneId = getResources().getString(R.string.video_zone_id);
        PwVideoInterstitialAd videoAd = PwAdvertisingModule.get().getVideoInterstitialAdForZone(this, zoneId);
        videoAd.show();
    }


    public void advancedVideoExample() {
        // generate a customized request
        String zoneId = getResources().getString(R.string.video_zone_id);
        Map<String, String> customParams = new HashMap<String, String>();
        customParams.put("cid", "123456");

        PwAdRequest request = PwAdvertisingModule.get().getAdRequestBuilder(zoneId)
                // enable during the development phase
                .setTestMode(true)
                .setKeywords(Arrays.asList("keyword1", "keyword2"))
                .setLocationTrackingEnabled(true)
                .getPwAdRequest();


        // get an ad instance using request
        PwVideoInterstitialAd videoAd = PwAdvertisingModule.get().getVideoInterstitialAd(this, request);

        // register for ad lifecycle callbacks
        videoAd.setListener(new PwVideoInterstitialAd.PwVideoInterstitialAdListener() {
            @Override
            public void videoInterstitialDidLoad(PwVideoInterstitialAd ad) {
                // show ad as soon as it's loaded
                Log.d(TAG, "VideoAd Did Load");
                ad.show();
            }

            @Override
            public void videoInterstitialDidClose(PwVideoInterstitialAd ad) {
                Log.d(TAG, "videoInterstitialDidClose");
            }

            @Override
            public void videoInterstitialDidFail(PwVideoInterstitialAd ad, String error) {
                Log.d(TAG, "videoInterstitialDidFail: " + error);
            }

            @Override
            public void videoInterstitialActionWillLeaveApplication(PwVideoInterstitialAd ad) {
                Log.d(TAG, "videoInterstitialActionWillLeaveApplication");
            }
        });

        // load ad... we'll be notified when it's ready
        videoAd.load();
    }


    public void simpleBannerExample() {
        PwBannerAdView bannerAdView = (PwBannerAdView)findViewById(R.id.bannerAdView);
        String zoneId = getResources().getString(R.string.banner_zone_id);
        bannerAdView.startRequestingAdsForZone(zoneId);
    }

    public void advancedBannerExample() {
        Log.d(TAG, "advancedBannerExample");
        // find the view in your layout
        PwBannerAdView bannerAdView = (PwBannerAdView)findViewById(R.id.bannerAdView);

        // Banner rotation interval; defaults to 60 seconds.
//        bannerAdView.setAdUpdateInterval(0); // no auto rotation
        bannerAdView.setAdUpdateInterval(30); // rotate every 30 seconds.

        // generate a customized request
        String zoneId = getResources().getString(R.string.banner_zone_id);

        Map<String, String> customParams = new HashMap<String, String>();
        customParams.put("custom key", "custom value");

        PwAdRequest request = PwAdvertisingModule.get().getAdRequestBuilder(zoneId)
                // enable during the development phase
                .setTestMode(true)
                // enable automatic gps based location tracking
                .setLocationTrackingEnabled(true)
                // optional keywords for custom targeting
                // .setKeywords(Arrays.asList("keyword1", "keyword2"))
                .setCustomParameters(customParams)
                .getPwAdRequest();

        // register for ad lifecycle callbacks
        bannerAdView.setListener(new PwBannerAdView.BannerAdListener() {
            @Override
            public void onReceiveBannerAd(PwBannerAdView ad) {
                Log.d(TAG, "Banner onReceiveBannerAd");
            }

            @Override
            public void onBannerAdError(PwBannerAdView ad, String errorMsg) {
                Log.d(TAG, "Banner onBannerAdError: " + errorMsg);
            }

            @Override
            public void onBannerAdFullscreen(PwBannerAdView ad) {
                Log.d(TAG, "Banner onBannerAdFullscreen");
            }

            @Override
            public void onBannerAdDismissFullscreen(PwBannerAdView ad) {
                Log.d(TAG, "Banner onBannerAdDismissFullscreen");
            }

            @Override
            public void onBannerAdLeaveApplication(PwBannerAdView ad) {
                Log.d(TAG, "Banner onBannerAdLeaveApplication");
            }
        });

//        // Optionally set location manually.
//        double lat = 40.7787895;
//        double lng = -73.9660945;
//        bannerAdView.updateLocation(lat, lng);

        // start banner rotating
        bannerAdView.startRequestingAds(request);
    }


    public void fireInterstitial(View sender) {
        simpleInterstitialExample();
        //advancedInterstitialExample();
    }

    public void fireVideoInterstitial(View sender) {
        //simpleVideoExample();
        advancedVideoExample();
    }

    public void fireBanner(View sender) {
        simpleBannerExample();
        //advancedBannerExample();
    }

    public void fireNativeAd(View sender) {
        Intent intent = new Intent(this, NativeAdActivity.class);
        startActivity(intent);
    }
}