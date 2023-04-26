package com.fofa.fofa_full_search.util;

import java.util.ArrayList;
import java.util.List;

public class DealCity {
    public static List<String> DealProvince(String que) {
        List<String> regions = new ArrayList<>();
        regions.add("Guangdong");
        regions.add("Beijing");
        regions.add("Shanghai");
        regions.add("Zhejiang");
        regions.add("Jiangsu");
        regions.add("Henan");
        regions.add("Fujian");
        regions.add("Sichuan");
        regions.add("Chongqing");
        regions.add("Shandong");
        regions.add("Shanxi");
        regions.add("Shaanxi");
        regions.add("Hunan");
        regions.add("Yunnan");
        regions.add("Hubei");
        regions.add("Liaoning");
        regions.add("Jiangxi");
        regions.add("Guizhou");
        regions.add("Guangxi");
        regions.add("Anhui");
        regions.add("Hebei");
        regions.add("Tianjin");
        regions.add("Ningxia Hui Autonomous Region");
        regions.add("Heilongjiang");
        regions.add("Hainan");
        regions.add("Jilin");
        regions.add("Xinjiang Uyghur Autonomous Region");
        regions.add("Inner Mongolia Autonomous Region");
        regions.add("Gansu");
        regions.add("Qinghai");
        regions.add("");
        return regions;
    }

