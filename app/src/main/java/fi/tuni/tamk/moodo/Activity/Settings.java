package fi.tuni.tamk.moodo.Activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import fi.tuni.tamk.moodo.Classes.CyclicTransitionDrawable;
import fi.tuni.tamk.moodo.R;

import android.util.Log;
import android.view.View;

public class Settings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Settings", "onCreate");
        setContentView(R.layout.settings_layout);
        initializeBackgroundTransition();
    }

    private void initializeBackgroundTransition() {
        View containerView = findViewById(R.id.root_view_settings);

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
