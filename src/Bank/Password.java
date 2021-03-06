package Bank;

import java.io.Serializable;

public class Password implements Serializable {
    private String password;

    public Password(String str) {
        // Checking format
        if (str.length() != 4)
            throw new IllegalArgumentException("Five numbers required for ID");

        // Checking if they are all numbers
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i)))
                throw new IllegalArgumentException("Only digits are accepted in ID");
        }

        password = str;
    }

    public String toString(){
        return password;
    }

    public boolean equals(Password comp) {
        return password.equals(comp.password);
    }
}
