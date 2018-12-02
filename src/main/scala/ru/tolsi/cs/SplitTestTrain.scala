package ru.tolsi.cs

import java.io.{File, FilenameFilter}

import scala.io.Source
import scala.util.Random
import scala.util.matching.Regex

// разбивает обучающую и тестовую выборки
object SplitTestTrain extends App {
  val all_classes_file = new File("/Users/tolsi/Dropbox/clash_royale_dataset/annotated/predefined_classes.txt")
  val assets = new File("/Users/tolsi/Dropbox/clash_royale_dataset/annotated/assets-resized")
  val assets_ann = new File("/Users/tolsi/Dropbox/clash_royale_dataset/annotated/assets-resized/yolo")
  val archers = new File("/Users/tolsi/Dropbox/clash_royale_dataset/annotated/first/archers_3")
  val archers_ann = new File("/Users/tolsi/Dropbox/clash_royale_dataset/annotated/first/archers_3_yolo")

  val assetsEachTypePercent = 0.01
  val screenShootsPercent = 0.5

  val r = new Random()

  val all_classes = Source.fromFile(all_classes_file).getLines().toSeq

  val assetsAll = assets.listFiles(new FilenameFilter {
    override def accept(dir: File, name: String): Boolean = name.endsWith(".png")
  })
  val assetsAnnAll = assets_ann.listFiles(new FilenameFilter {
    override def accept(dir: File, name: String): Boolean = name.endsWith(".txt")
  })
  val assetsCheck = (assetsAll ++ assetsAnnAll).groupBy(f => {
    f.getName.split("\\.").head
  }).filter(s => s._2.size != 2)
//  println(assetsCheck.keys.toList)

  val assetsTest = assetsAll.groupBy(f => {
    f.getName.split("_").init.toList
  }).mapValues(c => r.shuffle(c.toList)).mapValues(c =>
    c.take(2 + (c.size * assetsEachTypePercent).toInt)
  ).values.flatten.map(_.getName).toList.sorted
  val assetsTrain = assetsAll.map(_.getName).toSet -- assetsTest

  val archersAll = archers.listFiles(new FilenameFilter {
    override def accept(dir: File, name: String): Boolean = name.endsWith(".png")
  })
  val archersAnnAll = archers_ann.listFiles(new FilenameFilter {
    override def accept(dir: File, name: String): Boolean = name.endsWith(".txt")
  })
  val archersAnnByClasses = archersAnnAll.groupBy(f => {
    val s = Source.fromFile(f).mkString
    s.split(" ").head
  }).mapValues(_.length)

  val assetsAnnByClasses = assetsAnnAll.groupBy(f => {
    val s = Source.fromFile(f).mkString
    s.split(" ").head
  }).mapValues(_.length)
  val archersTest = archersAll.groupBy(_.getName.length)
    .mapValues(c => r.shuffle(c.toList)).mapValues(c => c.take(2 + (c.size * screenShootsPercent).toInt)).values.flatten.map(_.getName).toList.sorted

  val archersTrain = assetsAll.map(_.getName).toSet -- assetsTest

  println("total")
  val totalClassesShoots = archersAnnByClasses.toSeq.map {
    case (k,v) => all_classes(k.toInt) -> v
  }.sortBy(-_._2)
  totalClassesShoots.foreach(println)
  println(totalClassesShoots.length)
  println("assets")
  val assetsClassesShoots = assetsAnnByClasses.toSeq.map {
    case (k,v) =>
      all_classes(k.toInt) -> v
  }.sortBy(-_._2)
  assetsClassesShoots.foreach(println)
  println(assetsClassesShoots.length)

  println("test")
  (assetsTest ++ archersTest).foreach(println)
  println("train")
  (assetsTrain ++ archersTrain).foreach(println)
}
