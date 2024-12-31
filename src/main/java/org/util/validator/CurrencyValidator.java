package org.util.validator;

public class CurrencyValidator {
    public  String validate(String name, String code, String sign){
        if (name == null || !name.matches("^[A-Za-z]{1,20}$")) {
            return "Name must contain only Latin letters and be between 1 and 20 characters long.";
        }

        if (code == null || !code.matches("^[A-Z]{3}$")) {
            return "Code must consist of exactly 3 uppercase Latin letters.";
        }

        if (sign == null || !sign.matches("^.$")) {
            return "Sign must be exactly 1 character.";
        }
        return null;



    }
}
