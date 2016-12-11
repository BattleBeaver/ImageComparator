package controllers

import play.api.mvc.{Action, Controller}
import javax.inject.Inject

import service.ImageService

/**
  * Created by beaver on 11.12.16.
  */
class ImageController @Inject()(imageService: ImageService) extends Controller {

  def compare = Action(parse.multipartFormData) { request =>
    val fromImage = request.body.file("from").get
    val toImage = request.body.file("to").get



    Ok(imageService.compare(fromImage.ref.file, toImage.ref.file)).as("image/png").withHeaders("Content-Disposition" -> "attachment")
  }
}
