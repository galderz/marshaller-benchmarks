package org.infinispan.jmhbenchmarks.marshaller;

import java.io.IOException;

import org.infinispan.commands.CommandsFactory;
import org.infinispan.commands.remote.ClusteredGetCommand;
import org.infinispan.commands.remote.SingleRpcCommand;
import org.infinispan.commands.write.PutKeyValueCommand;
import org.infinispan.commons.marshall.StreamingMarshaller;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.metadata.EmbeddedMetadata;
import org.infinispan.metadata.Metadata;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

@State(Scope.Benchmark)
public class NewInfinispanHolder extends CommonHolder {

   static final String cfg = System.getProperty("infinispan.cfg", "dist-sync.xml");

   EmbeddedCacheManager mgr;

   CommandsFactory mkCmd;
   StreamingMarshaller marshaller;

   @Setup
   public void initializeState() throws IOException {
      mgr = new DefaultCacheManager(cfg);
      mgr.start();

      mkCmd = mgr.getCache().getAdvancedCache().getComponentRegistry().getComponent(CommandsFactory.class);
      marshaller = mgr.getGlobalComponentRegistry().getComponent(StreamingMarshaller.class);
   }

   @TearDown
   public void shutdownState() {
      mgr.stop();
      showBytes();
   }

   SingleRpcCommand mkPutCmd(Object key, Object value) {
      Metadata meta = new EmbeddedMetadata.Builder().lifespan(-1).maxIdle(-1).build();
      // 524288 is flags for IGNORE_RETURN_VALUES
      PutKeyValueCommand cmd = mkCmd.buildPutKeyValueCommand(key, value, meta, 524288);
      return mkCmd.buildSingleRpcCommand(cmd);
   }

   ClusteredGetCommand mkGetCmd(Object key) {
      return mkCmd.buildClusteredGetCommand(key, 0, false, null);
   }

}
