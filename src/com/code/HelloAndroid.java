package com.code;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HelloAndroid extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.intime);
    }
    
    public void onClickAnonymous(View view) {
		Intent i = new Intent(this, ListBusActivity.class);
		startActivity(i);
	}
    
}