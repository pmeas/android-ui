package zeinhijazi.com.pmeas.effects;

/**
 * Created by matt on 3/23/17.
 */

public class EffectsDefaults {
    /** Encapsulates the default values of an effect */
    public static abstract class EffectDefaults
    {
        final String name;
        final String jsonName;
        final boolean isComplex;
        final int divideFactor;

        protected EffectDefaults(String name, String jsonName, boolean isComplex, int divideFactor)
        {
            this.name = name;
            this.jsonName = jsonName;
            this.isComplex = isComplex;
            this.divideFactor = divideFactor;
        }

        public String getName() {
            return this.name;
        }

        public String getJsonName() {
            return this.jsonName;
        }

        public boolean isComplex() {
            return this.isComplex;
        }

        public int getDivideFactor() {
            return this.divideFactor;
        }
    }
    /** This is the class used for "simple" params which do not have additional "tracks". A simple param is simply an int value.*/
    public static class SimpleEffectDefaults
            extends EffectDefaults
    {
        int min, max, defaultValue;

        public SimpleEffectDefaults(String name, String jsonName, int min, int max, int defaultValue, int divideFactor) {
            super(name, jsonName, false, divideFactor);
            this.min = min;
            this.max = max;
            this.defaultValue = defaultValue;
        }

        public int getMin() {
            return this.min;
        }

        public int getMax() {
            return this.max;
        }

        public int getDefaultValue() {
            return this.defaultValue;
        }
    }
    /** Although this is structurally identical to SimpleEffectDefaults, it's semantically different.
     *  The min and max and default are shared between all complex param values.
     *  A complex effect is zero-or-more int values, each of which is subject to the same constraints.
     */
    public static class ComplexEffectDefaults
            extends EffectDefaults
    {
        int min, max, defaultValue;

        public ComplexEffectDefaults(String name, String jsonName, int min, int max, int defaultValue, int divideFactor) {
            super(name, jsonName, true, divideFactor);
            this.min = min;
            this.max = max;
            this.defaultValue = defaultValue;
        }

        public int getMin() {
            return this.min;
        }

        public int getMax() {
            return this.max;
        }

        public int getDefaultValue() {
            return this.defaultValue;
        }
    }
    /** These are the effects we have and their values. */
    public static enum EffectTypes {
        CHORUS("Chorus", "chorus",
                new SimpleEffectDefaults("Balance", "balance",
                        0, 100, 50, 100),
                new SimpleEffectDefaults("Feedback", "feedback",
                        0, 100, 50, 100),
                new ComplexEffectDefaults("Depth Min", "depth_min",
                        0, 500, 250, 100),
                new ComplexEffectDefaults("Depth Max", "depth_max",
                        0, 500, 250, 100)),
        DELAY("Delay", "delay",
                new SimpleEffectDefaults("Feedback", "feedback",
                        0, 100, 50, 100),
                new ComplexEffectDefaults("Delay Time (ms)", "delay",
                        0, 500, 50, 100)),
        DISTORTION("Distortion", "distortion",
                new SimpleEffectDefaults("Balance", "drive",
                        0, 100, 50, 100),
                new SimpleEffectDefaults("Tone", "slope",
                        0, 100, 50, 100)),
        FLANGER(Math.random() > 0.05? "Flanger":"Flanders", "flanger",
                new SimpleEffectDefaults("Depth", "depth",
                        0, 100, 0, 100),
                new SimpleEffectDefaults("Frequency", "freq",
                        0, 100, 0, 100),
                new SimpleEffectDefaults("Feedback", "feedback",
                        0, 100, 0, 100)),
        FREQUENCY_SHIFT("Frequency Shift", "freqshift",
                new ComplexEffectDefaults("Shift", "shift",
                        0, 1000, 0, 1)),
        HARMONIZER("Harmonizer", "harmonizer",
                new SimpleEffectDefaults("Feedback", "feedback",
                        0, 100, 50, 100),
                new ComplexEffectDefaults("Shift", "transpose",
                        -20, 40, 0, 1)),
        PHASER("Phaser", "phaser",
                new SimpleEffectDefaults("Frequency", "frequency",
                        0, 5000, 0, 1),
                new SimpleEffectDefaults("Spread", "spread",
                        0, 100, 11, 100),
                new SimpleEffectDefaults("Q-Factor", "q",
                        0, 50, 0, 1),
                new SimpleEffectDefaults("Feedback", "feedback",
                        0, 100, 0, 100),
                new SimpleEffectDefaults("Num", "num",
                        0, 50, 8, 1)),
        REVERB("Reverb", "reverb",
                new SimpleEffectDefaults("Balance", "balance",
                        0, 100, 50, 100),
                new SimpleEffectDefaults("Tone", "cutoff",
                        0, 35000, 3500, 1),
                new SimpleEffectDefaults("Delay", "revtime",
                        0, 1000, 0, 100),
                new SimpleEffectDefaults("Room Size", "roomsize",
                        25, 375, 50, 100));

        EffectDefaults[] defaults;
        String displayName;
        String jsonName;
        EffectTypes(String displayName, String jsonName, EffectDefaults... defaults)
        {
            this.displayName = displayName;
            this.jsonName = jsonName;
            this.defaults = defaults;
        }
    }
}
