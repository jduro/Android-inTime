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
    
    public void onClickAuth(View view) {
		startActivity(new Intent(this, AuthenticateActivity.class));
	}
    
    public void onClickAnonymous(View view) {
		Intent i = new Intent(this, ListBusActivity.class);
		i.putExtra("Value1", "This value one for ActivityTwo ");
		i.putExtra("Value2", "This value two ActivityTwo");
		// Set the request code to any code you like, you can identify the
		// callback via this code
		startActivity(i);
	}
    
}