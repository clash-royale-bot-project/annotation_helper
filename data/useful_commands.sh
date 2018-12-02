python3 /Users/tolsi/Documents/clash_royale_bot/bot/object_detection/convert2Yolo/example.py --datasets VOC --img_path /Users/tolsi/Dropbox/clash_royale_dataset/annotated/generated  --label /Users/tolsi/Dropbox/clash_royale_dataset/annotated/generated/voc   --convert_output_path /Users/tolsi/Dropbox/clash_royale_dataset/annotated/generated/yolo  --img_type ".png" --manipast_path /Users/tolsi/Dropbox/clash_royale_dataset/annotated/generated/yolo/manifest   --cls_list_file /Users/tolsi/Dropbox/clash_royale_dataset/annotated/predefined_classes.txt

find to_merge/ -path '*/.*' | xargs rm -f {}
find to_merge/ -maxdepth 3 -type f | xargs -I file rsync -av --progress --append file merged
ls merged | grep '.png' > all.txt
cat all.txt | sort > all_sorted.txt
awk 'NR % 6 != 0' all_sorted.txt > train.txt
awk 'NR % 6 == 0' all_sorted.txt > test.txt

ls | xargs -n 1 -J % mv % ../merged/