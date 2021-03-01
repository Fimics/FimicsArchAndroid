# coding=utf-8
import os
import time
import urllib.request as urllib


class AppInfo:
    def __init__(self, app_name, app_id, download_count):
        self.app_name = app_name
        self.download_count = download_count
        self.app_link = "http://www.anzhi.com/dl_app.php?s={0:1s}&n=5".format(app_id)


class Anzhi:
    def __init__(self):
        self.urlList = []
        self.baseurl = 'http://www.anzhi.com/list_1_{0:1d}_hot.html'

    def geturl(self, pageIndex):
        # 从1开始的
        for page in range(1, pageIndex + 1):
            self.urlList.append(self.baseurl.format(page))

    def spider(self):
        for i in range(len(self.urlList)):
            response = urllib.urlopen(self.urlList[i])
            html = response.readlines()

            app_info_list = []
            download_count = ''
            app_name = ''
            app_id = ''
            for i in range(len(html)):
                line = str(html[i].decode('utf-8'))
                if 'app_name' in line:
                    startIndex = line.index(".html") + 7
                    endIndex = line.index("</a>")
                    app_name = line[startIndex:endIndex]
                    # print(app_name)
                elif 'app_downnum l' in line and len(app_name) > 0:
                    startIndex = line.index("下载") + 3
                    endIndex = line.index("</span>")
                    download_count = line[startIndex:endIndex]

                    # 过滤掉下载量低于1000w的
                    indexOfWan = download_count.find("万")
                    indexOfYi = download_count.find("亿")
                    if indexOfYi == -1 and indexOfWan == -1 or indexOfWan != -1 and len(
                            download_count[0:indexOfWan]) <= 3:
                        app_name = ''
                        app_id = ''
                        download_count = ''

                elif 'opendown' in line and len(download_count) > 0:
                    startIndex = line.index("opendown(") + 9
                    endIndex = line.rindex(")")
                    app_id = line[startIndex:endIndex]
                if len(app_name) > 0 and len(download_count) > 0 and len(app_id) > 0:
                    app_info_list.append(AppInfo(app_name, app_id, download_count))
                    app_name = ''
                    app_id = ''
                    download_count = ''

            print('total download file size=' + str(len(app_info_list)))
            for index in range(len(app_info_list)):
                appInfo = app_info_list[index]
                # 创建下载目录
                file_dir = os.getcwd() + '/download'
                if not os.path.exists(file_dir):
                    os.mkdir(file_dir)

                # 过滤已下载完成的
                file_path = file_dir + '/' + appInfo.app_name + '.apk'
                if os.path.exists(file_path):
                    print('skip ,file already exit')
                    continue

                print('DOWNLOAD START....')
                print(
                    'appInfo：{ ' + 'index=' + str(index)
                    + "--app_name=" + appInfo.app_name
                    + "--app_link=" + appInfo.app_link
                    + "--downloadCount=" + appInfo.download_count + " }")

                start = time.time()
                urllib.urlretrieve(appInfo.app_link, file_path)
                end = time.time()
                print('DOWNLOAD COMPLETED '
                      + 'app_name=' + appInfo.app_name
                      + ',cost time=' + str(int(end - start)))

                # 一条优秀的分割线
                print('--------------------------------------')

    def start(self):
        self.geturl(10)
        self.spider()


if __name__ == '__main__':
    # 安智市场
    anzhi = Anzhi()
    anzhi.start()
