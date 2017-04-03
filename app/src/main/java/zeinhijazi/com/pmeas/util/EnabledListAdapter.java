package zeinhijazi.com.pmeas.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

import zeinhijazi.com.pmeas.R;
import zeinhijazi.com.pmeas.effects.Effect;

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

            Effect currentEffect = enabledEffects.get(position);
            Collection<String> params = currentEffect.getParamNames();

            TextView effectName = (TextView)view.findViewById(R.id.enabled_effect_name);
            effectName.setText(currentEffect.getDisplayName());



            //TODO: Make only one variable and just change the assignment into different ViewbyIds
            switch (params.size()) {
                case 4:
                    TextView fourthParam = (TextView)view.findViewById(R.id.fourthParamName);
                    fourthParam.setText(params.get(3));
                    fourthParam.setVisibility(View.VISIBLE);

                    SeekBar fourthBar = (SeekBar)view.findViewById(R.id.fourthParamSlider);
                    fourthBar.setVisibility(View.VISIBLE);
                case 3:
                    TextView thirdParam = (TextView)view.findViewById(R.id.thirdParamName);
                    thirdParam.setText(params.get(2));
                    thirdParam.setVisibility(View.VISIBLE);

                    SeekBar thirdBar = (SeekBar)view.findViewById(R.id.thirdParamSlider);
                    thirdBar.setVisibility(View.VISIBLE);
                case 2:
                    // do stuff
                    TextView secondParam = (TextView)view.findViewById(R.id.secondParamName);
                    secondParam.setText(params.get(1));
                    secondParam.setVisibility(View.VISIBLE);

                    SeekBar secondBar = (SeekBar)view.findViewById(R.id.secondParamSlider);
                    secondBar.setVisibility(View.VISIBLE);
                case 1:
                    // do stuff
                    TextView firstParam = (TextView)view.findViewById(R.id.firstParamName);
                    firstParam.setText(params.get(0));
                    firstParam.setVisibility(View.VISIBLE);

                    SeekBar firstBar = (SeekBar)view.findViewById(R.id.firstParamSlider);
                    firstBar.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }



        }

        return view;
    }
}
