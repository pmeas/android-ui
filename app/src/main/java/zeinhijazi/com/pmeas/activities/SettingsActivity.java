package zeinhijazi.com.pmeas.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class SettingsActivity extends AppCompatActivity {

    ArrayList<String> inputPorts;
    ArrayList<String> outputPorts;

    Spinner inSpinner;
    Spinner outSpinner;

    ArrayAdapter<String> inAdapter;
    ArrayAdapter<String> outAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        inputPorts = new ArrayList<>();
        outputPorts = new ArrayList<>();

        inSpinner = (Spinner)findViewById(R.id.inportsSpinner);
        outSpinner = (Spinner)findViewById(R.id.outputsSpinner);

        inAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner, inputPorts);
        inAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        inSpinner.setAdapter(inAdapter);

        outAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner, outputPorts);
        outAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        outSpinner.setAdapter(outAdapter);


        try {
            JSONObject requestEffects = new JSONObject();
            requestEffects.put("intent", "REQPORT");
            new BridgeAsync(this).execute(requestEffects.toString());
        } catch(JSONException e) {
            Log.e("JSON", "Json exception: " + e.getMessage());
        }

    }

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

    private class BridgeAsync extends AsyncTask<String, Void, String> {

        Context context;

        boolean updatePorts = false;

        BridgeAsync(Context context) {
            this.context = context;
        }

        BridgeAsync(Context context, boolean updatePorts) {
            this.context = context;
            this.updatePorts = updatePorts;
        }

        @Override
        protected String doInBackground(String... params) {
            String message = params[0];
            String result = null;

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
            if(result == null) {
                Toast.makeText(context, "Send Failed. Did you lose connection?", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                try {
                    if(updatePorts) {
                        Toast.makeText(context, "Updated Ports", Toast.LENGTH_SHORT).show();
                    }

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
