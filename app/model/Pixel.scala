package model

/**
  * Created by beaver on 11.12.16.
  */
case class Pixel(x: Int, y: Int) {
  def isNeighborTo(pixel: Pixel): Boolean = (x - 50 to x + 50).contains(pixel.x) && (y - 50 to y + 50).contains(pixel.y)
}
