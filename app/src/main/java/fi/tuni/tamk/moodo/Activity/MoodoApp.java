package fi.tuni.tamk.moodo.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import androidx.appcompat.app.AppCompatActivity;
import fi.tuni.tamk.moodo.Classes.CyclicTransitionDrawable;
import fi.tuni.tamk.moodo.R;
import fi.tuni.tamk.moodo.Classes.Routine;
import fi.tuni.tamk.moodo.Classes.SubRoutine;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MoodoApp extends AppCompatActivity {
    private ListView listView;
    private ArrayList<Routine> routineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MoodoApp", "onCreate");
        setContentView(R.layout.moodo_app);

        // Create view and add routines to list
        listView = findViewById(R.id.routine_list);
        routineList = new ArrayList<>();
        // Add the default routines for all
        addDefaultRoutines();
        addDefaultSubRoutines();

        // Add list of routines to the list view
        ArrayAdapter<Routine> adapter = new ArrayAdapter<>(this, R.layout.list_item, routineList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Routine listItem = (Routine) listView.getItemAtPosition(position);
                Log.d("MoodoApp", "Subroutines: " + listItem.getSubRoutines().toString());
                Intent intent = new Intent(getApplicationContext(), RoutineView.class);
                // Add clicked list routine to be displayed in RoutineView
                intent.putExtra("routine", listItem);
                startActivity(intent);
            }
        });

        initializeBackgroundTransition();
    }

    // Adds default routines for all (unless deleted if possible)
    private void addDefaultRoutines() {
        routineList.add(new Routine(1,"Pese hampaat", 300));
        routineList.add(new Routine(2,"Vie roskat", 300));
    }

    // Adds sub routines for default routines using routine id's
    private void addDefaultSubRoutines() {
        ArrayList<SubRoutine> subRoutines = new ArrayList<>();
        for(Routine item : routineList) {
            switch (item.getId()) {
                case 1:
                    subRoutines.add(new SubRoutine(1, "Ota hammasharja"));
                    subRoutines.add(new SubRoutine(2, "Lisää hammastahna"));
                    subRoutines.add(new SubRoutine(3, "Pese hampaita kolme minuuttia"));
                    subRoutines.add(new SubRoutine(4, "Huuhtele suu ja hammasharja"));
                    break;
                case 2:
                    subRoutines.add(new SubRoutine(1, "Sulje roskapussi"));
                    subRoutines.add(new SubRoutine(2, "Nosta roskapussi pois säiliöstä"));
                    subRoutines.add(new SubRoutine(3, "Vie roskat roskikseen"));
                    subRoutines.add(new SubRoutine(4, "Laita uusi roskapussi tilalle"));
            }
            if(subRoutines.size() != 0) {
                Log.d("MoodoApp", subRoutines.toString());
                item.setSubRoutines(subRoutines);
                subRoutines.clear();
            }
        }
    }

    public void openSettings(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    private void initializeBackgroundTransition() {
        View containerView = findViewById(R.id.root_view_moodo);

        Drawable bg_0 = getDrawable(R.drawable.bg_blue);
        Drawable bg_1 = getDrawable(R.drawable.bg_blueorange);
        Drawable bg_2 = getDrawable(R.drawable.bg_greenyellow);
        Drawable bg_3 = getDrawable(R.drawable.bg_green);
        Drawable bg_4 = getDrawable(R.drawable.bg_orange);
        Drawable bg_5 = getDrawable(R.drawable.bg_redorange);
        Drawable bg_6 = getDrawable(R.drawable.bg_lightred);
        Drawable bg_7 = getDrawable(R.drawable.bg_red);
        Drawable bg_8 = getDrawable(R.drawable.bg_purple);
        Drawable[] drawables = {bg_0, bg_1, bg_2, bg_3, bg_4, bg_5, bg_6, bg_7, bg_8};

        CyclicTransitionDrawable ctd = new CyclicTransitionDrawable(drawables);
        containerView.setBackground(ctd);
        ctd.startTransition(4000, 8000); // 1 second transition, 3 second pause between transitions.
    }
}
