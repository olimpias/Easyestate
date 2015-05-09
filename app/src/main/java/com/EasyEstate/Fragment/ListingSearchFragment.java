package com.EasyEstate.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.EasyEstate.Model.House;
import com.EasyEstate.Model.Listing;
import com.EasyEstate.R;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by canturker on 08/05/15.
 */
public class ListingSearchFragment extends Fragment {
    private ListingAdapter listingAdapter;
    private ListView listingListView;
    private String query = null;
    private String queryDetails = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listinglistfragment,container,false);
        listingListView = (ListView)view.findViewById(R.id.ListingListView);
        listingAdapter = new ListingAdapter(new ArrayList<Listing>(),getActivity());
        listingListView.setAdapter(listingAdapter);
        query = getArguments().getString(SearchFragment.QUERY);
        queryDetails = getArguments().getString(SearchFragment.QUERY_DETAILS);
        listingListView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                new NetworkConnection().execute(page);
            }
        });
        listingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(listingAdapter.getItemViewType(position) == listingAdapter.VIEW_TYPE_ACTIVITY){
                    Intent intent = new Intent(getActivity(),ListingActivity.class);
                    intent.putExtra(ListingActivity.AD_ID,listingAdapter.getListingList().get(position).getAdID());
                    String type;
                    if (listingAdapter.getListingList().get(position) instanceof House){
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
    private class NetworkConnection extends AsyncTask<Integer,Void,List<Listing>>{
        private int size;
        @Override
        protected List<Listing> doInBackground(Integer... params) {
            try {
                size = DatabaseConnection.getConnection().searchQuery(query,queryDetails);
                return DatabaseConnection.getConnection().selectSearchQuery(query,queryDetails,params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Listing> listings) {
            super.onPostExecute(listings);
            if(listings != null){
                listingAdapter.setServerListSize(size);
                for(int i = 0; i<listings.size() ; i++){
                    listingAdapter.getListingList().add(listings.get(i));
                }
                listingAdapter.notifyDataSetChanged();
            }
        }
    }
}
