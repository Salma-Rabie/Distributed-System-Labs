import scala.io.Source

object Accuracy {

  def distance(a:Array[Double], b:Array[Double]): Double = {
    math.sqrt(
      a.zip(b)
        .map{case(x,y) => math.pow(x-y,2)}
        .sum
    )
  }

  def main(args:Array[String]):Unit = {

    val centroids = Array(
      Array(5.01,3.42,1.46,0.24),
      Array(5.94,2.77,4.26,1.32),
      Array(6.58,2.97,5.55,2.03)
    )

    val counts =
      scala.collection.mutable.Map[(Int,String),Int]()
        .withDefaultValue(0)

    for(line <- Source.fromFile("iris_label.data").getLines()) {

      val parts = line.split(",")

      val point =
        parts.take(4).map(_.toDouble)

      val label =
        parts(4)

      val cluster =
        centroids.indices.minBy(
          c => distance(point, centroids(c))
        )

      counts((cluster,label)) += 1
    }

    println("\nCluster Distribution\n")

    counts.foreach(println)
  }
}
