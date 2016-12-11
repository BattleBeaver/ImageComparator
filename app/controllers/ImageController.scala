package controllers

import play.api.mvc.{Action, Controller}
import service.ImageComparator

/**
  * Created by beaver on 11.12.16.
  */
class ImageController extends Controller {

  def compare = Action(parse.multipartFormData) { request =>
    val fromImage = request.body.file("from").get
    val toImage = request.body.file("to").get

    val comparator = new ImageComparator(fromImage.ref.file, toImage.ref.file)


    Ok(comparator.compare()).as("image/png").withHeaders("Content-Disposition" -> "attachment")
  }
}
