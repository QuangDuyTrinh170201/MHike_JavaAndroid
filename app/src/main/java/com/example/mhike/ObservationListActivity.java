package com.example.mhike;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ObservationListActivity extends AppCompatActivity {

    private ArrayList<ObservationModel> observationList;
    ObservationModel modelObs;
    EditText observation, time, comment;
    TextView id, hikeId;
    ListView listObservation;
    ImageButton save, delete, clear, edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation_list);

        observation = findViewById(R.id.edtObservation);
        time = findViewById(R.id.edtTime);
        comment = findViewById(R.id.edtComment);

        listObservation = findViewById(R.id.lvObservation);
        save = findViewById(R.id.btnSaveObs);
        delete = findViewById(R.id.btnDeleteObs);
        clear = findViewById(R.id.btnClearData);
        edit = findViewById(R.id.btnEditObs);
        id = findViewById(R.id.txtObsId);
        hikeId = findViewById(R.id.txtHikeIdObs);

        Intent getIntentObservation = getIntent();
        String i = getIntentObservation.getStringExtra("idofhike");
        hikeId.setText(i);

        HikingDatabase db = new HikingDatabase(ObservationListActivity.this);
        observationList = db.listObservation(Integer.parseInt(hikeId.getText().toString()));

        ArrayAdapter<ObservationModel> adapter = new ArrayAdapter<>(ObservationListActivity.this, android.R.layout.simple_list_item_1, observationList);
        listObservation.setAdapter(adapter);

        int setHike = Integer.parseInt(hikeId.getText().toString());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntentObservation = getIntent();
                String i = getIntentObservation.getStringExtra("idofhike");
                String obs = observation.getText().toString();
                String timeObs = time.getText().toString();
                String cmt = comment.getText().toString();
                int hikeid = Integer.parseInt(i);
                modelObs = new ObservationModel(obs,timeObs,cmt,hikeid);

                if (TextUtils.isEmpty(obs) || TextUtils.isEmpty(timeObs)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ObservationListActivity.this);
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
                HikingDatabase db = new HikingDatabase(ObservationListActivity.this);
                long newObservation = db.insertObservation(modelObs);
                observationList = db.listObservation(setHike);

                ArrayAdapter<ObservationModel> adapterReload = new ArrayAdapter<>(ObservationListActivity.this, android.R.layout.simple_list_item_1, observationList);
                listObservation.setAdapter(adapterReload);
                Toast.makeText(ObservationListActivity.this, "New observation have been created with id: " +newObservation, Toast.LENGTH_SHORT).show();
                observation.setText("");
                time.setText("");
                comment.setText("");
            }
        });

        listObservation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                id.setText(String.valueOf(observationList.get(i).getId()));
                observation.setText(String.valueOf(observationList.get(i).getObservation()));
                time.setText(String.valueOf(observationList.get(i).getTime()));
                comment.setText(String.valueOf(observationList.get(i).getComment()));
                hikeId.setText(String.valueOf(observationList.get(i).getHikeId()));
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelObs = new ObservationModel(Integer.parseInt(id.getText().toString()));
                AlertDialog.Builder builder = new AlertDialog.Builder(ObservationListActivity.this);
                builder.setMessage("Are You Sure To Delete?")
                        .setTitle("Confirm Delete")
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                HikingDatabase db = new HikingDatabase(ObservationListActivity.this);
                                db.deleteObservation(modelObs);

                                Toast.makeText(ObservationListActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                                observationList = db.listObservation(Integer.parseInt(hikeId.getText().toString()));

                                ArrayAdapter<ObservationModel> adapterReload = new ArrayAdapter<>(ObservationListActivity.this, android.R.layout.simple_list_item_1, observationList);
                                listObservation.setAdapter(adapterReload);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelObs = new ObservationModel(Integer.parseInt(id.getText().toString()), observation.getText().toString(),
                        time.getText().toString(), comment.getText().toString(), Integer.parseInt(hikeId.getText().toString()));

                db.updateObservation(modelObs);
                HikingDatabase db1 = new HikingDatabase(ObservationListActivity.this);
                observationList = db1.listObservation(setHike);

                ArrayAdapter<ObservationModel> adapterReload = new ArrayAdapter<>(ObservationListActivity.this, android.R.layout.simple_list_item_1, observationList);
                listObservation.setAdapter(adapterReload);
                observation.setText("");
                time.setText("");
                comment.setText("");
            }
        });


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ObservationListActivity.this);
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
                                db.deleteAllObservation(Integer.parseInt(hikeId.getText().toString()));

                                observationList = db.listObservation(Integer.parseInt(hikeId.getText().toString()));

                                ArrayAdapter<ObservationModel> adapterReload = new ArrayAdapter<>(ObservationListActivity.this, android.R.layout.simple_list_item_1, observationList);
                                listObservation.setAdapter(adapterReload);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}