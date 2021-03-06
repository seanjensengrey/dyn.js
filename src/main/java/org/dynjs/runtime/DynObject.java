/**
 *  Copyright 2011 dynjs contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dynjs.runtime;

import org.dynjs.api.Scope;
import org.dynjs.exception.ReferenceError;
import org.dynjs.runtime.primitives.DynPrimitiveBoolean;
import org.dynjs.runtime.primitives.DynPrimitiveNumber;
import org.dynjs.runtime.primitives.DynPrimitiveUndefined;

import java.util.HashMap;
import java.util.Map;

import static org.dynjs.runtime.primitives.DynPrimitiveBoolean.FALSE;
import static org.dynjs.runtime.primitives.DynPrimitiveBoolean.TRUE;

public class DynObject implements DynAtom, Scope {

    private final Map<String, DynProperty> properties = new HashMap<>();

    public DynObject() {
        setProperty("prototype", DynPrimitiveUndefined.UNDEFINED);
    }

    public void setProperty(String key, DynAtom atom) {
        DynProperty property = new DynProperty(key).setAttribute("value", atom);
        this.properties.put(key, property);
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    public DynProperty getProperty(String key) {
        if (this.properties.containsKey(key)) {
            return this.properties.get(key);
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public Scope getEnclosingScope() {
        if (getProperty("prototype").getClass().isAssignableFrom(DynPrimitiveUndefined.class)) {
            return null;
        } else {
            return (DynObject) getProperty("prototype").getAttribute("value");
        }
    }

    @Override
    public DynAtom resolve(String name) {
        if (this.properties.containsKey(name)) {
            return this.properties.get(name).getAttribute("value");
        }
        throw new ReferenceError();
    }

    @Override
    public void define(String property, DynAtom value) {
        setProperty(property, value);
    }

    public static DynPrimitiveBoolean toBoolean(final DynAtom value) {
        if (value instanceof DynNumber) {
            DynNumber number = (DynNumber) value;
            return (number.isNaN() || number.getValue() == 0) ? FALSE : TRUE;
        } else if (value instanceof DynPrimitiveBoolean) {
            return (DynPrimitiveBoolean) value;
        } else if (value instanceof DynString) {
            DynString string = (DynString) value;
            return string.toString().equals("") ? FALSE : TRUE;
        }
        return (value instanceof DynObject) ? TRUE : FALSE;
    }

    public static DynPrimitiveBoolean eq(final DynAtom lhs, final DynAtom rhs) {
        if (lhs instanceof DynPrimitiveNumber && rhs instanceof DynPrimitiveNumber) {
            DynNumber n1 = new DynNumber((DynPrimitiveNumber) lhs);
            DynNumber n2 = new DynNumber((DynPrimitiveNumber) rhs);
            if (n1.isNaN() || n2.isNaN()) {
                return DynPrimitiveBoolean.FALSE;
            }
            if (n1.getValue() == n2.getValue()){
                return DynPrimitiveBoolean.TRUE;
            }
        }
        return DynPrimitiveBoolean.FALSE;
    }

}
