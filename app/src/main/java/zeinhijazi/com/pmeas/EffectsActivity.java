package zeinhijazi.com.pmeas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import zeinhijazi.com.pmeas.effects.Effect;
import zeinhijazi.com.pmeas.util.EnabledListAdapter;

public class EffectsActivity extends AppCompatActivity {

    ListView effectsListView;
    EnabledListAdapter listAdapter;
    ArrayList<Effect> effects;

    Button addNewEffectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effects);

        ArrayList<String> param = new ArrayList<>();
        param.add("Feedback");
        param.add("Slope");

        effects = new ArrayList<>();
        effects.add(new Effect("Distortion", param, new ArrayList<Float>()));
        effects.add(new Effect("Delay", new ArrayList<String>(), new ArrayList<Float>()));

        listAdapter = new EnabledListAdapter(this, effects);

        effectsListView = (ListView)findViewById(R.id.enabledEffectsList);
        effectsListView.setAdapter(listAdapter);
    }
}
