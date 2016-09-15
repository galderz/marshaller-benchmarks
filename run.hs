#!/usr/bin/env stack
-- stack --install-ghc runghc --package turtle

{-# LANGUAGE OverloadedStrings #-}

import Turtle

javaJar :: Text
javaJar =
    "java -jar"


marshallers :: [Text]
marshallers =
    ["master-marshaller", "new-marshaller"]


jarPath :: Text -> Text
jarPath prj =
    -- format (""%s%"/target/benchmarks-"%s%".jar") prj prj
    format ("benchmarks-"%s%".jar") prj


isolatedThreads :: [Int]
isolatedThreads =
    [16]
    -- [16, 32, 64, 128]
    -- [1]


groupedThreads :: [Int]
groupedThreads =
    [8]
    -- [8, 16, 32, 64]
    -- [1]


jvmArgsPrepend :: Text
jvmArgsPrepend =
    "-jvmArgsPrepend \"-Xmx4G -Xms4G -Xss512k\""


-- To speed up testing, add: -i 1 -wi 1
isolatedCmd :: Int -> Text -> Text
isolatedCmd t m =
    format (""%s%" "%s%" -t "%d%" -e Group "%s%"")
        javaJar
        (jarPath m)
        t
        jvmArgsPrepend


-- To speed up testing, add: -i 1 -wi 1
groupedCmd :: Int -> Text -> Text
groupedCmd t m =
    format (""%s%" "%s%" -tg "%d%","%d%" -e Isolated "%s%"")
        javaJar
        (jarPath m)
        t
        t
        jvmArgsPrepend


isolatedCmds :: Shell ExitCode
isolatedCmds =
    do  t <- select isolatedThreads
        m <- select marshallers
        liftIO (exec (isolatedCmd t m))


groupedCmds :: Shell ExitCode
groupedCmds =
    do  t <- select groupedThreads
        m <- select marshallers
        liftIO (exec (groupedCmd t m))



main :: IO ()
main =
    do  sh isolatedCmds
        sh groupedCmds


exec :: MonadIO io => Text -> io ExitCode
exec cmd =
    shell cmd empty
