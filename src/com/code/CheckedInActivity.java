package com.code;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.format.Time;
import android.widget.ListView;
import android.widget.TextView;

public class CheckedInActivity extends Activity {
	
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 5; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 10000; // in Milliseconds
    
    private static final String SERVER =       "http://ec2-23-20-61-43.compute-1.amazonaws.com:3000/check_in";
	
	protected TextView status;
	protected LocationManager locationManager;
	protected LocationListener locationListener2;
	
	protected ListView lv;
	
	private Time now;
	
	protected StopEntryAdapter stopEntryAdapter;
	
	BusEntry bus;
	StopEntry nextStop;
	StopEntry lastStop;
	String busStopId;

	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.stops_checked_in);
	    
	    stopEntryAdapter = new StopEntryAdapter(this, R.layout.stops_list_item);
	    
	    bus=(BusEntry) getIntent().getSerializableExtra("bus");
	    busStopId=getIntent().getStringExtra("busStopid").toString();
	    setTitle("You are in "+bus.getName());
	    
	    status = (TextView) findViewById(R.id.stopsStatus);
	    
	    now = new Time();
	    
	    // ListView
        lv = (ListView) findViewById(R.id.stopslist);
        lv.setAdapter(stopEntryAdapter);
	    
	    
	    MyState myState=(MyState)getApplicationContext();
	    locationManager = myState.getLocationManager();
	    
	    
//	    Location pos = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), true));
	    HttpClient hc = new DefaultHttpClient();
    	HttpGet request;
    	HttpResponse response;
    	InputStream content;
    	int responseCode;
    	List<StopEntry> busList;
    	try
    	{
      		request = new HttpGet(SERVER + "?bus_id="+bus.getId()+"&bus_stop_id="+busStopId);
      		request.setHeader("Accept", "application/json");
      		response = hc.execute(request);
      		content = response.getEntity().getContent();
      		responseCode = response.getStatusLine().getStatusCode();
      		
      		if(responseCode == HttpStatus.SC_OK)
      		{
      			busList = parseJSON(content);
      			nextStop=busList.get(0);
      			updateList(busList);
      		}
      		else
      			status.setText("Code " + responseCode);
    	}
    	catch (Exception e)
    	{
    		status.setText("Problem Connecting. " + e.getMessage());
    	}
	    
	    
	    
	    locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 
                MINIMUM_TIME_BETWEEN_UPDATES, 
                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                locationListener2 = new MyLocationListener2()
        );
        
