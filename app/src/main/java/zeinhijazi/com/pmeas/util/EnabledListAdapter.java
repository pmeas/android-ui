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

            TextView effectName = (TextView)view.findViewById(R.id.enabled_effect_name);
            effectName.setText(currentEffect.getDisplayName());

            // TODO: Create separate layout parameters with actual parameters; i.e center seekbar + text, etc.
            for(EffectsDefaults.EffectDefaults effectParam: params) {
                TextView paramaterName = new TextView(context);
                paramaterName.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                paramaterName.setText(effectParam.getName());


                SeekBar parameterSlider = new SeekBar(context);
                parameterSlider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                linearLayout.addView(paramaterName);
                linearLayout.addView(parameterSlider);

            }



        }

        return view;
    }
}
