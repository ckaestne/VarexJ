//
// Copyright (C) 2006 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
//
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
//
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//
package gov.nasa.jpf.vm;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.JPFException;
import gov.nasa.jpf.annotation.MJI;
import gov.nasa.jpf.util.IntTable;
import gov.nasa.jpf.util.JPFLogger;
import gov.nasa.jpf.util.ObjectConverter;
import gov.nasa.jpf.util.ObjectList;
import gov.nasa.jpf.util.RunListener;
import gov.nasa.jpf.util.RunRegistry;
import gov.nasa.jpf.util.json.CGCall;
import gov.nasa.jpf.util.json.JSONLexer;
import gov.nasa.jpf.util.json.JSONObject;
import gov.nasa.jpf.util.json.JSONParser;
import gov.nasa.jpf.vm.choice.DoubleChoiceFromList;
import gov.nasa.jpf.vm.choice.FloatChoiceFromList;
import gov.nasa.jpf.vm.choice.IntChoiceFromSet;
import gov.nasa.jpf.vm.choice.IntIntervalGenerator;
import gov.nasa.jpf.vm.choice.LongChoiceFromList;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.BitSet;
import java.util.List;

import cmu.conditional.Conditional;
import de.fosd.typechef.featureexpr.FeatureExpr;
import de.fosd.typechef.featureexpr.FeatureExprFactory;

/**
 * native peer class for programmatic JPF interface (that can be used inside
 * of apps to verify - if you are aware of the danger that comes with it)
 * 
 * this peer is a bit different in that it only uses static fields and methods because
 * its use is supposed to be JPF global (without classloader namespaces)
 */
public class JPF_gov_nasa_jpf_vm_Verify extends NativePeer {
  static final int MAX_COUNTERS = 10;

  static boolean isInitialized;
  
  // those are used to store search global int values (e.g. from TestJPF derived classes)
  static int[] counter;
  static IntTable<String> map;

  
  static boolean supportIgnorePath;
  static boolean breakSingleChoice;
  static boolean enableAtomic;

  static Config config;  // we need to keep this around for CG creation

  // our const ChoiceGenerator ctor argtypes
  static Class[] cgArgTypes = { Config.class, String.class };
  // this is our cache for ChoiceGenerator ctor parameters
  static Object[] cgArgs = { null, null };

  static BitSet[] bitSets;
  static int nextBitSet;

  
  public static boolean init (Config conf) {

    if (!isInitialized){
      supportIgnorePath = conf.getBoolean("vm.verify.ignore_path");
      breakSingleChoice = conf.getBoolean("cg.break_single_choice");
      enableAtomic = conf.getBoolean("cg.enable_atomic", true);

      counter = null;
      map = null;
      
      config = conf;

      Verify.setPeerClass( JPF_gov_nasa_jpf_vm_Verify.class);

      RunRegistry.getDefaultRegistry().addListener( new RunListener() {
        public void reset (RunRegistry reg){
          isInitialized = false;
        }
      });
    }
    return true;
  }

  
  public static final int NO_VALUE = -1;
  
  @MJI
  public static int getValue__Ljava_lang_String_2__I (MJIEnv env, int clsObjRef, int keyRef) {
    if (map == null) {
      return NO_VALUE;
    } else {
      String key = env.getStringObject(null, keyRef);
      IntTable.Entry<String> e = map.get(key);
      if (e != null) {
        return e.val;
      } else {
        return NO_VALUE;
      }
    }
  }
  
  @MJI
  public static void putValue__Ljava_lang_String_2I__V (MJIEnv env, int clsObjRef, int keyRef, int val) {
    if (map == null) {
      map = new IntTable<String>();
    }
    
    String key = env.getStringObject(null, keyRef);
    map.put(key, val);
  }
  
  @MJI
  public static int getCounter__I__I (MJIEnv env, int clsObjRef, int counterId) {
    if ((counter == null) || (counterId < 0) || (counterId >= counter.length)) {
      return 0;
    }

    return counter[counterId];
  }

  private static void ensureCounterCapacity (int counterId){
    if (counter == null) {
      counter = new int[(counterId >= MAX_COUNTERS) ? counterId+1 : MAX_COUNTERS];
    } else if (counterId >= counter.length) {
      int[] newCounter = new int[counterId+1];
      System.arraycopy(counter, 0, newCounter, 0, counter.length);
      counter = newCounter;
    }    
  }
  
  @MJI
  public static void resetCounter__I__V (MJIEnv env, int clsObjRef, int counterId) {
    if ((counter == null) || (counterId < 0) || (counterId >= counter.length)) {
      return;
    }
    counter[counterId] = 0;
  }

  @MJI
  public static void setCounter__II__V (MJIEnv env, int clsObjRef, int counterId, int val) {
    if (counterId < 0){
      return;
    }
    
    ensureCounterCapacity(counterId);
    counter[counterId] = val;
  }
  
  @MJI
  public static int incrementCounter__I__I (MJIEnv env, int clsObjRef, int counterId) {
    if (counterId < 0) {
      return 0;
    }

    ensureCounterCapacity(counterId);
    return ++counter[counterId];
  }

  private static void checkBitSetId(int id) {
    if (bitSets == null) {
      bitSets = new BitSet[id + 1];
    } else if (id >= bitSets.length) {
      BitSet[] newBitSets = new BitSet[id + 1];
      System.arraycopy(bitSets, 0, newBitSets, 0, bitSets.length);
      bitSets = newBitSets;
    }

    if (bitSets[id] == null) {
      bitSets[id] = new BitSet();
    }
  }

