package Bank;

import java.io.Serializable;

public class ID implements Serializable {
    private String id;

    public ID(String str) {
        String[] parts = str.split("-");

        // Checking format
        if (parts.length != 2 || parts[0].length() != 5 || parts[1].length() != 1)
            throw new IllegalArgumentException("Six numbers required for ID");

        // Checking if they are all numbers
        for (int i = 0; i < parts[0].length(); i++) {
            if (!Character.isDigit(parts[0].charAt(i)))
                throw new IllegalArgumentException("Only digits are accepted in ID");
        }
        if (!Character.isDigit(parts[1].charAt(0)))
            throw new IllegalArgumentException("Only digits are accepted in ID");

        id = str;
    }

    public String toString(){
        return id;
    }

    public boolean equals(ID comp) {
        return id.equals(comp.id);
    }
}
