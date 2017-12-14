
package com.yasuion.openglpyrmid2.utils;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import android.view.View;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

/**
 * Description:
 */
public class ShaderUtils {
    //顶点 ，颜色，纹理 创建着色器程序，加载着色器源码，加载纹理，给着色器变量赋值

    //顶点数据

    public int maTexCoorHandle;//顶点纹理坐标属性引用id
    //顶点坐标数据缓冲,顶点纹理坐标数据缓冲
    public FloatBuffer mVertexBuffer, mTexCoorBuffer;
    private FloatBuffer vertexBuffer;
    private int vSize;

    private int mProgram;
    private ShortBuffer indexBuffer;

    public void initData() {

        float[] dataPos = new float[]{
                0f, 1f, 0f, //p0
                -1f, -1f, 1f, //p1
                1f, -1f, 1f, //p2
                1f, -1f, -1f, //p3
                -1f, -1f, -1f //p4


        };//初始化顶点

        ByteBuffer buffer = ByteBuffer.allocateDirect(dataPos.length * 4);
        buffer.order(ByteOrder.nativeOrder());
        vertexBuffer = buffer.asFloatBuffer();
        vertexBuffer.put(dataPos);
        vertexBuffer.position(0);
        vSize = dataPos.length / 3;


        short index[] = {
                2,4,3,
                1,4,2,
                0,3,2,
                0,1,2,
                0,1,4,
                0,4,3

        };
        // 索引缓冲
        indexBuffer = getShortBuffer(index);


        // 纹理
        float texCoor[] = new float[]{
                0.5f, 0,
                0, 1,
                1, 1,
                0,1,
                1,1
               /* 0.5f, 0,
                0, 1.5f,
                1.5f, 1.5f*/
        };
        //创建顶点纹理坐标数据缓冲
        mTexCoorBuffer = getFloatBuffer(texCoor);
        //顶点纹理坐标数据的初始化================end============================


    }
    /**
     * @param index(这个是绘制顶点的索引数组);
     * @return Short型缓冲
     * @Effect 获取ShortBuffer数据缓冲;
     */
    public ShortBuffer getShortBuffer(short index[]) {
        ByteBuffer cc = ByteBuffer.allocateDirect(index.length * 2);
        cc.order(ByteOrder.nativeOrder());
        ShortBuffer indexBuffer = cc.asShortBuffer();
        indexBuffer.put(index);
        indexBuffer.position(0);
        return indexBuffer;
    }

    public FloatBuffer getFloatBuffer(float ver_Tex[]) {
        //创建顶点坐标数据缓冲
        //vc.length*4是因为一个整数四个字节
        ByteBuffer bb = ByteBuffer.allocateDirect(ver_Tex.length * 4);
        //设置字节顺序
        // 由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer转换
        bb.order(ByteOrder.nativeOrder());
        //转换为Float型缓冲
        FloatBuffer vertexBuffer = bb.asFloatBuffer();
        //向缓冲区中放入顶点坐标数据
        vertexBuffer.put(ver_Tex);
        //设置缓冲区起始位置
        vertexBuffer.position(0);
        return vertexBuffer;
    }





    public void created(View mView) {
        //初始化数据（顶点计算，创建顶点缓冲）
        initData();
        //深度测试
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        //创建着色程序
        mProgram = createProgram(mView.getResources(), "vshader/BallWithLight.glsl", "fshader/BallWithLight.glsl");
        //获取程序中顶点纹理坐标属性引用id
        maTexCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoor");
    }

    private float[] mViewMatrix = new float[16];//摄像机位置朝向9参数矩阵
    private float[] mProjectMatrix = new float[16];//4x4矩阵 投影用
    private float[] mMVPMatrix = new float[16];//最后起作用的总变换矩阵
    public static float[] mMMatrix = new float[16];//具体物体的移动旋转矩阵，旋转、平移
    public float xAngle = 0;//绕x轴旋转的角度

    //矩阵相乘操作
    public float[] getMatrix(float[] spec) {
        mMVPMatrix = new float[16];
        ////矩阵相乘操作()
        /*
         *  矩阵乘法计算, 将两个矩阵相乘, 并存入到第三个矩阵中
		 *  六个参数 :
		 *  ①② 参数 : 结果矩阵, 结果矩阵起始位移
		 *  ③④ 参数 : 左矩阵, 结果矩阵起始位移
		 *  ⑤⑥ 参数 : 右矩阵, 结果矩阵起始位移
		 */
        //位移操作
        Matrix.multiplyMM(mMVPMatrix, 0
                , mViewMatrix, 0,
                spec, 0);

        //合并投影和视口矩阵
        Matrix.multiplyMM(mMVPMatrix, 0
                , mProjectMatrix, 0,
                mMVPMatrix, 0);

        return mMVPMatrix;

    }
    public void changed(int width, int height) {
        //计算宽高比
        float ratio = (float) width / height;
        //设置透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 20);
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0,
                0.0f, 0.0f, 10.0f
                , 0f, 0f, 0f
                , 0f, 1.0f, 0.0f);
        //头是正常的
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }


    public void draw(int texId) {
        GLES20.glUseProgram(mProgram);
        int mMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        //初始化变换矩阵
        Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);
        //位移 z轴正向位移
        Matrix.translateM(mMMatrix, 0, 0, 0, 1);
        //旋转
        Matrix.rotateM(mMMatrix, 0, xAngle, 1, 1, 1);

        GLES20.glUniformMatrix4fv(mMatrix, 1, false, getMatrix(mMMatrix), 0);

        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        //画笔设置纹理数据
        GLES20.glVertexAttribPointer(maTexCoorHandle, 2, GLES20.GL_FLOAT,
                false, 0, mTexCoorBuffer);
        GLES20.glEnableVertexAttribArray(maTexCoorHandle);
        //绑定纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 18, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }



//===========================================================================================

    private static final String TAG = "ShaderUtils";


    public static void checkGLError(String op) {
        Log.e("wuwang", op);
    }

    public static int loadShader(int shaderType, String source) {
        int shader = GLES20.glCreateShader(shaderType);
        if (0 != shader) {
            GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);
            int[] compiled = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {
                Log.e(TAG, "Could not compile shader:" + shaderType);
                Log.e(TAG, "GLES20 Error:" + GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }


    public static int createProgram(String vertexSource, String fragmentSource) {
        int vertex = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertex == 0) return 0;
        int fragment = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (fragment == 0) return 0;
        int program = GLES20.glCreateProgram();
        if (program != 0) {
            GLES20.glAttachShader(program, vertex);
            checkGLError("Attach Vertex Shader");
            GLES20.glAttachShader(program, fragment);
            checkGLError("Attach Fragment Shader");
            GLES20.glLinkProgram(program);
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES20.GL_TRUE) {
                Log.e(TAG, "Could not link program:" + GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

    public static int createProgram(Resources res, String vertexRes, String fragmentRes) {
        return createProgram(loadFromAssetsFile(vertexRes, res), loadFromAssetsFile(fragmentRes, res));
    }

    public static String loadFromAssetsFile(String fname, Resources res) {
        StringBuilder result = new StringBuilder();
        try {
            InputStream is = res.getAssets().open(fname);
            int ch;
            byte[] buffer = new byte[1024];
            while (-1 != (ch = is.read(buffer))) {
                result.append(new String(buffer, 0, ch));
            }
        } catch (Exception e) {
            return null;
        }
        return result.toString().replaceAll("\\r\\n", "\n");
    }

}
