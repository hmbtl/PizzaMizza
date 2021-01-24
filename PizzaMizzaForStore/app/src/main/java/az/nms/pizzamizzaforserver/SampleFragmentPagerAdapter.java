package az.nms.pizzamizzaforserver;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by anar on 5/18/15.
 */
public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 7;

    private List<OrderListActivity.Section> sections;

    private String tabTitles[] = new String[] { "New", "Accepted", "Making","Ready","Delivering", "Delivered","Taken" };

    public SampleFragmentPagerAdapter(FragmentManager fm, List<OrderListActivity.Section> sections) {
        super(fm);
        this.sections = sections;
    }

    @Override
    public int getCount() {
        return sections.size();
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return sections.get(position).getTitle().getText().toString();
    }


}