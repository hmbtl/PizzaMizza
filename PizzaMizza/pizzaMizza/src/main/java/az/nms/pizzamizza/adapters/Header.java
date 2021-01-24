package az.nms.pizzamizza.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import az.nms.pizzamizza.R;
import az.nms.pizzamizza.adapters.DetailListAdapter.RowType;

public class Header implements Item {
	private final int name;

	public Header(int name) {
		this.name = name;
	}

	@Override
	public int getViewType() {
		return RowType.HEADER_ITEM.ordinal();
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		View view;
		if (convertView == null) {
			view = (View) inflater.inflate(R.layout.detail_topping_header_item,
					null);
			// Do some initialization
		} else {
			view = convertView;
		}

		TextView text = (TextView) view.findViewById(R.id.detail_header);
		text.setText(name);

		return view;
	}

}