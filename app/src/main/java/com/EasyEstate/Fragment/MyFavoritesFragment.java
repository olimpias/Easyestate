package com.EasyEstate.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.EasyEstate.Activity.ListingActivity;
import com.EasyEstate.Activity.MainActivity;
import com.EasyEstate.Adapter.EndlessScrollListener;
import com.EasyEstate.Adapter.ListingAdapter;
import com.EasyEstate.Database.DatabaseConnection;
import com.EasyEstate.Database.UserDoesNotLoginException;
import com.EasyEstate.Model.House;
import com.EasyEstate.Model.Listing;
import com.EasyEstate.R;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by canturker on 12/04/15.
 */
public class MyFavoritesFragment extends Fragment {
    private ListingAdapter adapter;
    private ListView listingListView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listinglistfragment,container,false);
        listingListView = (ListView)view.findViewById(R.id.ListingListView);
        adapter = new ListingAdapter(new ArrayList<Listing>(),getActivity());
        listingListView.setAdapter(adapter);
        listingListView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                new ConnectNetwork().execute(page);
            }
        });
        listingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(adapter.getItemViewType(position) == adapter.VIEW_TYPE_ACTIVITY){
                    Intent intent = new Intent(getActivity(),ListingActivity.class);
                    MainActivity.PAGE = MainActivity.FAVORITE_LISTING;
                    intent.putExtra(ListingActivity.AD_ID,adapter.getListingList().get(position).getAdID());
                    String type;
                    if (adapter.getListingList().get(position) instanceof House){
                        type ="0";
                    }else{
                        type = "1";
                    }
                    intent.putExtra(ListingActivity.AD_TYPE,type);
                    getActivity().startActivityForResult(intent,MainActivity.INSERT_LISTING);
                }
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    private class ConnectNetwork extends AsyncTask<Integer,Void,List<Listing>> {

        private int size;
        @Override
        protected List<Listing> doInBackground(Integer... params) {
            try {
                size = DatabaseConnection.getConnection().TotalFavoriteListingCount();
                return DatabaseConnection.getConnection().selectFavoriteListing(params[0]);
            } catch (UserDoesNotLoginException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Listing> listings) {
            super.onPostExecute(listings);
            if(listings != null){
                adapter.setServerListSize(size);
                for(int i = 0;i<listings.size();i++){
                    adapter.getListingList().add(listings.get(i));
                }
                adapter.notifyDataSetChanged();
            }
        }
    }
}
