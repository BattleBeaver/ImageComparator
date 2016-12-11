package service

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.{ByteArrayOutputStream, File}
import javax.imageio.ImageIO

import model.Pixel

import scala.collection.mutable.ArrayBuffer

/**
  * Created by beaver on 11.12.16.
  */
class ImageComparator(fromFile: File, toFile: File) {

  val imageFrom: BufferedImage = ImageIO.read(fromFile)
  val imageTo: BufferedImage = ImageIO.read(toFile)

  val result: BufferedImage = imageTo

  val invalidPixels = new ArrayBuffer[Pixel]

  val f_height: Int = imageFrom.getHeight
  val f_width: Int = imageFrom.getWidth

  val t_height: Int = imageFrom.getHeight
  val t_width: Int = imageFrom.getWidth

  def compare(): Array[Byte] = {
    for (x <- 0 until f_width) {
      for (y <- 0 until f_height) {
        if (imageFrom.getRGB(x, y) != imageTo.getRGB(x, y)) {
          //println(s"Pixels at x = $x and y = $y are different")
          invalidPixels += Pixel(x, y)
        }
      }
    }

    println(invalidPixels)
    println()
    println(aggregatedInvalidPixels.mkString("\n"))

    highlightAllGroups(aggregatedInvalidPixels)

    val baos: ByteArrayOutputStream = new ByteArrayOutputStream()
    ImageIO.write(result, "png", new File("/home/beaver/Pictures/test/1.png"))
    ImageIO.write(result, "png", baos)
    baos.toByteArray
  }

  def aggregatedInvalidPixels: ArrayBuffer[ArrayBuffer[Pixel]] = {
    val groupedPixels = new ArrayBuffer[ArrayBuffer[Pixel]]
    var tmpPixelBuffer = new ArrayBuffer[Pixel]


    val iter = invalidPixels.iterator

    var tmpPrev:Pixel = null

    while (iter.hasNext) {
      val curr = iter.next

      if (tmpPrev != null && !curr.isNeighborTo(tmpPrev)) {
        tmpPixelBuffer = new ArrayBuffer[Pixel]
      }
      tmpPixelBuffer += curr


      if (!iter.hasNext) {
        groupedPixels += tmpPixelBuffer
        tmpPixelBuffer = new ArrayBuffer[Pixel]
        return groupedPixels
      } else {
        val next = iter.next

        if (curr.eq(Pixel(288, 79)) || next.eq(Pixel(288, 79))) {
          println("here we are")
        }
        if (curr isNeighborTo next)
          tmpPixelBuffer += next
        else {
          groupedPixels += tmpPixelBuffer
          tmpPixelBuffer = new ArrayBuffer[Pixel]
          tmpPixelBuffer += next
        }
        tmpPrev = next
      }
    }

    groupedPixels
  }

  def highlightGroup(group: ArrayBuffer[Pixel]): Unit = {

    val x1 = group.map(_.x).max
    val x2 = group.map(_.x).min

    val y1 = group.map(_.y).max
    val y2 = group.map(_.y).min


    val graphics = result.createGraphics()
    graphics.setColor(Color.RED)

    graphics.drawRect(x2 - 5, y2 - 5, x1 - x2 + 10, y1 - y2 + 10)
    graphics.dispose()
  }

  def highlightAllGroups(buffer: ArrayBuffer[ArrayBuffer[Pixel]]): Unit = buffer.foreach(highlightGroup)

}