//	    Toast.makeText(getApplicationContext(), "Checked in " +bus.getId()+ " " + bus.getName()+ " " + bus.getLabel(), Toast.LENGTH_SHORT).show();
	    
	    
	    
	}
	
	@Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to check out?")
               .setCancelable(false)
               .setPositiveButton("Yes", new DialogInterface.OnClickListener()
               {
                   public void onClick(DialogInterface dialog, int id)
                   {
                	   
                	   	HttpClient hc = new DefaultHttpClient();
	   	    	    	HttpGet request;
	   	    	    	HttpResponse response;

	   	    	    	try
	   	    	    	{
	   	    	      		request = new HttpGet(SERVER + "ping?bus_id="+bus.getId()+"&bus_stop_id="+lastStop.getId());
	   	    	      		request.setHeader("Accept", "application/json");
	   	    	      		response = hc.execute(request);
	   	    	    	}
	   	    	    	catch (Exception e)
	   	    	    	{
	   	    	    		status.setText("Problem Connecting. " + e.getMessage());
	   	    	    	}
	                	   
	                	   
	                	   
	                	   
                	   
                	   locationManager.removeUpdates(locationListener2);
                	   finish();
                   }
               })
               .setNegativeButton("No", new DialogInterface.OnClickListener()
               {
                   public void onClick(DialogInterface dialog, int id)
                   {
                        dialog.cancel();
                   }
               });
        
        AlertDialog alert = builder.create();
        alert.show();
    }
	
	private class MyLocationListener2 implements LocationListener {
	    	
	    	public void onLocationChanged(Location location)
	    	{
//	    		Toast.makeText(getApplicationContext(), location.getLatitude()+" lat="+ location.getLongitude(), Toast.LENGTH_SHORT).show();
	    		//CHECK IF ARRIVED AT nextBUS
	    		now.setToNow();
	    		double distance=Math.sqrt(Math.pow(location.getLatitude()-Double.parseDouble(nextStop.getLat()), 2)+Math.pow(location.getLongitude()-Double.parseDouble(nextStop.getLon()), 2));
	    		if(distance>0.0002)
	    			status.setText("Last update " + now.hour + ":" + now.minute + ":" + now.second+"\n "+distance);
	    		else{
	    			
	    			status.setText("I'm very close biatx");
	    			
	    			
	    			
	    			
	    			HttpClient hc = new DefaultHttpClient();
	    	    	HttpGet request;
	    	    	HttpResponse response;
	    	    	InputStream content;
	    	    	int responseCode;
	    	    	List<StopEntry> busList;
	    	    	try
	    	    	{
	    	      		request = new HttpGet(SERVER + "ping?bus_id="+bus.getId()+"&bus_stop_id="+nextStop.getId());
	    	      		request.setHeader("Accept", "application/json");
	    	      		response = hc.execute(request);
	    	      		content = response.getEntity().getContent();
	    	      		responseCode = response.getStatusLine().getStatusCode();
	    	      		
	    	      		if(responseCode == HttpStatus.SC_OK)
	    	      		{
	    	      			busList = parseJSON(content);
	    	      			lastStop=nextStop;
	    	      			nextStop=busList.get(0);
	    	      			updateList(busList);
	    	      		}
	    	      		else
	    	      			status.setText("Code " + responseCode);
	    	    	}
	    	    	catch (Exception e)
	    	    	{
	    	    		status.setText("Problem Connecting. " + e.getMessage());
	    	    	}
	    		}
	      	}
	
			public void onProviderDisabled(String provider) {
				status.setText("Provider disabled by the user. GPS turned off");
			}
	
			public void onProviderEnabled(String provider) {
				status.setText("Provider \"" + provider + "\"");
			}
	
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
			}
	
    }
	
	private List<StopEntry> parseJSON(InputStream content)
    {	
    	String json = "";
    	
    	List<StopEntry> entries = new ArrayList<StopEntry>();
    	
    	try {
    		// Content to string
            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(content, "UTF-8"), 8);
			
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "n");
            }
            content.close();
            json = sb.toString();
            
            
            //Parse JSON
            JSONArray jArray=new JSONArray(json);
            if(jArray.length()==0)
//				status.setText("No bus stops around!");
            	setTitle("No bus stops around!");
            	
			else{
				JSONObject jObject=(JSONObject) jArray.get(0);
				String busName=jObject.get("name").toString();
				String busId=jObject.get("id").toString();
				JSONArray jBuses=jObject.getJSONArray("bus_stops");

				for(int j=0;j<jBuses.length();j++){
					JSONObject jBus=(JSONObject)jBuses.get(j);
					String id=jBus.get("id").toString();
					String name=jBus.get("name").toString();
					String lat=jBus.get("lat").toString();
					String lon=jBus.get("lon").toString();
					String predicted_time=jBus.get("predicted_time").toString();
					entries.add(new StopEntry(id,name,lon,lat,predicted_time));
				}
			}
            
    	}
    	catch (JSONException e)
        {
    		status.setText(json+"\n "+e.getMessage());
        }
    	catch (Exception e) 
    	{
    		status.setText(e.getMessage());
		}
    	
        return entries;
    }
	
	private void updateList(List<StopEntry> list)
    {
    	// Display last update time
		now.setToNow();
		status.setText("Last update " + now.hour + ":" + now.minute + ":" + now.second);
		
        // Populate the list, through the adapter
    	stopEntryAdapter.clear();
//    	Toast.makeText(getApplicationContext(), "Checked in " + busEntryAdapter.getCount(), Toast.LENGTH_SHORT).show();
        
    	for(StopEntry entry : list) {
        	stopEntryAdapter.add(entry);
        }
//    	Toast.makeText(getApplicationContext(), "Size " + stopEntryAdapter.getCount(), Toast.LENGTH_SHORT).show();
    	/*
    	 * Instead of before
    	 * 
    	for(final BusEntry entry : list) {
        	busEntryAdapter.add(entry);
        }
    	 */
        
    }

}
