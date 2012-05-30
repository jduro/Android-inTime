package com.code;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Adapts NewsEntry objects onto views for lists
 */
public final class StopEntryAdapter extends ArrayAdapter<StopEntry> {

	private final int newsItemLayoutResource;

	public StopEntryAdapter(final Context context, final int newsItemLayoutResource) {
		super(context, 0);
		this.newsItemLayoutResource = newsItemLayoutResource;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent)
	{
		
		// We need to get the best view (re-used if possible) and then
		// retrieve its corresponding ViewHolder, which optimizes lookup efficiency
		final View view = getWorkingView(convertView);
		final ViewHolder viewHolder = getViewHolder(view);
		final StopEntry entry = getItem(position);
		
		// Setting the views is straightforward
		viewHolder.stopIdView.setText(entry.getId());
		viewHolder.nameView.setText(entry.getName());
		viewHolder.timeLeftView.setText(entry.getArrivalTime()==null?"No predictions":entry.getArrivalTime()+" min");
		
		return view;
	}

	private View getWorkingView(final View convertView)
	{
		// The workingView is basically just the convertView re-used if possible
		// or inflated new if not possible
		View workingView = null;
		
		if(null == convertView) {
			final Context context = getContext();
			final LayoutInflater inflater = (LayoutInflater)context.getSystemService
		      (Context.LAYOUT_INFLATER_SERVICE);
			
			workingView = inflater.inflate(newsItemLayoutResource, null);
		} else {
			workingView = convertView;
		}
		
		return workingView;
	}
	
	private ViewHolder getViewHolder(final View workingView) {
		// The viewHolder allows us to avoid re-looking up view references
		// Since views are recycled, these references will never change
		final Object tag = workingView.getTag();
		ViewHolder viewHolder = null;
		
		
		if(null == tag || !(tag instanceof ViewHolder)) {
			viewHolder = new ViewHolder();
			
			viewHolder.nameView = (TextView) workingView.findViewById(R.id.bus_stop_name);
			viewHolder.timeLeftView = (TextView) workingView.findViewById(R.id.bus_stop_time_left);
			viewHolder.stopIdView = (TextView) workingView.findViewById(R.id.bus_stop_id);
			
			workingView.setTag(viewHolder);
			
		} else {
			viewHolder = (ViewHolder) tag;
		}
		
		return viewHolder;
	}
	
	/**
	 * ViewHolder allows us to avoid re-looking up view references
	 * Since views are recycled, these references will never change
	 */
	private static class ViewHolder {
		public TextView nameView;
		public TextView timeLeftView;
		public TextView stopIdView;
	}
	
	
}