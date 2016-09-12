package org.infinispan.jmhbenchmarks.marshaller;

import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

@State(Scope.Thread)
public class KeyGenerator {

   private static final int randomSeed = 17;
   private static final int keySpaceSize = 1_000_000;
   private static final int keyObjectSize = 20;
   private static final int valueObjectSize = 1_000;

   private RandomSequence keySequence;
   private ThreadLocalRandom r = ThreadLocalRandom.current();

   @Setup
   public void createSpace() {
      JDKRandomGenerator jdkRandomGenerator = new JDKRandomGenerator();
      jdkRandomGenerator.setSeed(randomSeed);
      RandomDataGenerator rndg = new RandomDataGenerator(jdkRandomGenerator);
      keySequence = new RandomSequence(rndg, keySpaceSize, keyObjectSize);
   }

   public Object getNextKey() {
      return keySequence.nextValue();
   }

   public Object getNextValue() {
      byte[] bytes = new byte[valueObjectSize];
      r.nextBytes(bytes);
      return bytes;
   }

   private class RandomSequence {
      private final Object[] list;
      private final int spaceSize;

      private int idx = -1;

      RandomSequence(RandomDataGenerator rndg, int spaceSize, int objectSize) {
         this.list = new Object[spaceSize];
         this.spaceSize = spaceSize;
         for (int i = 0; i < spaceSize; i++)
            list[i] = rndg.nextHexString(objectSize);
      }

      Object nextValue() {
         idx++;
         if (idx == spaceSize)
            idx = 0;

         return list[idx];
      }

   }

}
