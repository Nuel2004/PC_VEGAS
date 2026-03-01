package es.pcvegas.models;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

/**
 * Convierte Strings a Enums automáticamente para BeanUtils
 */
public class EnumConverter implements Converter {

    @Override
    public Object convert(Class type, Object o) {
        if (o == null) {
            return null;
        }

        String enumValName = (String) o;
        Enum[] enumConstants = (Enum[]) type.getEnumConstants();

        for (Enum enumConstant : enumConstants) {
            if (enumConstant.name().equalsIgnoreCase(enumValName)) {
                return (Object) enumConstant;
            }
        }

        throw new ConversionException(String.format("Error al convertir el valor '%s' a la clase %s", enumValName, type.toString()));
    }

}
