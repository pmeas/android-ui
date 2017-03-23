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
    private Map<String, ?> params = new HashMap<>();
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
    public Collection<String> getParamNames()
    {
        return params.keySet();
    }

    int getSimpleParam(String paramJsonName)
        throws IllegalStateException
    {
        if(isComplex(type, paramJsonName)) {
            throw new IllegalStateException(type.displayName+" is a complex type");
        }
        else {
            return (Integer) params.get(paramJsonName);
        }
    }

    Collection<Integer> getComplexParam(String paramJsonName)
        throws IllegalStateException
    {
        if(isComplex(type, paramJsonName)) {
            return (Collection<Integer>) params.get(paramJsonName);
        } else {
            throw new IllegalStateException(type.displayName+" is a simple type");
        }
    }
}
