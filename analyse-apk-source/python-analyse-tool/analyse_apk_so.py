# coding=utf-8
import os
import os.path
import zipfile

# This folder is custom
rootdir = os.getcwd() + "/download"
if not os.path.exists(rootdir):
    raise Exception(print('请先执行【download_anzhi_apks.py】下载top100 App'))


def analyse():
    armeabi = []
    armeabi_v7a = []
    armeabi_x86 = []
    arm64_v8a = []

    apk_files = os.listdir(rootdir)
    # print(str(len(apk_files)))
    for index in range(len(apk_files)):
        apk_file = apk_files[index]
        if not apk_file.endswith(".apk"): continue
        apk_file_name = os.path.join(rootdir, apk_file)
        print("分析中....{0:1d}%".format(index + 1) + "--" + apk_file)
        if os.path.isfile(apk_file_name):
            apk_zip_file = zipfile.ZipFile(os.path.join(rootdir, apk_file_name))
            apk_zip_files = apk_zip_file.filelist
            for file in apk_zip_files:
                file_name = file.filename
                if file_name.startswith('lib/'):
                    if file_name.startswith("lib/armeabi/") and apk_file not in armeabi:
                        armeabi.append(apk_file)
                    if file_name.startswith("lib/armeabi-v7a/") and apk_file not in armeabi_v7a:
                        armeabi_v7a.append(apk_file)
                    if file_name.startswith("lib/arm64-v8a/") and apk_file not in arm64_v8a:
                        arm64_v8a.append(apk_file)
                    if file_name.startswith("lib/x86/") and apk_file not in armeabi_x86:
                        armeabi_x86.append(apk_file)

    countOfAll = 0
    countOfAll_name = ''

    countOf_v7_v8 = 0
    countOf_v7_v8_name = ''

    countOf_eabi_v7 = 0
    countOf_eabi_v7_name = ''

    countOf_eabi_v8 = 0
    countOf_eabi_v8_name = ''

    count_armeabi = 0
    count_armeabi_name = ''

    count_v7a = 0
    count_v7a_name = ''

    count_v8a = 0
    count_v8a_name = ''
    count_x86 = 0
    count_x86_name = ''

    for apk_file in apk_files:
        if not apk_file.endswith(".apk"): continue
        if apk_file in armeabi and apk_file in armeabi_v7a and apk_file in arm64_v8a and apk_file in armeabi_x86:
            countOfAll += 1
            countOfAll_name += apk_file + ","
        if apk_file in armeabi and apk_file in armeabi_v7a and apk_file:
            countOf_eabi_v7 += 1
            countOf_eabi_v7_name += apk_file + ","
        if apk_file in armeabi and apk_file in arm64_v8a and apk_file:
            countOf_eabi_v8 += 1
            countOf_eabi_v8_name += apk_file + ","
        if apk_file in armeabi_v7a and apk_file in arm64_v8a and apk_file:
            countOf_v7_v8 += 1
            countOf_v7_v8_name += apk_file + ","
        if apk_file in armeabi and apk_file not in arm64_v8a and apk_file not in armeabi_v7a and apk_file not in armeabi_x86:
            count_armeabi += 1
            count_armeabi_name += apk_file + ","

        if apk_file in arm64_v8a and apk_file not in armeabi and apk_file not in armeabi_v7a and apk_file not in armeabi_x86:
            count_v8a += 1
            count_v8a_name += apk_file + ","
        if apk_file in armeabi_v7a and apk_file not in armeabi and apk_file not in arm64_v8a and apk_file not in armeabi_x86:
            count_v7a += 1
            count_v7a_name += apk_file + ","
        if apk_file in armeabi_x86 and apk_file not in armeabi and apk_file not in armeabi_v7a and apk_file not in arm64_v8a:
            count_x86 += 1
            count_x86_name += apk_file + ","

    print("=========安智市场Top100 多ABI架构的APP个数========")
    print("countOfAll:     " + str(countOfAll) + "  app_names:" + countOfAll_name
          + "\ncountOf_v7_v8:  " + str(countOf_v7_v8) + "  app_names:" + countOf_v7_v8_name
          + "\ncountOf_eabi_v7:" + str(countOf_eabi_v7) + "  app_names:" + countOf_eabi_v7_name
          + "\ncountOf_eabi_v8:" + str(countOf_eabi_v8) + "  app_names:" + countOf_eabi_v8_name)

    print("\n=======安智市场Top100应用 单ABI架构的APP个数========")
    print("count_armeabi:  " + str(count_armeabi) + " app_names:" + count_armeabi_name
          + "\ncount_v8a:      " + str(count_v8a) + "  app_names:" + count_v8a_name
          + "\ncount_v7a:      " + str(count_v7a) + " app_names:" + count_v7a_name
          + "\ncount_x86:      " + str(count_x86))


if __name__ == '__main__':
    analyse()