  @MJI
  public static void setBitInBitSet__IIZ__V(MJIEnv env, int clsObjRef, int id, int bitNum, boolean value) {
    checkBitSetId(id);
    bitSets[id].set(bitNum, value);
  }

  @MJI
  public static boolean getBitInBitSet__II__Z(MJIEnv env, int clsObjRef, int id, int bitNum) {
    checkBitSetId(id);
    return bitSets[id].get(bitNum);
  }

  @MJI
  public static long currentTimeMillis____J (MJIEnv env, int clsObjRef) {
    return System.currentTimeMillis();
  }

  @MJI
  public static String getType (int objRef, MJIEnv env) {
    return Types.getTypeName(env.getElementInfo(objRef).getType());
  }

  @MJI
  public static void addComment__Ljava_lang_String_2__V (MJIEnv env, int clsObjRef, int stringRef) {
    SystemState ss = env.getSystemState();
    String      cmt = env.getStringObject(null, stringRef);
    ss.getTrail().setAnnotation(cmt);
  }

  /** deprectated, just use assert */
  @MJI
  public static void assertTrue__Z__V (MJIEnv env, int clsObjRef, boolean b) {
    if (!b) {
      env.throwException(NativeMethodInfo.CTX, "java.lang.AssertionError", "assertTrue failed");
    }
  }

  // those are evil - use with extreme care
  @MJI
  public static void beginAtomic____V (MJIEnv env, int clsObjRef) {
    if (enableAtomic){
      ThreadInfo tiAtomic = env.getThreadInfo();
      if (!tiAtomic.isFirstStepInsn()){
        SystemState ss = env.getSystemState();
        ChoiceGenerator<?> cg = ss.getSchedulerFactory().createBeginAtomicCG(tiAtomic);
        if (ss.setNextChoiceGenerator(cg)) {
          env.repeatInvocation();
          return;
        }
      }

      env.getSystemState().incAtomic();
    }
  }
  
  @MJI
  public static void endAtomic____V (MJIEnv env, int clsObjRef) {
    if (enableAtomic){
      ThreadInfo tiAtomic = env.getThreadInfo();

      if (!tiAtomic.isFirstStepInsn()){
        env.getSystemState().decAtomic();

        SystemState ss = env.getSystemState();
        ChoiceGenerator<?> cg = ss.getSchedulerFactory().createEndAtomicCG(tiAtomic);
        if (ss.setNextChoiceGenerator(cg)) {
          env.repeatInvocation();
        }
      }
    }
  }

  @MJI
  public static void busyWait__J__V (MJIEnv env, int clsObjRef, long duration) {
    // nothing required here (we systematically explore scheduling
    // sequences anyway), but we need to intercept the call
  }

  @MJI
  public static void ignoreIf__Z__V (MJIEnv env, int clsObjRef, boolean cond) {
    if (supportIgnorePath) {
      env.getSystemState().setIgnored(cond);
    }
  }

  @MJI
  public static void interesting__Z__V (MJIEnv env, int clsObjRef, boolean cond) {
    env.getSystemState().setInteresting(cond);
  }

  @MJI
  public static void breakTransition__Ljava_lang_String_2__V (MJIEnv env, int clsObjRef, int reasonRef){
    ThreadInfo ti = env.getThreadInfo();
    String reason = env.getStringObject(null, reasonRef);
    ti.breakTransition(reason);
  }

  @MJI
  public static boolean isCalledFromClass__Ljava_lang_String_2__Z (MJIEnv env, int clsObjRef,
                                           int clsNameRef) {
    String refClassName = env.getStringObject(null, clsNameRef);
    ThreadInfo ti = env.getThreadInfo();

    StackFrame caller = ti.getLastInvokedStackFrame();
    if (caller != null){
      ClassInfo ci = caller.getClassInfo();
      return ci.isInstanceOf(refClassName);
    }

    return false;
  }


  static <T extends ChoiceGenerator<?>> T createChoiceGenerator (Class<T> cgClass, SystemState ss, String id) {
    T gen = null;

    cgArgs[0] = config;
    cgArgs[1] = id; // good thing we are not multithreaded (other fields are const)

    String key = id + ".class";
    gen = config.getEssentialInstance(key, cgClass, cgArgTypes, cgArgs);

    return gen;
  }

  static <T> T registerChoiceGenerator (MJIEnv env, SystemState ss, ThreadInfo ti, ChoiceGenerator<T> cg, T dummyVal){

    int n = cg.getTotalNumberOfChoices();
    if (n == 0) { // we need a CG
      ss.setIgnored(true);
      ti.breakTransition( cg.getId());

    } else if (n == 1 && !breakSingleChoice) {
      // no choice -> no CG optimization
      cg.advance();
      return cg.getNextChoice();

    } else {
      if (ss.setNextChoiceGenerator(cg)){
        env.repeatInvocation();
      }
    }

    return dummyVal;
  }

  static <T,C extends ChoiceGenerator<T>> T getNextChoice (SystemState ss, String id, Class<C> cgClass, Class<T> choiceClass){
    ChoiceGenerator<?> cg = ss.getCurrentChoiceGenerator(id, cgClass);

    assert (cg != null) : "no ChoiceGenerator of type " + cgClass.getName();
    return ((ChoiceGenerator<T>)cg).getNextChoice();
  }

