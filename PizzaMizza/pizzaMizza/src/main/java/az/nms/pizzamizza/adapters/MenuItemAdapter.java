package az.nms.pizzamizza.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import az.nms.pizzamizza.R;
import az.nms.pizzamizza.models.MenuItems;

public class MenuItemAdapter extends BaseAdapter {

	private Context context;
	private int layoutResourceId;
	
	private ItemsHolder holder;
	private MenuItems items[];
	
	public MenuItemAdapter(Context context, int layoutResourceId,MenuItems[] items) {
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.items = items;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return items[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub

		return 0;
	}

	@Override
	public View getView(int position, View row, ViewGroup parent) {
		// TODO Auto-generated method stub

	 holder = null;
		if (row == null) {
			holder = new ItemsHolder();
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder.icon  = (ImageView) row.findViewById(R.id.menu_icon);
			holder.title = (TextView) row.findViewById(R.id.menu_title);
			row.setTag(holder);
		} else {
			holder = (ItemsHolder) row.getTag();
		}
		
		holder.icon.setImageResource(items[position].icon);
		holder.title.setText(items[position].title);
		
		return row;
	}

	static class ItemsHolder {
		ImageView icon;
		TextView title;
	}

}
