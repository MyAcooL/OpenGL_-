package com.yasuion.openglpyrmid2.render;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.View;


import com.yasuion.openglpyrmid2.R;
import com.yasuion.openglpyrmid2.utils.ShaderUtils;

import java.io.IOException;
import java.io.InputStream;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

//第二步：渲染，继承 Renderer接口
//
public class FGLRender extends ShaderUtils implements GLSurfaceView.Renderer {


    //================实现的方法=====================
    private View mView;
    private int texuresID;
    private Context context;
    public FGLRender(View mView,Context context) {
        this.mView = mView;
        this.context = context;
    }
/*    public FGLRender(View mView) {
        this.mView = mView;
    }*/

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // 清空当前的所有颜色
        // 它是通过glClear使用红，绿，蓝以及AFA值来清除颜色缓冲区的，
        // 并且都被归一化在（0，1）之间的值，其实就是清空当前的所有颜色。
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);

        //开启深度测试
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        //调取算法中设置不同OpenGL API部分

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        xAngle = xAngle + 0.5f;
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        //初始化纹理
        initTexture();
        created(mView);
    }



    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.e("qingyan", "执行——onSurfaceChanged");
        //设置视口
        GLES20.glViewport(0, 0, width, height);

        //调取算法中设置不同OpenGL API部分
        changed(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //清除颜色缓冲以及深度缓冲
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        //调取算法中设置不同OpenGL API部分
        draw(texuresID);
    }
    private void initTexture() {
         //生成纹理id
        int[] texures = new int[1];


            texuresID = texures[0];


        //绑定纹理
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texuresID);
        //纹理过滤函数 glTexParameterf(要操作的纹理类型，过滤器，过滤参数)
        //要操作的纹理类型:GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_1D,GLES20.GL_TEXTURE_3D
        //过滤器
        // GLES20.GL_TEXTURE_MIN_FILTER指定缩小滤波的方法
        //GLES20.GL_TEXTURE_MAG_FILTER指定放大滤波的方法
        // 参数：GLES20.GL_NEARE(最邻近过滤，获得靠近纹理坐标点像素)
        //     GLES20.GL_LINEA(线性插值，获取坐标点附近4个像素的加权平均值)
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        //纹理环绕glTexParameterf( 要操作的纹理类型, 环绕方向,环绕参数 )
        //环绕方向:STR
        //环绕参数:
        //1.GL_CLAMP_TO_EDGE:超出纹理范围的坐标被截取城0和1，形成纹理边缘延展效果
        //2.GL_REPEA:超出纹理范围的坐标整数部分被忽略，形成重复使用
        //
         //截取纹理(2D纹理坐标是ST)
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);


//        //重用纹理
//        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
//                GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
//        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
//                GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
        //倒影
//        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
//                GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_MIRRORED_REPEAT);
//        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
//                GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_MIRRORED_REPEAT);


        //加载图片
        InputStream is=context.getResources().openRawResource(R.mipmap.wall);
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(is);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //加载纹理
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,//纹理类型
                0,//纹理层次，0表示基本的图像层，（可以理解为 直接贴图）
                bitmap, //纹理图像
                0);//纹理的边框尺寸
        bitmap.recycle();

    }
}




