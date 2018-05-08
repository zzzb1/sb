package cn.zb.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 用于图片转换 输出指定宽高的图片
 * @author zb Created in 10:46 PM 2018/5/8
 */
@RestController
@RequestMapping("image")
@Slf4j
public class ImageController {



    /**
     * 用于获取指定路径图片的流
     * widthOut和heightOut三种情况：
     *      widthOut为空，heightOut不为空时：输出指定高度，等比宽度的图片
     *      widthOut不为空，heightOut为空时：输出指定宽度，等比高度的图片
     *      widthOut和heightOut都为空时：输出原图
     *      widthOut和heightOut都不为空时：输出指定高度，指定宽度的图片
     * @param response          return
     * @param path              图片路径：path为全路径 用"-"替换"/"
     * @param widthOut          宽度：图片的输出宽度
     * @param heightOut         长度：图片的输出长度
     */
    @RequestMapping(value = "/compress/{path:.+}", method = RequestMethod.GET)
    public void imageCompression(HttpServletResponse response,
                                 @PathVariable(value = "path") String path,
                                 @RequestParam(value = "w", defaultValue = "0") int widthOut,
                                 @RequestParam(value = "h", defaultValue = "0") int heightOut){
        log.debug("开始压缩图片：{},", path);
        response.setHeader("Content-Type","image/jpg;charset=UTF-8");//设置响应的媒体类型，这样浏览器会识别出响应的是图
        if (StringUtils.isBlank(path)){
            log.warn("path 为空!");
            return;
        }
        //将-替换成/
        String[] pathStr = path.split("-");
        path = StringUtils.join(pathStr, "/");
        File file = new File(path);
        // 构造Image对象
        BufferedImage src;
        ServletOutputStream sos = null;
        try {
            ImageIO.setUseCache(false);
            src = ImageIO.read(file);
            double width;//src.getWidth()
            double height;//src.getHeight()
            //判断输入的长宽是否为空，单个为空时等比压缩，两个为空输出原图
            if (widthOut != 0 && heightOut != 0){
                width = widthOut;
                height = heightOut;
            }else if (widthOut != 0 || heightOut != 0){
                width = src.getWidth();
                height = src.getHeight();
                double scale;
                if (widthOut == 0){
                    //宽度为空时，根据宽度算出比例，再算出等比宽度
                    scale = height / heightOut;
                    width =  width / scale;
                    height = heightOut;
                }
                if (heightOut == 0){
                    scale = width / widthOut;
                    height = height / scale;
                    width = widthOut;
                }
            }else {
                width = src.getWidth();
                height = src.getHeight();
            }
            // 缩小边长
            BufferedImage tag = new BufferedImage((int)width, (int)height, BufferedImage.TYPE_INT_RGB);
            // 绘制 缩小  后的图片
            Graphics g = tag.getGraphics();
            g.drawImage(src, 0, 0, (int)width, (int)height, null);
            g.dispose();
            sos = response.getOutputStream();
            ImageIO.write(tag,   "JPG",   sos);
        } catch (IOException e) {
            log.error("imageCompression IOException:{}", e);
        } finally {
            if (sos != null) {
                try {
                    sos.close();
                } catch (IOException e) {
                    log.error("close ServletOutputStream IOException:{}", e);
                }
            }
        }
        log.debug("结束压缩图片...");
    }

}
