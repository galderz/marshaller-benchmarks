Infinispan Marshaller Benchmarks
================================

JMH-based benchmarks for comparing performance of different
Infinispan marshallers.

Before running benchmarks
-------------------------

In your developer machine, check out
[this branch](https://github.com/galderz/infinispan/tree/t_6906)
and build it by executing:

    $ mvn -DskipTests=true clean install --projects core -am

In your developer machine, build this project by calling:

    $ mvn -DskipTests=true clean install

Copy over benchmark jar files and run script to server where to run benchmarks:

    $ scp master-marshaller/target/benchmarks-master-marshaller.jar ...
    $ scp new-marshaller/target/benchmarks-new-marshaller.jar ...
    $ scp run.hs ...

In the server, install
[Haskell Stack](https://docs.haskellstack.org/en/stable/README/#how-to-install)
which is required to run the root `run.hs` file

Running benchmarks
------------------

In server, execute (first time it's executed it'll install GHC and other utils,
so it will take longer) :

    $ ./run.hs
