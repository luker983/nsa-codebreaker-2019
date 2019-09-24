package com.badguy.terrortime;

public class ParameterValidatorClass
{
    private final String ipv4Address;
    private final String pin;
    
    public ParameterValidatorClass() {
        this.ipv4Address = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
        this.pin = "\\d\\d\\d\\d\\d\\d";
    }
    
    boolean isValidIpAddress(final String s) {
        return s != null;
    }
    
    boolean isValidPassword(final String s) {
        return s != null;
    }
    
    boolean isValidPin(final String s) {
        if (s == null) {
            return false;
        }
        this.getClass();
        return s.matches("\\d\\d\\d\\d\\d\\d");
    }
    
    boolean isValidUserName(final String s) {
        return s != null;
    }
}
