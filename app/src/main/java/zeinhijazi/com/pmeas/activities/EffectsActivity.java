package zeinhijazi.com.pmeas.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Debug;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import zeinhijazi.com.pmeas.R;
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
            new Bridge().execute();
        } catch (IOException e) {
            Log.e("Effects Activity", "IO Exception creating bridge: " + e.getMessage());
        }
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
//                Toast.makeText(this, "Send Button Selected", Toast.LENGTH_SHORT).show();
                sendEffects();
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

    public void sendEffects() {

        try {
            JSONObject effectJSON = new JSONObject();
            JSONObject effectData = new JSONObject();

            effectJSON.put("intent", "EFFECT");

            View effectsListViewItem;
            for(int i = 0; i < effectsListView.getCount(); i++) {
                effectData = new JSONObject();
                effectsListViewItem = effectsListView.getChildAt(i);

                String numEffects = ((TextView)effectsListViewItem.findViewById(0)).getText().toString();
                /* Don't try to fix this line. It's not an actual error but a lint error because you
                technically shouldn't do this but our view is dynamically added so you have to. */
                String effectName = ((TextView)effectsListViewItem.findViewById(1)).getText().toString();

                effectData.put("name", effectName);

                for(int j = 2; j < Integer.parseInt(numEffects); j+=2) {
                    String paramName = ((TextView)effectsListViewItem.findViewById(j)).getText().toString();
                    float paramValue = Float.parseFloat(((TextView)effectsListViewItem.findViewById(j+1)).getText().toString());
                    effectData.put(paramName, paramValue);
                }

                effectJSON.put(String.valueOf(i), effectData);
            }

            System.out.println(effectJSON.toString());
            new BridgeAsync(this).execute(effectJSON.toString());


        } catch(JSONException e) {
            Log.e("JSON", "Json exception: " + e.getMessage());
        }
    }

    public static class BridgeAsync extends AsyncTask<String, Void, String> {

        Context context;

        public BridgeAsync(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String message = params[0];
            String result = null;

            try {
                if(!(Bridge.outputStream == null) && !(Bridge.inStream == null)) {
                    Bridge.outputStream.writeBytes(message);
                    result = Bridge.inStream.readLine();
                } else {
                }
            } catch(IOException e) {
                Log.e("BRIDGE", "Caught IOException on doInBg: " + e.getMessage());
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result == null) {
                Toast.makeText(context, "Send Failed. Did you lose connection?", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
