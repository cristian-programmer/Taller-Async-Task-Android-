package com.soimatecnolgy.myapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.soimatecnolgy.myapplication.adapter.MyArrayAdapter;
import com.soimatecnolgy.myapplication.model.MyDataModel;
import com.soimatecnolgy.myapplication.parser.JSONparser;
import com.soimatecnolgy.myapplication.util.InternetConnection;
import com.soimatecnolgy.myapplication.util.Keys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserList extends AppCompatActivity {
    private ListView listView;
    private ArrayList<MyDataModel> list;
    private MyArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);

        list = new ArrayList<>();
        adapter = new MyArrayAdapter(this, list);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(findViewById(R.id.parentLayout),
                        list.get(position).getNames() + " == "+ list.get(position).getCell(),
                        Snackbar.LENGTH_LONG)
                        .show();
                Toast toas = Toast.makeText(getApplicationContext(), "Click on FloatinActionButton JSON", Toast.LENGTH_LONG);
                toas.setGravity(Gravity.CENTER, 0, 0);
                toas.show();

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener( new View.OnClickListener(){
            @NonNull
            @Override
            public void onClick(@NonNull View view){
                Toast toas2 = Toast.makeText(getApplicationContext(), "Click on FloatinActionButton JSON", Toast.LENGTH_LONG);
                toas2.show();
                if(InternetConnection.checkConnection(getApplicationContext())){
                    new GetDataTask().execute();
                }else{
                    Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG)
                 .show();
                }

            }
        });
    }

    class GetDataTask extends AsyncTask<Void, Void, Void>{

        ProgressDialog dialog;
        int index;
        int x;

        protected void onPreExecute(){
            super.onPreExecute();
            x = list.size();
            if(x == 0){
                index = 0;
            }else{
                index = x;
            }
            dialog = new ProgressDialog(UserList.this);
            dialog.setTitle("Hey Wait Please" + x);
            dialog.setMessage(" I am gettin you JSON");
            dialog.show();

        }

        @NonNull
        @Override
        protected Void  doInBackground(Void... params){
            JSONObject jsonObject = JSONparser.getDataFromWeb();
            try {
                if(jsonObject != null ) {
                    if(jsonObject.length() > 0){
                        JSONArray array = jsonObject.getJSONArray(Keys.KEY_CONTACTS);
                        int lenArray = array.length();
                        if(lenArray > 0){
                            for(;index < lenArray; index++){
                                MyDataModel model = new MyDataModel();
                                JSONObject innerObject = array.getJSONObject(index);
                                String name = innerObject.getString(Keys.KEY_NAME);
                                String country = innerObject.getString(Keys.KEY_COUNTRY);

                                model.setNames(name);
                                model.setCell(country);
                                list.add(model);
                            }
                        }

                    }
                }

            }catch (JSONException e){
                Log.i(JSONparser.TAG, ""+ e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            /*
             * Checking if List size if more than zero then
             * Update ListView
             */

            if(list.size() > 0) {
                adapter.notifyDataSetChanged();
            } else {
                Snackbar.make(findViewById(R.id.parentLayout), "No Data Found", Snackbar.LENGTH_LONG).show();
            }
        }

    }

}

