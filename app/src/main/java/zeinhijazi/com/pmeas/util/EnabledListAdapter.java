package zeinhijazi.com.pmeas.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

import zeinhijazi.com.pmeas.R;
import zeinhijazi.com.pmeas.effects.Effect;
import zeinhijazi.com.pmeas.effects.EffectsDefaults;

/**
 * Created by zhijazi on 3/11/17.
 */

public class EnabledListAdapter extends ArrayAdapter<Effect> {

    private final Context context;

    private ArrayList<Effect> enabledEffects;

    private LayoutInflater layoutInflater;

    public EnabledListAdapter(Context context, ArrayList<Effect> effects) {
        super(context, -1, -1, effects);

        this.context = context;
        this.enabledEffects = effects;

        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private static class ViewHolder {
        public LinearLayout linearLayout;
        public Button removeBtn;
        public TextView effectName;
        public boolean paramsMade = false;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);


        final Effect effect = enabledEffects.get(position);
        EffectsDefaults.EffectDefaults[] effectParamaters = effect.getEffectParamNames();

        System.out.println("GETVIEW CALLED FOR EFFECTS: " + effect.getDisplayName());

        int latestId = 0;
        TextView latestIdValue = new TextView(context);
        latestIdValue.setVisibility(View.GONE);
        latestIdValue.setId(latestId++);

        TextView effectNameJSON = new TextView(context);
        effectNameJSON.setText(effect.getJsonName());
        effectNameJSON.setVisibility(View.GONE);
        effectNameJSON.setId(latestId++);

        Button removeEffectBtn = new Button(context);
        removeEffectBtn.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
        removeEffectBtn.setText("del");
        removeEffectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(effect);
//                notifyDataSetChanged();
            }
        });

        TextView effectName = new TextView(context);
        effectName.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
        effectName.setText(effect.getDisplayName());

        linearLayout.addView(removeEffectBtn);
        linearLayout.addView(effectName);

        linearLayout.addView(latestIdValue);
        linearLayout.addView(effectNameJSON);

        for (final EffectsDefaults.EffectDefaults parameter : effectParamaters) {

            TextView parameterName = new TextView(context);
            parameterName.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
            parameterName.setText(parameter.getName());

            final TextView parameterNameJSON = new TextView(context);
            parameterNameJSON.setText(parameter.getJsonName());
            parameterNameJSON.setVisibility(View.GONE);
            parameterNameJSON.setId(latestId++);

            SeekBar parameterSlider = new SeekBar(context);
            parameterSlider.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));

            int seekbarMax = 100;
            int seekbarDefProgress = 0;
            if(parameter.isComplex()) {
                seekbarMax = ((EffectsDefaults.ComplexEffectDefaults)parameter).getMax();
                seekbarDefProgress = ((EffectsDefaults.ComplexEffectDefaults)parameter).getDefaultValue();
            } else {
                seekbarMax = ((EffectsDefaults.SimpleEffectDefaults)parameter).getMax();
                seekbarDefProgress = ((EffectsDefaults.SimpleEffectDefaults)parameter).getDefaultValue();
            }

            parameterSlider.setMax(seekbarMax);
            parameterSlider.setProgress(seekbarDefProgress);

            final TextView parameterSliderValue = new TextView(context);
            parameterSliderValue.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
            float clampedValue = clampSeekValues(seekbarDefProgress, parameter);
            if(effect.getParams().containsKey(parameter.getJsonName())) {
                parameterSliderValue.setText(String.valueOf(effect.getParams().get(parameter.getJsonName())));
            } else {
                parameterSliderValue.setText(String.valueOf(clampedValue));
                effect.insertParams(parameter.getJsonName(), clampedValue);
            }
            parameterSliderValue.setId(latestId++);

            parameterSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    float clampedValue = clampSeekValues(progress, parameter);
                    parameterSliderValue.setText(String.valueOf(clampedValue));
                    effect.insertParams(parameter.getJsonName(), clampedValue);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });




            linearLayout.addView(parameterName);
            linearLayout.addView(parameterNameJSON);
            linearLayout.addView(parameterSlider);
            linearLayout.addView(parameterSliderValue);
        }

        latestIdValue.setText("" + latestId);
        return linearLayout;
    }

    private float clampSeekValues(int progress, EffectsDefaults.EffectDefaults effectParam) {
        int seekBarMin = 0;
        if(effectParam.isComplex()) {
            seekBarMin = ((EffectsDefaults.ComplexEffectDefaults)effectParam).getMin();
        } else {
            seekBarMin = ((EffectsDefaults.SimpleEffectDefaults)effectParam).getMin();

        }
        float clampedProgress = ((float)(progress + seekBarMin))/effectParam.getDivideFactor();

        return clampedProgress;
    }
}
