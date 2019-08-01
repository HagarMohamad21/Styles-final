package styles.zonetech.net.styles.Helpers;

import android.text.Editable;

public class TextPatternFormatter {


    public boolean isInputCorrect(Editable s, int totalSymbols, int dividerModulo, char divider) {
        boolean isCorrect = s.length() <= totalSymbols; // check size of entered string
        for (int i = 0; i < s.length(); i++) { // check that every element is right
            if (i > 0 && (i + 1) % dividerModulo == 0) {
                isCorrect &= divider == s.charAt(i);
            } else {
                isCorrect &= Character.isDigit(s.charAt(i));
            }
        }
        return isCorrect;
    }

    public String buildCorrectString(char[] digits, int dividerPosition, char divider) {
        final StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < digits.length; i++) {
            if (digits[i] != 0) {
                formatted.append(digits[i]);
                if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                    formatted.append(divider);
                }
            }
        }

        return formatted.toString();
    }

    public char[] getDigitArray(final Editable s, final int size) {
        char[] digits = new char[size];
        int index = 0;
        for (int i = 0; i < s.length() && index < size; i++) {
            char current = s.charAt(i);
            if (Character.isDigit(current)) {
                digits[index] = current;
                index++;
            }
        }
        return digits;
    }

    public boolean isInputCorrect(Editable s, int totalSymbols) {
        boolean isCorrect = s.length() <= totalSymbols; // check size of entered string 4
        for (int i = 0; i < s.length(); i++) { // check that every element is right
            isCorrect &= Character.isDigit(s.charAt(i));
        }
        return isCorrect;
    }

    public String buildCorrectString(char[] digits) {
        final StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < digits.length; i++) {
            if (digits[i] != 0) {
                formatted.append(digits[i]);
            }
        }

        return formatted.toString();
    }



}
