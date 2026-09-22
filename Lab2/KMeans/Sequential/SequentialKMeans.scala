import scala.io.Source

object SequentialKMeans {

  def distance(a: Array[Double], b: Array[Double]): Double = {
    math.sqrt(
      a.zip(b)
        .map { case (x, y) => math.pow(x - y, 2) }
        .sum
    )
  }

  def main(args: Array[String]): Unit = {

    val data = Source
      .fromFile(args(0))
      .getLines()
      .filter(_.trim.nonEmpty)
      .map(_.split(",").map(_.toDouble))
      .toArray

    var centroids = Array(
      Array(5.0, 3.0, 1.0, 0.0),
      Array(5.0, 2.0, 4.0, 1.0),
      Array(6.0, 3.0, 5.0, 2.0)
    )

    val iterations = 5

    val start = System.nanoTime()

    for (iter <- 1 to iterations) {

      val clusters =
        Array.fill(3)(
          scala.collection.mutable.ArrayBuffer[Array[Double]]()
        )

      for (point <- data) {

        val nearest =
          centroids.indices.minBy(
            c => distance(point, centroids(c))
          )

        clusters(nearest) += point
      }

      for (i <- centroids.indices) {

        if (clusters(i).nonEmpty) {

          val dimension =
            clusters(i)(0).length

          centroids(i) =
            Array.tabulate(dimension) { d =>
              clusters(i).map(_(d)).sum /
                clusters(i).size
            }
        }
      }

      println(s"\nIteration $iter")

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
    
    println("\nFinal Centroids:")

	centroids.zipWithIndex.foreach {
	  case (c,id) =>
	    println(
	      s"Cluster $id = ${c.mkString(",")}"
	    )
}
  }
}
