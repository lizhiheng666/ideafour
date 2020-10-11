package com.jt.service;

import com.jt.vo.ImageVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@PropertySource("classpath:/properties/image.properties")
public class FileServiceImpl implements FileService{

    //定义图片的类型集合.
    private static Set<String> imageTypeSet = new HashSet<>();

    //为属性动态赋值.
    @Value("${image.localDirPath}")
    private String localDirPath;    // = "D:/JT-SOFT/images"; //定义本地磁盘目录
    @Value("${image.urlPath}")
    private String urlPath;         //= "http://image.jt.com";     //定义了虚拟路径的域名

    static {
        imageTypeSet.add(".jpg"); //字母小写类型
        imageTypeSet.add(".png");
        imageTypeSet.add(".gif");
        //其他的省略.....
    }

    /**
     * 1.校验文件有效性    .jpg|.png|.gif.......
     * 2.校验文件是否为恶意程序     (木马.exe).jpg
     * 3.提高用户检索图片的效率   分目录存储.
     * 4.为了防止重名图片的提交   自定义文件名称.
     * 5.实现图片的物理上传     本地磁盘中.
     * 6.准备一个访问图片的虚拟路径
     * * @param uploadFile
     * @return
     */
    @Override
    public ImageVO upload(MultipartFile uploadFile) {
        //1.校验图片类型  1.利用正则表达式进行校验    2.利用集合进行校验 Set  数据是否存在即可.
        //1.1 获取图片名称   abc.jpg ABC.JPG
        String fileName = uploadFile.getOriginalFilename();
        fileName = fileName.toLowerCase(); //将所有的字母都小写
        //1.2 获取图片的类型
        int index = fileName.lastIndexOf(".");
        String fileType = fileName.substring(index);    //.jpg
        if(!imageTypeSet.contains(fileType)){ //如果类型不匹配

            return ImageVO.fail();  //图片上传失败.
        }

        //2.如何判断文件是否为恶意程序?  文件是否有图片的特有属性!!!!
        //2.1将上传的文件类型利用图片的API进行转化 如果转化不成功则一定不是图片.
        try {
            BufferedImage bufferedImage = ImageIO.read(uploadFile.getInputStream());
            //2.2校验是否有图片的特有属性  高度/宽度
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            //2.3校验宽度和高度是否有值.
            if(width == 0 || height == 0){

                return ImageVO.fail();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ImageVO.fail(); //返回失败即可
        }

        //3.实现分目录存储
        // 方案1: 利用hash之后每隔2-3位截取之后拼接
        // 方案2: 以时间为单位进行分隔  /yyyy/MM/dd/
        //3.1 利用工具API将时间转化为指定的格式
        String datePath = new SimpleDateFormat("/yyyy/MM/dd/").format(new Date());
        //3.2 动态生成文件目录  2部分=根目录+时间目录
        String localDir = localDirPath + datePath;

        //3.3判断目录是否存在, 如果不存在则新建目录
        File dirFile = new File(localDir);
        if(!dirFile.exists()){

            dirFile.mkdirs();   //如果不存在,则新建目录
        }

        //4. 防止文件重名,需要自定义文件名称 UUID
        //4.1生成uuid
        String  uuid =
                UUID.randomUUID().toString().replace("-", "");
        //4.2动态生成文件名称   uuid +.jpg
        String uuidFileName = uuid + fileType;

        //5.实现文件上传  准备文件全路径   目录+文件名称
        String realFilePath = localDir + uuidFileName;
        //5.1封装文件真实对象
        File imageFile = new File(realFilePath);
        //5.2实现文件上传
        try {
            uploadFile.transferTo(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return ImageVO.fail();  //告知文件上传失败
        }

        //6.实现路径拼接
        //图片存储的根目录 D:\JT-SOFT\images\2020\08\07\0e2ee0014382423b93c53d9b2decc5ec.jpg
        //拼接指定的虚拟路径.
        String url = urlPath + datePath + uuidFileName;
        return ImageVO.success(url);
    }
}
