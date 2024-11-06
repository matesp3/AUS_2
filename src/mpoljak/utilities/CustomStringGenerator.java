package mpoljak.utilities;

import java.util.Random;

public class CustomStringGenerator {

    private String customCharSet = "abcdefghijklmnopqrstuvwxyz0123456789"; // set of chars entering generation of string

    /**
     * @return set of chars entering generation of string. Example: customCharSet = "abcd1234". This means that string
     * value can be generated only from this set of chars.
     */
    public String getCustomCharSet() {
        return this.customCharSet;
    }

    /**
     * Sets new set of chars entering generation of string. Example: customCharSet = "abcd1234". This means that string
     * value can be generated only from this set of chars.
     *
     * @param customCharSet not empty set of chars
     */
    public void setCustomCharSet(String customCharSet) {
        if (customCharSet != null && !customCharSet.isEmpty())
            this.customCharSet = customCharSet;
    }

    /**
     * Generates string of given length by using generator
     * @param length    number of positions in generated string
     * @param generator generator of integer values with some seed
     * @return generated string
     */
    public String nextString(int length, Random generator) {
        StringBuilder sb = new StringBuilder(length);
        int len = this.customCharSet.length();
        for (int i = 0; i < length; i++) {
            sb.append(this.customCharSet.charAt(generator.nextInt(len)));
        }
        return sb.toString();
    }
}
