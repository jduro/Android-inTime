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
public final class BusEntryAdapter extends ArrayAdapter<BusEntry> {

	private final int newsItemLayoutResource;

	public BusEntryAdapter(final Context context, final int newsItemLayoutResource) {
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
		final BusEntry entry = getItem(position);
		
		// Setting the views is straightforward
		viewHolder.busIdView.setText(entry.getId());
		viewHolder.busNameView.setText(entry.getName());
		viewHolder.destinyView.setText(entry.getLabel());
		viewHolder.timeleftView.setText(entry.getTimeleft()==null?"No prediction":entry.getTimeleft()+" min");
		
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
			
			viewHolder.destinyView = (TextView) workingView.findViewById(R.id.news_entry_title);
			viewHolder.timeleftView = (TextView) workingView.findViewById(R.id.news_entry_subtitle);
			viewHolder.busIdView = (TextView) workingView.findViewById(R.id.bus_entry_id);
			viewHolder.busNameView = (TextView) workingView.findViewById(R.id.bus_entry_name);
			
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
		public TextView destinyView;
		public TextView timeleftView;
		public TextView busIdView;
		public TextView busNameView;
	}
	
	
}