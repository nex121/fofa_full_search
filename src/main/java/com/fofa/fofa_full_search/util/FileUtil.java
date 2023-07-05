package com.fofa.fofa_full_search.util;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileUtil {
    public static void generateFile() throws IOException {
        File file = new File("config.yaml");


        //如果文件不存在,则创建新文件
        if (!file.exists()) {
            file.createNewFile();
        } else {
            return;
        }

        String fileContent = "fofa_email: \nfofa_key: \nsearch_num: '1000'\nsearch_tag: '0'\nhistory: []";
        //使用FileWriter构建FileOutputStream对象进行文件输出
        try (FileOutputStream fos = new FileOutputStream(file);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(osw)) {
            //文件输出
            bw.write(fileContent);
        }
    }

    public static void generateFile1() throws IOException {
        File file = new File("vul.yaml");

        //如果文件不存在,则创建新文件
        if (!file.exists()) {
            file.createNewFile();
        } else {
            return;
        }

        String fileContent = "name: Root\n" +
                "children: []";
        //使用FileWriter构建FileOutputStream对象进行文件输出
        try (FileOutputStream fos = new FileOutputStream(file);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(osw)) {
            //文件输出
            bw.write(fileContent);
        }
    }
}
