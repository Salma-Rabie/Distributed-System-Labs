/**
 * Illustrates a WordCount in Java using Apache Spark
 */
package com.geekcap.javaworld.sparkexample;

import java.util.Arrays;

import scala.Tuple2;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

public class WordCount {

    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Usage: WordCount <input> <output>");
            System.exit(1);
        }

        String inputFile = args[0];
        String outputFile = args[1];

        // Create Spark configuration
        SparkConf conf = new SparkConf().setAppName("WordCount");

        // Create Spark context
        JavaSparkContext sc = new JavaSparkContext(conf);

        // Read input file
        JavaRDD<String> input = sc.textFile(inputFile);

        // Split lines into words
        JavaRDD<String> words = input.flatMap(
            (FlatMapFunction<String, String>) line ->
                Arrays.asList(line.split("\\s+")).iterator()
        );

        // Convert each word into (word,1)
        JavaPairRDD<String, Integer> ones = words.mapToPair(
            (PairFunction<String, String, Integer>) word ->
                new Tuple2<>(word, 1)
        );

        // Count occurrences
        JavaPairRDD<String, Integer> counts = ones.reduceByKey(
            (Function2<Integer, Integer, Integer>) Integer::sum
        );

        // Save output
        counts.saveAsTextFile(outputFile);

        sc.close();
    }
}
