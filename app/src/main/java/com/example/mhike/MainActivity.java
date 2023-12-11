package com.example.mhike;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ArrayList<HikingModel> hikeList = new ArrayList<>();
    HikingModel model;
    TabHost mainTab;
    Spinner sParking, sDifficulty;
    EditText hikeName, location, date, length, description, leaderName, edtSearch;
    ImageButton addNew, cancel, search, cancelS;
    ListView lvHike;
    Button clearAllData;


    private String[] parkingSelection = {"Yes", "No"};
    private String [] difficultySelection = {"Very Easy", "Easy", "Normal", "Hard", "Very Hard"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sParking = findViewById(R.id.spnParking);
        sDifficulty = findViewById(R.id.spnDifficulty);
        hikeName = findViewById(R.id.edtNameOfHike);
        location = findViewById(R.id.edtLocation);
        date = findViewById(R.id.edtDate);
        length = findViewById(R.id.edtLength);
        description = findViewById(R.id.edtDesc);
        leaderName = findViewById(R.id.edtLeader);
        addNew = findViewById(R.id.btnSave);
        cancel = findViewById(R.id.btnCancel);
        lvHike = findViewById(R.id.lvHome);
        search = findViewById(R.id.btnSearch);
        edtSearch = findViewById(R.id.edtSearch);
        cancelS = findViewById(R.id.btnCancelSearch);
        clearAllData = findViewById(R.id.btnClearAll);

        ArrayAdapter<String> yNAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, parkingSelection);
        yNAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sParking.setAdapter(yNAdapter);

        ArrayAdapter<String> difAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, difficultySelection);
        difAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sDifficulty.setAdapter(difAdapter);

        HikingDatabase db = new HikingDatabase(MainActivity.this);
        hikeList = db.listAll();

        final HikeAdapter hikeAdapter = new HikeAdapter(hikeList, db, MainActivity.this);
        lvHike.setAdapter(hikeAdapter);

        length.setText("0");

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HikingDatabase dbHelper = new HikingDatabase(getApplicationContext());
                String hikename = hikeName.getText().toString();
                String locationHike = location.getText().toString();
                String dateHike = date.getText().toString();
                int lengthHike = Integer.parseInt(length.getText().toString());
                String selectedParking = sParking.getSelectedItem().toString();
                String selectedDifficulty = sDifficulty.getSelectedItem().toString();
                String descriptionHike = description.getText().toString();
                String leader = leaderName.getText().toString();

                model = new HikingModel(hikename, locationHike, dateHike,  lengthHike, selectedParking, selectedDifficulty,
                        descriptionHike, leader);

                if (TextUtils.isEmpty(hikename) || TextUtils.isEmpty(locationHike) || TextUtils.isEmpty(selectedParking) ||
                        TextUtils.isEmpty(selectedDifficulty) || TextUtils.isEmpty(dateHike) || lengthHike == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Please fill in all required fields")
                            .setTitle("Required Fields")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }
                long hike = dbHelper.insertHike(model);
                hikeList = db.listAll();

                HikeAdapter hikeAdapter = new HikeAdapter(hikeList, db, MainActivity.this);
                lvHike.setAdapter(hikeAdapter);

                Toast.makeText(MainActivity.this, "New Hiking Log have been created with id: " +hike, Toast.LENGTH_SHORT).show();
            }
        });

        cancelS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hikeList = db.listAll();

                final HikeAdapter hikeAdapter = new HikeAdapter(hikeList, db, MainActivity.this);
                lvHike.setAdapter(hikeAdapter);
                edtSearch.setText("");
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hikeName.setText("");
                location.setText("");
                date.setText("");
                length.setText("0");
                sDifficulty.setSelection(0);
                sParking.setSelection(0);
                description.setText("");
                leaderName.setText("");
            }
        });

        lvHike.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                int id;
                id = hikeList.get(i).getId();
                Intent switchEdit = new Intent(MainActivity.this, DetailHikeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                bundle.putString("name", hikeList.get(i).getHikeName());
                bundle.putString("location", hikeList.get(i).getLocation());
                bundle.putString("date", hikeList.get(i).getDate());
                bundle.putInt("length", hikeList.get(i).getLength());
                bundle.putString("description", hikeList.get(i).getDescription());
                bundle.putString("leader", hikeList.get(i).getLeaderName());
                switchEdit.putExtras(bundle);
                startActivity(switchEdit);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HikingDatabase databaseForHikes = new HikingDatabase(MainActivity.this);
                String nameForSearch = edtSearch.getText().toString();
                hikeList = databaseForHikes.searchDataByName(nameForSearch);

                HikeAdapter sAdapter = new HikeAdapter(hikeList, databaseForHikes, MainActivity.this);
                lvHike.setAdapter(sAdapter);
            }
        });

        clearAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Are you sure to Delete all data?")
                        .setTitle("Delete All")
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                db.deleteAllHike();

                                hikeList = db.listAll();

                                ArrayAdapter<HikingModel> adapterReload = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, hikeList);
                                lvHike.setAdapter(adapterReload);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        TabHandle();
    }

    private void TabHandle() {
        mainTab = findViewById(R.id.mainTab);
        mainTab.setup();

        TabHost.TabSpec homeSpec, addNewSpec, hikeSpec;

        //homeSpec
        homeSpec = mainTab.newTabSpec("home");
        homeSpec.setContent(R.id.tab1);
        homeSpec.setIndicator("", getResources().getDrawable(R.drawable.home));
        mainTab.addTab(homeSpec);

        addNewSpec = mainTab.newTabSpec("addNew");
        addNewSpec.setContent(R.id.tab2);
        addNewSpec.setIndicator("", getResources().getDrawable(R.drawable.addnew));
        mainTab.addTab(addNewSpec);

        hikeSpec = mainTab.newTabSpec("hikeList");
        hikeSpec.setContent(R.id.tab3);
        hikeSpec.setIndicator("", getResources().getDrawable(R.drawable.list));
        mainTab.addTab(hikeSpec);
    }
}