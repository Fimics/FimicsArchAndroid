import os
import os.path
import zipfile
import math

# This folder is custom
rootdir = os.getcwd() + "/download"
if not os.path.exists(rootdir):
    raise Exception(print('请先执行【download_anzhi_apks.py】下载top100 App'))

file_filter = ['assets', 'res', 'lib', 'dex']


def analyse():
    total_size = 0
    total_size_of_so = 0
    total_size_of_dex = 0
    total_size_of_res = 0
    total_size_of_assets = 0

    apk_files = os.listdir(rootdir)
    print(str(len(apk_files)))
    for apk_file in apk_files:
        if not apk_file.endswith(".apk"): continue
        apk_file_name = os.path.join(rootdir, apk_file)
        if os.path.isfile(apk_file_name):
            apk_zip_file = zipfile.ZipFile(os.path.join(rootdir, apk_file_name))
            # apk size
            apk_size = 0
            # lib目录下 所有so文件体积
            so_size = 0
            # dex 文件体积
            dex_size = 0
            # res目录下所有文件体积
            res_size = 0
            # assets目录下所有文件体积
            asset_size = 0

            apk_zip_files = apk_zip_file.filelist

            for file in apk_zip_files:
                apk_size += file.compress_size
                file_name = file.filename

                if file_name.startswith('lib/'):
                    so_size += file.compress_size
                elif file_name.startswith('res/') or file_name.startswith('r/'):
                    res_size += file.compress_size
                elif file_name.startswith('assets/'):
                    asset_size += file.compress_size
                elif file_name.endswith('.dex'):
                    dex_size += file.compress_size

            total_size += apk_size
            total_size_of_so += so_size
            total_size_of_res += res_size
            total_size_of_dex += dex_size
            total_size_of_assets += asset_size

            print(apk_file + ": apk体积:" + str(math.ceil(apk_size / 1024 / 1024)) + "M"
                  + ",  so:" + str(int(so_size * 100 / apk_size).real) + "%"
                  + "  ,res:" + str(int(res_size * 100 / apk_size).real) + "%"
                  + "  ,dex:" + str(int(dex_size * 100 / apk_size)) + "%"
                  + "  ,asset:" + str(int(asset_size * 100 / apk_size)) + "%")
    print("\n==============================")
    print("安智市场Top100 Apk 各类型资源占比:"
          "  so:" + str(int(total_size_of_so * 100 / total_size).real) + "%"
          + "  ,res:" + str(int(total_size_of_res * 100 / total_size).real) + "%"
          + "  ,dex:" + str(int(total_size_of_dex * 100 / total_size).real) + "%"
          + "  ,asset:" + str(int(total_size_of_assets * 100 / total_size).real) + "%")


if __name__ == '__main__':
    analyse()
