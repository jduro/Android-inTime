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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ListBusActivity extends Activity {

	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 5; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 10000; // in Milliseconds
    private static final String SERVER_OLD = "http://ec2-75-101-189-134.compute-1.amazonaws.com:3000/bus_stops_by_coordinates";
    private static final String SERVER = "http://ec2-23-20-61-43.compute-1.amazonaws.com:3000/bus_stops_by_coordinates";
    
    ListBusActivity self;
    
    protected LocationManager locationManager;
    protected MyLocationListener locationListener;
    
    protected ListView lv;
    protected BusEntryAdapter busEntryAdapter;
    
    protected Button startButton;
    protected Button stopButton;
    protected TextView status;
    protected ProgressBar pbar;
    
    String busStopId;
    
    TextView busId;
	TextView busName;
	TextView busLabel;
	View view;
    
    private Time now;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_list);
        
        this.self=this;
        
        // List Item Adapter
        busEntryAdapter = new BusEntryAdapter(this, R.layout.bus_list_item);
        
        // ListView
        lv = (ListView) findViewById(R.id.list);
        pbar = (ProgressBar) findViewById(R.id.progressBar1);
        lv.setAdapter(busEntryAdapter);
        lv.setClickable(true);
        lv.setOnItemClickListener(new OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
            	self.view=view;
            	busId = (TextView)view.findViewById(R.id.bus_entry_id);
            	busName = (TextView)view.findViewById(R.id.bus_entry_name);
            	busLabel = (TextView)view.findViewById(R.id.news_entry_title);
            	
            	AlertDialog.Builder builder = new AlertDialog.Builder(self);
                builder.setMessage("Are you sure you want to check in "+busName.getText().toString()+" ?")
                       .setCancelable(false)
                       .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                       {
                           public void onClick(DialogInterface dialog, int id)
                           {
//        	                   	registerForContextMenu(busId);
                               	self.view.showContextMenu();
//                           	Toast.makeText(getApplicationContext(), "Checked in " + busId.getText(), Toast.LENGTH_SHORT).show();
                               
                               BusEntry bus=new BusEntry(busId.getText().toString(), busName.getText().toString(), busLabel.getText().toString());
                               Intent i = new Intent(self, CheckedInActivity.class);
                               i.putExtra("bus",bus);
                               i.putExtra("busStopid", busStopId);
                               startActivity(i);
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
        });
        
        // Status
        now = new Time();
        status = (TextView) findViewById(R.id.listStatus);
        
        // Location Manager
        if(isNetworkAvailable())
        {
//	        status.setText("Locating current spot...");
        	setTitle("Locating current spot...");
	        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	        
	        MyState myState=(MyState)getApplicationContext();
	        myState.setLocationManager(locationManager);
	        
	    	locationManager.requestLocationUpdates(
	                LocationManager.GPS_PROVIDER, 
	                MINIMUM_TIME_BETWEEN_UPDATES, 
	                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
	                locationListener = new MyLocationListener()
	        );
        }
        else
        	status.setText("Network not available");
    }
    
    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to go back?")
               .setCancelable(false)
               .setPositiveButton("Yes", new DialogInterface.OnClickListener()
               {
                   public void onClick(DialogInterface dialog, int id)
                   {
	                   	status.setText("");
	                   	if(locationManager!=null)
	                   		locationManager.removeUpdates(locationListener);
	                   	ListBusActivity.this.finish();
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
    
    
    private void updateList(List<BusEntry> list)
    {
    	// Display last update time
		now.setToNow();
		status.setText("Last update " + now.hour + ":" + now.minute + ":" + now.second);
		
        // Populate the list, through the adapter
    	busEntryAdapter.clear();
//    	Toast.makeText(getApplicationContext(), "Checked in " + busEntryAdapter.getCount(), Toast.LENGTH_SHORT).show();
        
    	for(BusEntry entry : list) {
        	busEntryAdapter.add(entry);
        }
        
    	/*
    	 * Instead of before
    	 * 
    	for(final BusEntry entry : list) {
        	busEntryAdapter.add(entry);
        }
    	 */
        
    }
  
//    private List<BusEntry> getNewsEntries()
//    {
//    	List<BusEntry> entries = new ArrayList<BusEntry>();
//    	    	 	
//    	entries.add(new BusEntry("1", "24T", "Quinta da Nora", "4m20s", R.drawable.news_icon_1));
//    	entries.add(new BusEntry("2", "7", "Tovim", "4m20s", R.drawable.news_icon_1));
//    	entries.add(new BusEntry("3", "34", "Pólo II da Universidade", "4m20s", R.drawable.news_icon_2));
//    	entries.add(new BusEntry("4", "37", "Hospitais UC", "4m20s", R.drawable.news_icon_1));
//    	
//    	return entries;
//    }
    
    private boolean isNetworkAvailable()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
    
    
    private List<BusEntry> parseJSON(InputStream content)
    {	
    	String json = "";
    	
    	List<BusEntry> entries = new ArrayList<BusEntry>();
    	
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
				pbar.setVisibility(View.INVISIBLE);
				JSONObject jObject=(JSONObject) jArray.get(0);
				String name=jObject.get("name").toString();
				busStopId=jObject.get("id").toString();
				JSONArray jBuses=jObject.getJSONArray("buses");
				//name e id
//				status.setText(name);
				setTitle(name);
				for(int j=0;j<jBuses.length();j++){
					JSONObject jBus=(JSONObject)jBuses.get(j);
					String id=jBus.get("id").toString();
					String labels=jBus.get("name").toString();
					entries.add(new BusEntry(id,labels.substring(0,labels.indexOf(" - ")),labels.substring(labels.indexOf(" - ")+3)));
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
    
    
    
    
    private class MyLocationListener implements LocationListener {
    	
    	public void onLocationChanged(Location location)
    	{
    		HttpClient hc = new DefaultHttpClient();
        	HttpGet request;
        	HttpResponse response;
        	InputStream content;
        	int responseCode;
        	List<BusEntry> busList;
        	pbar.setVisibility(View.VISIBLE);
        	try
        	{
          		request = new HttpGet(SERVER + "?lat="+location.getLatitude()+"&lon="+location.getLongitude());
          		request.setHeader("Accept", "application/json");
          		response = hc.execute(request);
          		content = response.getEntity().getContent();
          		responseCode = response.getStatusLine().getStatusCode();
          		
          		if(responseCode == HttpStatus.SC_OK)
          		{
          			busList = parseJSON(content);
          			updateList(busList);
//          			status.setText(SERVER + "?lat="+location.getLatitude()+"&lon="+location.getLongitude());
          		}
          		else
          			status.setText("Code " + responseCode);
        	}
        	catch (Exception e)
        	{
        		status.setText("Problem Connecting. " + e.getMessage());
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

}