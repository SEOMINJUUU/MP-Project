/*
Author: 20181683 임중혁
Last Modification date: 19.11.28
Function: Floating List View that showing Pub Items
 */


package com.bungae1112.final_proj.mainActivity.listView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bungae1112.final_proj.R;
import com.bungae1112.final_proj.tools.GetJson;
import com.bungae1112.final_proj.tools.JsonDataSet;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ListFragment extends Fragment
{
    private static final String TAG = "List Fragment";
    View fragView;
    SwipeRefreshLayout swipeRefreshLayout;

    String category;

    ArrayList<ItemData> itemList;
    ListView listView;
    ItemAdapter itemAdapter;

    Retrofit retrofit;
    GetJson getJson;
    JsonDataSet jsonDataSet = null;

    public ListFragment(String category)
    {
        this.category = category;
    }

    @ Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState )
    {
        fragView = inflater.inflate(R.layout.list_fragment, container, false);

        swipeRefreshLayout = fragView.findViewById(R.id.list_swipeLayout_sp);
        setSpOnClick();

        listView = (ListView) fragView.findViewById(R.id.list_listView_lv);

        retrofit = RetrofitClient.getClient1("http://54.180.153.64:3000");
        getJson = retrofit.create(GetJson.class);

        itemList = new ArrayList<ItemData>();
        InitItems();

        itemAdapter = new ItemAdapter(fragView.getContext(), itemList);
        listView.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();

        setListViewOnClick();

        return fragView;
    }

    public void InitItems(){
        itemList.clear();


        getJson.getData().enqueue(new Callback<JsonDataSet>()
        {
            @Override
            public void onResponse(Call<JsonDataSet> call, Response<JsonDataSet> response)
            {
                if ( response.isSuccessful() )
                {
                    jsonDataSet = response.body();
                    Log.d("DataSet", jsonDataSet.getResult());

                    if ( jsonDataSet != null && jsonDataSet.getResult().equals("success") )
                    {
                        List<ItemData> itemData = jsonDataSet.getRawJsonArr();

                        if (  category.equals("total") )
                            itemList.addAll(itemData);
                        else
                        {
                            for (ItemData data : itemData)
                            {
                                if( data.getCategory().equals(category) )
                                    itemList.add(data);
                            }
                        }
                    }
                    else {
                        Log.d("InitItems", "Error");
                    }

                    itemAdapter.notifyDataSetChanged();

                    swipeRefreshLayout.setRefreshing(false);
                }
                else {
                    Log.d("data.response", "response Error");
                }
            }

            @Override
            public void onFailure(Call<JsonDataSet> call, Throwable t) {
                Log.d("data", "Connect Failed\t" + t.getMessage());
            }
        });
    }

    public void setSpOnClick()
    {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                InitItems();
            }
        });

    }

    public void setListViewOnClick()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), com.bungae1112.final_proj.itemView.itemView.class);

                ItemData targ_data = itemList.get(position);

                intent.putExtra( "name", targ_data.getName() );
                intent.putExtra( "address", targ_data.getAddr() );
                intent.putExtra( "telnum", targ_data.getTel() );
                intent.putExtra( "menu", targ_data.getMenu() );
                intent.putExtra( "remain", targ_data.getRemain() );
                intent.putExtra( "seat", targ_data.getSeat() );


                startActivity(intent);
            }
        });

    }

    public static class RetrofitClient {
        private static Retrofit retrofit = null;
        public static Retrofit getClient1(String baseUrl) {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }
    }

}
