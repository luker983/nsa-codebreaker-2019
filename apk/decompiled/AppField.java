package com.badguy.terrortime;

abstract class AppField
{
    private AppFieldTypes type;
    
    public AppField(final AppFieldTypes type) {
        this.type = type;
    }
    
    @Override
    public abstract boolean equals(final Object p0);
    
    public abstract Object getValue();
    
    public abstract boolean isDefaultValue();
    
    public abstract boolean isValid();
    
    public abstract void setValueToDefault();
    
    @Override
    public abstract String toString();
    
    public abstract String typeAsString();
}