    public static List<String> DealCities(String province) {
        List<String> cities = new ArrayList<>();
        switch (province) {
            case "Beijing":
                cities.add("Beijing");
                break;
            case "Shanghai":
                cities.add("Shanghai");
                break;
            case "Tianjin":
                cities.add("Tianjin");
                break;
            case "Chongqing":
                cities.add("Chongqing");
                break;
            case "Hebei":
                cities.add("Shijiazhuang");
                cities.add("Tangshan");
                cities.add("Qinhuangdao");
                cities.add("Handan");
                cities.add("Xingtai");
                cities.add("Baoding");
                cities.add("Zhangjiakou");
                cities.add("Chengde");
                cities.add("Cangzhou");
                cities.add("Langfang");
                cities.add("Hengshui");
                break;
            case "Guangxi":
                cities.add("Nanning");
                cities.add("Liuzhou");
                cities.add("Guilin");
                cities.add("Wuzhou");
                cities.add("Beihai");
                cities.add("Fangchenggang");
                cities.add("Qinzhou");
                cities.add("Guigang");
                cities.add("Yulin");
                cities.add("Baise");
                cities.add("Hezhou");
                cities.add("Hechi");
                cities.add("Laibin");
                cities.add("Chongzuo");
                break;
            case "Yunnan":
                cities.add("Kunming");
                cities.add("Qujing");
                cities.add("Yuxi");
                cities.add("Baoshan");
                cities.add("Zhaotong");
                cities.add("Lijiang");
                cities.add("Pu'er");
                cities.add("Lincang");
                cities.add("Chuxiong");
                cities.add("Honghe");
                cities.add("Wenshan");
                cities.add("Xishuangbanna");
                cities.add("Dali");
                cities.add("Dehong");
                cities.add("Nujiang");
                cities.add("Diqing");
                break;
            case "Shanxi":
                cities.add("Taiyuan");
                cities.add("Datong");
                cities.add("Yangquan");
                cities.add("Changzhi");
                cities.add("Jincheng");
                cities.add("Shuozhou");
                cities.add("Jinzhong");
                cities.add("Yuncheng");
                cities.add("Xinzhou");
                cities.add("Linfen");
                cities.add("Lvliang");
                break;
            case "Inner Mongolia Autonomous Region":
                cities.add("Hohhot");
                cities.add("Baotou");
                cities.add("Wuhai");
                cities.add("Chifeng");
                cities.add("Tongliao");
                cities.add("Ordos");
                cities.add("Hulunbeier");
                cities.add("Bayannur");
                cities.add("Ulanqab");
                cities.add("Xing'an Meng");
                cities.add("Xilinguolemeng");
                cities.add("Alxa League");
                break;
            case "Liaoning":
                cities.add("Shenyang");
                cities.add("Dalian");
                cities.add("Anshan");
                cities.add("Fushun");
                cities.add("Benxi");
                cities.add("Dandong");
                cities.add("Jinzhou");
                cities.add("Yingkou");
                cities.add("Fuxin");
                cities.add("Liaoyang");
                cities.add("Panjin");
                cities.add("Tieling");
                cities.add("Chaoyang");
                cities.add("Huludao");

                break;
            case "Henan":
                cities.add("Zhengzhou");
                cities.add("Kaifeng");
                cities.add("Luoyang");
                cities.add("Pingdingshan");
                cities.add("Anyang");
                cities.add("Hebi");
                cities.add("Xinxiang");
                cities.add("Jiaozuo");
                cities.add("Puyang");
                cities.add("Xuchang");
                cities.add("Luohe");
                cities.add("Sanmenxia");
                cities.add("Nanyang");
                cities.add("Shangqiu");
                cities.add("Xinyang");
                cities.add("Zhoukou");
                cities.add("Zhumadian");

                break;
            case "Shandong":
                cities.add("Jinan");
                cities.add("Qingdao");
                cities.add("Zibo");
                cities.add("Zaozhuang");
                cities.add("Dongying");
                cities.add("Yantai");
                cities.add("Weifang");
                cities.add("Jining");
                cities.add("Tai'an");
                cities.add("Weihai");
                cities.add("Rizhao");
                cities.add("Laiwu");
                cities.add("Linyi");
                cities.add("Dezhou");
                cities.add("Liaocheng");
                cities.add("Binzhou");
                cities.add("Heze");

                break;
            case "Sichuan":
                cities.add("Chengdu");
                cities.add("Zigong");
                cities.add("Panzhihua");
                cities.add("Luzhou");
                cities.add("Deyang");
                cities.add("Mianyang");
                cities.add("Guangyuan");
                cities.add("Suining");
                cities.add("Neijiang");
                cities.add("Leshan");
                cities.add("Nanchong");
                cities.add("Meishan");
                cities.add("Yibin");
                cities.add("Guang'an");
                cities.add("Dazhou");
                cities.add("Ya'an");
                cities.add("Bazhong");
                cities.add("Ziyang");

                break;
            case "Hunan":
                cities.add("Changsha");
                cities.add("Zhuzhou");
                cities.add("Xiangtan");
                cities.add("Hengyang");
                cities.add("Shaoyang");
                cities.add("Yueyang");
                cities.add("Changde");
                cities.add("Zhangjiajie");
                cities.add("Yiyang");
                cities.add("Chenzhou");
                cities.add("Yongzhou");
                cities.add("Huaihua");
                cities.add("Loudi");
                break;
            case "Hubei":
                cities.add("Wuhan");
                cities.add("Huangshi");
                cities.add("Shiyan");
                cities.add("Yichang");
                cities.add("Xiangyang");
                cities.add("Ezhou");
                cities.add("Jingmen");
                cities.add("Xiaogan");
                cities.add("Jingzhou");
                cities.add("Huanggang");
                cities.add("Xianning");
                cities.add("Suizhou");
                cities.add("Xiantao");
                cities.add("Qianjiang");
                cities.add("Tianmen");
                break;
            case "Anhui":
                cities.add("Hefei");
                cities.add("Wuhu");
                cities.add("Bengbu");
                cities.add("Huainan");
                cities.add("Maanshan");
                cities.add("Huabei");
                cities.add("Tongling");
                cities.add("Anqing");
                cities.add("Huangshan");
                cities.add("Chuzhou");
                cities.add("Fuyang");
                cities.add("Cebu");
                cities.add("Lu'an");
                cities.add("Bozhou");
                cities.add("Chizhou");
                cities.add("Xuancheng");
                break;
            case "Jiangsu":
                cities.add("Nanjing");
                cities.add("Wuxi");
                cities.add("Xuzhou");
                cities.add("Changzhou");
                cities.add("Suzhou");
                cities.add("Nantong");
                cities.add("Lianyungang");
                cities.add("Huai'an");
                cities.add("Yancheng");
                cities.add("Yangzhou");
                cities.add("Zhenjiang");
                cities.add("Taizhou");
                cities.add("Suqian");
                break;
            case "Zhejiang":
                cities.add("Hangzhou");
                cities.add("Ningbo");
                cities.add("Wenzhou");
                cities.add("Jiaxing");
                cities.add("Huzhou");
                cities.add("Shaoxing");
                cities.add("Jinhua");
                cities.add("Quzhou");
                cities.add("Zhoushan");
                cities.add("Taizhou");
                cities.add("Lishui");
                break;
            case "Jiangxi":
                cities.add("Nanchang");
                cities.add("Jingdezhen");
                cities.add("Pingxiang");
                cities.add("Jiujiang");
                cities.add("Xinyu");
                cities.add("Yingtan");
                cities.add("Ganzhou");
                cities.add("Ji'an");
                cities.add("Yichun");
                cities.add("Fuzhou");
                cities.add("Shangrao");

                break;
            case "Jilin":
                cities.add("Changchun");
                cities.add("Jilin");
                cities.add("Siping");
                cities.add("Liaoyuan");
                cities.add("Tonghua");
                cities.add("Baishan");
                cities.add("Songyuan");
                cities.add("Baicheng");
                cities.add("Yanbian");
                break;
            case "Heilongjiang":
                cities.add("Harbin");
                cities.add("Qiqihar");
                cities.add("Jixi");
                cities.add("Hegang");
                cities.add("Shuangyashan");
                cities.add("Daqing");
                cities.add("Yichun");
                cities.add("Jiamusi");
                cities.add("Qitaihe");
                cities.add("Mudanjiang");
                cities.add("Heihe");
                cities.add("Suihua");
                cities.add("Daxinganling");

                break;
            case "Fujian":
                cities.add("Fuzhou");
                cities.add("Xiamen");
                cities.add("Putian");
                cities.add("Sanming");
                cities.add("Quanzhou");
                cities.add("Zhangzhou");
                cities.add("Nanping");
                cities.add("Longyan");
                cities.add("Ningde");

                break;
            case "Gansu":
                cities.add("Lanzhou");
                cities.add("Jiayuguan");
                cities.add("Jinchang");
                cities.add("Baiyin");
                cities.add("Tianshui");
                cities.add("Wuwei");
                cities.add("Zhangye");
                cities.add("Pingliang");
                cities.add("Jiuquan");
                cities.add("Qingyang");
                cities.add("Dingxi");
                cities.add("Longnan");
                cities.add("Linxia Hui");
                break;
            case "Shaanxi":
                cities.add("Xi'an");
                cities.add("Tongchuan");
                cities.add("Baoji");
                cities.add("Xianyang");
                cities.add("Weinan");
                cities.add("Yan'an");
                cities.add("Hanzhong");
                cities.add("Yulin");
                cities.add("Ankang");
                cities.add("Shangluo");
                break;
            case "Guizhou":
                cities.add("Guiyang");
                cities.add("Liupanshui");
                cities.add("Zunyi");
                cities.add("Anshun");
                cities.add("Bijie");
                cities.add("Tongren");
                break;

            case "Guangdong":
                cities.add("Guangzhou");
                cities.add("Shaoguan");
                cities.add("Shenzhen");
                cities.add("Zhuhai");
                cities.add("Shantou");
                cities.add("Foshan");
                cities.add("Jiangmen");
                cities.add("Zhanjiang");
                cities.add("Maoming");
                cities.add("Zhaoqing");
                cities.add("Huizhou");
                cities.add("Meizhou");
                cities.add("Shanwei");
                cities.add("Heyuan");
                cities.add("Yangjiang");
                cities.add("Qingyuan");
                cities.add("Dongguan");
                cities.add("Zhongshan");
                cities.add("Chaozhou");
                cities.add("Jieyang");
                cities.add("Yunfu");
                break;
            case "Qinghai":
                cities.add("Xining");
                cities.add("Haidong");
                break;
            case "Ningxia Hui Autonomous Region":
                cities.add("Yinchuan");
                cities.add("Shizuishan");
                cities.add("Wuzhong");
                cities.add("Guyuan");
                cities.add("Zhongwei");
                break;
            case "Xinjiang Uyghur Autonomous Region":
                cities.add("Urumqi");
                cities.add("Karamay");
                cities.add("Turpan");
                break;
            // 其他省份的城市信息可以按照类似的方式添加到 switch 语句中
            default:
                break;
        }
        return cities;
    }

}
