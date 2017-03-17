package zeinhijazi.com.pmeas.effects;

import java.util.ArrayList;

/**
 * Created by zhijazi on 3/11/17.
 */

public class Effect {

    private String effectName;
    private ArrayList<String> parameters;
    private ArrayList<Float> values;

    public Effect() {
        effectName = "";
        parameters = new ArrayList<>();
        values = new ArrayList<>();
    }

    public Effect(String effectName, ArrayList<String> parameters, ArrayList<Float> values) {
        this.effectName = effectName;
        this.parameters = parameters;
        this.values = values;
    }

    public String getEffectName() {
        return effectName;
    }

    public void setEffectName(String effectName) {
        this.effectName = effectName;
    }

    public ArrayList<String> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<String> parameters) {
        this.parameters = parameters;
    }

    public ArrayList<Float> getValues() {
        return values;
    }

    public void setValues(ArrayList<Float> values) {
        this.values = values;
    }
}
