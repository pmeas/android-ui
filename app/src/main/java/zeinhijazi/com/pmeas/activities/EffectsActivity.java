package zeinhijazi.com.pmeas.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import zeinhijazi.com.pmeas.R;
import zeinhijazi.com.pmeas.effects.Effect;
import zeinhijazi.com.pmeas.effects.EffectsDefaults;
import zeinhijazi.com.pmeas.util.Bridge;
import zeinhijazi.com.pmeas.util.EnabledListAdapter;

/**
 * This class allows the user to enable and disable effects. From here, they
 * can select effects and modify their parameters to achieve the effect they want. When
 * completed, the effects can be sent over to update on the backend system.
 */
public class EffectsActivity extends AppCompatActivity
{
    // The ListView view that contains all the effect views the user selected.
    ListView effectsListView;

    // The adapter required to outline the layout of each item in the list.
    EnabledListAdapter listAdapter;

    // The array that contains the currently enabled effects.
    ArrayList<Effect> effects;

    /**
     * Callback that gets called when a new activity is created. This can be seen as a "replacement"
     * for constructors in an Activity.
     *
     * @param savedInstanceState Used if the state of the application was previously saved.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effects);

        effects = new ArrayList<>();

        listAdapter = new EnabledListAdapter(this, effects);

        // Get the listview instance created from the XML file corresponding to the EffectsActivity.
        effectsListView = (ListView)findViewById(R.id.enabledEffectsList);

        // Tell the listview instance to follow the rules outlined in the custom adapter.
        effectsListView.setAdapter(listAdapter);


        // Run the bridge code to establish the connection to the modulation application.
        try {
            if(!Bridge.isConnected) {
                new Bridge(this).execute();
            }
        } catch (IOException e) {
            Log.e("Effects Activity", "IO Exception creating bridge: " + e.getMessage());
        }
    }

    /**
     * Callback after the options have been created. Simply points to the format of the menu
     * that will be created after it is loaded.
     *
     * @param menu The menu data that gets passed into the inflater.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.pmeasbar, menu);
        return true;
    }

    /**
     * Callback method that is activated when an item in the ActionBar is selected. In here, we
     * identify what item was selected and take appropriate action based on what the user wants
     * to do.
     *
     * @param item Information about the item that was selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Get item ID allows us to compare the ID to the ID's created in the pmeasbar style to identify what was selected.
        switch(item.getItemId()) {
            case R.id.action_send:
                // If the user clicks the play button, send the effects to the backend.
                sendEffects();
                return true;
            case R.id.action_add:
                /* If the user opts to add a new effect, open a dialog window that allows the user
                to select what effecet to add.
                 */

                // The list of effects to choose from.
                final CharSequence effectsNames[] = new CharSequence[] {"Distortion", "Delay", "Reverb", "Chorus", "Frequency Shift", "Harmonizer", "Flanger", "Phaser"};

                // AlertDialogs are OS-level windows that open up that allow the user to interact with them
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                // The main heading of the window.
                builder.setTitle("Choose an Effect");

                // What contents the alert window contains and what happens when each content is selected.
                // Basically, this code adds a new item to our list of effects and refreshes our listview.
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

                // Actually create and show the window.
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            case R.id.action_settings:
                // If the settings button was selected, create a new Activity and go to it.
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsIntent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Sends the effects gathered in the list to the modulation application.
     */
    public void sendEffects() {

        try {
            // Format our information into a JSON structure to conform with how the backend reads data.
            JSONObject effectJSON = new JSONObject();
            JSONObject effectData;

            // Tell the backend that we are sending new effects via intent.
            effectJSON.put("intent", "EFFECT");

            // Cycle through the listview (containing several Effect instances to get information on each one.
            View effectsListViewItem;
            for(int i = 0; i < effectsListView.getCount(); i++) {
                effectData = new JSONObject();

                // Get a new item in the listview corresponding to an enabled effect.
                effectsListViewItem = effectsListView.getChildAt(i);

                // Acquire all data about the selected effect and add them to our JSON structure.
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

            // Once all data is collected, send it off to the modulation application.
            new BridgeAsync(this).execute(effectJSON.toString());

        } catch(JSONException e) {
            Log.e("JSON", "Json exception: " + e.getMessage());
        }
    }

    /**
     * This class is responsible for handling communications with the backend in a separate thread.
     * It allows us to communicate with the backend and directly communicate with the UI thread once
     * the backend communication is completed.
     */
    private class BridgeAsync extends AsyncTask<String, Void, String> {

        Context context;

        BridgeAsync(Context context) {
            this.context = context;
        }

        /**
         * Perform the sending of the data in some separate thread.
         *
         * @param params The JSON data sent as one String.
         */
        @Override
        protected String doInBackground(String... params) {
            String message = params[0];
            String result = null;

            // Try to send the data to the modulation application and wait for a result.
            try {
                if(!(Bridge.outputStream == null) && !(Bridge.inStream == null)) {
                    Bridge.outputStream.writeBytes(message);
                    result = Bridge.inStream.readLine();
                }
            } catch(IOException e) {
                Log.e("BRIDGE", "Caught IOException on doInBg: " + e.getMessage());
            }

            return result;
        }

        /**
         * Decides what happens based on the result of the doInBackground() method.
         *
         * @param result The result of the call from doInBackground().
         */
        @Override
        protected void onPostExecute(String result) {
            if(result == null) {
                // If the sending was unsuccesful, print an error to the user.
                Toast.makeText(context, "Send Failed. Did you lose connection?", Toast.LENGTH_SHORT).show();
            } else {
                // If the sending is unsuccesful, print a success message to the user.
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
