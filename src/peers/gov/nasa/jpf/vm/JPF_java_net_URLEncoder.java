//
// Copyright (C) 2009 United States Government as represented by the
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import de.fosd.typechef.featureexpr.FeatureExpr;
import gov.nasa.jpf.annotation.MJI;

/**
 * native peer for java.net.URLEncoder forwarding
 */
@SuppressWarnings("deprecation")
public class JPF_java_net_URLEncoder extends NativePeer {

  // simple host delegation
  @MJI
  public int encode__Ljava_lang_String_2Ljava_lang_String_2__Ljava_lang_String_2(MJIEnv env, int clsObjRef,
          int sRef, int encRef, FeatureExpr ctx){
    String s = env.getStringObject(ctx, sRef);
    String enc = env.getStringObject(ctx, encRef);

    try {
      String e = URLEncoder.encode(s, enc);
      return env.newString(ctx, e);

    } catch (UnsupportedEncodingException x){
      env.throwException(ctx, "java.io.UnsupportedEncodingException", x.getMessage());
      return MJIEnv.NULL;
    }
  }
}
