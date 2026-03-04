package es.pcvegas.models;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

/**
 * Clase utilitaria para Apache Commons BeanUtils. Permite la conversión
 * automática de valores String (provenientes de formularios HTML) a tipos
 * Enumerados (Enum) en los Java Beans.
 *
 * * @author manuel
 */
public class EnumConverter implements Converter {

    /**
     * Convierte un objeto de entrada (generalmente String) al tipo Enum
     * destino.
     *
     * * @param type La clase del Enum destino.
     * @param o El objeto valor a convertir.
     * @return El valor Enum correspondiente.
     * @throws ConversionException Si el valor no coincide con ninguna constante
     * del Enum.
     */
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
