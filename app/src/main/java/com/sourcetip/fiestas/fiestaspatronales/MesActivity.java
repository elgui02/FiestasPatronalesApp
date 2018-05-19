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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

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
import java.util.List;

public class MesActivity extends AppCompatActivity {

    Spinner spinner;
    SharedPreferences sharedPref;
    int month;
    ArrayList<String> itemname = new ArrayList<String>();
    JSONArray obj;
    ListView list;
    String ln;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        sharedPref = getSharedPreferences("lang", Context.MODE_PRIVATE);
        ln = sharedPref.getString("lang","es");

        toolbar.setTitle(sharedPref.getString("fecha","Busqueda por mes"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Button btn = (Button) findViewById(R.id.button2);
        btn.setText(sharedPref.getString("buscar","search"));

        spinner = (Spinner)this.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter;
        List<String> meses;

        meses = new ArrayList<String>();
        for(int x = 1; x<=12; x++)
        {
            meses.add(sharedPref.getString(String.valueOf(x),String.valueOf(x)));
        }

        adapter = new ArrayAdapter<String>(getBaseContext(),
                android.R.layout.simple_spinner_item, meses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        list = (ListView)findViewById(R.id.list);
        final Intent intent = new Intent(this, FiestaActivity.class);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    JSONObject fclick = obj.getJSONObject(i);
                    intent.putExtra("nombre",fclick.getString("_nombre"));
                    intent.putExtra("municipio",fclick.getJSONObject("municipio").getString("_municipio"));
                    intent.putExtra("departamento",fclick.getJSONObject("municipio").getJSONObject("departamento").getString("_departamento"));
                    intent.putExtra("fecha",fclick.getString("_fecha") );
                    intent.putExtra("lat",fclick.getDouble("_latitud"));
                    intent.putExtra("lon",fclick.getDouble("_longitud"));
                    intent.putExtra("descripcion",fclick.getString("_descripcion"));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public void search(View view) {
        month = spinner.getSelectedItemPosition()+1;
        new JsonTask().execute("");
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        ProgressDialog pd;
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MesActivity.this);
            pd.setMessage(sharedPref.getString("espere","Please wait"));
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {

            String str="http://www.fiestas.sourcetip.com/rest/fiestaspatronales/"+String.valueOf(month)+"/mes/"+ln;
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
            String[] lstFiestas = itemname.toArray(new String[itemname.size()]);

            list.setAdapter(new ArrayAdapter<String>(MesActivity.this,
                    R.layout.fiesta_list,
                    R.id.Itemname,lstFiestas
            ));
        }
    }

}
