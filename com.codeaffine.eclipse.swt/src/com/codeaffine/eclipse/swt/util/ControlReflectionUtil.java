package com.codeaffine.eclipse.swt.util;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.List;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

public class ControlReflectionUtil {

  private final Unsafe unsafe;

  public static class Parameter<T> {

    private final Class<T> type;
    private final T instance;

    Parameter( T instance, Class<T> type ) {
      this.instance = instance;
      this.type = type;
    }
  }

  interface Operator<T> {
    T execute() throws Exception;
  }

  public ControlReflectionUtil() {
    unsafe = new Unsafe();
  }

  public Class<? extends Widget> defineWidgetClass( String name ) {
    return execute( () -> defineClass( name ) );
  }

  public <T extends Widget> T newInstance( Class<T> type ) {
    return execute( () -> createNewIewInstance( type ) );
  }

  public void invoke( Widget receiver, String methodName, Parameter<?> ... parameters ) {
    execute( () -> invokeMethod( receiver, methodName, parameters ) );
  }

  public void setField( Widget receiver, String fieldName, Object value  ) {
    execute( () -> setFieldValue( receiver, fieldName, value ) );
  }

  public <T> T getField( Widget receiver, String fieldName, Class<T> type ) {
    return execute( () -> getFieldValue( receiver, fieldName, type ) );
  }

  public static <T> Parameter<T> $( T instance, Class<T> type ) {
    return new Parameter<T>( instance, type );
  }

  @SuppressWarnings("unchecked")
  private Class<? extends Widget> defineClass( final String name ) {
    String path = name.replaceAll( "\\.", "/" ) + ".class";
    byte[] bytes = new ResourceLoader().load( path );
    ClassLoader loader = getClass().getClassLoader();
    ProtectionDomain domain = getClass().getProtectionDomain();
    return ( Class<? extends Widget> )unsafe.defineClass( name, bytes, 0, bytes.length, loader, domain  );
  }

  private <T extends Widget> T createNewIewInstance( Class<T> type ) throws Exception {
    return unsafe.newInstance( type );
  }

  private static Object invokeMethod( Widget receiver, String methodName, Parameter<?> ... parameters )
    throws Exception
  {
    Method method = getMethod( receiver, methodName, parameters );
    method.setAccessible( true );
    return invokeMethodInternal( receiver, method, parameters );
  }

  private static Object invokeMethodInternal( Widget receiver, Method method, Parameter<?>... parameters )
    throws Exception
  {
    try {
      return method.invoke( receiver );
    } catch( Exception exception ) {
      return method.invoke( receiver, calculateParameterValues( parameters ) );
    }
  }

  private static Method getMethod( Widget receiver, String methodName, Parameter<?> ... parameters )
    throws NoSuchMethodException
  {
    try {
      return getMethodInternal( receiver, methodName );
    } catch( NoSuchMethodException noReceiverMethod ) {
      return getMethodInternal( receiver, methodName, calculateParameterTypes( parameters ) );
    }
  }

  private static Method getMethodInternal( Widget receiver, String methodName, Class<?> ... parameterTypes )
    throws NoSuchMethodException
  {
    try {
      return Control.class.getDeclaredMethod( methodName, parameterTypes );
    } catch( NoSuchMethodException noReceiverMethod ) {
      return receiver.getClass().getDeclaredMethod( methodName, parameterTypes );
    }
  }

  private static Object setFieldValue( Object receiver, String fieldName, Object value ) throws Exception {
    Field field = getField( receiver, fieldName );
    field.setAccessible( true );
    field.set( receiver, value );
    return null;
  }

  private static <T> T getFieldValue( Widget receiver, String fieldName, Class<T> type ) throws Exception {
    Field field = getField( receiver, fieldName );
    field.setAccessible( true );
    return type.cast( field.get( receiver ) );
  }

  private static Field getField( Object receiver, String fieldName ) throws NoSuchFieldException {
    try {
      return receiver.getClass().getDeclaredField( fieldName );
    } catch( NoSuchFieldException noReceiverField ) {
      try {
        return Control.class.getDeclaredField( fieldName );
      } catch( NoSuchFieldException noControlField ) {
        return Widget.class.getDeclaredField( fieldName );
      }
    }
  }

  private static <T> T execute( Operator<T> operator ) {
    try {
      return operator.execute();
    } catch( RuntimeException rte ) {
      throw rte;
    } catch( InvocationTargetException ite ) {
      if( ite.getCause() instanceof RuntimeException ) {
        throw ( RuntimeException )ite.getCause();
      }
      throw new RuntimeException( ite.getCause() );
    } catch( Exception e ) {
      throw new RuntimeException( e );
    }
  }

  private static Class<?>[] calculateParameterTypes( Parameter<?>[] parameters ) {
    List<Class<?>> classes = asList( parameters ).stream().map( parameter -> parameter.type ).collect( toList() );
    return classes.toArray( new Class[ classes.size() ] );
  }

  private static Object[] calculateParameterValues( Parameter<?>[] parameters ) {
    return asList( parameters ).stream().map( parameter -> parameter.instance ).collect( toList() ).toArray();
  }
}