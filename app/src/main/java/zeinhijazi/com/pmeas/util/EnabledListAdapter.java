package zeinhijazi.com.pmeas.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private EffectViewHolder viewHolder;


    public EnabledListAdapter(Context context, ArrayList<Effect> effects) {
        super(context, R.layout.enabled_listview, effects);

        this.context = context;
        this.enabledEffects = effects;

        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static class EffectViewHolder {
        Button removeBtn;
        TextView effectName;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Effect effect = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.enabled_listview, parent, false);
        }

        TextView effectName = (TextView)convertView.findViewById(R.id.enabled_effect_name);
        effectName.setText(effect.getDisplayName());

        Button removeEffectBtn = (Button)convertView.findViewById(R.id.removeEffect);
        removeEffectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(effect);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private String clampSeekValues(int progress, EffectsDefaults.EffectDefaults effectParam) {
        int seekBarMin = 0;
        if(effectParam.isComplex()) {
            seekBarMin = ((EffectsDefaults.ComplexEffectDefaults)effectParam).getMin();
        } else {
            seekBarMin = ((EffectsDefaults.SimpleEffectDefaults)effectParam).getMin();

        }
        float clampedProgress = ((float)(progress + seekBarMin))/effectParam.getDivideFactor();

        return String.valueOf(clampedProgress);
    }
}