  @MJI
  public static boolean getBoolean____Z (MJIEnv env, int clsObjRef) {
    ThreadInfo ti = env.getThreadInfo();
    SystemState ss = env.getSystemState();
    ChoiceGenerator<?> cg;

    if (!ti.isFirstStepInsn()) { // first time around
      cg = new BooleanChoiceGenerator(config, "verifyGetBoolean");
      if (ss.setNextChoiceGenerator(cg)){
        env.repeatInvocation();
      }
      return true;  // not used if we repeat

    } else {  // this is what really returns results
      return getNextChoice(ss,"verifyGetBoolean", BooleanChoiceGenerator.class,Boolean.class);
    }
  }

  @MJI
  public static boolean getBoolean__Z__Z (MJIEnv env, int clsObjRef, boolean falseFirst) {
    ThreadInfo ti = env.getThreadInfo();
    SystemState ss = env.getSystemState();
    ChoiceGenerator<?> cg;

    if (!ti.isFirstStepInsn()) { // first time around
      cg = new BooleanChoiceGenerator( "verifyGetBoolean(Z)", falseFirst );
      if (ss.setNextChoiceGenerator(cg)){
        env.repeatInvocation();
      }
      return true;  // not used if we repeat

    } else {  // this is what really returns results
      return getNextChoice(ss,"verifyGetBoolean(Z)", BooleanChoiceGenerator.class, Boolean.class);
    }
  }

  @MJI
  public static int getInt__II__I (MJIEnv env, int clsObjRef, int min, int max) {
    ThreadInfo ti = env.getThreadInfo();
    SystemState ss = env.getSystemState();

    if (!ti.isFirstStepInsn()) { // first time around

      if (min > max){
        int t = max;
        max = min;
        min = t;
      }

      IntChoiceGenerator cg = new IntIntervalGenerator( "verifyGetInt(II)", min,max);
      return registerChoiceGenerator(env,ss,ti,cg,0);

    } else {
      return getNextChoice(ss, "verifyGetInt(II)", IntChoiceGenerator.class, Integer.class);
    }
  }

  static int getIntFromList (MJIEnv env, int[] values){
    ThreadInfo ti = env.getThreadInfo();
    SystemState ss = env.getSystemState();

    if (!ti.isFirstStepInsn()) { // first time around
      ChoiceGenerator<Integer> cg = new IntChoiceFromSet( "verifyGetIntSet([I)", values);
      return registerChoiceGenerator(env,ss,ti,cg,0);

    } else {
      return getNextChoice(ss, "verifyGetIntSet([I)", IntChoiceGenerator.class, Integer.class);
    }    
  }
  
  @MJI
  public static int getIntFromList___3I__I (MJIEnv env, int clsObjRef, int valArrayRef){
    int[] values = env.getIntArrayObject(NativeMethodInfo.CTX, valArrayRef);
    return getIntFromList( env, values);
  }

  @MJI
  public static int getInt__Ljava_lang_String_2__I (MJIEnv env, int clsObjRef, int idRef) {
    ThreadInfo ti = env.getThreadInfo();
    SystemState ss = env.getSystemState();


    if (!ti.isFirstStepInsn()) { // first time around
      String id = env.getStringObject(null, idRef);
      IntChoiceGenerator cg = createChoiceGenerator( IntChoiceGenerator.class, ss, id);
      return registerChoiceGenerator(env,ss,ti,cg, 0);

    } else {
      String id = env.getStringObject(null, idRef);
      return getNextChoice(ss, id, IntChoiceGenerator.class,Integer.class);
    }
  }

  static long getLongFromList (MJIEnv env, long[] values){
    ThreadInfo ti = env.getThreadInfo();
    SystemState ss = env.getSystemState();

    if (!ti.isFirstStepInsn()) { // first time around
      ChoiceGenerator<Long> cg = new LongChoiceFromList( "verifyLongList([J)", values);
      return registerChoiceGenerator(env,ss,ti,cg,0L);

    } else {
      return getNextChoice(ss, "verifyLongList([J)", LongChoiceGenerator.class, Long.class);
    }    
  }
  
  @MJI
  public static long getLongFromList___3J__J (MJIEnv env, int clsObjRef, int valArrayRef){
    Conditional<Long>[] values = env.getLongArrayObject(valArrayRef);
    long [] a = new long[values.length];
	for (int i = 0; i < values.length; i++) {
			a[i] = values[i].getValue();
	}
    
    
    return getLongFromList( env, a);    
  }
  
  @MJI
  public static int getObject__Ljava_lang_String_2__Ljava_lang_Object_2 (MJIEnv env, int clsObjRef, int idRef) {
    ThreadInfo ti = env.getThreadInfo();
    SystemState ss = env.getSystemState();

    if (!ti.isFirstStepInsn()) { // first time around
      String id = env.getStringObject(null, idRef);
      ReferenceChoiceGenerator cg = createChoiceGenerator( ReferenceChoiceGenerator.class, ss, id);
      return registerChoiceGenerator(env,ss,ti,cg, 0);

    } else {
      String id = env.getStringObject(null, idRef);
      return getNextChoice(ss, id, ReferenceChoiceGenerator.class,Integer.class);
    }
  }

  @MJI
  public static double getDouble__Ljava_lang_String_2__D (MJIEnv env, int clsObjRef, int idRef) {
    ThreadInfo ti = env.getThreadInfo();
    SystemState ss = env.getSystemState();

    if (!ti.isFirstStepInsn()) { // first time around
      String id = env.getStringObject(null, idRef);
      DoubleChoiceGenerator cg = createChoiceGenerator( DoubleChoiceGenerator.class, ss, id);
      return registerChoiceGenerator(env,ss,ti,cg, 0.0);

    } else {
      String id = env.getStringObject(null, idRef);
      return getNextChoice(ss, id, DoubleChoiceGenerator.class,Double.class);
    }
  }

