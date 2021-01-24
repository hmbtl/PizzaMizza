package az.nms.pizzamizza.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import az.nms.pizzamizza.models.DrawerItems;
import az.nms.pizzamizza.R;

public class DrawerAdapter extends ArrayAdapter<DrawerItems> {

	Context context;
	int layoutResourceId;
	DrawerItems data[] = null;
	private ItemsHolder holder = null;

	public DrawerAdapter(Context context, int layoutResourceId,
			DrawerItems[] data) {
		super(context, layoutResourceId, data);
		// TODO Auto-generated constructor stub
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View row = convertView;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new ItemsHolder();
			holder.img = (ImageView) row.findViewById(R.id.drawer_icon);
			holder.title = (TextView) row.findViewById(R.id.drawer_title);

			row.setTag(holder);
		} else {
			holder = (ItemsHolder) row.getTag();
		}
		DrawerItems item = data[position];
		holder.img.setImageResource(item.icon);
		holder.title.setText(item.title);

		return row;

	}

	static class ItemsHolder {
		ImageView img;
		TextView title;
	}

}
