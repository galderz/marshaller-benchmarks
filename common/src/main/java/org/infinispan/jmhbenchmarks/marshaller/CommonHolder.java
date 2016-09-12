package org.infinispan.jmhbenchmarks.marshaller;

import java.util.concurrent.atomic.LongAdder;

public abstract class CommonHolder {

   final LongAdder getBytes = new LongAdder();
   final LongAdder getCount = new LongAdder();
   final LongAdder putBytes = new LongAdder();
   final LongAdder putCount = new LongAdder();

   public void getBytesDone(long bytes) {
      getBytes.add(bytes);
      getCount.increment();
   }

   public void putBytesDone(long bytes) {
      putBytes.add(bytes);
      putCount.increment();
   }

   public void showBytes() {
      System.out.println("Total get command bytes: " + getBytes.longValue());
      System.out.println("Total get commands: " + getCount.longValue());
      System.out.println("Total put command bytes: " + putBytes.longValue());
      System.out.println("Total put commands: " + putCount.longValue());
   }

}
