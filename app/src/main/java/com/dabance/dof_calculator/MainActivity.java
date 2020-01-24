package com.dabance.dof_calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dabance.dof_calculator.model.Lens;
import com.dabance.dof_calculator.model.LensManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_REQUEST_CODE = 1;
    private static final int EDIT_DELETE_REQUEST_CODE = 2;

    private LensManager lensManager = LensManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadSavedLens();
        showLens();
        registerClickFeedback();

        FloatingActionButton fab = findViewById(R.id.addLensButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = AddLensActivity.makeIntent(MainActivity.this);
                startActivityForResult(intent, ADD_REQUEST_CODE);

            }
        });

    }

    private void loadSavedLens(){

        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(getString(R.string.sharedPrefFile), Context.MODE_PRIVATE);
        String make = sharedPreferences.getString(getString(R.string.lensMakeList), "");
        int focal = sharedPreferences.getInt(getString(R.string.lensFocalList), 0);
        double aperture = Double.longBitsToDouble(sharedPreferences.getLong(getString(R.string.lensApertureList), Double.doubleToLongBits(1.4)));
        lensManager.addLens(new Lens(make, aperture, focal));

    }

//    private void loadLens(){
//
//        lensManager.addLens(new Lens("Canon", 1.8, 50));
//        lensManager.addLens(new Lens("Tamron", 2.8, 90));
//        lensManager.addLens(new Lens("Sigma", 2.8, 200));
//        lensManager.addLens(new Lens("Nikon", 4, 200));
//
//    }

    private void showLens(){

        if(!lensManager.getLensList().isEmpty()){

            TextView tv = findViewById(R.id.emptyLensTextView);
            tv.setVisibility(View.INVISIBLE);

            ArrayAdapter<Lens> adapter = new ArrayAdapter<>(this, R.layout.lenslayout, lensManager.getLensList());
            ListView lv = findViewById(R.id.lensListView);
            lv.setAdapter(adapter);
            lv.setVisibility(View.VISIBLE);

        } else {

            ListView lv = findViewById(R.id.lensListView);
            lv.setVisibility(View.INVISIBLE);

            TextView tv = findViewById(R.id.emptyLensTextView);
            tv.setVisibility(View.VISIBLE);

        }
    }

    private void registerClickFeedback(){

        ListView lv = findViewById(R.id.lensListView);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                String message = "You have selected lens: " + ((TextView) view).getText().toString();
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                Intent intent = CalculationsActivity.makeIntent(MainActivity.this, position);
                startActivityForResult(intent, EDIT_DELETE_REQUEST_CODE);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_REQUEST_CODE || requestCode == EDIT_DELETE_REQUEST_CODE){
            if(resultCode == RESULT_OK){

                showLens();
            }
        }
    }
}
