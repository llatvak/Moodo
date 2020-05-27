package fi.tuni.tamk.moodo.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import fi.tuni.tamk.moodo.Classes.LocaleHelper;
import fi.tuni.tamk.moodo.Classes.Util;
import fi.tuni.tamk.moodo.R;
import fi.tuni.tamk.moodo.Classes.Routine;
import fi.tuni.tamk.moodo.Classes.SubRoutine;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class MoodoApp extends AppCompatActivity {
    private ListView listView;
    private ArrayList<Routine> routineList;
    public static ArrayList<Integer> deletedRoutines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MoodoApp", "onCreate");
        setContentView(R.layout.moodo_app);
        LocaleHelper.setLocale(this, LocaleHelper.getLanguage(this));

        // Create view and routinelist
        listView = findViewById(R.id.routine_list);
        routineList = new ArrayList<>();

        // Load routine data from .json file if first time startup
        if(Util.checkAppStart(this) == Util.AppStart.FIRST_TIME) {
            loadRoutineData();
        }

        // Add routines from local storage file to list
        ArrayList<Routine> customRoutines = (ArrayList<Routine>) Util.read(this);
        if(customRoutines != null) {
            routineList.addAll(customRoutines);
        }

        // Add list of routines to the list view
        ArrayAdapter<Routine> adapter = new ArrayAdapter<>(this, R.layout.list_item, routineList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Routine listItem = (Routine) listView.getItemAtPosition(position);
                //Log.d("MoodoApp", "Subroutines: " + listItem.getSubRoutines().toString());
                Intent intent = new Intent(getApplicationContext(), RoutineView.class);
                // Add clicked list routine to be displayed in RoutineView
                intent.putExtra("routine", listItem);
                startActivity(intent);
            }
        });
        Util.initializeBackgroundTransition(findViewById(R.id.root_view_moodo));

        listView.setOnItemLongClickListener((parent, view, pos, id) -> {
            if(id > 8) {
                final int which_item = pos;
                System.out.println("Item deleted " + id);
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Are you sure?")
                        .setMessage("Delete this item?")
                        .setPositiveButton("OK", (dialog, which) -> {
                            routineList.remove(which_item);
                            adapter.notifyDataSetChanged();
                            //deletedRoutines.add(which_item);
                            Util.write(this, null, routineList);
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
            return true;
        });

    }

    public void openSettings(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    // Loads JSON data from json file and convert it to string
    private void loadRoutineData() {
        Resources res = getResources();
        InputStream is = null;
        if(LocaleHelper.getLanguage(this).equals("fi")) {
            is = res.openRawResource(R.raw.routine_data_fin);
        } else {
            is = res.openRawResource(R.raw.routine_data_en);
        }
        Scanner scanner = new Scanner(is);
        StringBuilder builder = new StringBuilder();
        while(scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }
        parseJson(builder.toString());
    }

    // Parse string into routine and subroutines for adding into local storage
    private void parseJson(String jsonString) {
        // Temp array for storing subroutines
        ArrayList<SubRoutine> subRoutines = new ArrayList<>();
        try {
            // Json file root object
            JSONObject root = new JSONObject(jsonString);
            // Routine JsonArray
            JSONArray routineArray = root.getJSONArray("routines");
            // Loop through routine array and add routines to routine list
            for(int i = 0; i < routineArray.length(); i++) {
                // Get current routine and add to array
                JSONObject currRoutine = routineArray.getJSONObject((i));
                Routine tempRoutine = new Routine(currRoutine.getInt("id"),
                        currRoutine.getString("name"),
                        currRoutine.getInt("time"));
                // Loop through subroutine array included inside every routine JSONObject
                JSONArray subroutineArray = currRoutine.getJSONArray("subroutines");
                for(int j = 0; j < subroutineArray.length(); j++) {
                    // Get current subroutine and add to list
                    JSONObject currSubRoutine = subroutineArray.getJSONObject(j);
                    subRoutines.add(new SubRoutine(currRoutine.getInt("id"),
                            currSubRoutine.getString("description")));
                }
                // After looping through whole data add temp list to routine's subroutine list
                // and clear the subroutines list
                tempRoutine.setSubRoutines(subRoutines);
                // Write temp routine to storage
                Util.write(this, tempRoutine, Util.read(this));
                subRoutines.clear();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void openCreateRoutineView(View v) {
        Intent intent = new Intent(this, CreateRoutineView.class);
        intent.putExtra("totalRoutines", routineList.size() + 1);
        startActivity(intent);
    }
}
