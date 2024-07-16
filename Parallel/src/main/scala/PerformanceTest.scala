package parf
import scala.compiletime.uninitialized

object PerformanceTest {
  private var startTime: Long = uninitialized

  def perstart(): Unit = {
    startTime = System.nanoTime()
  }

  def perend(): Unit = {
    val endTime = System.nanoTime()
    val timeTaken = (endTime - startTime) / 1e6d // Convert to milliseconds
    println(s"Time taken: $timeTaken ms")
  }
}