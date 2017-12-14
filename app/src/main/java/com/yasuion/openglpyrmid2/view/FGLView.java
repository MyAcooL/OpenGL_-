package com.yasuion.openglpyrmid2.view;


import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.yasuion.openglpyrmid2.render.FGLRender;


/**
 * 创建时间：2017/12/09
 *
 * @author 苏青岩
 * @Description
 */

//OpenGL ES实现步骤：
//    创建GLSurfaceView，初始化(设置渲染器)
//第一步：继承GLSurfaceView，实现构造，初始化GLSurfaceView
//    1.1继承GLSurfaceView
public class FGLView extends GLSurfaceView {
    private FGLRender renderer;

    //1.2实现构造
    public FGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //1.3初始化GLSurfaceView
        init();
    }


    //1.3初始化GLSurfaceView
    private void init() {
        //1.3.1 创建一个OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        renderer = new FGLRender(this,getContext());

        //1.3.2设置渲染器
        setRenderer(renderer);
        //1.4设置render模式
        setRenderMode(RENDERMODE_CONTINUOUSLY);
    }


}
