package com.sourcetip.fiestas.fiestaspatronales;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by willy on 3/05/18.
*/
public class ConnectServer extends AsyncTask<Void, Void, JSONObject>
{

    @Override
    protected JSONObject doInBackground(Void... params)
    {

        String str="http://www.fiestas.sourcetip.com/rest/fiestaspatronales";
        URLConnection urlConn = null;
        BufferedReader bufferedReader = null;
        try
        {
            URL url = new URL(str);
            urlConn = url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

            StringBuffer stringBuffer = new StringBuffer();
            JsonReader js = new JsonReader(new InputStreamReader(urlConn.getInputStream()));
            js.beginArray();
            while (js.hasNext()) {
                js.beginObject();
                while (js.hasNext()) {
                    String name = String.valueOf(js.nextName());
                    switch (name) {
                        case "id":
                            Log.d("text",name);
                            break;
                        default:
                            js.skipValue();
                            break;
                    }
                }
                js.endObject();
            }
            js.endArray();
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                stringBuffer.append(line);
            }

            JSONArray ObjectArray = new JSONArray(stringBuffer.toString());
            return ObjectArray.toJSONObject(ObjectArray);
        }
        catch(Exception ex)
        {
            Log.e("App", "yourDataTask", ex);
            return null;
        }
        finally
        {
            if(bufferedReader != null)
            {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPostExecute(JSONObject response)
    {
        if(response != null)
        {
            //Log.d("JSON", response.toString());
            try {
                Log.d("App", "Success: " + response.getString("nombre"));
            } catch (JSONException ex) {
                Log.e("App", "Failure", ex);
            }
        }
    }
}