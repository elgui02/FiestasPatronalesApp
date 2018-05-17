package com.sourcetip.fiestas.fiestaspatronales;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<String> itemname = new ArrayList<String>();
    JSONArray obj;
    ListView list;
    String ln;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        SharedPreferences sharedPref = getSharedPreferences("lang", Context.MODE_PRIVATE);
        ln = sharedPref.getString("lang","es");
        toolbar.setTitle(sharedPref.getString("principal", "Fiestas patronales Guatemala"));

        setSupportActionBar(toolbar);

        if( ln.isEmpty() )
        {
            Intent intent = new Intent(this, Settingsctivity.class);
            startActivity(intent);
        }
        list = (ListView)findViewById(R.id.list);
        new JsonTask().execute("");

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        MenuItem nav_departamentos = menu.findItem(R.id.nav_departamentos);
        nav_departamentos.setTitle(sharedPref.getString("departamentos","Departamentos"));

        MenuItem nav_fecha = menu.findItem(R.id.nav_fecha);
        nav_fecha.setTitle(sharedPref.getString("fecha","fecha"));

        MenuItem nav_set = menu.findItem(R.id.nav_settings);
        nav_set.setTitle(sharedPref.getString("preferencias","preferencias"));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings)
        {
            Intent intent = new Intent(this, Settingsctivity.class);
            startActivity(intent);
        }
        else if( id == R.id.nav_departamentos)
        {
            Intent intentD = new Intent(this, DepartamentoActivity.class);
            startActivity(intentD);
        }
        else if( id == R.id.nav_fecha )
        {
            Intent intentM = new Intent(this, MesActivity.class);
            startActivity(intentM);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        ProgressDialog pd;
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(Main2Activity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {

            String str="http://www.fiestas.sourcetip.com/rest/fiestaspatronales/"+ln;
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

            list.setAdapter(new ArrayAdapter<String>(Main2Activity.this,
                    R.layout.fiesta_list,
                    R.id.Itemname,lstFiestas
            ));
        }
    }
}
