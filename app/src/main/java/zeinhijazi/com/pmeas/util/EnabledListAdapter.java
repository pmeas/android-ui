package zeinhijazi.com.pmeas.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import zeinhijazi.com.pmeas.R;
import zeinhijazi.com.pmeas.effects.Effect;
import zeinhijazi.com.pmeas.effects.EffectsDefaults;

/**
 * The custom list adapter necessary to outline how the items in the ListView will look like when
 * a new item is created.
 *
 * Created by zhijazi on 3/11/17.
 */
public class EnabledListAdapter extends ArrayAdapter<Effect> {

    // Needed to identify what Activity the created Views are associated with.
    private final Context context;

    // An internal list of the effects enabled by the user.
    private ArrayList<Effect> enabledEffects;

    public EnabledListAdapter(Context context, ArrayList<Effect> effects) {
        super(context, -1, -1, effects);

        this.context = context;
        this.enabledEffects = effects;
    }

    /**
     * Called every time the ListView is updated via actions like adding or deleting an effect, etc.
     * Because the items are dynamically created (reliant on what parameters each {@link Effect} has,
     * we need to tell the view every time exactly what is being placed from the beginning (i.e there is
     * no foundational XML that this is built on top of)
     *
     * @param position the index of the effect that is being updated.
     * @param convertView the view from which we can access the elements of.
     * @param parent parent element of the view.
     * @return the view itself when completed with updating the list view.
     */
    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Create a new linear layout that will format all elements created vertically.
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        // The effect data that we are updating.
        final Effect effect = enabledEffects.get(position);
        EffectsDefaults.EffectDefaults[] effectParamaters = effect.getEffectParamNames();

        // We set the IDs of specific Views so that we can access their information later when converting to JSON.
        int latestId = 0;
        TextView latestIdValue = new TextView(context);
        latestIdValue.setVisibility(View.GONE);
        latestIdValue.setId(latestId++);

        // The JSON name of the effect - hidden from the user.
        TextView effectNameJSON = new TextView(context);
        effectNameJSON.setText(effect.getJsonName());
        effectNameJSON.setVisibility(View.GONE);
        effectNameJSON.setId(latestId++);


        // The RelativeLayout allows us to align the delete item with the effect name on the same row.
        RelativeLayout topBar = new RelativeLayout(context);
        topBar.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        // Create and set the settings for the delete icon.
        ImageView removeEffectBtn = new ImageView(context);
        removeEffectBtn.setLayoutParams(new RelativeLayout.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)removeEffectBtn.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.setMargins(0, 0, 20, 0);
        removeEffectBtn.setImageResource(R.drawable.ic_delete_forever_white_36dp);
        removeEffectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(effect);
            }
        });

        // Create and set the settings for the effect name.
        TextView effectName = new TextView(context);
        effectName.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        effectName.setText(effect.getDisplayName());
        effectName.setGravity(Gravity.CENTER);
        effectName.setTextSize(24);

        // Add the two elements to the relativelayout then add the relative layout to the linearlayout to have it
        // appear on the screen.
        topBar.addView(effectName);
        topBar.addView(removeEffectBtn);

        linearLayout.addView(topBar);

        linearLayout.addView(latestIdValue);
        linearLayout.addView(effectNameJSON);

        // For EACH parameter in the effect, add the respective parameters with a slider to the linearlayout.
        for (final EffectsDefaults.EffectDefaults parameter : effectParamaters) {

            // Add the parameter name to the layout.
            TextView parameterName = new TextView(context);
            parameterName.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
            parameterName.setText(parameter.getName());
            parameterName.setGravity(Gravity.CENTER_HORIZONTAL);

            // Add the hidden paramter JSON that we can access the value of.
            final TextView parameterNameJSON = new TextView(context);
            parameterNameJSON.setText(parameter.getJsonName());
            parameterNameJSON.setVisibility(View.GONE);
            parameterNameJSON.setId(latestId++);

            // Create a slider for that paramter.
            SeekBar parameterSlider = new SeekBar(context);
            parameterSlider.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));


            // Find the maximum and default values of the slider the paramter starts with when first enabled.
            int seekbarMax = 100;
            int seekbarDefProgress = 0;
            if(parameter.isComplex()) {
                seekbarMax = ((EffectsDefaults.ComplexEffectDefaults)parameter).getMax();
                seekbarDefProgress = ((EffectsDefaults.ComplexEffectDefaults)parameter).getDefaultValue();
            } else {
                seekbarMax = ((EffectsDefaults.SimpleEffectDefaults)parameter).getMax();
                seekbarDefProgress = ((EffectsDefaults.SimpleEffectDefaults)parameter).getDefaultValue();
            }

            // Set the maximum and default values just retrieved to the slider.
            parameterSlider.setMax(seekbarMax);
            parameterSlider.setProgress(seekbarDefProgress);

            // Display a number corresponding to the slider value.
            final TextView parameterSliderValue = new TextView(context);
            parameterSliderValue.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
            parameterSliderValue.setGravity(Gravity.CENTER_HORIZONTAL);

            // Clamp the values to the ranges defined in the design document.
            float clampedValue = clampSeekValues(seekbarDefProgress, parameter);

            // If the parameter has previously been adjusted, the next time this is list is being re-created use
            // the effect the user has set the parameter to.
            if(effect.getParams().containsKey(parameter.getJsonName())) {
                parameterSliderValue.setText(String.valueOf(effect.getParams().get(parameter.getJsonName())));
                parameterSlider.setProgress(effect.getUnclampedParams().get(parameter.getJsonName()));
            } else {
                parameterSliderValue.setText(String.valueOf(clampedValue));
                effect.insertParams(parameter.getJsonName(), clampedValue);
                effect.insertUnclampedParam(parameter.getJsonName(), seekbarDefProgress);
            }
            parameterSliderValue.setId(latestId++);

            // Every time the slider is adjusted, change the value of the text that corresponds to the
            // value of the slider.
            parameterSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    float clampedValue = clampSeekValues(progress, parameter);
                    parameterSliderValue.setText(String.valueOf(clampedValue));
                    effect.insertParams(parameter.getJsonName(), clampedValue);
                    effect.insertUnclampedParam(parameter.getJsonName(), progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            // Add all these elements to our linearlayout to be able to see them on the screen.
            linearLayout.addView(parameterName);
            linearLayout.addView(parameterNameJSON);
            linearLayout.addView(parameterSlider);
            linearLayout.addView(parameterSliderValue);
        }

        latestIdValue.setText("" + latestId);
        return linearLayout;
    }

    /**
     * Format the text that corresponds with the slide rvalue so that it is within the range
     * defined by the design document.
     *
     * @param progress What the new value of the slider is.
     * @param effectParam The information about the parameter that we can extract set values out of.
     * @return the adjusted value of the seekbar to be within the aforementioned ranges.
     */
    private float clampSeekValues(int progress, EffectsDefaults.EffectDefaults effectParam) {
        int seekBarMin = 0;
        if(effectParam.isComplex()) {
            seekBarMin = ((EffectsDefaults.ComplexEffectDefaults)effectParam).getMin();
        } else {
            seekBarMin = ((EffectsDefaults.SimpleEffectDefaults)effectParam).getMin();

        }

        // The divide factor allows us to keep the within the range, as the seekbar goes from 0 - paramterMax.
        return ((float)(progress + seekBarMin))/effectParam.getDivideFactor();
    }
}
