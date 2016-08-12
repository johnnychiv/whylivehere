package uk.ac.qub.fmcvicker01.mapstest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 *  Created by Fiachra McVicker & Johnathon Chivers.
 */

/**
 *  Public class Instructions_1 is extended from the MapsActivity.java class and implements View.OnClickListener which
 *  enables a callback to be invoked when a view is clicked.
 */
public class Instructions_1 extends Activity implements View.OnClickListener {

    Button button_next2;

    /**
     *  onCreate method used to initialise the activity and setContentView with activity_instructions_1 layout resource defining
     *  the UI.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions_1);

        // method to determine how the intent is initially brought to the screen. It references two animations which have been created to
        // slide the current activity out to the left and the new activity in from the right.
        overridePendingTransition(R.anim.animation_to_left, R.anim.animation2_to_left);

        // Using findViewById(int) to retrieve the button next2 widget in the UI that is needed to interact with programmatically.
        // Setting onClickListener so that when the button next2 is pressed, the code will be executed.
        button_next2 = (Button) findViewById(R.id.button_next2);
        button_next2.setOnClickListener(this);
    }

    /**
     *  Create intent functionality for the next2 and back button
     */
    @Override
    public void onClick(View v) {
        //Create switch statement to switch between the two buttons
        switch (v.getId()) {
            //When button next2 is pressed, the Instructions_2 class will appear
            case R.id.button_next2:
                this.finish();
                break;
        }
    }
}

