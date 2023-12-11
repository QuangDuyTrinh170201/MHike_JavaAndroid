package com.example.mhike;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class DetailHikeActivity extends AppCompatActivity {

    EditText hikename, location, date, length, description, leader, id;
    Spinner sParking, sDifficulty;
    Button cancel, delete, edit, observation;

    private String[] parkingSelection = {"Yes", "No"};
    private String [] difficultySelection = {"Very Easy", "Easy", "Normal", "Hard", "Very Hard"};

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_hike);

        id = findViewById(R.id.edtId);
        hikename = findViewById(R.id.edtHikeNameEdit);
        location = findViewById(R.id.edtLocationEdit);
        date = findViewById(R.id.edtDateEdit);
        length = findViewById(R.id.edtLengthEdit);
        description = findViewById(R.id.edtDescEdit);
        leader = findViewById(R.id.edtLeaderNameEdit);
        sParking = findViewById(R.id.spnParkingEdit);
        sDifficulty = findViewById(R.id.spnDifficultyEdit);
        cancel = findViewById(R.id.btnCancelDetail);
        delete = findViewById(R.id.btnDeleteHike);
        edit = findViewById(R.id.btnEdit);
        observation = findViewById(R.id.btnObservation);

        ArrayAdapter<String> yNAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, parkingSelection);
        yNAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sParking.setAdapter(yNAdapter);

        ArrayAdapter<String> difAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, difficultySelection);
        difAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sDifficulty.setAdapter(difAdapter);

        Intent getIntentForEdit = getIntent();
        Bundle bundle = getIntentForEdit.getExtras();

        int bundleId = bundle.getInt("id", 0);
        String bundleName = bundle.getString("name", "");
        String bundleLocation = bundle.getString("location", "");
        String bundleDate = bundle.getString("date", "");
        int bundleLength = bundle.getInt("length", 0);
        String bundleDesc = bundle.getString("description", "");
        String bundleLeader = bundle.getString("leader", "");

        id.setText(String.valueOf(bundleId));
        hikename.setText(bundleName);
        location.setText(bundleLocation);
        date.setText(bundleDate);
        length.setText(String.valueOf(bundleLength));
        description.setText(bundleDesc);
        leader.setText(bundleLeader);

        HikingDatabase hikeDb = new HikingDatabase(DetailHikeActivity.this);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HikingModel model = new HikingModel(Integer.parseInt(id.getText().toString()));
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailHikeActivity.this);
                builder.setMessage("Are You Sure To Delete?")
                        .setTitle("ConfirmDelete")
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                hikeDb.deleteHike(model);
                                Toast.makeText(DetailHikeActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DetailHikeActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder editDialog = new AlertDialog.Builder(DetailHikeActivity.this);
                editDialog.setMessage("Are You Sure To Complete?")
                        .setTitle("Edit Message")
                        .setNegativeButton("CONTINUE EDIT", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int i) {
                                HikingModel model = new HikingModel(Integer.parseInt(id.getText().toString()), hikename.getText().toString(),
                                        location.getText().toString(), date.getText().toString(), Integer.parseInt(length.getText().toString()),
                                        sParking.getSelectedItem().toString(), sDifficulty.getSelectedItem().toString(),
                                        description.getText().toString(), leader.getText().toString());

                                hikeDb.updateHike(model);
                                Intent intent = new Intent(DetailHikeActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });
                AlertDialog dialog = editDialog.create();
                dialog.show();
            }
        });

        observation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailHikeActivity.this, ObservationListActivity.class);
                i.putExtra("idofhike", id.getText().toString());
                startActivity(i);
            }
        });
    }
}