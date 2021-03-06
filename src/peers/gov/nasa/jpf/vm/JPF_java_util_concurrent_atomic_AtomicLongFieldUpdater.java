//
// Copyright (C) 2007 United States Government as represented by the
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

import cmu.conditional.One;
import de.fosd.typechef.featureexpr.FeatureExpr;
import gov.nasa.jpf.annotation.MJI;


/**
 * a full peer for the AtomicLongFieldUpdater
 */
@SuppressWarnings("deprecation")
public class JPF_java_util_concurrent_atomic_AtomicLongFieldUpdater extends AtomicFieldUpdater {

  @MJI
  public void $init__Ljava_lang_Class_2Ljava_lang_String_2__V (MJIEnv env, int objRef,
                                 int tClsObjRef, int fNameRef, FeatureExpr ctx) {

    // direct Object subclass, so we don't have to call a super ctor

    ClassInfo ci = env.getReferredClassInfo(ctx, tClsObjRef);
    String fname = env.getStringObject(ctx, fNameRef);
    FieldInfo fi = ci.getInstanceField(fname);

    ClassInfo fci = fi.getTypeClassInfo();

    if (!fci.isPrimitive() || !fci.getName().equals("long")) {
      // that's also just an approximation, but we need to check
      env.throwException(ctx, "java.lang.RuntimeException", "wrong field type");
    }

    int fidx = fi.getFieldIndex();
    env.setIntField(ctx, objRef, "fieldId", One.valueOf(fidx));
  }

  @MJI
  public boolean compareAndSet__Ljava_lang_Object_2JJ__Z
  (MJIEnv env, int objRef, int tRef, long fExpect, long fUpdate, FeatureExpr ctx){

    if (isNewPorFieldBoundary(env, objRef, tRef) && createAndSetFieldCG(env, tRef)) {
      return false;  // re-executed anyways
    }

    
	int fidx = env.getIntField(objRef, "fieldId").getValue().intValue();
    ElementInfo ei = env.getModifiableElementInfo(tRef);
    FieldInfo fi = env.getClassInfo(tRef).getInstanceField(fidx);

    long v = ei.getLongField(fi).getValue();
    if (v == fExpect) {
      ei.setLongField(ctx, fi, new One<>(fUpdate));
      return true;
    } else {
      return false;
    }
  }

  @MJI
  public boolean weakCompareAndSet__Ljava_lang_Object_2JJ__Z
  (MJIEnv env, int objRef, int tRef, long fExpect, long fUpdate, FeatureExpr ctx){
    return(compareAndSet__Ljava_lang_Object_2JJ__Z(env, objRef, tRef, fExpect, fUpdate, ctx));
  }

  @MJI
  public void set__Ljava_lang_Object_2J__
  (MJIEnv env, int objRef, int tRef, long fNewValue, FeatureExpr ctx){

    if (isNewPorFieldBoundary(env, objRef, tRef) && createAndSetFieldCG(env, tRef)) {
      return;  // re-executed anyways
    }

    
	int fidx = env.getIntField(objRef, "fieldId").getValue().intValue();
    ElementInfo ei = env.getModifiableElementInfo(tRef);
    FieldInfo fi = env.getClassInfo(tRef).getInstanceField(fidx);

    ei.setLongField(ctx, fi, new One<>(fNewValue));
  }

  @MJI
  public void lazySet__Ljava_lang_Object_2J__
  (MJIEnv env, int objRef, int tRef, long fNewValue, FeatureExpr ctx){
     set__Ljava_lang_Object_2J__(env, objRef, tRef, fNewValue, ctx);
  }

  @MJI
  public long get__Ljava_lang_Object_2__J
  (MJIEnv env, int objRef, int tRef, FeatureExpr ctx){

    if (isNewPorFieldBoundary(env, objRef, tRef) && createAndSetFieldCG(env, tRef)) {
      return 0;  // re-executed anyways
    }

    int fidx = env.getIntField(objRef, "fieldId").getValue().intValue();
    ElementInfo ei = env.getElementInfo(tRef);
    FieldInfo fi = env.getClassInfo(tRef).getInstanceField(fidx);

    return ei.getLongField(fi).getValue();
  }

  @MJI
  public long getAndSet__Ljava_lang_Object_2J__J
  (MJIEnv env, int objRef, int tRef, long fNewValue, FeatureExpr ctx){

    if (isNewPorFieldBoundary(env, objRef, tRef) && createAndSetFieldCG(env, tRef)) {
      return 0;  // re-executed anyways
    }

    
	int fidx = env.getIntField(objRef, "fieldId").getValue().intValue();
    ElementInfo ei = env.getModifiableElementInfo(tRef);
    FieldInfo fi = env.getClassInfo(tRef).getInstanceField(fidx);
    long result = ei.getLongField(fi).getValue();

    ei.setLongField(ctx, fi, new One<>(fNewValue));

    return result;
  }

  @MJI
  public long getAndAdd__Ljava_lang_Object_2J__J
  (MJIEnv env, int objRef, int tRef, long fDelta, FeatureExpr ctx){

    if (isNewPorFieldBoundary(env, objRef, tRef) && createAndSetFieldCG(env, tRef)) {
      return 0;  // re-executed anyways
    }

    
	int fidx = env.getIntField(objRef, "fieldId").getValue().intValue();
    ElementInfo ei = env.getModifiableElementInfo(tRef);
    FieldInfo fi = env.getClassInfo(tRef).getInstanceField(fidx);
    long result = ei.getLongField(fi).getValue();

    ei.setLongField(ctx, fi, new One<>(result + fDelta));

    return result;
  }
}
