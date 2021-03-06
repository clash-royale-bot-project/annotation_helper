package ru.tolsi.cs

import java.io.{File, FilenameFilter, PrintWriter}

import scala.io.Source

// Заполняет класс юнита по диапозону файлов
object MassClassifyByFiles extends App {
  val annotate = Seq(
    ("building_basic_cannon_sprite_01.xml", "building_basic_cannon_sprite_19.xml", "cannon_red"),
    ("building_basic_cannon_sprite_20.xml", "building_basic_cannon_sprite_38.xml", "cannon_blue"),
    ("building_elixir_collector_sprite_3.xml", "building_elixir_collector_sprite_3.xml", "elixir_collector_red"),
    ("building_elixir_collector_sprite_4.xml", "building_elixir_collector_sprite_4.xml", "elixir_collector_blue"),
    ("building_mega_bomb_sprite_01.xml", "building_mega_bomb_sprite_01.xml", "giant_bomb_red"),
    ("building_mega_bomb_sprite_13.xml", "building_mega_bomb_sprite_13.xml", "giant_bomb_blue"),
    ("building_mortar_sprite_00.xml", "building_mortar_sprite_04.xml", "mortar_blue"),
    ("building_mortar_sprite_05.xml", "building_mortar_sprite_09.xml", "mortar_red"),
    ("chr_balloon_sprite_02.xml", "chr_balloon_sprite_02.xml", "balloon_red"),
    ("chr_balloon_sprite_09.xml", "chr_balloon_sprite_09.xml", "balloon_blue"),
    ("building_barbarian_hut_sprite_0.xml", "building_barbarian_hut_sprite_0.xml", "barbarian_barrel_red"),
    ("building_barbarian_hut_sprite_1.xml", "building_barbarian_hut_sprite_1.xml", "barbarian_barrel_blue"),
    ("building_inferno_tower_sprite_0.xml", "building_inferno_tower_sprite_0.xml", "inferno_tower_red"),
    ("building_inferno_tower_sprite_2.xml", "building_inferno_tower_sprite_2.xml", "inferno_tower_blue"),
    ("building_tombstone_sprite_00.xml", "building_tombstone_sprite_00.xml", "tombstone_red"),
    ("building_tombstone_sprite_17.xml", "building_tombstone_sprite_17.xml", "tombstone_blue"),
    ("building_tesla_sprite_02.xml", "building_tesla_sprite_02.xml", "tesla_blue"),
    ("building_tesla_sprite_35.xml", "building_tesla_sprite_35.xml", "tesla_red"),
    ("chr_axe_man_sprite_000.xml", "chr_axe_man_sprite_358.xml", "executioner_blue"),
    ("chr_archer_sprite_000.xml", "chr_archer_sprite_125.xml", "archer_blue"),
    ("chr_archer_sprite_126.xml", "chr_archer_sprite_251.xml", "archer_red"),
    ("chr_skeleton_balloon_sprite_2.xml", "chr_skeleton_balloon_sprite_2.xml", "skeleton_barrel_red"),
    ("chr_skeleton_balloon_sprite_5.xml", "chr_skeleton_balloon_sprite_5.xml", "skeleton_barrel_blue"),
    ("chr_baby_dragon_sprite_000.xml", "chr_baby_dragon_sprite_161.xml", "baby_dragon_blue"),
    ("chr_baby_dragon_sprite_163.xml", "chr_baby_dragon_sprite_171.xml", "inferno_dragon_blue"),
    ("chr_baby_dragon_sprite_173.xml", "chr_baby_dragon_sprite_325.xml", "baby_dragon_blue"),
    ("chr_baby_dragon_sprite_326.xml", "chr_baby_dragon_sprite_477.xml", "baby_dragon_red"),
    ("chr_baby_dragon_sprite_479.xml", "chr_baby_dragon_sprite_542.xml", "baby_dragon_blue"),
    ("chr_baby_dragon_sprite_545.xml", "chr_baby_dragon_sprite_630.xml", "baby_dragon_red"),
    ("chr_baby_dragon_sprite_632.xml", "chr_baby_dragon_sprite_649.xml", "inferno_dragon_red"),
    ("chr_bandit_sprite_000.xml", "chr_bandit_sprite_323.xml", "bandit_blue"),
    ("chr_barbarian_sprite_0000.xml", "chr_barbarian_sprite_0071.xml", "elite_barbarian_blue"),
    ("chr_barbarian_sprite_0072.xml", "chr_barbarian_sprite_0240.xml", "elite_barbarian_red"),
    ("chr_barbarian_sprite_0072.xml", "chr_barbarian_sprite_0240.xml", "elite_barbarian_red"),
    ("chr_barbarian_sprite_0243.xml", "chr_barbarian_sprite_0339.xml", "elite_barbarian_blue"),
    ("chr_barbarian_sprite_0687.xml", "chr_barbarian_sprite_0695.xml", "barbarian_blue"),
    ("chr_barbarian_sprite_0696.xml", "chr_barbarian_sprite_0866.xml", "barbarian_red"),
    ("chr_barbarian_sprite_0867.xml", "chr_barbarian_sprite_1028.xml", "barbarian_blue"),
    ("chr_black_knight_sprite_0000.xml", "chr_black_knight_sprite_0017.xml", "dark_prince_red"),
    ("chr_black_knight_sprite_0018.xml", "chr_black_knight_sprite_0529.xml", "dark_prince_blue"),
    ("chr_black_knight_sprite_0530.xml", "chr_black_knight_sprite_1015.xml", "dark_prince_red"),
    ("chr_bomber_sprite_000.xml", "chr_bomber_sprite_224.xml", "bomber_blue"),
    ("chr_bomber_sprite_225.xml", "chr_bomber_sprite_431.xml", "bomber_red"),
    ("chr_bowler_sprite_000.xml", "chr_bowler_sprite_090.xml", "bowler_red"),
    ("chr_bowler_sprite_091.xml", "chr_bowler_sprite_103.xml", "bowler_blue"),
    ("chr_bowler_sprite_105.xml", "chr_bowler_sprite_195.xml", "bowler_red"),
    ("chr_bowler_sprite_196.xml", "chr_bowler_sprite_392.xml", "bowler_blue"),
    ("chr_bowler_sprite_393.xml", "chr_bowler_sprite_444.xml", "bowler_red"),
    ("chr_bowler_sprite_447.xml", "chr_bowler_sprite_485.xml", "bowler_blue"),
    ("chr_dark_witch_sprite_000.xml", "chr_dark_witch_sprite_268.xml", "night_witch_blue"),
    ("chr_electro_wizard_sprite_000.xml", "chr_electro_wizard_sprite_276.xml", "electro_wizard_blue"),
    ("chr_giant_skeleton_sprite_000.xml", "chr_giant_skeleton_sprite_008.xml", "giant_skeleton_blue"),
    ("chr_giant_skeleton_sprite_009.xml", "chr_giant_skeleton_sprite_233.xml", "giant_skeleton_red"),
    ("chr_giant_skeleton_sprite_234.xml", "chr_giant_skeleton_sprite_449.xml", "giant_skeleton_blue"),
    ("chr_giant_sprite_000.xml", "chr_giant_sprite_158.xml", "giant_blue"),
    ("chr_giant_sprite_160.xml", "chr_giant_sprite_287.xml", "giant_red"),
    ("chr_giant_sprite_288.xml", "chr_giant_sprite_431.xml", "giant_blue"),
    ("chr_giant_sprite_432.xml", "chr_giant_sprite_447.xml", "giant_red"),
    ("chr_giant_sprite_448.xml", "chr_giant_sprite_466.xml", "giant_blue"),
    ("chr_goblin_archer_sprite_000.xml", "chr_goblin_archer_sprite_079.xml", "spear_goblin_blue"),
    ("chr_goblin_archer_sprite_080.xml", "chr_goblin_archer_sprite_096.xml", "spear_goblin_red"),
    ("chr_goblin_archer_sprite_098.xml", "chr_goblin_archer_sprite_107.xml", "spear_goblin_blue"),
    ("chr_goblin_archer_sprite_108.xml", "chr_goblin_archer_sprite_197.xml", "spear_goblin_red"),
    ("chr_goblin_archer_sprite_198.xml", "chr_goblin_archer_sprite_269.xml", "spear_goblin_blue"),
    ("chr_goblin_archer_sprite_270.xml", "chr_goblin_archer_sprite_341.xml", "spear_goblin_red"),
    ("chr_goblin_blowdart_sprite_000.xml", "chr_goblin_blowdart_sprite_071.xml", "dart_goblin_blue"),
    ("chr_goblin_blowdart_sprite_072.xml", "chr_goblin_blowdart_sprite_143.xml", "dart_goblin_red"),
    ("chr_goblin_blowdart_sprite_072.xml", "chr_goblin_blowdart_sprite_143.xml", "dart_goblin_red"),
    ("chr_goblin_sprite_000.xml", "chr_goblin_sprite_071.xml", "goblin_blue"),
    ("chr_goblin_sprite_072.xml", "chr_goblin_sprite_208.xml", "goblin_red"),
    ("chr_goblin_sprite_209.xml", "chr_goblin_sprite_280.xml", "goblin_blue"),
    ("chr_goblin_sprite_281.xml", "chr_goblin_sprite_287.xml", "goblin_red"),
    ("chr_golem_sprite_000.xml", "chr_golem_sprite_108.xml", "golem_blue"),
    ("chr_golem_sprite_109.xml", "chr_golem_sprite_218.xml", "golem_red"),
    ("chr_golem_sprite_307.xml", "chr_golem_sprite_360.xml", "golem_red"),
    ("chr_golemite_sprite_000.xml", "chr_golemite_sprite_214.xml", "golem_mini_red"),
    ("chr_golemite_sprite_215.xml", "chr_golemite_sprite_360.xml", "golem_mini_blue"),
    ("chr_hog_rider_sprite_000.xml", "chr_hog_rider_sprite_170.xml", "hog_rider_red"),
    ("chr_hog_rider_sprite_171.xml", "chr_hog_rider_sprite_341.xml", "hog_rider_blue"),
    ("chr_ice_wizard_sprite_000.xml", "chr_ice_wizard_sprite_334.xml", "ice_wizard_blue"),
    ("chr_knight_sprite_000.xml", "chr_knight_sprite_115.xml", "knight_blue"),
    ("chr_knight_sprite_116.xml", "chr_knight_sprite_358.xml", "knight_red"),
    ("chr_knight_sprite_359.xml", "chr_knight_sprite_485.xml", "knight_blue"),
    ("chr_lava_hound_sprite_000.xml", "chr_lava_hound_sprite_135.xml", "lava_hound_red"),
    ("chr_lava_hound_sprite_136.xml", "chr_lava_hound_sprite_271.xml", "lava_hound_blue"),
    ("chr_lava_hound_sprite_272.xml", "chr_lava_hound_sprite_288.xml", "lava_hound_red"),
    ("chr_lava_hound_sprite_272.xml", "chr_lava_hound_sprite_288.xml", "lava_hound_red"),
    ("chr_lava_hound_sprite_289.xml", "chr_lava_hound_sprite_305.xml", "lava_hound_blue"),
    ("chr_mega_knight_sprite_000.xml", "chr_mega_knight_sprite_083.xml", "mega_knight_blue"),
    ("chr_mega_knight_sprite_084.xml", "chr_mega_knight_sprite_192.xml", "mega_knight_red"),
    ("chr_mega_knight_sprite_193.xml", "chr_mega_knight_sprite_384.xml", "mega_knight_blue"),
    ("chr_mega_knight_sprite_387.xml", "chr_mega_knight_sprite_485.xml", "mega_knight_red"),
    ("chr_mega_knight_sprite_486.xml", "chr_mega_knight_sprite_618.xml", "mega_knight_blue"),
    ("chr_mega_minion_sprite_000.xml", "chr_mega_minion_sprite_071.xml", "mega_minion_blue"),
    ("chr_mega_minion_sprite_072.xml", "chr_mega_minion_sprite_143.xml", "mega_minion_red"),
    ("chr_mega_minion_sprite_144.xml", "chr_mega_minion_sprite_167.xml", "mega_minion_blue"),
    ("chr_mega_minion_sprite_168.xml", "chr_mega_minion_sprite_226.xml", "mega_minion_red"),
    ("chr_mega_minion_sprite_227.xml", "chr_mega_minion_sprite_251.xml", "mega_minion_blue"),
    ("chr_miner_sprite_000.xml", "chr_miner_sprite_207.xml", "miner_red"),
    ("chr_miner_sprite_208.xml", "chr_miner_sprite_414.xml", "miner_blue"),
    ("chr_mini_pekka_sprite_000.xml", "chr_mini_pekka_sprite_107.xml", "mini_pekka_blue"),
    ("chr_mini_pekka_sprite_108.xml", "chr_mini_pekka_sprite_315.xml", "mini_pekka_red"),
    ("chr_mini_pekka_sprite_316.xml", "chr_mini_pekka_sprite_415.xml", "mini_pekka_blue"),
    ("chr_minion_sprite_000.xml", "chr_minion_sprite_071.xml", "minion_blue"),
    ("chr_minion_sprite_072.xml", "chr_minion_sprite_206.xml", "minion_red"),
    ("chr_minion_sprite_207.xml", "chr_minion_sprite_269.xml", "minion_blue"),
    ("chr_musketeer_sprite_000.xml", "chr_musketeer_sprite_209.xml", "musketeer_red"),
    ("chr_musketeer_sprite_210.xml", "chr_musketeer_sprite_413.xml", "musketeer_blue"),
    ("chr_pekka_sprite_000.xml", "chr_pekka_sprite_134.xml", "pekka_blue"),
    ("chr_pekka_sprite_135.xml", "chr_pekka_sprite_332.xml", "pekka_red"),
    ("chr_pekka_sprite_333.xml", "chr_pekka_sprite_395.xml", "pekka_blue"),
    ("chr_prince_sprite_000.xml", "chr_prince_sprite_260.xml", "prince_red"),
    ("chr_prince_sprite_261.xml", "chr_prince_sprite_521.xml", "prince_blue"),
    ("chr_princess_sprite_000.xml", "chr_princess_sprite_013.xml", "princess_red"),
    ("chr_princess_sprite_014.xml", "chr_princess_sprite_016.xml", "princess_blue"),
    ("chr_princess_sprite_018.xml", "chr_princess_sprite_025.xml", "princess_red"),
    ("chr_princess_sprite_026.xml", "chr_princess_sprite_028.xml", "princess_blue"),
    ("chr_princess_sprite_030.xml", "chr_princess_sprite_037.xml", "princess_red"),
    ("chr_princess_sprite_038.xml", "chr_princess_sprite_040.xml", "princess_blue"),
    ("chr_princess_sprite_042.xml", "chr_princess_sprite_049.xml", "princess_red"),
    ("chr_princess_sprite_050.xml", "chr_princess_sprite_052.xml", "princess_blue"),
    ("chr_princess_sprite_054.xml", "chr_princess_sprite_061.xml", "princess_red"),
    ("chr_princess_sprite_062.xml", "chr_princess_sprite_064.xml", "princess_blue"),
    ("chr_princess_sprite_066.xml", "chr_princess_sprite_073.xml", "princess_red"),
    ("chr_princess_sprite_074.xml", "chr_princess_sprite_076.xml", "princess_blue"),
    ("chr_princess_sprite_078.xml", "chr_princess_sprite_085.xml", "princess_red"),
    ("chr_princess_sprite_086.xml", "chr_princess_sprite_088.xml", "princess_blue"),
    ("chr_princess_sprite_090.xml", "chr_princess_sprite_097.xml", "princess_red"),
    ("chr_princess_sprite_098.xml", "chr_princess_sprite_100.xml", "princess_blue"),
    ("chr_princess_sprite_102.xml", "chr_princess_sprite_109.xml", "princess_red"),
    ("chr_princess_sprite_110.xml", "chr_princess_sprite_112.xml", "princess_blue"),
    ("chr_princess_sprite_114.xml", "chr_princess_sprite_333.xml", "princess_red"),
    ("chr_princess_sprite_335.xml", "chr_princess_sprite_337.xml", "princess_blue"),
    ("chr_princess_sprite_114.xml", "chr_princess_sprite_333.xml", "princess_red"),
    ("chr_princess_sprite_335.xml", "chr_princess_sprite_337.xml", "princess_blue"),
    ("chr_princess_sprite_339.xml", "chr_princess_sprite_351.xml", "princess_red"),
    ("chr_princess_sprite_353.xml", "chr_princess_sprite_355.xml", "princess_blue"),
    ("chr_princess_sprite_357.xml", "chr_princess_sprite_369.xml", "princess_red"),
    ("chr_princess_sprite_371.xml", "chr_princess_sprite_373.xml", "princess_blue"),
    ("chr_princess_sprite_375.xml", "chr_princess_sprite_386.xml", "princess_red"),
    ("chr_princess_sprite_388.xml", "chr_princess_sprite_390.xml", "princess_blue"),
    ("chr_princess_sprite_392.xml", "chr_princess_sprite_404.xml", "princess_red"),
    ("chr_princess_sprite_406.xml", "chr_princess_sprite_408.xml", "princess_blue"),
    ("chr_princess_sprite_410.xml", "chr_princess_sprite_422.xml", "princess_red"),
    ("chr_princess_sprite_424.xml", "chr_princess_sprite_426.xml", "princess_blue"),
    ("chr_princess_sprite_428.xml", "chr_princess_sprite_430.xml", "princess_red"),
    ("chr_princess_sprite_431.xml", "chr_princess_sprite_487.xml", "princess_blue"),
    ("chr_princess_sprite_488.xml", "chr_princess_sprite_495.xml", "princess_red"),
    ("chr_princess_sprite_496.xml", "chr_princess_sprite_747.xml", "princess_blue"),
    ("chr_rage_barbarian_sprite_000.xml", "chr_rage_barbarian_sprite_063.xml", "lumberjack_blue"),
    ("chr_rage_barbarian_sprite_064.xml", "chr_rage_barbarian_sprite_111.xml", "lumberjack_red"),
    ("chr_rage_barbarian_sprite_112.xml", "chr_rage_barbarian_sprite_126.xml", "lumberjack_blue"),
    ("chr_rage_barbarian_sprite_128.xml", "chr_rage_barbarian_sprite_169.xml", "lumberjack_red"),
    ("chr_rage_barbarian_sprite_170.xml", "chr_rage_barbarian_sprite_210.xml", "lumberjack_blue"),
    ("chr_rage_barbarian_sprite_212.xml", "chr_rage_barbarian_sprite_227.xml", "lumberjack_red"),
    ("chr_rage_barbarian_sprite_228.xml", "chr_rage_barbarian_sprite_235.xml", "lumberjack_blue"),
    ("chr_rage_barbarian_sprite_236.xml", "chr_rage_barbarian_sprite_295.xml", "lumberjack_red"),
    ("chr_rage_barbarian_sprite_296.xml", "chr_rage_barbarian_sprite_345.xml", "lumberjack_blue"),
    ("chr_royal_giant_sprite_000.xml", "chr_royal_giant_sprite_252.xml", "royal_giant_red"),
    ("chr_royal_giant_sprite_252.xml", "chr_royal_giant_sprite_476.xml", "royal_giant_blue"),
    ("chr_skeleton_warrior_sprite_000.xml", "chr_skeleton_warrior_sprite_017.xml", "guard_red"),
    ("chr_skeleton_warrior_sprite_019.xml", "chr_skeleton_warrior_sprite_297.xml", "guard_blue"),
    ("chr_skeleton_warrior_sprite_299.xml", "chr_skeleton_warrior_sprite_640.xml", "guard_red"),
    ("chr_skeleton_warrior_sprite_641.xml", "chr_skeleton_warrior_sprite_718.xml", "guard_blue"),
    ("chr_snowman_sprite_000.xml", "chr_snowman_sprite_278.xml", "ice_golem_blue"),
    ("chr_valkyrie_sprite_000.xml", "chr_valkyrie_sprite_080.xml", "valkyrie_blue"),
    ("chr_valkyrie_sprite_081.xml", "chr_valkyrie_sprite_270.xml", "valkyrie_red"),
    ("chr_valkyrie_sprite_271.xml", "chr_valkyrie_sprite_378.xml", "valkyrie_blue"),
    ("chr_witch_sprite_000.xml", "chr_witch_sprite_080.xml", "witch_blue"),
    ("chr_witch_sprite_081.xml", "chr_witch_sprite_207.xml", "witch_red"),
    ("chr_witch_sprite_208.xml", "chr_witch_sprite_252.xml", "witch_blue"),
    ("chr_wizard_sprite_000.xml", "chr_wizard_sprite_071.xml", "wizard_blue"),
    ("chr_wizard_sprite_072.xml", "chr_wizard_sprite_240.xml", "wizard_red"),
    ("chr_wizard_sprite_240.xml", "chr_wizard_sprite_311.xml", "wizard_blue"),
    ("chr_zap_machine_sprite_009.xml", "chr_zap_machine_sprite_221.xml", "sparky_red"),
    ("chr_zap_machine_sprite_225.xml", "chr_zap_machine_sprite_251.xml", "sparky_red"),
    ("spell_goblin_barrel_sprite_00.xml", "spell_goblin_barrel_sprite_12.xml", "goblin_barrel"),
  )
  val path = new File("/Users/tolsi/Dropbox/clash_royale_dataset/annotated/assets-resized/voc")
  annotate.foreach {
    case (from, to, replace) =>
      path.listFiles(new FilenameFilter {
        override def accept(dir: File, name: String): Boolean = name.endsWith(".xml") &&
          name >= from &&
          name <= to
      }).foreach(f => {
        val lines = Source.fromFile(f).mkString
        val replaced = lines.replaceAll("<name>.*</name>", s"<name>$replace</name>")
        val writer = new PrintWriter(f)
        writer.println(replaced)
        writer.close()
      })
  }
}
