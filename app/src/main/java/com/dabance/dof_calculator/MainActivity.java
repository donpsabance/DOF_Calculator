package com.dabance.dof_calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_REQUEST_CODE = 1;
    private LensManager lensManager = LensManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadLens();
        showLens();
        registerClickFeedback();
        setTitle(getString(R.string.title));

        FloatingActionButton fab = findViewById(R.id.addLensButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = AddLensActivity.makeIntent(MainActivity.this);
                startActivityForResult(intent, ADD_REQUEST_CODE);
            }
        });

    }

    private void loadLens(){

        lensManager.addLens(new Lens("Canon", 1.8, 50));
        lensManager.addLens(new Lens("Tamron", 2.8, 90));
        lensManager.addLens(new Lens("Sigma", 2.8, 200));
        lensManager.addLens(new Lens("Nikon", 4, 200));

    }

    private void showLens(){

        ArrayAdapter<Lens> adapter = new ArrayAdapter<>(this, R.layout.lenslayout, lensManager.getLensList());
        ListView lv = findViewById(R.id.lensListView);
        lv.setAdapter(adapter);

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
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_REQUEST_CODE){
            if(resultCode == RESULT_OK){

                showLens();
            }
        }
    }

}
