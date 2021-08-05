//package org.springframework.objenesis.strategy;
//
//import java.io.Serializable;
//import org.springframework.objenesis.instantiator.ObjectInstantiator;
////import org.springframework.objenesis.instantiator.android.Android10Instantiator;
////import org.springframework.objenesis.instantiator.android.Android17Instantiator;
////import org.springframework.objenesis.instantiator.android.Android18Instantiator;
////import org.springframework.objenesis.instantiator.basic.AccessibleInstantiator;
////import org.springframework.objenesis.instantiator.basic.ObjectInputStreamInstantiator;
////import org.springframework.objenesis.instantiator.gcj.GCJInstantiator;
////import org.springframework.objenesis.instantiator.jrockit.JRockitLegacyInstantiator;
////import org.springframework.objenesis.instantiator.perc.PercInstantiator;
////import org.springframework.objenesis.instantiator.sun.SunReflectionFactoryInstantiator;
////import org.springframework.objenesis.instantiator.sun.UnsafeFactoryInstantiator;
//import org.springframework.objenesis.strategy.BaseInstantiatorStrategy;
//import org.springframework.objenesis.strategy.PlatformDescription;
//
//public class StdInstantiatorStrategy extends BaseInstantiatorStrategy {
//    public StdInstantiatorStrategy() {
//    }
//
//    public <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> type) {
//        return (ObjectInstantiator)(!PlatformDescription.isThisJVM("Java HotSpot") && !PlatformDescription.isThisJVM("OpenJDK")?(!PlatformDescription.isThisJVM("BEA")?(PlatformDescription.isThisJVM("Dalvik")?ryInstantiator(type)))):(!PlatformDescription.VM_VERSION.startsWith("1.4") || PlatformDescription.VENDOR_VERSION.startsWith("R") || PlatformDescription.VM_INFO != null && PlatformDescription.VM_INFO.startsWith("R25.1") && PlatformDescription.VM_INFO.startsWith("R25.2")?new SunReflectionFactoryInstantiator(type):new JRockitLegacyInstantiator(type))):(PlatformDescription.isGoogleAppEngine()?(Serializable.class.isAssignableFrom(type)?new ObjectInputStreamInstantiator(type):new AccessibleInstantiator(type)):new SunReflectionFactoryInstantiator(type)));
//    }
//}