  @MJI
  public static double getDoubleFromList (MJIEnv env, double[] values){
    ThreadInfo ti = env.getThreadInfo();
    SystemState ss = env.getSystemState();

    if (!ti.isFirstStepInsn()) { // first time around
      ChoiceGenerator<Double> cg = new DoubleChoiceFromList("verifyDoubleList([D)", values);
      return registerChoiceGenerator(env,ss,ti,cg, 0.0);

    } else {
      return getNextChoice(ss, "verifyDoubleList([D)", DoubleChoiceFromList.class,Double.class);
    }    
  }
  
  @MJI
  public static double getDoubleFromList___3D__D (MJIEnv env, int clsObjRef, int valArrayRef){
    double[] values = env.getDoubleArrayObject(valArrayRef);
    return getDoubleFromList( env, values);
  }

  @MJI
  public static float getFloatFromList (MJIEnv env, float[] values){
    ThreadInfo ti = env.getThreadInfo();
    SystemState ss = env.getSystemState();

    if (!ti.isFirstStepInsn()) { // first time around
      ChoiceGenerator<Float> cg = new FloatChoiceFromList("verifyFloatList([F)", values);
      return registerChoiceGenerator(env,ss,ti,cg, 0.0f);

    } else {
      return getNextChoice(ss, "verifyFloatList([F)", FloatChoiceFromList.class, Float.class);
    }    
  }
  
  @MJI
  public static float getFloatFromList___3F__F (MJIEnv env, int clsObjRef, int valArrayRef){
    Conditional<Float>[] condValues = env.getFloatArrayObject(valArrayRef);
    float[] values = new float[condValues.length];
    for (int i = 0; i < condValues.length; i++) {
    	values[i] = condValues[i].getValue(); 
    }
    
    return getFloatFromList( env, values);
  }

  @MJI
  public static void print__Ljava_lang_String_2I__V (MJIEnv env, int clsRef, int sRef, int val){
    String s = env.getStringObject(null, sRef);
    System.out.println(s + " : " + val);
  }

  @MJI
  public static void print__Ljava_lang_String_2Z__V (MJIEnv env, int clsRef, int sRef, boolean val){
    String s = env.getStringObject(null, sRef);
    System.out.println(s + " : " + val);
  }

  @MJI
  public static void print___3Ljava_lang_String_2__V (MJIEnv env, int clsRef, int argsRef){
    int n = env.getArrayLength(NativeMethodInfo.CTX, argsRef);
    for (int i=0; i<n; i++){
      int aref = env.getReferenceArrayElement(argsRef, i);
      String s = env.getStringObject(null, aref);
      System.out.print(s);
    }
  }
  
  @MJI
  public static void print__Ljava_lang_String_2__V (MJIEnv env, int clsRef, int sRef){
    String s = env.getStringObject(null, sRef);
    System.out.print(s);
  }

  @MJI
  public static void println__Ljava_lang_String_2__V (MJIEnv env, int clsRef, int sRef){
    String s = env.getStringObject(null, sRef);
    System.out.println(s);
  }

  
  @MJI
  public static void println____V (MJIEnv env, int clsRef){
    System.out.println();
  }
  
  //--- various attribute test methods
  
  private static int getAttribute (MJIEnv env, Object a){
    if (a != null) {
      if (a instanceof Integer) {
        return ((Integer) a).intValue();
      } else {
        env.throwException(NativeMethodInfo.CTX, "java.lang.RuntimeException", "element attribute not an Integer: " + a);
      }
    }

    return 0;
  }
  
  private static int getAttributeList (MJIEnv env, Object a){
    if (a != null) {
      int l = ObjectList.size(a);
      int[] attrs = new int[l];
      int i = 0;
      for (Integer v : ObjectList.typedIterator(a, Integer.class)) {
        attrs[i++] = v;
      }
      if (i != l) {
        env.throwException(NativeMethodInfo.CTX, "java.lang.RuntimeException", "found non-Integer attributes");
        return 0;
      }

      return env.newIntArray(NativeMethodInfo.CTX, attrs);
      
    } else {
      return MJIEnv.NULL;
    }
  }
  
  @MJI
  public static void setObjectAttribute__Ljava_lang_Object_2I__V (MJIEnv env, int clsRef, int oRef, int attr){
    if (oRef != MJIEnv.NULL){
      ElementInfo ei = env.getElementInfo(oRef);
      ei.setObjectAttr(Integer.valueOf(attr));
    }
  }
  
  @MJI
  public static int getObjectAttribute__Ljava_lang_Object_2__I (MJIEnv env, int clsRef, int oRef){
    if (oRef != MJIEnv.NULL){
      ElementInfo ei = env.getElementInfo(oRef);
      return getAttribute( env, ei.getObjectAttr());
    }

    return 0;
  }
  
  @MJI
  public static void addObjectAttribute__Ljava_lang_Object_2I__V (MJIEnv env, int clsRef, int oRef, int attr){
    if (oRef != MJIEnv.NULL){
      ElementInfo ei = env.getElementInfo(oRef);
      ei.addObjectAttr(Integer.valueOf(attr));
    }
  }
  
