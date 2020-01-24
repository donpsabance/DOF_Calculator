package com.dabance.dof_calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.dabance.dof_calculator.model.Lens;
import com.dabance.dof_calculator.model.LensManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_REQUEST_CODE = 1;
    private static final int EDIT_DELETE_REQUEST_CODE = 2;

    private LensManager lensManager = LensManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadSavedLens();
        loadDefaultLens();
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

    @Override
    protected void onStop(){
        super.onStop();

        //When program closes, or switches activity, everything from LensManager is saved onto SharedPreference file
        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(getString(R.string.sharedPrefFile), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for(int i = 0; i < lensManager.getLensList().size(); i++){
            editor.putInt(lensManager.getLensList().get(i).getInfo(), 1);
            editor.apply();
        }
        lensManager.getLensList().clear();
    }

    @Override
    protected void onStart(){
        super.onStart();

        //load everything from SharedPreference onto LensManager, this usually happens on startup or coming back from
        //another activity, LensManager list is used for displaying list of lenses and gets deleted and reloaded after
        loadSavedLens();
        showLens();
    }

    private void loadSavedLens(){

        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(getString(R.string.sharedPrefFile), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Map<String, ?> lensList = sharedPreferences.getAll();

        //read the key's only
        for(String key : lensList.keySet()){
            extractLensInfo(key);
        }
        editor.clear().apply();
    }

    private void extractLensInfo(String list){

        String[] listValues = list.split(",");

        String make = listValues[0];
        double aperture = Double.parseDouble(listValues[1]);
        int focal = Integer.parseInt(listValues[2]);

        Lens lens = new Lens(make, aperture, focal);
        lensManager.addLens(lens);

    }

    private void loadDefaultLens() {

        if (lensManager.getLensList().size() == 0) {

            lensManager.addLens(new Lens("Canon", 1.8, 50));
            lensManager.addLens(new Lens("Tamron", 2.8, 90));
            lensManager.addLens(new Lens("Sigma", 2.8, 200));
            lensManager.addLens(new Lens("Nikon", 4, 200));

        }
    }

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

                //need to send raw data because lensManager list won't be able after switching activity
                //because SharedPreference will be used instead
                Intent intent = CalculationsActivity.makeIntent(MainActivity.this, lensManager.getLensList().get(position).getInfo());
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
