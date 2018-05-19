package com.sourcetip.fiestas.fiestaspatronales;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class DepartamentoActivity extends AppCompatActivity {

    ArrayList<String> itemname = new ArrayList<String>();
    JSONArray obj;
    ListView list;
    String ln;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departamento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        sharedPref = getSharedPreferences("lang", Context.MODE_PRIVATE);
        toolbar.setTitle(sharedPref.getString("departamentos","Busqueda por departamento"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        list = (ListView)findViewById(R.id.list);
        final Intent intent = new Intent(this, MunicipioActivity.class);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    JSONObject fclick = obj.getJSONObject(i);
                    intent.putExtra("id",fclick.getString("id"));
                    intent.putExtra("nombre",fclick.getString("_departamento"));
                    Log.d("departamento",fclick.getString("_departamento"));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        new JsonTask().execute("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        ProgressDialog pd;
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(DepartamentoActivity.this);
            pd.setMessage(sharedPref.getString("espere","Please wait"));
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {

            String str="http://www.fiestas.sourcetip.com/rest/departamentos";
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
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

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
                    itemname.add(fiesta.getString("_departamento"));
                    Log.d("My App", fiesta.getString("_departamento"));
                }


            } catch (Throwable tx) {
                Log.e("My App", tx.toString());
            }

            String[] lstFiestas = itemname.toArray(new String[itemname.size()]);

            list.setAdapter(new ArrayAdapter<String>(DepartamentoActivity.this,
                    R.layout.data_list,
                    R.id.Itemname,lstFiestas
            ));
        }
    }

}
