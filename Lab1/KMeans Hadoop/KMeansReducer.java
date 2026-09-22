import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class KMeansReducer
        extends Reducer<Text,Text,Text,Text> {

    @Override
    public void reduce(
            Text key,
            Iterable<Text> values,
            Context context)
            throws IOException,
            InterruptedException {

        double[] sum = null;

        int count = 0;

        for (Text value : values) {

            String[] parts =
                    value.toString().split(",");

            if (sum == null) {

                sum =
                  new double[parts.length];
            }

            for (int i = 0;
                 i < parts.length;
                 i++) {

                sum[i] +=
                   Double.parseDouble(parts[i]);
            }

            count++;
        }

        StringBuilder centroid =
                new StringBuilder();

        for (int i = 0;
             i < sum.length;
             i++) {

            centroid.append(
              sum[i] / count
            );

            if (i != sum.length - 1)
                centroid.append(",");
        }

        context.write(
            key,
            new Text(
                centroid.toString()
            )
        );
    }
}
