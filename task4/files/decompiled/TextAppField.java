package com.badguy.terrortime;

public class TextAppField extends AppField
{
    private static String DEFAULTVALUE;
    private String value;
    
    static {
        TextAppField.DEFAULTVALUE = "";
    }
    
    public TextAppField() {
        super(AppFieldTypes.TEXT);
        this.setValueToDefault();
    }
    
    public TextAppField(final String value) {
        super(AppFieldTypes.TEXT);
        if (value != null) {
            this.value = value;
        }
        else {
            this.value = TextAppField.DEFAULTVALUE;
        }
    }
    
    @Override
    public final boolean equals(final Object o) {
        return this == o || (o != null && this.getClass() == o.getClass() && this.isValid() && TextAppField.class.cast(o).isDefaultValue() == this.isDefaultValue() && this.value.equals(TextAppField.class.cast(o).getValue()));
    }
    
    @Override
    public final String getValue() {
        if (this.value == null) {
            return null;
        }
        if (this.isDefaultValue()) {
            return TextAppField.DEFAULTVALUE;
        }
        return String.valueOf(this.value);
    }
    
    @Override
    public final boolean isDefaultValue() {
        return this.isValid() && this.value.equals(TextAppField.DEFAULTVALUE);
    }
    
    @Override
    public final boolean isValid() {
        return this.value != null;
    }
    
    public final void setValue(final String value) {
        if (value != null) {
            this.value = value;
        }
        else {
            this.value = TextAppField.DEFAULTVALUE;
        }
    }
    
    @Override
    public final void setValueToDefault() {
        this.value = "";
    }
    
    @Override
    public final String toString() {
        return this.getValue();
    }
    
    @Override
    public final String typeAsString() {
        return "TEXT";
    }
}
