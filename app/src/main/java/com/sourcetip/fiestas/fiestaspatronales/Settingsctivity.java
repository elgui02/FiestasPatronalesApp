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
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Settingsctivity extends AppCompatActivity {

    Spinner spinner;
    String ln;
    JSONObject obj;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingsctivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        intent = new Intent(this, Main2Activity.class);
        spinner = (Spinner)this.findViewById(R.id.spinner);

    // Step 2: Create and fill an ArrayAdapter with a bunch of "State" objects
    ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
          android.R.layout.simple_spinner_item, new Language[] {
            new Language("af","Afrikaans"),
            new Language("sq","Albanian"),
            new Language("am","Amharic"),
            new Language("ar","Arabic"),
            new Language("hy","Armenian"),
            new Language("az","Azeerbaijani"),
            new Language("eu","Basque"),
            new Language("be","Belarusian"),
            new Language("bn","Bengali"),
            new Language("bs","Bosnian"),
            new Language("bg","Bulgarian"),
            new Language("ca","Catalan"),
            new Language("ceb","Cebuano"),
            new Language("Zh-CN","Chinese (Simplified)"),
            new Language("Zh-TW","Chinese (Traditional)"),
            new Language("co","Corsican"),
            new Language("hr","Croatian"),
            new Language("cs","Czech"),
            new Language("da","Danish"),
            new Language("nl","Dutch"),
            new Language("en","English"),
            new Language("eo","Esperanto"),
            new Language("et","Estonian"),
            new Language("fi","Finnish"),
            new Language("fr","French"),
            new Language("fy","Frisian"),
            new Language("gl","Galician"),
            new Language("ka","Georgian"),
            new Language("de","German"),
            new Language("el","Greek"),
            new Language("gu","Gujarati"),
            new Language("ht","Haitian Creole"),
            new Language("ha","Hausa"),
            new Language("haw","Hawaiian"),
            new Language("iw","Hebrew"),
            new Language("hi","Hindi"),
            new Language("hmn","Hmong"),
            new Language("hu","Hungarian"),
            new Language("is","Icelandic"),
            new Language("ig","Igbo"),
            new Language("id","Indonesian"),
            new Language("ga","Irish"),
            new Language("it","Italian"),
            new Language("ja","Japanese"),
            new Language("jw","Javanese"),
            new Language("kn","Kannada"),
            new Language("kk","Kazakh"),
            new Language("km","Khmer"),
            new Language("ko","Korean"),
            new Language("ku","Kurdish"),
            new Language("ky","Kyrgyz"),
            new Language("lo","Lao"),
            new Language("la","Latin"),
            new Language("lv","Latvian"),
            new Language("lt","Lithuanian"),
            new Language("lb","Luxembourgish"),
            new Language("mk","Macedonian"),
            new Language("mg","Malagasy"),
            new Language("ms","Malay"),
            new Language("ml","Malayalam"),
            new Language("mt","Maltese"),
            new Language("mi","Maori"),
            new Language("mr","Marathi"),
            new Language("mn","Mongolian"),
            new Language("my","Myanmar (Burmese)"),
            new Language("ne","Nepali"),
            new Language("no","Norwegian"),
            new Language("ny","Nyanja (Chichewa)"),
            new Language("ps","Pashto"),
            new Language("fa","Persian"),
            new Language("pl","Polish"),
            new Language("pt","Portuguese (Portugal, Brazil)"),
            new Language("pa","Punjabi"),
            new Language("ro","Romanian"),
            new Language("ru","Russian"),
            new Language("sm","Samoan"),
            new Language("gd","Scots Gaelic"),
            new Language("sr","Serbian"),
            new Language("st","Sesotho"),
            new Language("sn","Shona"),
            new Language("sd","Sindhi"),
            new Language("si","Sinhala (Sinhalese)"),
            new Language("sk","Slovak"),
            new Language("sl","Slovenian"),
            new Language("so","Somali"),
            new Language("es","Spanish"),
            new Language("su","Sundanese"),
            new Language("sw","Swahili"),
            new Language("sv","Swedish"),
            new Language("tl","Tagalog (Filipino)"),
            new Language("tg","Tajik"),
            new Language("ta","Tamil"),
            new Language("te","Telugu"),
            new Language("th","Thai"),
            new Language("tr","Turkish"),
            new Language("uk","Ukrainian"),
            new Language("ur","Urdu"),
            new Language("uz","Uzbek"),
            new Language("vi","Vietnamese"),
            new Language("cy","Welsh"),
            new Language("xh","Xhosa"),
            new Language("yi","Yiddish"),
            new Language("yo","Yoruba"),
            new Language("zu","Zulu")
    });

    // Step 3: Tell the spinner about our adapter
    spinner.setAdapter(spinnerArrayAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public void save(View view)
    {

        SharedPreferences sharedPref = getSharedPreferences("lang", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        Language lng = (Language)spinner.getSelectedItem();
        ln = lng.getCode();
        editor.putString("lang", lng.getCode());
        editor.commit();

        new JsonTask().execute("");


    }

    private class JsonTask extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(Settingsctivity.this);
            pd.setMessage("Charge translation");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {

            String str="http://www.fiestas.sourcetip.com/rest/preferencias/"+ln;
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

                obj = new JSONObject(json);
                Log.d("json",obj.getJSONObject("meses").toString());

                SharedPreferences sharedPref = getSharedPreferences("lang", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                Language lng = (Language)spinner.getSelectedItem();
                ln = lng.getCode();
                editor.putString("principal", obj.getString("principal"));
                editor.putString("departamentos", obj.getString("departamentos"));
                for(int x=1;x<=12;x++)
                {
                    editor.putString(String.valueOf(x),obj.getJSONObject("meses").getString(String.valueOf(x)));
                }

                editor.commit();

                startActivity(intent);
                finish();
                /*for (int x = 0; x < obj.length(); x++) {
                    JSONObject fiesta = obj.getJSONObject(x);
                    itemname.add(fiesta.getString("_nombre")+"\n"+fiesta.getJSONObject("municipio").get("_municipio"));
                    Log.d("My App", fiesta.getString("_nombre"));
                }*/


            } catch (Throwable tx) {
                Log.e("My App", tx.toString());
            }
            Log.e("array size","text");

            /*String[] lstFiestas = itemname.toArray(new String[itemname.size()]);

            list.setAdapter(new ArrayAdapter<String>(Main2Activity.this,
                    R.layout.fiesta_list,
                    R.id.Itemname,lstFiestas
            ));*/
        }
    }
}
