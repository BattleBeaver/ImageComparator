package service

import java.awt.Color
import java.io.{ByteArrayOutputStream, File}
import javax.imageio.ImageIO

import model.Pixel

import scala.collection.mutable.ArrayBuffer

/**
  * Created by beaver on 11.12.16.
  */
class ImageService {

  def compare(fromFile: File, toFile: File): Array[Byte] = {
    val imageFrom = ImageIO.read(fromFile)
    val imageTo = ImageIO.read(toFile)

    val f_height = imageFrom.getHeight
    val f_width = imageFrom.getWidth

    val t_height = imageFrom.getHeight
    val t_width = imageFrom.getWidth

    val result = imageTo

    val invalidPixels = new ArrayBuffer[Pixel]
    for (x <- 0 until f_width) {
      for (y <- 0 until f_height) {
        if (imageFrom.getRGB(x, y) != imageTo.getRGB(x, y)) {
          println(s"Pixels at x = $x and y = $y are different")
          invalidPixels += Pixel(x, y)
        }
      }
    }

    val x1 = invalidPixels.map(_.x).max
    val x2 = invalidPixels.map(_.x).min

    val y1 = invalidPixels.map(_.y).max
    val y2 = invalidPixels.map(_.y).min

    val graphics = result.createGraphics()
    graphics.setColor(Color.RED);

    graphics.drawRect(x2 - 5, y2 - 5, x1 - x2 + 10, y1 - y2 + 10)
    graphics.dispose()

    val baos: ByteArrayOutputStream = new ByteArrayOutputStream()
    ImageIO.write(result, "png", new File("/home/beaver/Pictures/test/1.png"))
    ImageIO.write(result, "png", baos)
    baos.toByteArray
  }

}
