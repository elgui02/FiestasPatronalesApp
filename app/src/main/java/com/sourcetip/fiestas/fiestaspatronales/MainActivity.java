package com.sourcetip.fiestas.fiestaspatronales;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //String[] itemname ={};
    ArrayList<String> itemname = new ArrayList<String>();
    JSONArray obj;

    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPref = getSharedPreferences("lang", Context.MODE_PRIVATE);
        String ln = sharedPref.getString("lang","");

        if( ln.isEmpty() )
        {
            Intent intent = new Intent(this, Settingsctivity.class);
            startActivity(intent);
        }
        list = (ListView)findViewById(R.id.list);
        new JsonTask().execute("");

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    JSONObject fclick = obj.getJSONObject(i);
                    Log.d("adapter", fclick.getString("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //new ConnectServer().execute();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, Settingsctivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

private class JsonTask extends AsyncTask<String, String, String> {

    ProgressDialog pd;
    protected void onPreExecute() {
        super.onPreExecute();

        pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("Please wait");
        pd.setCancelable(false);
        pd.show();
    }

    protected String doInBackground(String... params) {

        String str="http://www.fiestas.sourcetip.com/rest/fiestaspatronales";
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(str);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();


            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
                //Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

            }

            return buffer.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String json) {
        super.onPostExecute(json);
        if (pd.isShowing()) {
            pd.dismiss();
        }

        try {

            obj = new JSONArray(json);
            for (int x = 0; x < obj.length(); x++) {
                JSONObject fiesta = obj.getJSONObject(x);
                itemname.add(fiesta.getString("_nombre")+"\n"+fiesta.getJSONObject("municipio").get("_municipio"));
                Log.d("My App", fiesta.getString("_nombre"));
            }


        } catch (Throwable tx) {
            Log.e("My App", tx.toString());
        }
        Log.e("array size","text");
        Log.d("array size",String.valueOf(itemname.size()));

        String[] lstFiestas = itemname.toArray(new String[itemname.size()]);

        list.setAdapter(new ArrayAdapter<String>(MainActivity.this,
                R.layout.fiesta_list,
                R.id.Itemname,lstFiestas
        ));
    }
}
}