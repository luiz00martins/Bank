package Bank;

import java.io.Serializable;

public class Agency implements Serializable {
    private String agency;

    public Agency(String str) {
        String[] parts = str.split("-");

        // Checking format
        if (parts.length != 2 || parts[0].length() != 4 || parts[1].length() != 1)
            throw new IllegalArgumentException("Five numbers required for ID");

        // Checking if they are all numbers
        for (int i = 0; i < parts[0].length(); i++) {
            if (!Character.isDigit(parts[0].charAt(i)))
                throw new IllegalArgumentException("Only digits are accepted in ID");
        }
        if (!Character.isDigit(parts[1].charAt(0)))
            throw new IllegalArgumentException("Only digits are accepted in ID");

        agency = str;
    }

    public String toString(){
        return agency;
    }
}
