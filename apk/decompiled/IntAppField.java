package com.badguy.terrortime;

public class IntAppField extends AppField
{
    private static Integer DEFAULTVALUE;
    private Integer value;
    
    static {
        IntAppField.DEFAULTVALUE = 0;
    }
    
    public IntAppField() {
        super(AppFieldTypes.INT);
        this.setValueToDefault();
    }
    
    public IntAppField(final IntAppField intAppField) {
        super(AppFieldTypes.INT);
        if (intAppField != null) {
            this.value = intAppField.getValue();
        }
        else {
            this.value = IntAppField.DEFAULTVALUE;
        }
    }
    
    public IntAppField(final Integer n) {
        super(AppFieldTypes.INT);
        this.setValue((int)n);
    }
    
    @Override
    public final boolean equals(final Object o) {
        return this == o || (o != null && this.getClass() == o.getClass() && this.isValid() && IntAppField.class.cast(o).isDefaultValue() == this.isDefaultValue() && this.value.equals(IntAppField.class.cast(o).getValue()));
    }
    
    @Override
    public final Integer getValue() {
        if (this.value == null) {
            return null;
        }
        if (this.isDefaultValue()) {
            return IntAppField.DEFAULTVALUE;
        }
        return this.value;
    }
    
    @Override
    public final boolean isDefaultValue() {
        return this.isValid() && this.value.equals(IntAppField.DEFAULTVALUE);
    }
    
    @Override
    public final boolean isValid() {
        return this.value != null;
    }
    
    public final void setValue(final Integer n) {
        if (n != null) {
            this.value = new Integer(n);
        }
        else {
            this.value = IntAppField.DEFAULTVALUE;
        }
    }
    
    @Override
    public final void setValueToDefault() {
        this.value = IntAppField.DEFAULTVALUE;
    }
    
    @Override
    public final String toString() {
        return this.getValue().toString();
    }
    
    @Override
    public final String typeAsString() {
        return "INTEGER";
    }
}
