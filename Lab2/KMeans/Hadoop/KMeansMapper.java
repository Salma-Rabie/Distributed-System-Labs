import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class KMeansMapper
        extends Mapper<LongWritable, Text, Text, Text> {

    private Centroid[] centroids;

    @Override
    protected void setup(Context context)
            throws IOException, InterruptedException {

        String c0 =
                context.getConfiguration()
                        .get("centroid0");

        String c1 =
                context.getConfiguration()
                        .get("centroid1");

        String c2 =
                context.getConfiguration()
                        .get("centroid2");

        centroids = new Centroid[] {
                new Centroid(c0),
                new Centroid(c1),
                new Centroid(c2)
        };
    }

    @Override
    public void map(
            LongWritable key,
            Text value,
            Context context)
            throws IOException, InterruptedException {

        String line =
                value.toString().trim();

        if (line.isEmpty()) {
            return;
        }

        String[] parts =
                line.split(",");

        double[] point =
                new double[parts.length];

        for (int i = 0; i < parts.length; i++) {

            point[i] =
                    Double.parseDouble(
                            parts[i]
                    );
        }

        int nearest = 0;

        double bestDistance =
                centroids[0]
                        .distance(point);

        for (int i = 1;
             i < centroids.length;
             i++) {

            double d =
                    centroids[i]
                            .distance(point);

            if (d < bestDistance) {

                bestDistance = d;
                nearest = i;
            }
        }

        context.write(
                new Text(
                        String.valueOf(
                                nearest
                        )
                ),
                new Text(line)
        );
    }
}
