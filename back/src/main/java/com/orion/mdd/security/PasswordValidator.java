package com.orion.mdd.security;

import java.util.ArrayList;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * PasswordValidator used to validate password at account creation and update
 */
public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Override
    public void initialize(Password constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        ArrayList<String> charList=new ArrayList<String>();
        charList.add("abcdefghijklmnopqrstuvwxyzéèêàûüç");
        charList.add("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        charList.add("0123456789");
        charList.add("&#{}()<>[]-|_@=+$£%µ*,?;.:/!\\");
    
        Boolean forbidden = true;
        Boolean foundMaj = false;
        Boolean foundMin = false;
        Boolean foundNb = false;
        Boolean foundSpecial = false;
        Integer i = 0;

        String string = password;
        if (string.length()>0) {
            while(i<string.length() && forbidden){
                if (charList.get(0).indexOf(string.charAt(i))>=0) {foundMin=true;}
                if (charList.get(1).indexOf(string.charAt(i))>=0) {foundMaj=true;}
                if (charList.get(2).indexOf(string.charAt(i))>=0) {foundNb=true;}
                if (charList.get(3).indexOf(string.charAt(i))>=0) {foundSpecial=true;}
                if(foundMin && foundMaj && foundNb && foundSpecial){
                    forbidden= false;
                }
                i++;
            }
        }
        return !forbidden;
    }
}