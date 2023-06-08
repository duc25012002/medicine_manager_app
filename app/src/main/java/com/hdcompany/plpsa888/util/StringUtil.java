package com.hdcompany.plpsa888.util;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

public final class StringUtil {

    public static boolean isEmpty(String input) {
        return input == null || input.isEmpty() || ("").equals(input.trim());
    }

    @NonNull
    public static String getDottedNumber(String str) {
        int count = 0;
        StringBuilder strb = new StringBuilder(str);
        for (int i = str.length() - 1; i > -1; i--) {
            if (count == 3) {
                if (  str.charAt(0) != '-' ||  i == 1 && str.charAt(0) != '-' ) {
                    strb.insert(i + 1, ".");
                    count = 0;
                }
            }
            count++;
        }
        return strb.toString();
    }

    @NonNull
    @Contract(pure = true)
    public static String getDoubleNumber(int number) {
        if (number < 10) {
            return "0" + number;
        } else return "" + number;
    }

    @NonNull
    private static String reverseString(String str) {
        StringBuilder sb = new StringBuilder(str);
        sb.reverse();
        return sb.toString();
    }

    @NonNull
    public static String getSum(@NonNull String term1, @NonNull String term2) {
        if (term1.contains("-")) {
            term1 = term1.replace("-", "");
            return getDifference(term1, term2);
        }
        if (term2.contains("-")) {
            term2 = term2.replace("-", "");
            return getDifference(term2, term1);
        }
        StringBuilder result = new StringBuilder();
        int size1 = term1.length();
        int size2 = term2.length();
        int maxSize = size1 + size2;
        term1 = reverseString(term1);
        term2 = reverseString(term2);

        if (size1 < maxSize) {
            StringBuilder term1Builder = new StringBuilder(term1);
            for (int i = maxSize; i >= size1; i--) {
                term1Builder.append("0");
            }
            term1 = term1Builder.toString();
        }
        if (size2 < maxSize) {
            StringBuilder term2Builder = new StringBuilder(term2);
            for (int i = maxSize; i >= size2; i--) {
                term2Builder.append("0");
            }
            term2 = term2Builder.toString();
        }

        int carry = 0; // Số dư
        for (int i = 0; i < maxSize; i++) {
            int sum = 0;
            sum = term1.charAt(i) - '0' + term2.charAt(i) - '0' + carry;
            result.append(sum % 10);
            carry = sum / 10;
        }
        if (carry == 1) {
            result.append(1);
        }

        // Reverse String again
        result = new StringBuilder(reverseString(result.toString()));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < maxSize; i++) {
            if (i == 0 && result.charAt(i) == '0') {
                result.deleteCharAt(i);
                i--;
                maxSize--;
                continue;
            }
            sb.append(result.charAt(i));
        }
        return sb.length() == 0 ? "0" : sb.toString().replaceAll("^0", "").replaceAll("^0", "").replaceAll("^0", "").replaceAll("^0", "").replaceAll("^0", "").replaceAll("^0", "").replaceAll("^0", "").replaceAll("^0", "");
    }

    //
    @NonNull
    public static String getMultiple(@NonNull String factor1, @NonNull String factor2) {
        int[] result = new int[factor1.length() + factor2.length()];
        for (int i = factor1.length() - 1; i >= 0; i--) {
            for (int j = factor2.length() - 1; j >= 0; j--) {
                int mul = (factor1.charAt(i) - '0') * (factor2.charAt(j) - '0');
                int sum = mul + result[i + j + 1];
                result[i + j] += sum / 10;
                result[i + j + 1] = sum % 10;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.length; i++) {
            if (i == 0 && result[i] == 0) {
                continue;
            }
            sb.append(result[i]);
        }
        return sb.length() == 0 ? "0" : sb.toString().replaceAll("^0", "").replaceAll("^0", "").replaceAll("^0", "").replaceAll("^0", "").replaceAll("^0", "").replaceAll("^0", "").replaceAll("^0", "").replaceAll("^0", "");
    }

    // Returns true if deductible is smaller than subtractive.
    public static boolean isSmaller(@NonNull String deductible, @NonNull String subtractive) {
        // Calculate lengths of both string
        deductible = deductible.replace("-", "");
        subtractive = subtractive.replace("-", "");

        int n1 = deductible.length(), n2 = subtractive.length();
        if (n1 < n2)
            return true;
        if (n2 < n1)
            return false;

        for (int i = 0; i < n1; i++)
            if (deductible.charAt(i) < subtractive.charAt(i))
                return true;
            else if (deductible.charAt(i) > subtractive.charAt(i))
                return false;

        return false;
    }

    // Function for find difference of larger numbers
    @NonNull
    public static String getDifference(String deductible, String subtractive) {

        if(deductible.contains("-") && subtractive.contains("-")){
            deductible = deductible.replace("-", "");
            subtractive = subtractive.replace("-", "");
            return "-" + getSum(deductible,subtractive);
        }

        if (deductible.contains("-")) {
            deductible = deductible.replace("-", "");
        }
        if (subtractive.contains("-")) {
            subtractive = subtractive.replace("-", "");
        }

        String sign = "";
        // Before proceeding further, make sure deductible
        // is not smaller
        if (isSmaller(deductible, subtractive)) {
            String t = deductible;
            deductible = subtractive;
            subtractive = t;
            sign = "-";
        }

        // Take an empty string for storing result
        StringBuilder result = new StringBuilder();

        // Calculate length of both string
        int n1 = deductible.length(), n2 = subtractive.length();

        // Reverse both of strings
        deductible = reverseString(deductible);
        subtractive = reverseString(subtractive);

        int carry = 0; // Mượn

        // Run loop till small string length
        // and subtract digit of deductible to subtractive
        for (int i = 0; i < n2; i++) {
            // Do school mathematics, compute difference of
            // current digits
            int sub = (deductible.charAt(i) - '0' - (subtractive.charAt(i) - '0') - carry);

            // If subtraction is less than zero
            // we add then we add 10 into sub and
            // take carry as 1 for calculating next step
            if (sub < 0) {
                sub = sub + 10;
                carry = 1;
            } else {
                carry = 0;
            }
            result.append((char) (sub + '0'));
        }

        // subtract remaining digits of larger number
        for (int i = n2; i < n1; i++) {
            int sub = (deductible.charAt(i) - '0' - carry);

            // if the sub value is -ve, then make it
            // positive
            if (sub < 0) {
                sub = sub + 10;
                carry = 1;
            } else
                carry = 0;

            result.append((char) (sub + '0'));
        }

        // reverse resultant string
        return sign + reverseString(result.toString()).replaceAll("^0", "").replaceAll("^0", "").replaceAll("^0", "").replaceAll("^0", "").replaceAll("^0", "").replaceAll("^0", "").replaceAll("^0", "").replaceAll("^0", "");
    }

    @NonNull
    public static String getDivision(@NonNull String divided_number, int divisor) {

        // As result can be very
        // large store it in string
        // but since we need to modify
        // it very often so using
        // string builder
        StringBuilder result = new StringBuilder();

        // We will be iterating
        // the dividend so converting
        // it to char array
        char[] dividend = divided_number.toCharArray();

        // Initially the carry
        // would be zero
        int carry = 0;

        // Iterate the dividend
        for (char c : dividend) {
            // Prepare the number to
            // be divided
            int x = carry * 10 + Character.getNumericValue(c);

            // Append the result with
            // partial quotient
            result.append(x / divisor);

            // Prepare the carry for
            // the next Iteration
            carry = x % divisor;
        }

        // Remove any leading zeros
        for (int i = 0; i < result.length(); i++) {
            if (result.charAt(i) != '0') {
                // Return the result
                /*
                THE RESULT NOT ACCURATE CAUSE AFTER DECIMAL
                 */
                return result.substring(i).replaceAll("^0", "").replaceAll("^0", "").replaceAll("^0", "");
            }
        }
        // Return empty string
        // if number is empty
        return "";
    }
}
