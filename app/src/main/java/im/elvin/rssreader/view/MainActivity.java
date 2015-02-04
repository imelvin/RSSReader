package im.elvin.rssreader.view;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.text.Html;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import im.elvin.rssreader.R;
import im.elvin.rssreader.dao.RSSFeedDao;
import im.elvin.rssreader.dao.RSSFeedDaoImpl;
import im.elvin.rssreader.helper.RSSHelper;
import im.elvin.rssreader.model.RSSFeed;
import im.elvin.rssreader.model.RSSItem;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, PullToRefreshBase.OnRefreshListener2<ListView> {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private PullToRefreshListView itemListView;

    private RSSFeed feed;
    private LinkedList<Map<String, Object>> itemList;
    private SimpleAdapter itemListAdapter;
    private RSSFeedDao feedDao;
    private String topRowId = "0";
    private String bottomRowId = "0";

    private int pageLimit = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_item_list);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        feedDao = new RSSFeedDaoImpl(this.getApplicationContext());

        itemListView = (PullToRefreshListView) findViewById(R.id.item_list);
        itemListView.setOnRefreshListener(this);
        itemListView.setMode(PullToRefreshBase.Mode.BOTH);

        setPullLabels();

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = (Map<String, Object>) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), RSSItemDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("rss_item_id", map.get("rss_item_id").toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        this.registerForContextMenu(itemListView.getRefreshableView());
    }

    private void setPullLabels() {
        ILoadingLayout startLabels = itemListView.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel(getString(R.string.pull_down_to_refresh));
        startLabels.setRefreshingLabel(getString(R.string.refreshing));
        startLabels.setReleaseLabel(getString(R.string.release_to_load));

        ILoadingLayout endLabels = itemListView.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel(getString(R.string.pull_up_to_load_more));
        endLabels.setRefreshingLabel(getString(R.string.refreshing));
        endLabels.setReleaseLabel(getString(R.string.release_to_load));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        GetMoreTask getMoreTask = new GetMoreTask();
        getMoreTask.execute();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        LoadDataTask loadDataTask = new LoadDataTask();
        loadDataTask.execute();
    }

    public void loadFeed(String feedId) {
        List<RSSItem> itemList = feedDao.getItemListByFeedId(feedId, 0, pageLimit);
        if (itemList != null && itemList.size() > 0) {
            topRowId = itemList.get(0).getItemId();
            bottomRowId = itemList.get(itemList.size() - 1).getItemId();
            this.loadItemList(itemList);

        }
    }

    public void loadItemList(List<RSSItem> items) {
        String [] mFrom = new String[] {"rss_item_title", "rss_item_description"};
        int [] mTo = new int[] {R.id.rss_item_title, R.id.rss_item_description};

        itemList = convertMapList(items);

        itemListAdapter = new SimpleAdapter(this.getApplicationContext(), itemList, R.layout.rssitem_list_item, mFrom, mTo);
        itemListView.setAdapter(itemListAdapter);
    }

    private LinkedList<Map<String, Object>> convertMapList(List<RSSItem> items) {
        LinkedList<Map<String, Object>> itemList = new LinkedList<Map<String, Object>>();
        Map<String,Object> mMap = null;
        for (RSSItem item : items) {
            mMap = new HashMap<String,Object>();
            mMap.put("rss_item_id", item.getItemId());
            mMap.put("rss_item_title", Html.fromHtml(item.getTitle()));
            mMap.put("rss_item_description", Html.fromHtml(item.getDescription()));
            itemList.add(mMap);
        }
        return itemList;
    }

    private class LoadDataTask extends AsyncTask<Void, Void, List<RSSItem>> {
        @Override
        protected List<RSSItem> doInBackground(Void... params) {
            List<RSSItem> newItems = null;
            try {
                RSSFeed newFeed = RSSHelper.parseFeed(feed.getAddress());
                feedDao.addItems(feed.getFeedId(), newFeed.getItemList());
                newItems = feedDao.getNewItemListByFeedId(feed.getFeedId(), String.valueOf(topRowId));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return newItems;
        }

        @Override
        protected void onPostExecute(List<RSSItem> rssItems) {
            if (rssItems != null && rssItems.size() > 0) {
                LinkedList<Map<String, Object>> items = convertMapList(rssItems);
                itemList.addAll(0, items);
                topRowId = rssItems.get(0).getItemId();
                itemListAdapter.notifyDataSetChanged();
            }
            itemListView.onRefreshComplete();
            super.onPostExecute(rssItems);
        }
    }

    private class GetMoreTask extends AsyncTask<Void, Void, List<RSSItem>> {
        @Override
        protected List<RSSItem> doInBackground(Void... params) {
            List<RSSItem> items = feedDao.getOldItemListByFeedId(feed.getFeedId(), bottomRowId, pageLimit);
            return items;
        }

        @Override
        protected void onPostExecute(List<RSSItem> rssItems) {
            if (rssItems != null && rssItems.size() > 0) {
                LinkedList<Map<String, Object>> items = convertMapList(rssItems);
                itemList.addAll(itemList.size(), items);
                bottomRowId = rssItems.get(rssItems.size() - 1).getItemId();
                itemListAdapter.notifyDataSetChanged();
            }
            itemListView.onRefreshComplete();
            super.onPostExecute(rssItems);
        }
    }

    @Override
    public void onFeedSelected(int position, RSSFeed feed) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();

        String feedId = feed.getFeedId();
        this.feed = feed;
        this.loadFeed(feedId);
//        itemListView.setRefreshing();
    }

    public void onSectionAttached(int number) {
        
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("操作");
        menu.add(Menu.NONE, Menu.FIRST + 1, 1, "添加收藏");
        menu.add(Menu.NONE, Menu.FIRST + 2, 1, "删除条目");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        int position = info.position - 1;
        Map<String, Object> itemMap = itemList.get(position);
        String itemId = (String) itemMap.get("rss_item_id");

        switch (item.getItemId()) {
            // 收藏
            case Menu.FIRST + 1:
                feedDao.addFavorite(itemId);
                Toast.makeText(getApplicationContext(), "已收藏", Toast.LENGTH_SHORT).show();
                break;
            // 删除
            case Menu.FIRST + 2:
                itemList.remove(position);
                itemListAdapter.notifyDataSetChanged();
                feedDao.deleteItem(itemId);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
