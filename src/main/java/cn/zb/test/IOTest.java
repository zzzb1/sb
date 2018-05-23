package cn.zb.test;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author zb Created in 10:36 PM 2018/5/23
 */
@Slf4j
public class IOTest {

    public static void main(String[] args) {
//        readByte();
        copyFile();
    }

    private static void copyFile() {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(new File("/Users/zhangbo/Downloads/a.txt")));
            bos = new BufferedOutputStream(new FileOutputStream(new File("/Users/zhangbo/Downloads/b.txt")));
            byte[] size = new byte[100];
            int len;
            while ((len = bis.read(size)) != -1) {
                bos.write(size, 0, len);
                bos.flush();
            }
        } catch (Exception e) {
            log.error("io 异常");
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private static void readByte() {
        BufferedInputStream bis = null;
        try {
            File file = new File("/Users/zhangbo/Downloads/a.txt");
            bis = new BufferedInputStream(new FileInputStream(file));
            byte[] size = new byte[100];
            int len;
            while ((len = bis.read(size)) != -1) {
                log.debug("{}, {}", size, len);
            }

        } catch (Exception e) {
            log.error("io 异常");
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
