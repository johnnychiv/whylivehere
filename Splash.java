package uk.ac.qub.fmcvicker01.mapstest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Aine McElhinney 19/03/2016.
 */
public class Splash extends Activity {
    /**
     *  final int to hold the Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 1500;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash);

        // New Handler to start the MapsActivity and close this Splash-Screen after some seconds.
        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          // Create an Intent that will start the MapsActivity.
                                          Intent mainIntent = new Intent(Splash.this, MapsActivity.class);
                                          Splash.this.startActivity(mainIntent);
                                          Splash.this.finish();
                                      }
                                  },
                //how long the splash to last
                SPLASH_DISPLAY_LENGTH);
    }
}
