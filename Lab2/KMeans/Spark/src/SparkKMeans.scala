import org.apache.spark.sql.SparkSession

object SparkKMeans {

  def distance(a: Array[Double], b: Array[Double]): Double = {
    math.sqrt(
      a.zip(b)
        .map { case (x, y) => math.pow(x - y, 2) }
        .sum
    )
  }

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("SparkKMeans")
      .master("local[*]")
      .getOrCreate()

    val sc = spark.sparkContext

    val data = sc.textFile(args(0))
      .filter(_.trim.nonEmpty)
      .map(_.split(",").map(_.trim.toDouble))
      .cache()

    var centroids = Array(
      Array(5.0, 3.0, 1.0, 0.0),
      Array(5.0, 2.0, 4.0, 1.0),
      Array(6.0, 3.0, 5.0, 2.0)
    )

    val iterations = 5

    val start = System.nanoTime()

    for (i <- 1 to iterations) {

      val clusters = data.map { point =>

        val nearest = centroids.indices.minBy(
          c => distance(point, centroids(c))
        )

        (nearest, point)
      }

      val newCentroids = clusters
        .groupByKey()
        .mapValues { points =>

          val pts = points.toArray

          val dimension = pts(0).length

          Array.tabulate(dimension) { d =>
            pts.map(_(d)).sum / pts.length
          }
        }
        .collect()

      for ((cluster, centroid) <- newCentroids) {
        centroids(cluster) = centroid
      }

      println("\n===================")
      println(s"Iteration $i")
      println("===================")

      centroids.zipWithIndex.foreach {
        case (c, id) =>
          println(
            s"Centroid $id = ${c.mkString(",")}"
          )
      }
    }

    val end = System.nanoTime()

    println(
      "\nRuntime = " +
        ((end - start) / 1e9) +
        " seconds"
    )

    spark.stop()
  }
}
