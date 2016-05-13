MaaS Advertising SDK for Android
================

Version 2.1.7

This is Phunware's Android SDK for the MaaS Advertising module. Visit http://maas.phunware.com/ for more details and to sign up.



Requirements
------------

- MaaS Core v1.3.2 or greater
- Google Play Services to enable Advertising ID support (recommended); installation instructions [here](https://developer.android.com/google/play-services/id.html)


Getting Started
---------------

- [Download MaaS Advertising](https://github.com/phunware/maas-ads-android-sdk/archive/master.zip) and run the included sample app.
- Continue reading below for installation and integration instructions.
- Be sure to read the [documentation](http://phunware.github.io/maas-ads-android-sdk/) for additional details.



Installation
------------

The following libraries are required:
````
PWCore-1.3.13.jar
````

MaaS Advertising depends on MaaSCore.jar, which is available here: https://github.com/phunware/maas-core-android-sdk

It's recommended that you add the MaaS libraries to the 'libs' directory. This directory should contain MaaSCore.jar
and MaaSMaaSAdvertising.jar, as well as any other MaaS libraries that you are using.

Update your `AndroidManifest.xml` to include the following permissions and activity:

````xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>

<!-- Optional permissions to enable ad geotargeting:
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
-->

<!-- Inside of the application tag: -->
<activity
    android:name="com.phunware.advertising.internal.PwAdActivity"
    android:configChanges="keyboard|keyboardHidden|orientation|screenSize" />

````
See [AndroidManifest.xml](Sample/sample/src/main/AndroidManifest.xml) for an example manifest file.



Documentation
------------

Documentation is included in HTML format in the `Docs` folder and [zipped](https://github.com/phunware/maas-ads-android-sdk/blob/master/MaaSAdvertising-javadoc.zip?raw=true) as well.



Integration
-----------

The primary methods in MaaS Advertising involve displaying the various ad types:


### Native Ad Usage

Native ads are advertisments designed to fit naturally into your app's look and feel. Predefined ad features
are provided as a JSON payload which your app consumes in a template that follows your UI's theme.

*Example Code Usage*
````java
import com.phunware.advertising.*;

// ...

String zoneId = "YOUR_NATIVE_AD_ZONE_ID";
PwNativeAd nativeAd = PwAdvertisingModule.get().getNativeAdForZone(context, zoneId);
nativeAd.setListener(new PwNativeAd.PwNativeAdListener() {
    @Override
    public void nativeAdDidLoad(PwNativeAd nativeAd) {
        try {
            renderUiFromNativeAd(nativeAd);

        } catch (JSONException e) {
            // log error and discard this native ad instance
        }
    }

    @Override
    public void nativeAdDidFail(PwNativeAd nativeAd, String errMsg) {
        // The ad failed to load and errMsg describes why.
        // Error messages are not intended for user display.
    }
});

nativeAd.load();


// ...


// ... when native ad data is displayed on screen:
nativeAd.trackImpression();


// ...


// ... when native ad is clicked:
nativeAd.click(context);
````

````java

private void renderUiFromNativeAd(PwNativeAd nativeAd) throws JSONException {
    JSONObject json = new JSONObject(nativeAd.getAdData());
    String adtitle = json.optString("adtitle");
    String imageurl = json.optString("iconurl");
    double stars = json.optDouble("rating");
    String html = json.optString("html");
    String adtext = json.optString("adtext");
    String cta = json.optString("cta");

    // Use the data to build a view item of your own design.
}
````

To request multiple ads at once:
````java
String zoneId = "YOUR_NATIVE_AD_ZONE_ID";
PwAdRequest request = PwAdvertisingModule.get().getAdRequestForZone(zoneId);

PwAdvertisingModule.get().getNativeAdLoader();
int numberOfAdsToLoad = 10;
PwAdLoader<PwNativeAd> adLoader = PwAdvertisingModule.get().getNativeAdLoader();

adLoader.multiLoad(context, request, numberOfAdsToLoad,
        new PwAdLoader.PwAdLoaderListener<PwNativeAd>() {
            @Override
            public void onSuccess(PwAdLoader adLoader, List<PwNativeAd> nativeAdsList) {
                for(PwNativeAd nativeAd : nativeAdsList) {
                    // Use the native ad to build a view item.
                    try {
                        renderUiFromNativeAd(nativeAd);
                    } catch (JSONException e) {
                        // Log the error and discard this native ad instance.
                    }
                }
            }

            @Override
            public void onFail(PwAdLoader adLoader, String errMsg) {
                // No ads are returned and the errMsg describes why.
                // Error messages are not intended for user display.
            }
        }
);
````

Code samples and advanced implementation can be found in the 
[native ad example code](Sample/sample/src/main/java/com/phunware/advertising/sample/NativeAdActivity.java)


### Banner Usage

Banners are inline ads that are shown alongside your app's interface.

**NOTE**: For XML usage only.
Add this to your layout xml:
````xml
<!-- Add a banner to your layout xml. -->
<!-- This will cause a 320x50 ad to be created, which will automatically kick off ad rotation. -->
<com.phunware.advertising.PwBannerAdView
    android:id="@+id/bannerAd"
    android:layout_width="320dp"
    android:layout_height="50dp"
    zone="YOUR_ZONE_ID" />
````

Add this to your layout xml: (note that "zone" is not specified)
````xml
<!-- Add a banner to your layout xml. -->
<!-- This will cause a 320x50 ad to be created, which will automatically kick off ad rotation. -->
<com.phunware.advertising.PwBannerAdView
    android:id="@+id/bannerAd"
    android:layout_width="320dp"
    android:layout_height="50dp" />
````

Add this to your activity:
````java
import com.phunware.advertising.*;

// ...

PwBannerAdView bannerAdView = (PwBannerAdView)findViewById(R.id.bannerAd);
bannerAdView.startRequestingAdsForZone("YOUR_BANNER_ZONE_ID");
````

Advanced implementation can be found in the [example code](Sample/sample/src/main/java/com/phunware/advertising/sample/ExampleActivity.java).


### Interstitial Usage

Interstitials are best used at discrete stopping points in your app's flow, such as at the end of a game level or when the player dies.

*Example Usage*
````java
import com.phunware.advertising.*;

// ...

PwInterstitialAd interstitialAd = PwAdvertisingModule.get().getInterstitialAdForZone(this, "YOUR_INTERSTITIAL_ZONE_ID");
interstitialAd.show();
````

Advanced implementation can be found in the [example code](Sample/sample/src/main/java/com/phunware/advertising/sample/ExampleActivity.java).


### Video Ads Usage

Video ads are interstitial ads that play a video. They are best used at discrete
stopping points in your app's flow, such as at the end of a game level or when the player dies.

*Example Usage*
````java
import com.phunware.advertising.*;

// ...

PwVideoInterstitialAd videoAd = PwAdvertisingModule.get().getVideoInterstitialAdForZone(this, "YOUR_VIDEO_ZONE_ID");
videoAd.show();
````
Advanced implementation can be found in the [example code](Sample/sample/src/main/java/com/phunware/advertising/sample/ExampleActivity.java).
