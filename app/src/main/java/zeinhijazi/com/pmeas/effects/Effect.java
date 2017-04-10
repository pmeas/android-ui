package zeinhijazi.com.pmeas.effects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by zhijazi on 3/11/17.
 */

public class Effect {
    private EffectsDefaults.EffectTypes type;
    private Map<String, Float> params = new HashMap<>();
    public Effect(EffectsDefaults.EffectTypes type) {
        this.type = type;
    }

    static boolean isComplex(EffectsDefaults.EffectTypes type, String paramJsonName)
        throws NoSuchElementException
    {
        for (EffectsDefaults.EffectDefaults effect : type.defaults) {
            if(effect.name.equals(paramJsonName)) {
                return effect.isComplex;
            }
        }
        throw new NoSuchElementException("Key "+paramJsonName+" does not exist in "+type.displayName+"'s list of params");
    }

    static boolean hasParam(EffectsDefaults.EffectTypes type, String paramJsonName)
    {
        for (EffectsDefaults.EffectDefaults effect : type.defaults) {
            if(effect.name.equals(paramJsonName)) {
                return true;
            }
        }
        return false;
    }

    public String getDisplayName()
    {
        return type.displayName;
    }
    public String getJsonName() {
        return type.jsonName;
    }
    public EffectsDefaults.EffectDefaults[] getEffectParamNames() { return type.defaults; }
    public Collection<String> getParamNames()
    {
        return params.keySet();
    }
    public Map<String, Float> getParams() { return params; }

    public float getSimpleParam(String paramJsonName)
        throws IllegalStateException
    {
        if(isComplex(type, paramJsonName)) {
            throw new IllegalStateException(type.displayName+" is a complex type");
        }
        else {
            return (Float) params.get(paramJsonName);
        }
    }

    // TODO: We dont seem to need this anymore...
//    Collection<Float> getComplexParam(String paramJsonName)
//        throws IllegalStateException
//    {
//        if(isComplex(type, paramJsonName)) {
//            return (Collection<Float>) params.get(paramJsonName);
//        } else {
//            throw new IllegalStateException(type.displayName+" is a simple type");
//        }
//    }

    public void insertParams(String jsonName, float value) {
        params.put(jsonName, value);
    }
}
