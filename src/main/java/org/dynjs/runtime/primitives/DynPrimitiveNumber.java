package org.dynjs.runtime.primitives;

import org.dynjs.runtime.DynAtom;

public class DynPrimitiveNumber implements DynAtom {

    private final String value;
    private final Integer radix;

    public DynPrimitiveNumber(String value, Integer radix) {
        this.value = value;
        this.radix = radix;
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }

    public Double getDoubleValue() {
        return Double.parseDouble(this.value);
    }

    @Override
    public String toString() {
        return "DynPrimitiveNumber{" +
                "value='" + value + '\'' +
                ", radix=" + radix +
                '}';
    }
}
