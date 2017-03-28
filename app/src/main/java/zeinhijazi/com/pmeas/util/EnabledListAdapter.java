package zeinhijazi.com.pmeas.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
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

    private RelativeLayout relativeLayout;
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

            relativeLayout = (RelativeLayout)view.findViewById(R.id.layout_id);

            Effect currentEffect = enabledEffects.get(position);

            TextView effectName = (TextView)view.findViewById(R.id.enabled_effect_name);
            Collection<String> params = currentEffect.getParamNames();
            effectName.setText(currentEffect.getDisplayName());

            for(String param: params) {
                TextView paramName = new TextView(context);
                paramName.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                paramName.setText(param);
                relativeLayout.addView(paramName);
            }



//            Button btnTag = new Button(context);
//            btnTag.setText("Lmao");
//            relativeLayout.addView(btnTag);

        }

        return view;
    }
}
