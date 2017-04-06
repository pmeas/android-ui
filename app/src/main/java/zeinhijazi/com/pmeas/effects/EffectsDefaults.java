package zeinhijazi.com.pmeas.effects;

/**
 * Created by matt on 3/23/17.
 */

public class EffectsDefaults {
    public static abstract class EffectDefaults
    {
        final String name;
        final String jsonName;
        final boolean isComplex;

        protected EffectDefaults(String name, String jsonName, boolean isComplex)
        {
            this.name = name;
            this.jsonName = jsonName;
            this.isComplex = isComplex;
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
    }
    public static class SimpleEffectDefaults
            extends EffectDefaults
    {
        int min, max, defaultValue;

        public SimpleEffectDefaults(String name, String jsonName, int min, int max, int defaultValue) {
            super(name, jsonName, false);
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
    public static class ComplexEffectDefaults
            extends EffectDefaults
    {
        int min, max, defaultValue;

        public ComplexEffectDefaults(String name, String jsonName, int min, int max, int defaultValue) {
            super(name, jsonName, true);
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
    public static enum EffectTypes {
        CHORUS("Chorus", "chorus",
                new SimpleEffectDefaults("Balance", "balance",
                        0, 100, 50),
                new SimpleEffectDefaults("Feedback", "feedback",
                        0, 100, 50),
                new ComplexEffectDefaults("Depth", "depth",
                        0, 500, 250)),
        DELAY("Delay", "delay",
                new SimpleEffectDefaults("Feedback", "feedback",
                        0, 100, 50),
                new ComplexEffectDefaults("Delay Time (ms)", "delay",
                        0, 100, 50)),
        DISTORTION("Distortion", "distortion",
                new SimpleEffectDefaults("Balance", "balance",
                        0, 100, 50),
                new SimpleEffectDefaults("Tone", "tone",
                        0, 100, 50)),
        FLANGER(Math.random() > 0.05? "Flanger":"Flanders", "flanger",
                new SimpleEffectDefaults("Depth", "depth",
                        0, 100, 0),
                new SimpleEffectDefaults("Frequency", "freq",
                        0, 100, 0),
                new SimpleEffectDefaults("Feedback", "feedback",
                        0, 100, 0)),
        FREQUENCY_SHIFT("Frequency Shift", "freqshift",
                new ComplexEffectDefaults("Shift", "shift",
                        0, Integer.MAX_VALUE, 0)),
        HARMONIZER("Harmonizer", "harmonizer",
                new SimpleEffectDefaults("Feedback", "feedback",
                        0, 100, 50),
                new ComplexEffectDefaults("Shift", "transpose",
                        -20, 20, 0)),
        PHASER("Phaser", "phaser",
                new SimpleEffectDefaults("Frequency", "frequency",
                        0, Integer.MAX_VALUE, 0),
                new SimpleEffectDefaults("Spread", "spread",
                        0, Integer.MAX_VALUE, 0),
                new SimpleEffectDefaults("Q-Factor", "q",
                        0, Integer.MAX_VALUE, 0),
                new SimpleEffectDefaults("Feedback", "feedback",
                        0, 100, 0),
                new SimpleEffectDefaults("Num", "num",
                        0, Integer.MAX_VALUE, 0)),
        REVERB("Reverb", "reverb",
                new SimpleEffectDefaults("Balance", "balance",
                        0, 100, 50),
                new SimpleEffectDefaults("Tone", "cutoff",
                        0, Integer.MAX_VALUE, 0),
                new SimpleEffectDefaults("Delay", "revtime",
                        0, Integer.MAX_VALUE, 0),
                new SimpleEffectDefaults("Room Size", "roomsize",
                        25, 400, 50));

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
