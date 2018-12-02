package ru.tolsi.cs

import java.io.{File, FilenameFilter, PrintWriter}

import com.sksamuel.scrimage.{Color, Image}
import monix.eval.Task
import monix.execution.Scheduler
import ru.tolsi.cs.Annotate.{AnnotationObject, Area}
import ru.tolsi.cs.filter.TransparentColorizeFilter

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.control.NonFatal
import scala.util.{Random, Try}

// Заполняет класс юнита по диапозону файлов
object GenerateScreensFromUnits extends App {
  val annotate = Seq(
    ("building_basic_cannon_sprite_01.png", "building_basic_cannon_sprite_19.png", "cannon_red"),
    ("building_basic_cannon_sprite_20.png", "building_basic_cannon_sprite_38.png", "cannon_blue"),
    ("building_elixir_collector_sprite_3.png", "building_elixir_collector_sprite_3.png", "elixir_collector_red"),
    ("building_elixir_collector_sprite_4.png", "building_elixir_collector_sprite_4.png", "elixir_collector_blue"),
    ("building_mega_bomb_sprite_01.png", "building_mega_bomb_sprite_01.png", "giant_bomb_red"),
    ("building_mega_bomb_sprite_13.png", "building_mega_bomb_sprite_13.png", "giant_bomb_blue"),
    ("building_mortar_sprite_00.png", "building_mortar_sprite_04.png", "mortar_blue"),
    ("building_mortar_sprite_05.png", "building_mortar_sprite_09.png", "mortar_red"),
    ("chr_balloon_sprite_02.png", "chr_balloon_sprite_02.png", "balloon_red"),
    ("chr_balloon_sprite_09.png", "chr_balloon_sprite_09.png", "balloon_blue"),
    ("building_barbarian_hut_sprite_0.png", "building_barbarian_hut_sprite_0.png", "barbarian_barrel_red"),
    ("building_barbarian_hut_sprite_1.png", "building_barbarian_hut_sprite_1.png", "barbarian_barrel_blue"),
    ("building_inferno_tower_sprite_0.png", "building_inferno_tower_sprite_0.png", "inferno_tower_red"),
    ("building_inferno_tower_sprite_2.png", "building_inferno_tower_sprite_2.png", "inferno_tower_blue"),
    ("building_tombstone_sprite_00.png", "building_tombstone_sprite_00.png", "tombstone_red"),
    ("building_tombstone_sprite_17.png", "building_tombstone_sprite_17.png", "tombstone_blue"),
    ("building_tesla_sprite_02.png", "building_tesla_sprite_02.png", "tesla_blue"),
    ("building_tesla_sprite_35.png", "building_tesla_sprite_35.png", "tesla_red"),
    ("chr_axe_man_sprite_000.png", "chr_axe_man_sprite_358.png", "executioner_blue"),
    ("chr_archer_sprite_000.png", "chr_archer_sprite_125.png", "archer_blue"),
    ("chr_archer_sprite_126.png", "chr_archer_sprite_251.png", "archer_red"),
    ("chr_skeleton_balloon_sprite_2.png", "chr_skeleton_balloon_sprite_2.png", "skeleton_barrel_red"),
    ("chr_skeleton_balloon_sprite_5.png", "chr_skeleton_balloon_sprite_5.png", "skeleton_barrel_blue"),
    ("chr_baby_dragon_sprite_000.png", "chr_baby_dragon_sprite_161.png", "baby_dragon_blue"),
    ("chr_baby_dragon_sprite_163.png", "chr_baby_dragon_sprite_171.png", "inferno_dragon_blue"),
    ("chr_baby_dragon_sprite_173.png", "chr_baby_dragon_sprite_325.png", "baby_dragon_blue"),
    ("chr_baby_dragon_sprite_326.png", "chr_baby_dragon_sprite_477.png", "baby_dragon_red"),
    ("chr_baby_dragon_sprite_479.png", "chr_baby_dragon_sprite_542.png", "baby_dragon_blue"),
    ("chr_baby_dragon_sprite_545.png", "chr_baby_dragon_sprite_630.png", "baby_dragon_red"),
    ("chr_baby_dragon_sprite_632.png", "chr_baby_dragon_sprite_649.png", "inferno_dragon_red"),
    ("chr_bandit_sprite_000.png", "chr_bandit_sprite_323.png", "bandit_blue"),
    ("chr_barbarian_sprite_0000.png", "chr_barbarian_sprite_0071.png", "elite_barbarian_blue"),
    ("chr_barbarian_sprite_0072.png", "chr_barbarian_sprite_0240.png", "elite_barbarian_red"),
    ("chr_barbarian_sprite_0072.png", "chr_barbarian_sprite_0240.png", "elite_barbarian_red"),
    ("chr_barbarian_sprite_0243.png", "chr_barbarian_sprite_0339.png", "elite_barbarian_blue"),
    ("chr_barbarian_sprite_0687.png", "chr_barbarian_sprite_0695.png", "barbarian_blue"),
    ("chr_barbarian_sprite_0696.png", "chr_barbarian_sprite_0866.png", "barbarian_red"),
    ("chr_barbarian_sprite_0867.png", "chr_barbarian_sprite_1028.png", "barbarian_blue"),
    ("chr_black_knight_sprite_0000.png", "chr_black_knight_sprite_0017.png", "dark_prince_red"),
    ("chr_black_knight_sprite_0018.png", "chr_black_knight_sprite_0529.png", "dark_prince_blue"),
    ("chr_black_knight_sprite_0530.png", "chr_black_knight_sprite_1015.png", "dark_prince_red"),
    ("chr_bomber_sprite_000.png", "chr_bomber_sprite_224.png", "bomber_blue"),
    ("chr_bomber_sprite_225.png", "chr_bomber_sprite_431.png", "bomber_red"),
    ("chr_bowler_sprite_000.png", "chr_bowler_sprite_090.png", "bowler_red"),
    ("chr_bowler_sprite_091.png", "chr_bowler_sprite_103.png", "bowler_blue"),
    ("chr_bowler_sprite_105.png", "chr_bowler_sprite_195.png", "bowler_red"),
    ("chr_bowler_sprite_196.png", "chr_bowler_sprite_392.png", "bowler_blue"),
    ("chr_bowler_sprite_393.png", "chr_bowler_sprite_444.png", "bowler_red"),
    ("chr_bowler_sprite_447.png", "chr_bowler_sprite_485.png", "bowler_blue"),
    ("chr_dark_witch_sprite_000.png", "chr_dark_witch_sprite_268.png", "night_witch_blue"),
    ("chr_electro_wizard_sprite_000.png", "chr_electro_wizard_sprite_276.png", "electro_wizard_blue"),
    ("chr_giant_skeleton_sprite_000.png", "chr_giant_skeleton_sprite_008.png", "giant_skeleton_blue"),
    ("chr_giant_skeleton_sprite_009.png", "chr_giant_skeleton_sprite_233.png", "giant_skeleton_red"),
    ("chr_giant_skeleton_sprite_234.png", "chr_giant_skeleton_sprite_449.png", "giant_skeleton_blue"),
    ("chr_giant_sprite_000.png", "chr_giant_sprite_158.png", "giant_blue"),
    ("chr_giant_sprite_160.png", "chr_giant_sprite_287.png", "giant_red"),
    ("chr_giant_sprite_288.png", "chr_giant_sprite_431.png", "giant_blue"),
    ("chr_giant_sprite_432.png", "chr_giant_sprite_447.png", "giant_red"),
    ("chr_giant_sprite_448.png", "chr_giant_sprite_466.png", "giant_blue"),
    ("chr_goblin_archer_sprite_000.png", "chr_goblin_archer_sprite_079.png", "spear_goblin_blue"),
    ("chr_goblin_archer_sprite_080.png", "chr_goblin_archer_sprite_096.png", "spear_goblin_red"),
    ("chr_goblin_archer_sprite_098.png", "chr_goblin_archer_sprite_107.png", "spear_goblin_blue"),
    ("chr_goblin_archer_sprite_108.png", "chr_goblin_archer_sprite_197.png", "spear_goblin_red"),
    ("chr_goblin_archer_sprite_198.png", "chr_goblin_archer_sprite_269.png", "spear_goblin_blue"),
    ("chr_goblin_archer_sprite_270.png", "chr_goblin_archer_sprite_341.png", "spear_goblin_red"),
    ("chr_goblin_blowdart_sprite_000.png", "chr_goblin_blowdart_sprite_071.png", "dart_goblin_blue"),
    ("chr_goblin_blowdart_sprite_072.png", "chr_goblin_blowdart_sprite_143.png", "dart_goblin_red"),
    ("chr_goblin_blowdart_sprite_072.png", "chr_goblin_blowdart_sprite_143.png", "dart_goblin_red"),
    ("chr_goblin_sprite_000.png", "chr_goblin_sprite_071.png", "goblin_blue"),
    ("chr_goblin_sprite_072.png", "chr_goblin_sprite_208.png", "goblin_red"),
    ("chr_goblin_sprite_209.png", "chr_goblin_sprite_280.png", "goblin_blue"),
    ("chr_goblin_sprite_281.png", "chr_goblin_sprite_287.png", "goblin_red"),
    ("chr_golem_sprite_000.png", "chr_golem_sprite_108.png", "golem_blue"),
    ("chr_golem_sprite_109.png", "chr_golem_sprite_218.png", "golem_red"),
    ("chr_golem_sprite_307.png", "chr_golem_sprite_360.png", "golem_red"),
    ("chr_golemite_sprite_000.png", "chr_golemite_sprite_214.png", "golem_mini_red"),
    ("chr_golemite_sprite_215.png", "chr_golemite_sprite_360.png", "golem_mini_blue"),
    ("chr_hog_rider_sprite_000.png", "chr_hog_rider_sprite_170.png", "hog_rider_red"),
    ("chr_hog_rider_sprite_171.png", "chr_hog_rider_sprite_341.png", "hog_rider_blue"),
    ("chr_ice_wizard_sprite_000.png", "chr_ice_wizard_sprite_334.png", "ice_wizard_blue"),
    ("chr_knight_sprite_000.png", "chr_knight_sprite_115.png", "knight_blue"),
    ("chr_knight_sprite_116.png", "chr_knight_sprite_358.png", "knight_red"),
    ("chr_knight_sprite_359.png", "chr_knight_sprite_485.png", "knight_blue"),
    ("chr_lava_hound_sprite_000.png", "chr_lava_hound_sprite_135.png", "lava_hound_red"),
    ("chr_lava_hound_sprite_136.png", "chr_lava_hound_sprite_271.png", "lava_hound_blue"),
    ("chr_lava_hound_sprite_272.png", "chr_lava_hound_sprite_288.png", "lava_hound_red"),
    ("chr_lava_hound_sprite_272.png", "chr_lava_hound_sprite_288.png", "lava_hound_red"),
    ("chr_lava_hound_sprite_289.png", "chr_lava_hound_sprite_305.png", "lava_hound_blue"),
    ("chr_mega_knight_sprite_000.png", "chr_mega_knight_sprite_083.png", "mega_knight_blue"),
    ("chr_mega_knight_sprite_084.png", "chr_mega_knight_sprite_192.png", "mega_knight_red"),
    ("chr_mega_knight_sprite_193.png", "chr_mega_knight_sprite_384.png", "mega_knight_blue"),
    ("chr_mega_knight_sprite_387.png", "chr_mega_knight_sprite_485.png", "mega_knight_red"),
    ("chr_mega_knight_sprite_486.png", "chr_mega_knight_sprite_618.png", "mega_knight_blue"),
    ("chr_mega_minion_sprite_000.png", "chr_mega_minion_sprite_071.png", "mega_minion_blue"),
    ("chr_mega_minion_sprite_072.png", "chr_mega_minion_sprite_143.png", "mega_minion_red"),
    ("chr_mega_minion_sprite_144.png", "chr_mega_minion_sprite_167.png", "mega_minion_blue"),
    ("chr_mega_minion_sprite_168.png", "chr_mega_minion_sprite_226.png", "mega_minion_red"),
    ("chr_mega_minion_sprite_227.png", "chr_mega_minion_sprite_251.png", "mega_minion_blue"),
    ("chr_miner_sprite_000.png", "chr_miner_sprite_207.png", "miner_red"),
    ("chr_miner_sprite_208.png", "chr_miner_sprite_414.png", "miner_blue"),
    ("chr_mini_pekka_sprite_000.png", "chr_mini_pekka_sprite_107.png", "mini_pekka_blue"),
    ("chr_mini_pekka_sprite_108.png", "chr_mini_pekka_sprite_315.png", "mini_pekka_red"),
    ("chr_mini_pekka_sprite_316.png", "chr_mini_pekka_sprite_415.png", "mini_pekka_blue"),
    ("chr_minion_sprite_000.png", "chr_minion_sprite_071.png", "minion_blue"),
    ("chr_minion_sprite_072.png", "chr_minion_sprite_206.png", "minion_red"),
    ("chr_minion_sprite_207.png", "chr_minion_sprite_269.png", "minion_blue"),
    ("chr_musketeer_sprite_000.png", "chr_musketeer_sprite_209.png", "musketeer_red"),
    ("chr_musketeer_sprite_210.png", "chr_musketeer_sprite_413.png", "musketeer_blue"),
    ("chr_pekka_sprite_000.png", "chr_pekka_sprite_134.png", "pekka_blue"),
    ("chr_pekka_sprite_135.png", "chr_pekka_sprite_332.png", "pekka_red"),
    ("chr_pekka_sprite_333.png", "chr_pekka_sprite_395.png", "pekka_blue"),
    ("chr_prince_sprite_000.png", "chr_prince_sprite_260.png", "prince_red"),
    ("chr_prince_sprite_261.png", "chr_prince_sprite_521.png", "prince_blue"),
    ("chr_princess_sprite_000.png", "chr_princess_sprite_013.png", "princess_red"),
    ("chr_princess_sprite_014.png", "chr_princess_sprite_016.png", "princess_blue"),
    ("chr_princess_sprite_018.png", "chr_princess_sprite_025.png", "princess_red"),
    ("chr_princess_sprite_026.png", "chr_princess_sprite_028.png", "princess_blue"),
    ("chr_princess_sprite_030.png", "chr_princess_sprite_037.png", "princess_red"),
    ("chr_princess_sprite_038.png", "chr_princess_sprite_040.png", "princess_blue"),
    ("chr_princess_sprite_042.png", "chr_princess_sprite_049.png", "princess_red"),
    ("chr_princess_sprite_050.png", "chr_princess_sprite_052.png", "princess_blue"),
    ("chr_princess_sprite_054.png", "chr_princess_sprite_061.png", "princess_red"),
    ("chr_princess_sprite_062.png", "chr_princess_sprite_064.png", "princess_blue"),
    ("chr_princess_sprite_066.png", "chr_princess_sprite_073.png", "princess_red"),
    ("chr_princess_sprite_074.png", "chr_princess_sprite_076.png", "princess_blue"),
    ("chr_princess_sprite_078.png", "chr_princess_sprite_085.png", "princess_red"),
    ("chr_princess_sprite_086.png", "chr_princess_sprite_088.png", "princess_blue"),
    ("chr_princess_sprite_090.png", "chr_princess_sprite_097.png", "princess_red"),
    ("chr_princess_sprite_098.png", "chr_princess_sprite_100.png", "princess_blue"),
    ("chr_princess_sprite_102.png", "chr_princess_sprite_109.png", "princess_red"),
    ("chr_princess_sprite_110.png", "chr_princess_sprite_112.png", "princess_blue"),
    ("chr_princess_sprite_114.png", "chr_princess_sprite_333.png", "princess_red"),
    ("chr_princess_sprite_335.png", "chr_princess_sprite_337.png", "princess_blue"),
    ("chr_princess_sprite_114.png", "chr_princess_sprite_333.png", "princess_red"),
    ("chr_princess_sprite_335.png", "chr_princess_sprite_337.png", "princess_blue"),
    ("chr_princess_sprite_339.png", "chr_princess_sprite_351.png", "princess_red"),
    ("chr_princess_sprite_353.png", "chr_princess_sprite_355.png", "princess_blue"),
    ("chr_princess_sprite_357.png", "chr_princess_sprite_369.png", "princess_red"),
    ("chr_princess_sprite_371.png", "chr_princess_sprite_373.png", "princess_blue"),
    ("chr_princess_sprite_375.png", "chr_princess_sprite_386.png", "princess_red"),
    ("chr_princess_sprite_388.png", "chr_princess_sprite_390.png", "princess_blue"),
    ("chr_princess_sprite_392.png", "chr_princess_sprite_404.png", "princess_red"),
    ("chr_princess_sprite_406.png", "chr_princess_sprite_408.png", "princess_blue"),
    ("chr_princess_sprite_410.png", "chr_princess_sprite_422.png", "princess_red"),
    ("chr_princess_sprite_424.png", "chr_princess_sprite_426.png", "princess_blue"),
    ("chr_princess_sprite_428.png", "chr_princess_sprite_430.png", "princess_red"),
    ("chr_princess_sprite_431.png", "chr_princess_sprite_487.png", "princess_blue"),
    ("chr_princess_sprite_488.png", "chr_princess_sprite_495.png", "princess_red"),
    ("chr_princess_sprite_496.png", "chr_princess_sprite_747.png", "princess_blue"),
    ("chr_rage_barbarian_sprite_000.png", "chr_rage_barbarian_sprite_063.png", "lumberjack_blue"),
    ("chr_rage_barbarian_sprite_064.png", "chr_rage_barbarian_sprite_111.png", "lumberjack_red"),
    ("chr_rage_barbarian_sprite_112.png", "chr_rage_barbarian_sprite_126.png", "lumberjack_blue"),
    ("chr_rage_barbarian_sprite_128.png", "chr_rage_barbarian_sprite_169.png", "lumberjack_red"),
    ("chr_rage_barbarian_sprite_170.png", "chr_rage_barbarian_sprite_210.png", "lumberjack_blue"),
    ("chr_rage_barbarian_sprite_212.png", "chr_rage_barbarian_sprite_227.png", "lumberjack_red"),
    ("chr_rage_barbarian_sprite_228.png", "chr_rage_barbarian_sprite_235.png", "lumberjack_blue"),
    ("chr_rage_barbarian_sprite_236.png", "chr_rage_barbarian_sprite_295.png", "lumberjack_red"),
    ("chr_rage_barbarian_sprite_296.png", "chr_rage_barbarian_sprite_345.png", "lumberjack_blue"),
    ("chr_royal_giant_sprite_000.png", "chr_royal_giant_sprite_252.png", "royal_giant_red"),
    ("chr_royal_giant_sprite_252.png", "chr_royal_giant_sprite_476.png", "royal_giant_blue"),
    ("chr_skeleton_warrior_sprite_000.png", "chr_skeleton_warrior_sprite_017.png", "guard_red"),
    ("chr_skeleton_warrior_sprite_019.png", "chr_skeleton_warrior_sprite_297.png", "guard_blue"),
    ("chr_skeleton_warrior_sprite_299.png", "chr_skeleton_warrior_sprite_640.png", "guard_red"),
    ("chr_skeleton_warrior_sprite_641.png", "chr_skeleton_warrior_sprite_718.png", "guard_blue"),
    ("chr_snowman_sprite_000.png", "chr_snowman_sprite_278.png", "ice_golem_blue"),
    ("chr_valkyrie_sprite_000.png", "chr_valkyrie_sprite_080.png", "valkyrie_blue"),
    ("chr_valkyrie_sprite_081.png", "chr_valkyrie_sprite_270.png", "valkyrie_red"),
    ("chr_valkyrie_sprite_271.png", "chr_valkyrie_sprite_378.png", "valkyrie_blue"),
    ("chr_witch_sprite_000.png", "chr_witch_sprite_080.png", "witch_blue"),
    ("chr_witch_sprite_081.png", "chr_witch_sprite_207.png", "witch_red"),
    ("chr_witch_sprite_208.png", "chr_witch_sprite_252.png", "witch_blue"),
    ("chr_wizard_sprite_000.png", "chr_wizard_sprite_071.png", "wizard_blue"),
    ("chr_wizard_sprite_072.png", "chr_wizard_sprite_240.png", "wizard_red"),
    ("chr_wizard_sprite_240.png", "chr_wizard_sprite_311.png", "wizard_blue"),
    ("chr_zap_machine_sprite_009.png", "chr_zap_machine_sprite_221.png", "sparky_red"),
    ("chr_zap_machine_sprite_225.png", "chr_zap_machine_sprite_251.png", "sparky_red"),
    ("spell_goblin_barrel_sprite_00.png", "spell_goblin_barrel_sprite_12.png", "goblin_barrel"),
  )
  val scale = 0.9f
  val scaleWindow = 0.35f
  val scaleFinalImage = 0.7f
  val path = new File("/home/tolsi/Документы/clash_royale/annotate-images/data/assets")
  val pathOut = new File("/home/tolsi/Документы/clash_royale/5")
  pathOut.mkdirs()
  val annPathOut = new File("/home/tolsi/Документы/clash_royale/5_voc")
  annPathOut.mkdirs()
  val arenas = new File("/home/tolsi/Документы/clash_royale/annotate-images/data/cropped")
  val arenasImages = arenas.listFiles().map(Image.fromFile).toList ++ List(
    Color.Black,
    Color.White,
    Color.apply(255, 0, 0),
    Color.apply(0, 255, 0),
    Color.apply(0, 0, 255),
    Color.apply(255, 255, 0),
    Color.apply(0, 255, 255),
    Color.apply(255, 0, 255)
  ).map(c => Image.filled(700, 950, c))

