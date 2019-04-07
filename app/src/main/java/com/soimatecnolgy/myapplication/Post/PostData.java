package com.soimatecnolgy.myapplication.Post;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.soimatecnolgy.myapplication.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.Buffer;
import java.util.Iterator;



public class PostData extends AppCompatActivity {
    TextView tcell;
    TextView tnames;
    Button button;
    String names;
    String cell;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frorm);

        button = (Button) findViewById(R.id.btn_signup);
        tcell = (EditText) findViewById(R.id.input_cell);
        tnames = (EditText) findViewById(R.id.input_names);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cell = tcell.getText().toString();
                names = tnames.getText().toString();

                new SendRequest().execute();

            }
        });
    }
    /*AQUI USO ASYNC TASK PARA REALIZAR LAS PETICIONES A GOOGLE DRIVE*/ 
    public class SendRequest extends AsyncTask<String, Void, String> {
        protected void onPreExecute(){

        }

        protected  String doInBackground( String... arg0 ){
            try {
                URL url = new URL("https://script.google.com/macros/s/AKfycbxYBp5d14QICxgv9hsTIOUPmB-3noEYa_1JIfzktEnLS4XPiT0/exec");
                JSONObject postDataParams = new JSONObject();
                String id = "1zPze1WmNHI7o7wtArapjEpRJpCr-8dypTRrEqOv7kKU";
                postDataParams.put("cell", cell);
                postDataParams.put("names", names);
                postDataParams.put("id", id);

                Log.e("-----Params-----",postDataParams.toString());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(1500);
                con.setConnectTimeout(1500);
                con.setDoInput(true);
                con.setDoOutput(true);

                OutputStream os = con.getOutputStream();
                BufferedWriter write =
                        new BufferedWriter(
                                new OutputStreamWriter(os,"UTF-8"));
                write.write(getPostDataString(postDataParams));
                write.flush();
                write.close();

                int responseCode = con.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    BufferedReader in =
                            new BufferedReader(
                                    new InputStreamReader(con.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while((line = in.readLine()) != null){
                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();
                }else{
                    return new String("false: "+ responseCode);
                }


            }catch (Exception e){
                return new String("Exception: " + e.getMessage());

            }

        }


        protected void  onPostExecute(String result){
            Toast.makeText(getApplicationContext(),
                    result,
                    Toast.LENGTH_LONG)
                    .show();
        }

    }

    public String getPostDataString(JSONObject params) throws Exception{

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();
        while(itr.hasNext()){
            String key = itr.next();
            Object value = params.get(key);

            if(first) {
                first =  false;
            }else{
                result.append("&");
            }

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }

        return result.toString();
    }

}
