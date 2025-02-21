package edu.arep.parcial1.HttpServer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflexCalculator {

    public static String calculator(String query) throws NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException{
            Class<?> clazz = Math.class;
            Method m;
            String response ="";
            String[] params = format(query);
            if(params.length == 2){
                Double param = Double.valueOf(params[1]);
                m = clazz.getDeclaredMethod(params[0]);
                Object result = m.invoke(null, param);
                response = String.valueOf(result);
            }else{
                m = clazz.getDeclaredMethod(params[0]);
                Double[] paramss = new Double[(params.length)-1];
                for(int i=1; i < params.length ;i++){
                    paramss[i] =  Double.valueOf(params[i]);
                }
                Object result = m.invoke(null, paramss);
                response = String.valueOf(result);
            }
        return response;
        }
    
        public static String[] format(String query){
            String queryValues = query.substring(18);
            System.out.println(queryValues);
            String[] cadena = queryValues.split("\\(");
            String method = cadena[0];
            String values = cadena[1];
            String[] val = values.split("\\)");
            String[] params = val[0].split(",");
            String[] finalFormat = new String[1+params.length];
            finalFormat[0] = method;
            for(int i=1; i < 1+params.length; i++){
                finalFormat[i] = params[i-1];
            }
            return finalFormat;
        }
    
    
}
