import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import org.apache.hadoop.mapreduce.Job;

import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class KMeansDriver {

    public static void main(String[] args)
            throws Exception {

        long start =
            System.nanoTime();

        String[] centroids = {

            "5,3,1,0",

            "5,2,4,1",

            "6,3,5,2"
        };

        for (int iter = 1;
             iter <= 5;
             iter++) {

            Configuration conf =
                new Configuration();

            conf.set(
              "centroid0",
              centroids[0]);

            conf.set(
              "centroid1",
              centroids[1]);

            conf.set(
              "centroid2",
              centroids[2]);

            Job job =
                Job.getInstance(
                    conf,
                    "KMeans Iteration "
                    + iter);

            job.setJarByClass(
                KMeansDriver.class);

            job.setMapperClass(
                KMeansMapper.class);

            job.setReducerClass(
                KMeansReducer.class);

            job.setOutputKeyClass(
                Text.class);

            job.setOutputValueClass(
                Text.class);

            FileInputFormat
              .addInputPath(
                 job,
                 new Path(args[0]));

            Path outputPath =
              new Path(
                args[1] + iter);

            FileOutputFormat
              .setOutputPath(
                  job,
                  outputPath);

            job.waitForCompletion(true);

            FileSystem fs =
                FileSystem.get(conf);

            BufferedReader br =
                new BufferedReader(
                    new InputStreamReader(
                        fs.open(
                          new Path(
                           outputPath
                           + "/part-r-00000"
                          )
                        )
                    )
                );

            System.out.println(
                "\nIteration "
                + iter);

            String line;

            while ((line =
                  br.readLine())
                  != null) {

                System.out.println(
                  line);

                String[] parts =
                    line.split("\t");

                int cluster =
                  Integer.parseInt(
                     parts[0]);

                centroids[cluster] =
                    parts[1];
            }

            br.close();
        }

        long end =
            System.nanoTime();

        System.out.println(
           "\nTotal Runtime = "
           + ((end-start)/1e9)
           + " seconds"
        );

        System.out.println(
            "\nFinal Centroids:"
        );

        for (int i = 0;
             i < centroids.length;
             i++) {

            System.out.println(
                "Cluster "
                + i
                + " = "
                + centroids[i]
            );
        }
    }
}
