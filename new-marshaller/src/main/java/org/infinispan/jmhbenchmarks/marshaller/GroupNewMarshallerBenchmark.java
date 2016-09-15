package org.infinispan.jmhbenchmarks.marshaller;

import org.infinispan.commands.remote.ClusteredGetCommand;
import org.infinispan.commands.remote.SingleRpcCommand;
import org.infinispan.commons.io.ByteBuffer;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.infra.Blackhole;

@Fork(value = 1, jvmArgs = {
      "-XX:+HeapDumpOnOutOfMemoryError",
      "-XX:HeapDumpPath=/tmp/java_heap",
      "-Djava.net.preferIPv4Stack=true",
})
@BenchmarkMode(Mode.Throughput)
public class GroupNewMarshallerBenchmark {

   @Benchmark
   @Group("newMarshallerGetPut")
   public void newMarshallerGet(NewInfinispanHolder ih, Blackhole bh, KeyGenerator kg) throws Exception {
      Object key = kg.getNextKey();
      ClusteredGetCommand cmdToBytes = ih.mkGetCmd(key);
      ByteBuffer buf = ih.marshaller.objectToBuffer(cmdToBytes);
      int size = buf.getBuf().length;
      Object cmdFromBytes = ih.marshaller.objectFromByteBuffer(buf.getBuf(), 0, size);
      bh.consume(cmdFromBytes);
      ih.getBytesDone(size);
   }

   @Benchmark
   @Group("newMarshallerGetPut")
   public void newMarshallerPut(NewInfinispanHolder ih, Blackhole bh, KeyGenerator kg) throws Exception {
      Object key = kg.getNextKey();
      Object value = kg.getNextValue();
      SingleRpcCommand cmdToBytes = ih.mkPutCmd(key, value);
      ByteBuffer buf = ih.marshaller.objectToBuffer(cmdToBytes);
      int size = buf.getBuf().length;
      Object cmdFromBytes = ih.marshaller.objectFromByteBuffer(buf.getBuf(), 0, size);
      bh.consume(cmdFromBytes);
      ih.putBytesDone(buf.getBuf().length);
   }

}
