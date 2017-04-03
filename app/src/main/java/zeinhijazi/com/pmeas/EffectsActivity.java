package zeinhijazi.com.pmeas;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import zeinhijazi.com.pmeas.effects.Effect;
import zeinhijazi.com.pmeas.effects.EffectsDefaults;
import zeinhijazi.com.pmeas.util.EnabledListAdapter;

public class EffectsActivity extends AppCompatActivity
{
    ListView effectsListView;
    EnabledListAdapter listAdapter;
    ArrayList<Effect> effects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effects);

        ArrayList<String> param = new ArrayList<>();
        param.add("Feedback");
        param.add("Slope");

        effects = new ArrayList<>();
        effects.add(new Effect(EffectsDefaults.EffectTypes.DISTORTION));
        effects.add(new Effect(EffectsDefaults.EffectTypes.DELAY));
        effects.add(new Effect(EffectsDefaults.EffectTypes.CHORUS));

        listAdapter = new EnabledListAdapter(this, effects);

        effectsListView = (ListView)findViewById(R.id.enabledEffectsList);
        effectsListView.setAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.pmeasbar, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch(item.getItemId()) {
//            case R.id.action_send:
//                Toast.makeText(this, "Send Button Selected", Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.action_add:
//                Toast.makeText(this, "Add Button Selected", Toast.LENGTH_SHORT).show();
//                final CharSequence effectsNames[] = new CharSequence[] {"Distortion", "Delay", "Reverb", "Chorus", "Frequency Shift", "Harmonizer", "Flanger", "Phaser"};
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("Choose an Effect");
//                builder.setItems(effectsNames, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        System.out.print("ADD EFFECT CLICK INDEX: " + which +", EFFECT NAME: " + effectsNames[which]);
//                        ArrayList<String> effectParameters = new ArrayList<>();
//                        switch (which) {
//                            case 0:
//                                // stuff
//                                effectParameters.add("Distortion");
//                                effectParameters.add("Tone");
//                                break;
//                            case 1:
//                                // stuff
//                                effectParameters.add("Delay Time");
//                                effectParameters.add("Feedback");
//                                break;
//                            case 2:
//                                // stuff
//                                effectParameters.add("Balance");
//                                effectParameters.add("Tone");
//                                effectParameters.add("Delay");
//                                effectParameters.add("Room Size");
//                                break;
//                            case 3:
//                                // stuff
//                                effectParameters.add("Balance");
//                                effectParameters.add("Rate");
//                                effectParameters.add("Depth");
//                                break;
//                            case 4:
//                                // stuff
//                                effectParameters.add("Pitch");
//                                break;
//                            case 5:
//                                // stuff
//                                effectParameters.add("Feedback");
//                                effectParameters.add("Shift");
//                                break;
//                            case 6:
//                                // stuff
//                                effectParameters.add("Depth");
//                                effectParameters.add("Frequency");
//                                effectParameters.add("Feedback");
//                                break;
//                            case 7:
//                                // stuff
//                                effectParameters.add("Frequency");
//                                effectParameters.add("Spread");
//                                effectParameters.add("Feedback");
//                                effectParameters.add("Num");
//                                break;
//                            default:
//                                break;
//                        }
//
//                        // TODO: Instead of going through the listarray for all values, you can just
//                        // TODO: store the created effects in some global variable that we can then read.
//                        // TODO: But then there is issue with looking at what values are... so maybe listview only way.
//                        // TODO: Also we maybe dont need the FLoats arraylist in an effect if we can read raw seekbar values.
//                        String selectedEffect = effectsNames[which].toString();
//                        effects.add(new Effect(selectedEffect, effectParameters, new ArrayList<Float>()));
//
//                        listAdapter.notifyDataSetChanged();
//
//                        dialog.cancel();
//                    }
//                });
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}
