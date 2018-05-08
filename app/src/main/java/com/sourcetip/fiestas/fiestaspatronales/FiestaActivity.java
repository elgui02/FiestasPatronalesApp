package com.sourcetip.fiestas.fiestaspatronales;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FiestaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiesta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Bundle b = getIntent().getExtras();
        TextView txtFiesta = (TextView) findViewById(R.id.fiesta);
        TextView txtMunicipio = (TextView) findViewById(R.id.municipio);
        TextView txtFecha = (TextView) findViewById(R.id.fecha);
        TextView txtDesc = (TextView) findViewById(R.id.desc);

        txtFiesta.setText(b.getString("nombre"));

        txtMunicipio.setText(b.getString("municipio")+" - "+b.getString("departamento"));
        txtFecha.setText(b.getString("fecha"));
        txtDesc.setText(b.getString("descripcion"));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final Intent intent = new Intent(this, MapsActivity.class);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("nombre",b.getString("nombre"));
                intent.putExtra("lat", b.getDouble("lat"));
                intent.putExtra("lon", b.getDouble("lon"));

                startActivity(intent);
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
