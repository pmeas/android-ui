package zeinhijazi.com.pmeas.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
        super(context, R.layout.enabled_listview, effects);

        this.context = context;
        this.enabledEffects = effects;

        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if(view == null) {
            view = layoutInflater.inflate(R.layout.enabled_listview, parent, false);

            LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.layout_id);

            Effect currentEffect = enabledEffects.get(position);
            EffectsDefaults.EffectDefaults[] params = currentEffect.getEffectParamNames();

            int latestId = 0;

            TextView latestIdValue = new TextView(context);
            latestIdValue.setVisibility(View.GONE);
            latestIdValue.setId(latestId++);

            TextView effectName = (TextView)view.findViewById(R.id.enabled_effect_name);
            effectName.setText(currentEffect.getDisplayName());

            TextView effectJsonName = new TextView(context);
            effectJsonName.setText(currentEffect.getJsonName()); //TODO: This should be getDisplayName() but changed to json name for easy of use for now.
            effectJsonName.setId(latestId++);

            // TODO: Create separate layout parameters with actual parameters; i.e center seekbar + text, etc.
            for(final EffectsDefaults.EffectDefaults effectParam: params) {

                TextView paramaterName = new TextView(context);
                paramaterName.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                TextView displayParamName = new TextView(context);
                displayParamName.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                displayParamName.setText(effectParam.getName());

                paramaterName.setText(effectParam.getJsonName());
                paramaterName.setId(latestId++);



                SeekBar parameterSlider = new SeekBar(context);
                parameterSlider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                final TextView sliderValue = new TextView(context);
                sliderValue.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                sliderValue.setId(latestId++);

                int seekbarMax = 100;
                int seekbarDefProgress = 0;
                if(effectParam.isComplex()) {
                    seekbarMax = ((EffectsDefaults.ComplexEffectDefaults)effectParam).getMax();
                    seekbarDefProgress = ((EffectsDefaults.ComplexEffectDefaults)effectParam).getDefaultValue();
                } else {
                    seekbarMax = ((EffectsDefaults.SimpleEffectDefaults)effectParam).getMax();
                    seekbarDefProgress = ((EffectsDefaults.SimpleEffectDefaults)effectParam).getDefaultValue();

                }

                parameterSlider.setMax(seekbarMax);
                parameterSlider.setProgress(seekbarDefProgress);

                parameterSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        int seekBarMin = 0;
                        if(effectParam.isComplex()) {
                            seekBarMin = ((EffectsDefaults.ComplexEffectDefaults)effectParam).getMin();
                        } else {
                            seekBarMin = ((EffectsDefaults.SimpleEffectDefaults)effectParam).getMin();

                        }
                        float clampedProgress = ((float)(progress + seekBarMin))/effectParam.getDivideFactor();
                        sliderValue.setText(String.valueOf(clampedProgress));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                linearLayout.addView(displayParamName);
                linearLayout.addView(parameterSlider);
                linearLayout.addView(sliderValue);

            }


            latestIdValue.setText(String.valueOf(latestId));
            linearLayout.addView(latestIdValue);
        }

        return view;
    }
}