  val filesByType = annotate.flatMap {
    case (from, to, replace) =>
      path.listFiles(new FilenameFilter {
        override def accept(dir: File, name: String): Boolean = name.endsWith(".png") &&
          name >= from &&
          name <= to
      }).map(f => {
        replace -> f
      })
  }
  // limit each type count
  val r = new Random()

  def placeUnitOnArea(unit: Image, image: Image, area: Area): Area = {
    val randomX = area.x0 + r.nextInt(math.max(1, area.x1 - area.x0 - unit.width))
    val randomY = area.y0 + r.nextInt(math.max(1, area.y1 - area.y0 - unit.height))
    val graphics = image.awt.createGraphics()
    graphics.drawImage(unit.awt, randomX, randomY, null)
    Area(randomX, randomY, randomX + unit.width, randomY + unit.height)
  }

  def processEveryUnit(t: String, unitFile: File): Image = {
    val a = if (r.nextBoolean()) 0 else r.nextInt(128)
    val colorFilter = if (t.contains("red")) {
      TransparentColorizeFilter(255, 0, 0, a)
    } else if (t.contains("blue")) {
      TransparentColorizeFilter(0, 0, 255, a)
    } else TransparentColorizeFilter(0, 0, 0, 0)

    val typeScale = if (unitFile.getName.contains("spell_")) {
      1
    } else {
      // 1 +/- 0.5
      scale + (-scaleWindow + (r.nextFloat() * 2 * scaleWindow))
    }

    Image.fromFile(unitFile).scale(typeScale).filter(colorFilter)
  }

