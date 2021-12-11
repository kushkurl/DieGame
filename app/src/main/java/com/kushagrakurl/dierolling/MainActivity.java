package com.kushagrakurl.dierolling;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AddNewDie.UpdateDieArray, ResultPopUp.UpdateLOGArray {
    public static ArrayList<String> logHistory;
    ArrayList<String> die; //{ "4", "6", "8", "10", "12", "20"};

    String selectedDie = "";
    SharedPreferences sharedpreferences;

    ImageView play;
    FloatingActionButton newbutton;
    TextView rollDietext;
    Button clearLog;

    protected RecyclerView mRecyclerView;
    protected CustomAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    //public ArrayList<String> logHistory;
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected MainActivity.LayoutManagerType mCurrentLayoutManagerType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedpreferences = getSharedPreferences("appData", Context.MODE_PRIVATE);
        //sharedpreferences.edit().clear().commit();

        //loading logs and die types from sharedpreferences
        String allDie = sharedpreferences.getString("die", "");
        String logs = sharedpreferences.getString("logHistory","");


        if(allDie == "") {
            //if there are no dies or app is run for firt time bydefault it will show these dies
            die = new ArrayList<String>(
                    Arrays.asList("4", "6", "8", "10", "12", "20"));
        }
        else{
            die =  new ArrayList<String>(Arrays.asList(allDie.split(",")));
        }

        if(logs == "") {
            logHistory = new ArrayList<String>();
        }
        else{
            logHistory = new ArrayList<String>(Arrays.asList(logs.split(",")));
        }


        rollDietext = findViewById(R.id.rollDiefor);

        //to add a new type of die
        newbutton = findViewById(R.id.addDie);
        newbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment addnewdie = new AddNewDie();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frmLayout, addnewdie,null);
                //ADDING TO STACK TO RESTORE AGAIN WHEN BACK BUTTON IS PRESSED
                fragmentTransaction.addToBackStack(addnewdie.getClass().getName());
                fragmentTransaction.commit();
            }
        });

        //to roll a die and click play button and get result
        play = (ImageView)findViewById(R.id.playButton);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedDie != "") {
                    ResultPopUp popUpClass = new ResultPopUp(selectedDie);
                    popUpClass.showPopupWindow(v);
                }

            }
        });

        //to clear all the history of die rolling
        clearLog = findViewById(R.id.clearLogs);
        clearLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedpreferences.edit().remove("logHistory").commit();
                logHistory = new ArrayList<>();
                CustomAdapter adapter = new CustomAdapter(logHistory);
                mRecyclerView.setAdapter(adapter);
            }
        });

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,die);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(this);

        mCurrentLayoutManagerType = MainActivity.LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (MainActivity.LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        //setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        mAdapter = new CustomAdapter(logHistory);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);


        setRecyclerViewLayoutManager(mCurrentLayoutManagerType.GRID_LAYOUT_MANAGER);

    }

    public void setRecyclerViewLayoutManager(MainActivity.LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        //grid layout to show logs in 4 columns
        mLayoutManager = new GridLayoutManager(this, 4);
        mCurrentLayoutManagerType = MainActivity.LayoutManagerType.GRID_LAYOUT_MANAGER;

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    //save logs and new logs on app close
    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedpreferences = getSharedPreferences("appData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("die", String.join(",", die));
        editor.putString("logHistory", String.join(",", logHistory));
        editor.apply();
    }

    //save logs and new logs on app stop
    @Override
    protected void onStop() {
        super.onStop();
        sharedpreferences = getSharedPreferences("appData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("die", String.join(",", die));
        editor.putString("logHistory", String.join(",", logHistory));
        editor.apply();

    }

    //save logs and new logs on app pause
    @Override
    protected void onPause() {
        super.onPause();
        sharedpreferences = getSharedPreferences("appData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("die", String.join(",", die));
        editor.putString("logHistory", String.join(",", logHistory));
        editor.apply();

    }

    //update spinner list by adding new die type in die list
    @Override
    public void updateDieList(String newDieSides) {
        die.add(newDieSides);
        //Toast.makeText(this, "Selected Item: ", Toast.LENGTH_SHORT).show();
    }

    //update logs list by adding new logs to logs list
    @Override
    public void updateLogs(String addLog) {
        // Shuffling the data of ArrayList using system time
        ArrayList<String> logArr = MainActivity.logHistory;
        logArr.add(addLog);
        CustomAdapter adapter = new CustomAdapter(logArr);
        mRecyclerView.setAdapter(adapter);
    }

        //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        selectedDie = die.get(position);
        rollDietext.setText("Roll "+ selectedDie + " side die");
        //Toast.makeText(getApplicationContext(),die.get(position) , Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        rollDietext.setText("");
        // TODO Auto-generated method stub
    }


}