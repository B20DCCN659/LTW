package com.example.btl_web.utils;

import com.example.btl_web.constant.Constant;
import com.example.btl_web.constant.Constant.*;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConvertUtils {
    public static <E,D> D convertEntityToDto(E entity, Class<D> dtoClass)
    {
        D dto = null;
        try {
            dto = dtoClass.newInstance();
            Field entityFields[] = entity.getClass().getDeclaredFields();
            Field dtoFields[] = dtoClass.getDeclaredFields();

            for(Field entityField: entityFields)
            {
                entityField.setAccessible(true);
                if(entityField.get(entity) == null)
                    continue;
                for(Field dtoField: dtoFields)
                {
                    dtoField.setAccessible(true);
                    if(dtoField.getName().equals(entityField.getName()) && !entityField.getName().equals(Constant.PASS_WORD))
                    {
                        if(entityField.getType() == Long.class && dtoField.getType() == String.class &&
                                (entityField.getName().equals(Dto.CREATE_DATE) || entityField.getName().equals(Dto.MODIFIED_DATE) || entityField.getName().equals(Dto.REGISTRATION_AT)))
                        {
                            String timeStamp = convertTimeEntiTyToDto(entity, entityField);
                            dtoField.set(dto, timeStamp);
                        }
                        else
                        {
                            dtoField.set(dto, entityField.get(entity));
                        }
                        break;
                    }
                }
            }
         } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return dto;
    }

    public static <E> String convertTimeEntiTyToDto(E entity, Field entityField)
    {
        if(entityField == null)
            return null;
        String time = null;
        try {
            long timeStamp = (long) entityField.get(entity);
            Date dateTime = new Date(timeStamp);
            SimpleDateFormat sdf = new SimpleDateFormat(Dto.DATE_FORMAT);
            time = sdf.format(dateTime);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static <E,D> E convertDtoToEntity(D dto, Class<E> entityClass)
    {
        E entity = null;
        try {
            entity = entityClass.newInstance();
            Field entityFields[] = entityClass.getDeclaredFields();
            Field dtoFields[] = dto.getClass().getDeclaredFields();

            for(Field entityField: entityFields)
            {
                entityField.setAccessible(true);
                for(Field dtoField: dtoFields)
                {
                    dtoField.setAccessible(true);
                    if(entityField.getName().equals(dtoField.getName()))
                    {
                        if(entityField.getType() == Long.class && dtoField.getType() == String.class &&
                                entityField.getName().equals(Dto.CREATE_DATE) || entityField.getName().equals(Dto.MODIFIED_DATE) || entityField.getName().equals(Dto.REGISTRATION_AT))
                        {
                            Long timeStamp = convertTimeDtoToEntity(dto, dtoField);
                            entityField.set(entity, timeStamp);
                        }
                        else
                        {
                            entityField.set(entity, dtoField.get(dto));
                        }
                        break;
                    }
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return entity;
    }

    private static <D> long convertTimeDtoToEntity(D dto, Field dtoField)
    {
        long timeStamp = 0;
        String timeStampStr = null;

        try
        {
            timeStampStr = dtoField.get(dto).toString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Date date = new Date(timeStampStr);
        timeStamp = date.getTime();
        return timeStamp;
    }

    public static <E,D> List<D> convertListEntitiesToDtos(List<E> entities, Class<D> dtoClass)
    {
        List<D> dtos = new ArrayList<>();
        for(E entity: entities)
        {
            dtos.add(convertEntityToDto(entity, dtoClass));
        }
        return dtos;
    }

    public static Long convertStringDateToLong(String dateStr)
    {
        Date date = new Date(dateStr);

        return date.getTime();
    }
}