  @MJI
  public static int getObjectAttributes__Ljava_lang_Object_2___3I (MJIEnv env, int clsRef, int oRef){
    if (oRef != MJIEnv.NULL){
      ElementInfo ei = env.getElementInfo(oRef);
      return getAttributeList( env, ei.getObjectAttr());
    }

    return MJIEnv.NULL;
  }
  
  @MJI
  public static void setFieldAttribute__Ljava_lang_Object_2Ljava_lang_String_2I__V (MJIEnv env, int clsRef,
                                                                                    int oRef, int fnRef, int attr){
    if (oRef != MJIEnv.NULL){
      ElementInfo ei = env.getElementInfo(oRef);
      if (ei != null){
        String fname = env.getStringObject(null, fnRef);
        FieldInfo fi = ei.getFieldInfo(fname);

        if (fi != null) {
          ei.setFieldAttr(fi, Integer.valueOf(attr));
        } else {
          env.throwException(NativeMethodInfo.CTX,
                  "java.lang.NoSuchFieldException", ei.getClassInfo().getName() + '.' + fname);
        }
      } else {
        env.throwException(NativeMethodInfo.CTX, "java.lang.RuntimeException", "illegal reference value: " + oRef);
      }
    }
  }
  
  @MJI
  public static int getFieldAttribute__Ljava_lang_Object_2Ljava_lang_String_2__I (MJIEnv env, int clsRef,
                                                                                    int oRef, int fnRef){
    if (oRef != MJIEnv.NULL){
      ElementInfo ei = env.getElementInfo(oRef);
      if (ei != null){
        String fname = env.getStringObject(null, fnRef);
        FieldInfo fi = ei.getFieldInfo(fname);

        if (fi != null) {
          return getAttribute( env, ei.getFieldAttr(fi));
        } else {
          env.throwException(NativeMethodInfo.CTX,
                  "java.lang.NoSuchFieldException", ei.toString() + '.' + fname);
        }
      } else {
        env.throwException(NativeMethodInfo.CTX, "java.lang.RuntimeException", "illegal reference value: " + oRef);
      }
    }

    return 0;
  }
  
  @MJI
  public static void addFieldAttribute__Ljava_lang_Object_2Ljava_lang_String_2I__V (MJIEnv env, int clsRef,
                                                                                    int oRef, int fnRef, int attr){
    if (oRef != MJIEnv.NULL){
      ElementInfo ei = env.getElementInfo(oRef);
      if (ei != null){
        String fname = env.getStringObject(null, fnRef);
        FieldInfo fi = ei.getFieldInfo(fname);

        if (fi != null) {
          ei.addFieldAttr(fi, Integer.valueOf(attr));
        } else {
          env.throwException(NativeMethodInfo.CTX,
                  "java.lang.NoSuchFieldException", ei.getClassInfo().getName() + '.' + fname);
        }
      } else {
        env.throwException(NativeMethodInfo.CTX, "java.lang.RuntimeException", "illegal reference value: " + oRef);
      }
    }
  }

  @MJI
  public static int getFieldAttributes__Ljava_lang_Object_2Ljava_lang_String_2___3I (MJIEnv env, int clsRef,
                                                                                    int oRef, int fnRef){
    if (oRef != MJIEnv.NULL){
      ElementInfo ei = env.getElementInfo(oRef);
      if (ei != null){
        String fname = env.getStringObject(null, fnRef);
        FieldInfo fi = ei.getFieldInfo(fname);

        if (fi != null) {
          return getAttributeList( env, ei.getFieldAttr(fi));          
        } else {
          env.throwException(NativeMethodInfo.CTX,
                  "java.lang.NoSuchFieldException", ei.toString() + '.' + fname);
        }
      } else {
        env.throwException(NativeMethodInfo.CTX, "java.lang.RuntimeException", "illegal reference value: " + oRef);
      }
    }

    return MJIEnv.NULL;
  }

  @MJI
  public static void setLocalAttribute__Ljava_lang_String_2I__V (MJIEnv env, int clsRef, int varRef, int attr) {
    String slotName = env.getStringObject(null, varRef);
    StackFrame frame = env.getModifiableCallerStackFrame(); // we are executing in a NativeStackFrame

    if (!frame.getMethodInfo().isStatic() &&  slotName.equals("this")) {
      frame.setLocalAttr(0, Integer.valueOf(attr)); // only for instance methods of course

    } else {
      int slotIdx = frame.getLocalVariableSlotIndex(slotName);
      if (slotIdx >= 0) {
        frame.setLocalAttr(slotIdx, Integer.valueOf(attr));
      } else {
        env.throwException(NativeMethodInfo.CTX, "java.lang.RuntimeException", "local variable not found: " + slotName);
      }
    }
  }

  @MJI
  public static int getLocalAttribute__Ljava_lang_String_2__I (MJIEnv env, int clsRef, int varRef) {
    String slotName = env.getStringObject(null, varRef);
    ThreadInfo ti = env.getThreadInfo();
    StackFrame frame = env.getCallerStackFrame();

    int slotIdx = frame.getLocalVariableSlotIndex(slotName);
    if (slotIdx >= 0) {
      return getAttribute( env, frame.getLocalAttr(slotIdx));
    } else {
      env.throwException(NativeMethodInfo.CTX, "java.lang.RuntimeException", "local variable not found: " + slotName);
      return 0;
    }
  }

