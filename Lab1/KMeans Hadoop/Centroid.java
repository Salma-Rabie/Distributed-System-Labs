public class Centroid {

    private double[] values;

    public Centroid(double[] values) {
        this.values = values;
    }

    public Centroid(String line) {

        String[] parts = line.split(",");

        values = new double[parts.length];

        for (int i = 0; i < parts.length; i++) {
            values[i] = Double.parseDouble(parts[i]);
        }
    }

    public double[] getValues() {
        return values;
    }

    public double distance(double[] point) {

        double sum = 0;

        for (int i = 0; i < values.length; i++) {

            double diff = point[i] - values[i];

            sum += diff * diff;
        }

        return Math.sqrt(sum);
    }
}
