package zeinhijazi.com.pmeas.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import zeinhijazi.com.pmeas.R;
import zeinhijazi.com.pmeas.util.Bridge;

/**
 * This class allows the user to change the ports to listen on in case a new instrument is plugged in.
 */
public class SettingsActivity extends AppCompatActivity {

    // A list of the input and output ports received from the modulation application.
    ArrayList<String> inputPorts;
    ArrayList<String> outputPorts;

    // Dropdown menus that contain the lists for the playback and capture ports.
    Spinner inSpinner;
    Spinner outSpinner;

    // Adapters for the dropdown items to determine what happens when an item is clicked.
    ArrayAdapter<String> inAdapter;
    ArrayAdapter<String> outAdapter;

    /**
     * Callback that gets called when a new activity is created. This can be seen as a "replacement"
     * for constructors in an Activity.
     *
     * @param savedInstanceState Used if the state of the application was previously saved.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Create the "Back" icon next to the ActionBar heading to allow the user to go back a page.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_navigate_before_white_48dp);

        inputPorts = new ArrayList<>();
        outputPorts = new ArrayList<>();

        inSpinner = (Spinner)findViewById(R.id.inportsSpinner);
        outSpinner = (Spinner)findViewById(R.id.outputsSpinner);

        // Create how the dropdown menus will look like.
        inAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner, inputPorts);
        inAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        inSpinner.setAdapter(inAdapter);

        outAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner, outputPorts);
        outAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        outSpinner.setAdapter(outAdapter);


        // Try to acquire the ports from the modulation application to allow us to occupy the
        // dropdown menus.
        try {
            JSONObject requestEffects = new JSONObject();
            requestEffects.put("intent", "REQPORT");
            new BridgeAsync(this).execute(requestEffects.toString());
        } catch(JSONException e) {
            Log.e("JSON", "Json exception: " + e.getMessage());
        }

    }

    /**
     * Sends the updated selected ports the user wants to playback and capture audio on to the
     * modulation application.
     *
     * @param view The callback item when a button is clicked (as created from the XML format for the settings
     *             activity)
     */
    public void sendPorts(View view) {
        try {
            JSONObject selectedPorts = new JSONObject();
            selectedPorts.put("intent", "UPDATEPORT");

            String selectedCapture = inSpinner.getSelectedItem().toString();
            String selectedPlayback = outSpinner.getSelectedItem().toString();
            selectedPorts.put("in", selectedCapture);
            selectedPorts.put("out", selectedPlayback);

            new BridgeAsync(this, true).execute(selectedPorts.toString());
        } catch (JSONException e) {
            Log.e("JSON", "Json exception: " + e.getMessage());
        }
    }

    /**
     * Helper class for the Activity that allows networking to the modulation application to happen
     * in a separate thread.
     */
    private class BridgeAsync extends AsyncTask<String, Void, String> {

        Context context;

        boolean updatePorts = false;

        BridgeAsync(Context context) {
            this.context = context;
        }

        /** Separate constructor allows us to differentiate between whether we are retrieving ports
         * or updating the ports.
         *
         * @param context Used to show text to the user as a {@link Toast}
         * @param updatePorts
         */
        BridgeAsync(Context context, boolean updatePorts) {
            this.context = context;
            this.updatePorts = updatePorts;
        }

        /** As you may have suspected, this is the code that gets run in the background of the task.
         */
        @Override
        protected String doInBackground(String... params) {
            String message = params[0];
            String result = null;

            // Send the message to the modulation application and get the results.
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

        @Override
        protected void onPostExecute(String result) {
            // If the communications failed, print an error message to the user.
            if(result == null) {
                Toast.makeText(context, "Send Failed. Did you lose connection?", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                try {
                    // If we are just updating the ports, show the result of the message.
                    if(updatePorts) {
                        Toast.makeText(context, "Updated Ports", Toast.LENGTH_SHORT).show();
                        return; // TODO: See why this was not in earlier code.
                    }


                    // Retrieve the ports from the REQPORTS intention message.
                    JSONObject ports = new JSONObject(result);

                    // Acquire the input ports data
                    JSONArray portsData = (JSONArray)ports.get("input");
                    if(portsData != null) {
                        for(int i = 0; i < portsData.length(); i++) {
                            inputPorts.add(portsData.getString(i));
                        }
                    }
                    inAdapter.notifyDataSetChanged();

                    // Acquire the output ports data
                    portsData = (JSONArray)ports.get("output");
                    if(portsData != null) {
                        for(int i = 0; i < portsData.length(); i++) {
                            outputPorts.add(portsData.getString(i));
                        }
                    }
                    outAdapter.notifyDataSetChanged();

                } catch(JSONException e) {
                    Log.e("BRIDGE", "Caught IOException on doInBg: " + e.getMessage());
                }

            }
        }
    }
}