  @MJI
  public static void addLocalAttribute__Ljava_lang_String_2I__V (MJIEnv env, int clsRef, int varRef, int attr) {
    String slotName = env.getStringObject(null, varRef);
    StackFrame frame = env.getModifiableCallerStackFrame(); // we are executing in a NativeStackFrame

    if (!frame.getMethodInfo().isStatic() &&  slotName.equals("this")) {
      frame.addLocalAttr(0, Integer.valueOf(attr)); // only for instance methods of course

    } else {
      int slotIdx = frame.getLocalVariableSlotIndex(slotName);
      if (slotIdx >= 0) {
        frame.addLocalAttr(slotIdx, Integer.valueOf(attr));
      } else {
        env.throwException(NativeMethodInfo.CTX, "java.lang.RuntimeException", "local variable not found: " + slotName);
      }
    }
  }
  
  @MJI
  public static int getLocalAttributes__Ljava_lang_String_2___3I (MJIEnv env, int clsRef, int varRef) {
    String slotName = env.getStringObject(null, varRef);
    ThreadInfo ti = env.getThreadInfo();
    StackFrame frame = env.getCallerStackFrame();

    int slotIdx = frame.getLocalVariableSlotIndex(slotName);
    if (slotIdx >= 0) {
      return getAttributeList( env, frame.getLocalAttr(slotIdx));
    } else {
      env.throwException(NativeMethodInfo.CTX, "java.lang.RuntimeException", "local variable not found: " + slotName);
    }
    
    return MJIEnv.NULL;
  }
  
  @MJI
  public static void setElementAttribute__Ljava_lang_Object_2II__V (MJIEnv env, int clsRef,
                                                                    int oRef, int idx, int attr){
    if (oRef != MJIEnv.NULL){
      ElementInfo ei = env.getElementInfo(oRef);

      if (ei != null){
        if (ei.isArray()) {
          if (idx < ei.arrayLength()) {
            ei.setElementAttr(idx, Integer.valueOf(attr));
          } else {
            env.throwException(NativeMethodInfo.CTX,
                    "java.lang.ArrayIndexOutOfBoundsException", Integer.toString(idx));
          }
        } else {
          env.throwException(NativeMethodInfo.CTX,
                  "java.lang.RuntimeException", "not an array: " + ei);
        }
      } else {
        env.throwException(NativeMethodInfo.CTX, "java.lang.RuntimeException", "illegal reference value: " + oRef);
      }
    }
  }

  @MJI
  public static int getElementAttribute__Ljava_lang_Object_2I__I (MJIEnv env, int clsRef,
                                                                  int oRef, int idx){
    if (oRef != MJIEnv.NULL){
      ElementInfo ei = env.getElementInfo(oRef);

      if (ei != null) {
        if (ei.isArray()) {
          if (idx < ei.arrayLength()) {
            return getAttribute( env, ei.getElementAttr( idx));
          } else {
            env.throwException(NativeMethodInfo.CTX,
                    "java.lang.ArrayIndexOutOfBoundsException", Integer.toString(idx));
          }
        } else {
          env.throwException(NativeMethodInfo.CTX,
                  "java.lang.RuntimeException", "not an array: " + ei);
        }
      } else {
        env.throwException(NativeMethodInfo.CTX, "java.lang.RuntimeException", "illegal reference value: " + oRef);
      }
    }

    return 0;
  }

  @MJI
  public static void addElementAttribute__Ljava_lang_Object_2II__V (MJIEnv env, int clsRef,
                                                                    int oRef, int idx, int attr){
    if (oRef != MJIEnv.NULL){
      ElementInfo ei = env.getElementInfo(oRef);

      if (ei != null){
        if (ei.isArray()) {
          if (idx < ei.arrayLength()) {
            ei.addElementAttr(idx, Integer.valueOf(attr));
          } else {
            env.throwException(NativeMethodInfo.CTX,
                    "java.lang.ArrayIndexOutOfBoundsException", Integer.toString(idx));
          }
        } else {
          env.throwException(NativeMethodInfo.CTX,
                  "java.lang.RuntimeException", "not an array: " + ei);
        }
      } else {
        env.throwException(NativeMethodInfo.CTX, "java.lang.RuntimeException", "illegal reference value: " + oRef);
      }
    }
  }

  @MJI
  public static int getElementAttributes__Ljava_lang_Object_2I___3I (MJIEnv env, int clsRef,
                                                                        int oRef, int idx){
    if (oRef != MJIEnv.NULL){
      ElementInfo ei = env.getElementInfo(oRef);
      if (ei != null) {
        if (ei.isArray()) {
          if (idx < ei.arrayLength()) {
            return getAttributeList( env, ei.getElementAttr( idx));
          } else {
            env.throwException(NativeMethodInfo.CTX,
                    "java.lang.ArrayIndexOutOfBoundsException", Integer.toString(idx));
          }
        } else {
          env.throwException(NativeMethodInfo.CTX,
                  "java.lang.RuntimeException", "not an array: " + ei);
        }
      } else {
        env.throwException(NativeMethodInfo.CTX, "java.lang.RuntimeException", "illegal reference value: " + oRef);
      }
    }

    return MJIEnv.NULL;
  }

  
  /**
   *  deprecated, use getBoolean()
   */
  @MJI
  public static boolean randomBool (MJIEnv env, int clsObjRef) {
    //SystemState ss = env.getSystemState();
    //return (ss.random(2) != 0);

    return getBoolean____Z(env, clsObjRef);
  }



  /**
   * deprecated, use getInt
   */
  @MJI
  public static int random__I__I (MJIEnv env, int clsObjRef, int x) {
   return getInt__II__I( env, clsObjRef, 0, x);
  }

