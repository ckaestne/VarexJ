package gov.nasa.jpf.test.java.lang.reflect;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;

import org.junit.Test;

import gov.nasa.jpf.util.test.TestJPF;

public class FieldTest extends TestJPF {

  public static final int testField1 = 1;

  public String testField2;

  public static void main (String[] args) throws SecurityException, NoSuchFieldException{
    runTestsOfThisClass(args);
  }

  @Test
  public void equalsTest () throws SecurityException, NoSuchFieldException{
    if (verifyNoPropertyViolation()){
      Field f1 = FieldTest.class.getField("testField1");
      Field f2 = FieldTest.class.getField("testField1");
      Field f3 = FieldTest.class.getField("testField2");
      Field f4 = FieldTest.class.getField("testField2");
      assertTrue(f1.equals(f2));
      assertTrue(f3.equals(f4));
      assertFalse(f1.equals(f3));
    }
  }

  @Test
  public void toStringTest () throws SecurityException, NoSuchFieldException{
    if (verifyNoPropertyViolation()){
      Field f1 = FieldTest.class.getField("testField1");
      System.out.println(f1);
      assertEquals(f1.toString(), "public static final int gov.nasa.jpf.test.java.lang.reflect.FieldTest.testField1");
      Field f2 = FieldTest.class.getField("testField2");
      assertEquals(f2.toString(), "public java.lang.String gov.nasa.jpf.test.java.lang.reflect.FieldTest.testField2");
    }
  }

  enum TestEnum{
    f1, f2, f3
  }

  @Test
  public void isEnumConstantTest (){
    if (verifyNoPropertyViolation()){
      for (Field f : TestEnum.class.getFields())
        assertTrue(f.isEnumConstant());
    }
  }

  @Test
  public void hashCodeTest () throws SecurityException, NoSuchFieldException{
    if (verifyNoPropertyViolation()){
      Field f1 = FieldTest.class.getField("testField1");
      Field f2 = FieldTest.class.getField("testField1");
      Field f3 = FieldTest.class.getField("testField2");
      assertTrue(f1.hashCode() == f2.hashCode());
      assertFalse(f1.hashCode() == f3.hashCode());
    }
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  public @interface A1 {
  }

  public class Super {
    @A1
    public int f;
  }

  public class Sub {
    public int f;
  }

  @Test
  public void getDeclaredAnnotationsTest () throws SecurityException, NoSuchFieldException{
    if (verifyNoPropertyViolation()){
      Field f1 = Sub.class.getField("f");
      Field f2 = Super.class.getField("f");
      assertEquals(f1.getDeclaredAnnotations().length, 0);
      assertEquals(f2.getDeclaredAnnotations().length, 1);
    }
  }
}
