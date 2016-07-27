package com.phunware.advertising.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.phunware.advertising.PwAdLoader;
import com.phunware.advertising.PwAdvertisingModule;
import com.phunware.advertising.PwBannerAdView;
import com.phunware.advertising.PwInterstitialAd;
import com.phunware.advertising.PwLandingPageAd;
import com.phunware.advertising.PwNativeAd;
import com.phunware.advertising.PwVideoInterstitialAd;
import com.phunware.advertising.sample.nativeads.NativeAds;

import java.util.Arrays;
import java.util.List;

public class ExampleActivity extends AppCompatActivity {
    private final static String TAG = "AdvertisingSample";

    private PwBannerAdView mBannerAdView;
    private ViewGroup mNativeAdHolder;
    private Spinner mSpNativeAds;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mBannerAdView = (PwBannerAdView)findViewById(R.id.bannerAdView);
        mNativeAdHolder = (ViewGroup) findViewById(R.id.native_ad_view_placeholder);
        mSpNativeAds = (Spinner)findViewById(R.id.native_ad_type_spinner);
        mSpNativeAds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1){
                    fireNativeNewsFeed();
                }else if(position == 2){
                    fireNativeContentWall();
                }else if(position == 3){
                    fireNativeContentStream();
                }else if(position == 4){
                    fireNativeAppWall();
                }else if(position == 5){
                    fireNativeIcons();
                }else if(position == 6){
                    fireNativeAdClean();
                }else if(position == 7){
                    fireNativeAd3Up(Gravity.START);
                }else if(position == 8){
                    fireNativeAd3Up(Gravity.CENTER_HORIZONTAL);
                }else if(position == 9){
                    fireNativeAd3Up(Gravity.END);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // test that you've integrated properly
        // NOTE: remove this before your app goes live!
        //PwAdvertisingModule.getInstance().validateSetup(this);
    }

    public void simpleInterstitialExample() {
        String zoneId = getString(R.string.intrs_zone_id);
        PwInterstitialAd interstitialAd = PwInterstitialAd.getInstance(this, zoneId);
        interstitialAd.show();
    }

    public void advancedInterstitialExample() {
        // generate a customized request
        String zoneId = getString(R.string.intrs_zone_id);

        PwInterstitialAd interstitialAd = PwInterstitialAd.getInstance(this, zoneId);
        interstitialAd.setTestMode(true);
        interstitialAd.setKeywords(Arrays.asList("keyword1", "keyword2"));

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
        String zoneId = getString(R.string.video_zone_id);
        PwVideoInterstitialAd videoAd = PwVideoInterstitialAd.getInstance(getApplicationContext(), zoneId);
        videoAd.show();
    }


    public void advancedVideoExample() {
        // generate a customized request
        String zoneId = getString(R.string.video_zone_id);

        /* custom parameters only supported in legacy API
        Map<String, String> customParams = new HashMap<String, String>();
        customParams.put("cid", "123456");

        PwAdRequest request = PwAdvertisingModule.get().getAdRequestBuilder(zoneId)
                // enable during the development phase
                .setTestMode(true)
                // enable automatic gps based location tracking
                .setLocationTrackingEnabled(true)
                // optional keywords for custom targeting
                .setKeywords(Arrays.asList("keyword1", "keyword2"))
                .setCustomParameters(customParams)
                .getPwAdRequest();

        // get an ad instance using request
        PwVideoInterstitialAd videoAd = PwAdvertisingModule.get().getVideoInterstitialAd(this, request);
        */

        // register for ad lifecycle callbacks
        PwVideoInterstitialAd videoAd = PwVideoInterstitialAd.getInstance(getApplicationContext(), zoneId);
        videoAd.setTestMode(true);
        videoAd.setPlacementType(PwVideoInterstitialAd.TYPE_ALL);
        videoAd.updateLocation(42.621535114613685, -5.595249100000046);
        videoAd.setKeywords(Arrays.asList("keyword1", "keyword2"));
        videoAd.setListener(new PwVideoInterstitialAd.PwVideoInterstitialAdListener() {
            @Override
            public void videoInterstitialDidLoad(PwVideoInterstitialAd videoInterstitialAd) {
                videoInterstitialAd.show();
            }

            @Override
            public void videoInterstitialDidClose(PwVideoInterstitialAd videoInterstitialAd) {

            }

            @Override
            public void videoInterstitialDidFail(PwVideoInterstitialAd videoInterstitialAd, String error) {

            }

            @Override
            public void videoInterstitialActionWillLeaveApplication(PwVideoInterstitialAd videoInterstitialAd) {

            }
        });

        videoAd.load();
    }

    public void simpleBannerExample() {
        String zoneId = getString(R.string.banner_zone_id);
        mBannerAdView.startRequestingAdsForZone(zoneId);
    }

    public void advancedBannerExample() {
        Log.d(TAG, "advancedBannerExample");

        // Banner rotation interval; defaults to 60 seconds.
        // mBannerAdView.setAdUpdateInterval(0); // no auto rotation
        mBannerAdView.setAdUpdateInterval(30); // rotate every 30 seconds.

        String zoneId = getString(R.string.banner_zone_id);

        // generate a customized request
        /* DEPRECATED custom parameters support only in the old API
        Map<String, String> customParams = new HashMap<String, String>();
        customParams.put("custom key", "custom value");

        PwAdRequest request = PwAdvertisingModule.get().getAdRequestBuilder(zoneId)
                // enable during the development phase
                .setTestMode(true)
                // enable automatic gps based location tracking
                .setLocationTrackingEnabled(true)
                // optional keywords for custom targeting
                .setKeywords(Arrays.asList("keyword1", "keyword2"))
                .setCustomParameters(customParams)
                .getPwAdRequest();
                */
        mBannerAdView.setZone(zoneId)
                .setTestMode(true)                                  // enable during the development phase
                .setKeywords(Arrays.asList("keyword1", "keyword2")); // optional keywords for custom targeting

        // register for ad lifecycle callbacks
        mBannerAdView.setListener(new PwBannerAdView.BannerAdListener() {
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

        // Optionally set location manually.
//        double lat = 40.7787895;
//        double lng = -73.9660945;
//        mBannerAdView.updateLocation(lat, lng);

        // start banner rotating
        mBannerAdView.startRequestingAds();
    }


    public void fireInterstitial(View sender) {
//        simpleInterstitialExample();
        advancedInterstitialExample();
    }

    public void fireVideoInterstitial(View sender) {
//        simpleVideoExample();
        advancedVideoExample();
    }

    public void fireBanner(View sender) {
        clearViews();
        mBannerAdView.setVisibility(View.VISIBLE);
//      simpleBannerExample();
        advancedBannerExample();
    }


    public void simpleLandingPageExample() {
        String zoneId = getString(R.string.landing_page_zone_id);
        PwLandingPageAd pageAd = PwLandingPageAd.getInstance(this, zoneId);
        pageAd.show();
    }

    public void advancedLandingPagelExample() {
        // generate a customized request
        String zoneId = getString(R.string.landing_page_zone_id);

        PwLandingPageAd pageAd = PwLandingPageAd.getInstance(this, zoneId);
        pageAd.setTestMode(true);
        pageAd.setKeywords(Arrays.asList("keyword1", "keyword2"));

        // register for ad lifecycle callbacks
        pageAd.setListener(new PwLandingPageAd.PwPageAdListener() {
            @Override
            public void pageDidLoad(PwLandingPageAd ad) {
                // show ad as soon as it's loaded
                Log.d(TAG, "Page Did Load");
                ad.show();
            }

            @Override
            public void pageDidClose(PwLandingPageAd ad) {
                Log.d(TAG, "Page Did Close");
            }

            @Override
            public void pageDidFail(PwLandingPageAd ad, String error) {
                Log.d(TAG, "Page Did Fail: " + error);
            }

            @Override
            public void pageActionWillLeaveApplication(PwLandingPageAd ad) {
                Log.d(TAG, "Page Will Leave App");
            }
        });

        // load ad... we'll be notified when it's ready
        pageAd.load();
    }

    public void fire3dBanner(View sender){
        Log.d(TAG, "3dBannerExample");

        mBannerAdView.setAdUpdateInterval(30); // rotate every 30 seconds.
        mBannerAdView.set3dAnimation(true); //This is importat to see the 3d animation. False is by default.

        String zoneId = getString(R.string.banner_zone_id);

        mBannerAdView.setZone(zoneId)
                .setTestMode(true)                                  // enable during the development phase
                .setKeywords(Arrays.asList("keyword1", "keyword2")); // optional keywords for custom targeting

        // register for ad lifecycle callbacks
        mBannerAdView.setListener(new PwBannerAdView.BannerAdListener() {
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

        // Optionally set location manually.
//        double lat = 40.7787895;
//        double lng = -73.9660945;
//        mBannerAdView.updateLocation(lat, lng);

        // start banner rotating
        mBannerAdView.startRequestingAds();
    }

    public void fireLandingPage(View sender) {
//        simpleLandingPageExample();
        advancedLandingPagelExample();
    }

    public void fireNativeNewsFeed() {
        clearViews();

        String zoneId = getString(R.string.native_zone_id);

        PwNativeAd nativeAd = PwNativeAd.getInstance(this, zoneId);
        nativeAd.setTestMode(true);
        nativeAd.setListener(new PwNativeAd.PwNativeAdListener() {
            @Override
            public void nativeAdDidLoad(PwNativeAd nativeAd) {
                View view = NativeAds.getNewsFeedView(ExampleActivity.this, nativeAd);
                mNativeAdHolder.addView(view);
                mNativeAdHolder.setVisibility(View.VISIBLE);
            }

            @Override
            public void nativeAdDidFail(PwNativeAd nativeAd, String errMsg) {
                // The ad failed to load and the errMsg describes why.
                // Error messages are not intended for user display.
                Log.e(TAG, errMsg);
            }
        });

        nativeAd.load();
    }

    public void fireNativeContentWall() {
        clearViews();

        String zoneId = getString(R.string.native_zone_id);

        PwNativeAd nativeAd = PwNativeAd.getInstance(this, zoneId);
        nativeAd.setTestMode(true);
        nativeAd.setListener(new PwNativeAd.PwNativeAdListener() {
            @Override
            public void nativeAdDidLoad(PwNativeAd nativeAd) {
                View view = NativeAds.getContentWallView(ExampleActivity.this, nativeAd);
                mNativeAdHolder.addView(view);
                mNativeAdHolder.setVisibility(View.VISIBLE);
            }

            @Override
            public void nativeAdDidFail(PwNativeAd nativeAd, String errMsg) {
                // The ad failed to load and the errMsg describes why.
                // Error messages are not intended for user display.
                Log.e(TAG, errMsg);
            }
        });

        nativeAd.load();
    }

    public void fireNativeContentStream() {
        Intent intent = new Intent(this, NativeAdActivity.class);
        intent.putExtra(NativeAdActivity.EXTRA_TYPE, NativeAdActivity.TYPE_CONTENT_STREAM);
        startActivity(intent);
    }

    public void fireNativeAppWall() {
        Intent intent = new Intent(this, NativeAdActivity.class);
        intent.putExtra(NativeAdActivity.EXTRA_TYPE, NativeAdActivity.TYPE_APP_WALL);
        startActivity(intent);
    }

    public void fireNativeIcons() {
        clearViews();

        String zoneId = getString(R.string.native_zone_id);

        PwAdLoader<PwNativeAd> adLoader = PwAdLoader.getNativeAdLoader();
        adLoader.setZone(zoneId);
        adLoader.setTestMode(true);

        adLoader.loadAds(this, 3, new PwAdLoader.PwAdLoaderListener<PwNativeAd>() {
            @Override
            public void onSuccess(PwAdLoader loader, List<PwNativeAd> ads) {
                View innerView = NativeAds.getIconsView(ExampleActivity.this, ads, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mNativeAdHolder.setVisibility(View.GONE);
                        mNativeAdHolder.removeAllViewsInLayout();
                    }
                });
                mNativeAdHolder.addView(innerView);
                mNativeAdHolder.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFail(PwAdLoader loader, String error) {
                Log.e(TAG, error);
            }
        });
    }

    public void fireNativeAdClean(){
        clearViews();

        String zoneId = getString(R.string.native_zone_id);

        PwNativeAd nativeAd = PwNativeAd.getInstance(this, zoneId);
        nativeAd.setTestMode(true);
        nativeAd.setListener(new PwNativeAd.PwNativeAdListener() {
            @Override
            public void nativeAdDidLoad(PwNativeAd nativeAd) {
                View view = NativeAds.getCleanView(ExampleActivity.this, nativeAd);
                mNativeAdHolder.addView(view);
                mNativeAdHolder.setVisibility(View.VISIBLE);
            }

            @Override
            public void nativeAdDidFail(PwNativeAd nativeAd, String errMsg) {
                // The ad failed to load and the errMsg describes why.
                // Error messages are not intended for user display.
                Log.e(TAG, errMsg);
            }
        });

        nativeAd.load();
    }

    public void fireNativeAd3Up(final int gravity){
        clearViews();
        String zoneId = getString(R.string.native_zone_id);

        PwAdLoader<PwNativeAd> adLoader = PwAdLoader.getNativeAdLoader();
        adLoader.setZone(zoneId);
        adLoader.setTestMode(true);

        int countAds;

        if(gravity == Gravity.CENTER_HORIZONTAL){
            countAds = 3;
        }else{
            countAds = 2;
        }

        adLoader.loadAds(this, countAds, new PwAdLoader.PwAdLoaderListener<PwNativeAd>() {
            @Override
            public void onSuccess(PwAdLoader loader, List<PwNativeAd> ads) {
                View innerView = NativeAds.get3UpView(ExampleActivity.this, ads, gravity);
                mNativeAdHolder.addView(innerView);
                mNativeAdHolder.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFail(PwAdLoader loader, String error) {
                Log.e(TAG, error);
            }
        });
    }

    private void clearViews(){
        if(mBannerAdView != null){
            mBannerAdView.stopRequestingAds();
            mBannerAdView.setVisibility(View.GONE);
        }
        if(mNativeAdHolder != null){
            mNativeAdHolder.removeAllViewsInLayout();
        }
    }
}