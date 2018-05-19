package com.sourcetip.fiestas.fiestaspatronales;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static java.lang.System.in;

public class CreditsActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    FloatingActionButton fab;
    int color;
    String lstNombres[] = {"Guillermo Arana", "Rocío Rosas", "Francisco Valle", "Alejandro Tuch", "Kevin Hernandez"};
    int lstColor[] = {Color.BLACK, Color.RED, Color.DKGRAY, Color.CYAN, Color.GREEN };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        sharedPref = getSharedPreferences("lang", Context.MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle(sharedPref.getString("info","Acerca de"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Button btn = (Button)findViewById(R.id.button4);
        btn.setText(sharedPref.getString("team","TEAM"));

        TextView txtDesc = (TextView)findViewById(R.id.textView3);
        txtDesc.setText(sharedPref.getString("desc",""));

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        color = 0;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(color >=5)
                {
                    color =0;
                    fab.setVisibility(View.GONE);

                }else {
                    fab.setImageBitmap(textAsBitmap(lstNombres[color], 40, Color.WHITE));
                    fab.setBackgroundTintList(ColorStateList.valueOf(lstColor[color]));
                    color++;
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

    //method to convert your text to image
    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    public void show(View view)
    {
        fab.setVisibility(View.VISIBLE);
        fab.setImageBitmap(textAsBitmap(lstNombres[color], 40, Color.WHITE));
        fab.setBackgroundTintList(ColorStateList.valueOf(lstColor[color]));
        //fab.setBackgroundColor(lstColor[color]);
        color++;
    }

}
