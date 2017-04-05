package zeinhijazi.com.pmeas;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import zeinhijazi.com.pmeas.effects.Effect;
import zeinhijazi.com.pmeas.effects.EffectsDefaults;
import zeinhijazi.com.pmeas.util.Bridge;
import zeinhijazi.com.pmeas.util.EnabledListAdapter;

public class EffectsActivity extends AppCompatActivity
{
    ListView effectsListView;
    EnabledListAdapter listAdapter;
    ArrayList<Effect> effects;

    Bridge bridge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effects);

        ArrayList<String> param = new ArrayList<>();
        param.add("Feedback");
        param.add("Slope");

        effects = new ArrayList<>();

        listAdapter = new EnabledListAdapter(this, effects);

        effectsListView = (ListView)findViewById(R.id.enabledEffectsList);
        effectsListView.setAdapter(listAdapter);

        try {
            bridge = new Bridge();
        } catch (IOException e) {
            Log.e("Effects Activity", "IO Exception making bridge: " + e.getMessage());
        }

        bridge.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.pmeasbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_send:
                Toast.makeText(this, "Send Button Selected", Toast.LENGTH_SHORT).show();
                new Bridge.BridgeAsync().execute("{\"intent\": \"EFFECT\", \"0\":{\"name\": \"delay\", \"delay\": 1, \"feedback\": 0.5}}");
                return true;
            case R.id.action_add:
                Toast.makeText(this, "Add Button Selected", Toast.LENGTH_SHORT).show();
                final CharSequence effectsNames[] = new CharSequence[] {"Distortion", "Delay", "Reverb", "Chorus", "Frequency Shift", "Harmonizer", "Flanger", "Phaser"};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose an Effect");
                builder.setItems(effectsNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // stuff
                                effects.add(new Effect(EffectsDefaults.EffectTypes.DISTORTION));
                                break;
                            case 1:
                                // stuff
                                effects.add(new Effect(EffectsDefaults.EffectTypes.DELAY));
                                break;
                            case 2:
                                // stuff
                                effects.add(new Effect(EffectsDefaults.EffectTypes.REVERB));
                                break;
                            case 3:
                                // stuff
                                effects.add(new Effect(EffectsDefaults.EffectTypes.CHORUS));
                                break;
                            case 4:
                                // stuff
                                effects.add(new Effect(EffectsDefaults.EffectTypes.FREQUENCY_SHIFT));
                                break;
                            case 5:
                                // stuff
                                effects.add(new Effect(EffectsDefaults.EffectTypes.HARMONIZER));
                                break;
                            case 6:
                                // stuff
                                effects.add(new Effect(EffectsDefaults.EffectTypes.FLANGER));
                                break;
                            case 7:
                                // stuff
                                effects.add(new Effect(EffectsDefaults.EffectTypes.PHASER));
                                break;
                            default:
                                break;
                        }

                        listAdapter.notifyDataSetChanged();

                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