  static void boring__Z__V (MJIEnv env, int clsObjRef, boolean b) {
    env.getSystemState().setBoring(b);
  }

  @MJI
  public static boolean isRunningInJPF____Z(MJIEnv env, int clsObjRef) {
    return true;
  }

  @MJI
  public static boolean vmIsMatchingStates____Z(MJIEnv env, int clsObjRef) {
    return env.getVM().getStateSet() != null;
  }

  @MJI
  public static void storeTrace__Ljava_lang_String_2Ljava_lang_String_2__V (MJIEnv env, int clsObjRef,
                                      int filenameRef, int commentRef) {
    String fileName = env.getStringObject(null, filenameRef);
    String comment = env.getStringObject(null, commentRef);
    env.getVM().storeTrace(fileName, comment, config.getBoolean("trace.verbose", false));
  }

  @MJI
  public static void terminateSearch____V (MJIEnv env, int clsObjRef) {
    JPF jpf = env.getVM().getJPF();
    jpf.getSearch().terminate();
  }

  @MJI
  public static boolean isTraceReplay____Z (MJIEnv env, int clsObjRef) {
    return env.getVM().isTraceReplay();
  }

  @MJI
  public static boolean isShared__Ljava_lang_Object_2__Z (MJIEnv env, int clsObjRef, int objRef){
    if (objRef != MJIEnv.NULL){
      ElementInfo ei = env.getElementInfo(objRef);
      if (ei != null){
        return ei.isShared();
      }
    }
    
    return false;
  }
  
  @MJI
  public static void setShared__Ljava_lang_Object_2Z__V (MJIEnv env, int clsObjRef, int objRef, boolean isShared) {
    if (objRef != MJIEnv.NULL){
      ElementInfo ei = env.getElementInfo(objRef);
      if (ei != null){
        if (ei.getClassInfo() == ClassLoaderInfo.getCurrentSystemClassLoader().getClassClassInfo()) {
          // it's a class object, set static fields shared
          ei = env.getStaticElementInfo(objRef);
        }
        
        if (ei.isShared() != isShared) {
          ei = ei.getModifiableInstance();
          ei.setShared( isShared);
        }
      }
    }    
  }

  @MJI
  public static void freezeSharedness__Ljava_lang_Object_2Z__V (MJIEnv env, int clsObjRef, int objRef, boolean freeze) {
    if (objRef != MJIEnv.NULL){
      ElementInfo ei = env.getElementInfo(objRef);
      if (ei != null) {
        if (ei.getClassInfo() == ClassLoaderInfo.getCurrentSystemClassLoader().getClassClassInfo()) { 
          // it's a class object, freeze sharedness of static fields
          ei = env.getStaticElementInfo(objRef);
        }

        if (ei.isSharednessFrozen() != freeze) {
          ei = ei.getModifiableInstance();
          ei.freezeSharedness(freeze);
        }
      }
    }    
  }

  @MJI
  public static void setProperties___3Ljava_lang_String_2__V (MJIEnv env, int clsObjRef, int argRef) {
    if (argRef != MJIEnv.NULL) {
      Config conf = env.getConfig();

      int n = env.getArrayLength(NativeMethodInfo.CTX, argRef);
      for (int i=0; i<n; i++) {
        int pRef = env.getReferenceArrayElement(argRef, i);
        if (pRef != MJIEnv.NULL) {
          String p = env.getStringObject(NativeMethodInfo.CTX, pRef);
          config.parse(p);
        }
      }
    }
  }

  @MJI
  public static int getProperty__Ljava_lang_String_2__Ljava_lang_String_2 (MJIEnv env, int clsObjRef, int keyRef) {
    if (keyRef != MJIEnv.NULL){
      Config conf = env.getConfig();

      String key = env.getStringObject(null, keyRef);
      String val = config.getString(key);

      if (val != null){
        return env.newString(FeatureExprFactory.True(), val);
      } else {
        return MJIEnv.NULL;
      }

    } else {
      return MJIEnv.NULL;
    }
  }

  @MJI
  public static void printPathOutput__ZLjava_lang_String_2__V (MJIEnv env, int clsObjRef, boolean cond, int msgRef){
    if (cond){
      printPathOutput__Ljava_lang_String_2__V(env,clsObjRef,msgRef);
    }
  }

  @MJI
  public static void printPathOutput__Ljava_lang_String_2__V (MJIEnv env, int clsObjRef, int msgRef){
    VM vm = env.getVM();

    System.out.println();
    if (msgRef != MJIEnv.NULL){
      String msg = env.getStringObject(null, msgRef);
      System.out.println("~~~~~~~~~~~~~~~~~~~~~~~ begin program output at: " + msg);
    } else {
      System.out.println("~~~~~~~~~~~~~~~~~~~~~~~ begin path output");
    }

    for (Transition t : vm.getPath()) {
      String s = t.getOutput();
      if (s != null) {
        System.out.print(s);
      }
    }

    // we might be in the middle of a transition that isn't stored yet in the path
    String s = vm.getPendingOutput();
    if (s != null) {
      System.out.print(s);
    }

    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~ end path output");
  }


