/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blind;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

/**
 *
 * @author oadm
 */
public class Modect {
    private BufferedImage img1;
    private BufferedImage img2;
    private String device;
    private String video;
    private int current_diff;
    int max_diff;
    private boolean detected;
    private int start_compile;
    private int end_compile;
    private int incremenator;
    private int num_event;
    private AutoSence auto_sence;
    
    Modect(String device){
        this.video=Configuration.getServiceDirectory()+"service/record/"+device+"/record.avi";
        this.max_diff=Configuration.getSence(device);
        this.detected=false;
        this.incremenator=0;
        this.device=device;
        this.auto_sence = new AutoSence(this.device);
        this.auto_sence.start();
        
    }
    
    void getImage1(){
        BufferedImage img=null;
        try {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(this.video);
        //grabber.
        
        OpenCVFrameConverter.ToIplImage converterToIplImage = new OpenCVFrameConverter.ToIplImage();
        
        grabber.start();
        grabber.setFrameNumber(1*24);//*(int)this.fps);
        Frame frame = grabber.grab();
        opencv_core.IplImage image = converterToIplImage.convert(frame);
        Java2DFrameConverter bimConverter = new Java2DFrameConverter();

        img=bimConverter.getBufferedImage(frame,1);
 

        }catch(Exception e){
           e.printStackTrace();
        }
        this.img1=img;
        //Blind.cams.get(0).rec.dectThr.bad_src1=true;
                
    }
    void getImage2(int diff){
        this.current_diff=diff;
        if(this.img2!=null){
            replaceImages();
        }
        BufferedImage img=null;
        try {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(this.video);
        
        
        OpenCVFrameConverter.ToIplImage converterToIplImage = new OpenCVFrameConverter.ToIplImage();
        
        grabber.start();
        //grabber.setTimestamp(diff);
        grabber.setFrameNumber(diff*24);//*(int)this.fps);// fps установлен в команде ffmpeg в классе Record
        Frame frame = grabber.grab();
        opencv_core.IplImage image = converterToIplImage.convert(frame);
        Java2DFrameConverter bimConverter = new Java2DFrameConverter();
        img=bimConverter.getBufferedImage(frame,1);
        //System.out.println(image);
        }catch(Exception e){
           e.printStackTrace();
        }
        this.img2=img;

        
    }
    void presetImages(){
        File maskf = new File(Configuration.getServiceDirectory()+"service/record/"+device+"/mask.jpeg");
        if(maskf.exists()){
           try{
               //File mask_file = new File("/etc/blind/service/record/"+name+"/mask.jpeg");
        BufferedImage mask = null;
        mask=ImageIO.read(maskf);
        int width_mask = mask.getWidth(null);
        int height_mask = mask.getHeight(null);
        
        for(int h=0;h<height_mask;h++){
            for(int w=0;w<width_mask;w++){
                int rgbm = mask.getRGB(w, h);
                int rm = (rgbm >> 16) & 0xff;
                int gm = (rgbm >>  8) & 0xff;
                int bm = (rgbm      ) & 0xff;
                if(rm==255 && gm==255 && bm==255){
                    this.img1.setRGB(w, h, rgbm);
                    //this.img2.setRGB(w, h, rgbm);
                   
                }
            }
        }
        for(int h=0;h<height_mask;h++){
            for(int w=0;w<width_mask;w++){
                int rgbm = mask.getRGB(w, h);
                int rm = (rgbm >> 16) & 0xff;
                int gm = (rgbm >>  8) & 0xff;
                int bm = (rgbm      ) & 0xff;
                if(rm==255 && gm==255 && bm==255){
                    //this.img1.setRGB(w, h, rgbm);
                    this.img2.setRGB(w, h, rgbm);
                   
                }
            }
        }
           } catch(IOException e){
               
           }
        }
    }
    void compareImages(){
        //ImagePreset img1pres=new ImagePreset();
        //BufferedImage img1=img1pres.presetToDect(image1, name);
        //ImagePreset img2pres=new ImagePreset();
        //BufferedImage img2=img1pres.presetToDect(image2, name);
        
        

        
        
        
        
        int width1 = img1.getWidth(null);
        int width2 = img2.getWidth(null);
        int height1 = img1.getHeight(null);
        int height2 = img2.getHeight(null);
        long difff = 0;
        for (int y = 0; y < height1; y++) {
            for (int x = 0; x < width1; x++) {
                int rgb1 = img1.getRGB(x, y);
                int rgb2 = img2.getRGB(x, y);
                int r1 = (rgb1 >> 16) & 0xff;
                int g1 = (rgb1 >>  8) & 0xff;
                int b1 = (rgb1      ) & 0xff;
                int r2 = (rgb2 >> 16) & 0xff;
                int g2 = (rgb2 >>  8) & 0xff;
                int b2 = (rgb2      ) & 0xff;
                difff += Math.abs(r1 - r2);
                difff += Math.abs(g1 - g2);
                difff += Math.abs(b1 - b2);
            }
        }
    double n = width1 * height1 * 3;
    double p = difff / n / 255.0;
    //System.out.println("diff percent: !!!!!!!!!!!!!"+name + p+"///"+(p * 100.0));
   /* try 
{                               
ImageIO.write(img2, "png", new File("/etc/blind/image"+new Date().getTime()+".png"));
System.out.println("Success! 1");
//ImageIO.write(img2, "png", new File("/etc/blind/image1.bmp"));
//System.out.println("Success! 1");
}

catch (IOException e) 
{
// TODO Auto-generated catch block
e.printStackTrace();
}*/
   //System.out.println(this.device);
   //System.out.println(p*100+" Max:"+max_diff);
    //System.out.println(p*1000+" Max:"+max_diff);
    
    //Добавляем в автосенс
    this.auto_sence.sence_arr.add((int)p*1000);
    
    
    if(p*1000>=max_diff){//&&p*100<9){//если есть изменение картинки
        
        if(this.detected){//если засечено, то событие продолжается
            
            //continue;
        }else{//если только засекли - выставляем засекли и начало события
            this.start_compile=this.current_diff;
            this.detected=true;
            //добавляем событие в базу
            SimpleDateFormat sdfn = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
            String sqldate=sdfn.format(new Date());
            
            
            String request_string="select * from `events` order by `id` desc limit 1";
            String[] values = new String[1];
            values[0]="id";
            SQL request = new SQL();
            this.num_event=Integer.parseInt(request.select(request_string, values).get(0)[0])+1;
            Logger.log("Event detected on "+this.device+". ("+this.num_event+")");
            SQL ins_event = new SQL();
            ins_event.insertUpdate("insert into `events` (`id`,`start_time`,`end_time`,`monitor`,`unact`) values ('"+this.num_event+"','"+sqldate+"',null,'"+this.device+"',null)");
        }
        //compile();
    }else{
        if(this.detected){//если уже движение закончилось - начинаем счетчик
            //счетчик 5-ти секунд
            this.incremenator++;
            if(this.incremenator>=5){
                this.detected=false;
                this.incremenator=0;
                this.end_compile=this.current_diff;
                Logger.log("Event finished on "+this.device+". ("+this.num_event+")");
                compile();
            }
        }
        //return false;
    }    
    }
    void compile(){
        SimpleDateFormat sdfn = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
            String sqldate=sdfn.format(new Date());
        CompileEvent cmpl = new CompileEvent(this.start_compile,this.end_compile,device,sqldate,this.num_event);
        cmpl.start();
        //Event ev = new Event();
    }
    void replaceImages(){
        this.img1=this.img2;
        this.img2=null;
    }
}
