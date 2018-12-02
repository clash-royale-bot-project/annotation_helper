package ru.tolsi.cs

import java.io.{File, FilenameFilter, PrintWriter}

import com.sksamuel.scrimage.Image
import com.sksamuel.scrimage.ScaleMethod

import scala.io.Source
import scala.util.control.NonFatal

// Создает файл аннотации с полным размером файла
object Annotate {
  case class Area(x0: Int, y0: Int, x1: Int, y1: Int) {
    def scale(v: Float): Area = {
      def scaledInt(i: Int): Int = (i * v).toInt
      Area(
        scaledInt(x0),
        scaledInt(y0),
        scaledInt(x1),
        scaledInt(y1),
      )
    }

  }
  case class AnnotationObject(t: String, area: Area)
  def createFileContent(folder: String, filename: String, path: String, width: Int, height: Int, objects: Seq[AnnotationObject]): String =
    s"""<annotation>
       |	<folder>$folder</folder>
       |	<filename>$filename</filename>
       |	<path>$path</path>
       |	<source>
       |		<database>Unknown</database>
       |	</source>
       |	<size>
       |		<width>$width</width>
       |		<height>$height</height>
       |		<depth>3</depth>
       |	</size>
       |	<segmented>0</segmented>
    """.stripMargin + objects.map(o => s"""
       |	<object>
       |		<name>${o.t}</name>
       |		<pose>Unspecified</pose>
       |		<truncated>0</truncated>
       |		<difficult>0</difficult>
       |		<bndbox>
       |			<xmin>${o.area.x0}</xmin>
       |			<ymin>${o.area.y0}</ymin>
       |			<xmax>${o.area.x1}</xmax>
       |			<ymax>${o.area.y1}</ymax>
       |		</bndbox>
       |	</object>""".stripMargin).mkString +
      """
       |</annotation>
    """.stripMargin.trim



  def main(args: Array[String]): Unit = {
    val inputImagesPath = new File("/Users/tolsi/Dropbox/clash_royale_dataset/annotated/assets")
    val annotationOutPath = new File("/Users/tolsi/Dropbox/clash_royale_dataset/annotated/assets-resized/voc")
    val resizedOutPath = new File("/Users/tolsi/Dropbox/clash_royale_dataset/annotated/assets-resized")
    val allClassesFile = new File("/Users/tolsi/Dropbox/clash_royale_dataset/annotated/predefined_classes.txt")
    val scaleBy: Float = 0.35f

    val classes = Source.fromFile(allClassesFile).getLines().map(_.replace("_red", "").replace("_blue", "")).toSet

    inputImagesPath.listFiles(new FilenameFilter {
      override def accept(dir: File, name: String): Boolean = name.endsWith(".png")
    }).foreach(f => {
      val name = f.getName
      val unitClass = classes.find(c => name.contains(c)).getOrElse("unknown")
      try {
        val i = Image.fromFile(f)
        val scaledImage = i.scale(scaleBy, ScaleMethod.Bicubic)
        val resizedFileOutput = new File(resizedOutPath.getAbsolutePath + s"/$name")
        val annotationFileOutput = new File(annotationOutPath.getAbsolutePath + s"/${name.takeWhile(_ != '.')}.xml")
        val content = createFileContent(resizedOutPath.getName, resizedFileOutput.getName, resizedFileOutput.getAbsolutePath,
          scaledImage.width, scaledImage.height, Seq(AnnotationObject(unitClass, Area(0, 0, scaledImage.width, scaledImage.height))))
        println(annotationFileOutput.getAbsolutePath)
        scaledImage.output(resizedFileOutput)
        val writer = new PrintWriter(annotationFileOutput)
        writer.println(content)
        writer.close()
      } catch {
        case NonFatal(e) => println(s"wtf with ${f.getAbsolutePath}")
      }
    })
  }
}
