package com.badguy.terrortime;

import java.util.*;

public class BlobAppField extends AppField
{
    private static byte[] DEFAULTVALUE;
    private byte[] value;
    
    static {
        BlobAppField.DEFAULTVALUE = null;
    }
    
    public BlobAppField() {
        super(AppFieldTypes.BIN);
        this.value = null;
    }
    
    public BlobAppField(final BlobAppField blobAppField) {
        super(AppFieldTypes.BIN);
        this.value = null;
        this.setValue(blobAppField.getValue());
    }
    
    public BlobAppField(final byte[] value) {
        super(AppFieldTypes.BIN);
        this.value = null;
        this.setValue(value);
    }
    
    @Override
    public final boolean equals(final Object o) {
        return this == o || (o != null && this.getClass() == o.getClass() && this.isValid() && this.isDefaultValue() == BlobAppField.class.cast(o).isDefaultValue() && Arrays.equals(this.value, BlobAppField.class.cast(o).getValue()));
    }
    
    @Override
    public final byte[] getValue() {
        final byte[] value = this.value;
        if (value == null) {
            return null;
        }
        final byte[] defaultvalue = BlobAppField.DEFAULTVALUE;
        if (value == defaultvalue) {
            return defaultvalue;
        }
        return Arrays.copyOf(value, value.length);
    }
    
    @Override
    public final boolean isDefaultValue() {
        return this.value == BlobAppField.DEFAULTVALUE;
    }
    
    @Override
    public final boolean isValid() {
        return true;
    }
    
    public final void setValue(final byte[] array) {
        if (array != null && array != BlobAppField.DEFAULTVALUE) {
            this.value = Arrays.copyOf(array, array.length);
        }
        else {
            this.value = BlobAppField.DEFAULTVALUE;
        }
    }
    
    @Override
    public final void setValueToDefault() {
        this.value = BlobAppField.DEFAULTVALUE;
    }
    
    @Override
    public final String toString() {
        if (this.value == null) {
            return "null";
        }
        return Arrays.toString(this.getValue());
    }
    
    @Override
    public final String typeAsString() {
        return "BLOB";
    }
}