  // the JSON object initialization
  @MJI
  public static int createFromJSON__Ljava_lang_Class_2Ljava_lang_String_2__Ljava_lang_Object_2(
          MJIEnv env, int clsObjRef, int newObjClsRef, int jsonStringRef) {
    ThreadInfo ti = env.getThreadInfo();
    SystemState ss = env.getSystemState();
    
    String jsonString = env.getStringObject(null, jsonStringRef);    
    JSONLexer lexer = new JSONLexer(jsonString);
    JSONParser parser = new JSONParser(lexer);
    JSONObject jsonObject = parser.parse();
        
    if (jsonObject != null) {
      ClassInfo ci = env.getReferredClassInfo( NativeMethodInfo.CTX, newObjClsRef);
      
      // check if we need any class init (and hence reexecution) before creating any CGs
      if (jsonObject.requiresClinitExecution(ci,ti)){
        env.repeatInvocation();
        return MJIEnv.NULL;
      }

      if (!ti.isFirstStepInsn()) {
        // Top half - get and register CGs we need to set to fill object from JSON
        List<ChoiceGenerator<?>> cgList = CGCall.createCGList(jsonObject);
        if (cgList.isEmpty()){
        	
          return jsonObject.fillObject(NativeMethodInfo.CTX, env, ci, null, "");
          
        } else {
          for (ChoiceGenerator<?> cg : cgList) {
            ss.setNextChoiceGenerator(cg);
          }

          env.repeatInvocation();
          return MJIEnv.NULL;
        }
        
      } else {
        // Bottom half - fill object with JSON and current values of CGs
        ChoiceGenerator<?>[] cgs = ss.getChoiceGenerators();

        return jsonObject.fillObject(NativeMethodInfo.CTX, env, ci, cgs, "");
      }

    } else {
      return MJIEnv.NULL;
    }
  }
  
  @MJI
  public static int readObjectFromFile__Ljava_lang_Class_2Ljava_lang_String_2__Ljava_lang_Object_2(
          MJIEnv env, int clsObjRef, int newObjClsRef, int fileNameRef) {
	  FeatureExpr ctx = NativeMethodInfo.CTX;
    int typeNameRef = env.getReferenceField(ctx, newObjClsRef, "name").getValue();
    String typeName = env.getStringObject(null, typeNameRef);
    String fileName = env.getStringObject(null, fileNameRef);

    try {

      FileInputStream fis = new FileInputStream(fileName);
      ObjectInputStream ois = new ObjectInputStream(fis);
      Object javaObject = ois.readObject();
      String readObjectTypeName = javaObject.getClass().getCanonicalName();
      
      int readObjRef = ObjectConverter.JPFObjectFromJavaObject(ctx, env, javaObject);

      return readObjRef;
      
    } catch (ClinitRequired clix){
      env.repeatInvocation();
      return MJIEnv.NULL;
      
    } catch (IOException iox){
      throw new JPFException("failure reading object from file: " + fileName, iox);
    } catch (ClassNotFoundException cnfx){
      throw new JPFException("failure reading object from file: " + fileName, cnfx);      
    }
  }
  
  //--- those need to be kept in sync with the model side
  public static final int SEVERE = 1;
  public static final int WARNING = 2;
  public static final int INFO = 3;
  public static final int FINE = 4;
  public static final int FINER = 5;
  public static final int FINEST = 6;

  
  private static void log (JPFLogger logger, int logLevel, String msg){
    switch (logLevel){
    case SEVERE:
      logger.severe( msg);
      break;
    case WARNING:
      logger.warning( msg);
      break;
    case INFO:
      logger.info( msg);
      break;
    case FINE:
      logger.fine( msg);
      break;
    case FINER:
      logger.finer( msg);
      break;
    case FINEST:
      logger.finest( msg);
      break;
    default:
      throw new JPFException("unknown log level " + logLevel + " for logger " + logger.getName());
    }    
  }
  
  @MJI
  public static void log__Ljava_lang_String_2ILjava_lang_String_2__V (MJIEnv env, int clsObjRef,
      int loggerIdRef, int logLevel, int msgRef){
    String loggerId = env.getStringObject(null, loggerIdRef);
    String msg = env.getStringObject(null, msgRef);
    JPFLogger logger = JPF.getLogger(loggerId);
    
    log( logger, logLevel, msg);
  }

  @MJI
  public static void log__Ljava_lang_String_2ILjava_lang_String_2Ljava_lang_String_2__V (MJIEnv env, int clsObjRef,
      int loggerIdRef, int logLevel, int arg1Ref, int arg2Ref){
    String loggerId = env.getStringObject(null, loggerIdRef);
    String msg = env.getStringObject(null, arg1Ref) + env.getStringObject(null, arg2Ref);
    JPFLogger logger = JPF.getLogger(loggerId);
    
    log( logger, logLevel, msg);
  }

  @MJI
  public static void log__Ljava_lang_String_2ILjava_lang_String_2_3Ljava_lang_Object_2__V (MJIEnv env, int clsObjRef,
      int loggerIdRef, int logLevel, int fmtRef, int argsRef){
    String loggerId = env.getStringObject(null, loggerIdRef);
    String fmt = env.getStringObject(null, fmtRef);
    JPFLogger logger = JPF.getLogger(loggerId);

    Conditional<Integer>[] argRefs = env.getReferenceArrayObject( argsRef);
    Object[] args = new Object[argRefs.length];
    for (int i=0; i<args.length; i++){
      ElementInfo eiArg = env.getElementInfo(argRefs[i].getValue());
      if (eiArg.isStringObject()){
        args[i] = env.getStringObject(null, argRefs[i].getValue());
      } else if (eiArg.isBoxObject()){
        args[i] = eiArg.asBoxObject(); 
      } else {
        args[i] = eiArg.toString();
      }
    }
    
    String msg = String.format(fmt, args);
    
    log( logger, logLevel, msg);
  }
}