  def writeImageToFile(i: Image, f: String): Try[Unit] = Try(i.output(f))

  def writeAnnotationToFile(image: Image, info: Seq[AnnotationObject], annotationFilePath: String, imageFolder: String, imageFileName: String): Try[Unit] = Try {
    val pw = new PrintWriter(annotationFilePath)
    try {
      pw.write(Annotate.createFileContent(imageFolder, imageFileName, imageFolder + '/' + imageFileName, image.width, image.height, info))
    } finally {
      pw.close()
    }
  }

  case class AreaSelector(w: Int, h: Int, splitX: Int, splitY: Int) {
    def select(x: Int, y: Int): Area = {
      require(x < splitX)
      require(y < splitY)
      val xBy = w / splitX
      val yBy = h / splitY
      Area(
        x * xBy,
        y * yBy,
        (x + 1) * xBy,
        (y + 1) * yBy,
      )
    }
  }

  val splitX = 4
  val splitY = 4

  for {
    iteration <- 1 to 20
  } {
    val groupedByImages = r.shuffle(filesByType).grouped(splitX * splitY)
    val tasks = groupedByImages.zipWithIndex.map {
      case (s, i) => Task {
        val workImage = r.shuffle(arenasImages).head.copy
        val units = s.map(t => t._1 -> processEveryUnit(t._1, t._2))

        val unitsI = units.iterator
        val ae = AreaSelector(workImage.width,  workImage.height, splitX, splitY)
        val typeWithAreas = for {
          x <- 0 until splitX
          y <- 0 until splitY
          if unitsI.hasNext
        } yield  {
          val (t, unit) = unitsI.next()
          t -> placeUnitOnArea(unit, workImage,ae.select(x, y)).scale(scaleFinalImage)
        }

        val outImageName = s"$iteration-$i.png"
        val outImagePath = pathOut.getAbsolutePath + "/" + outImageName
        val outAnnPath = annPathOut + "/" + s"$iteration-$i.xml"

        val scaledFinalImage = workImage.scale(scaleFinalImage)

        {
          for {
            _ <- writeImageToFile(scaledFinalImage, outImagePath)
            _ <- writeAnnotationToFile(scaledFinalImage, typeWithAreas.map(AnnotationObject.tupled), outAnnPath, pathOut.getAbsolutePath, outImageName)
          } yield {
            println(s"$iteration-$i")
          }
        }.recover { case NonFatal(e) =>
          Try(new File(outImagePath).delete())
          Try(new File(outAnnPath).delete())
        }
      }
      case _ => Task.unit
    }

    val seqTask = Task.gather(tasks)
    Await.result(seqTask.runAsync(Scheduler.forkJoin(10, 30)), Duration.Inf)
  }
}
