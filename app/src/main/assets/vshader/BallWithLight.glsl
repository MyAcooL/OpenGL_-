
attribute vec3 vPosition;       //顶点位置
uniform mat4 vMatrix;        //总变换矩阵
varying vec4 vDiffuse;//要传递的散射光变量
attribute vec2 aTexCoor; // 纹理坐标
varying vec2 vTextureCoord;// 传递的纹理数据

vec4 pointLight(vec3 normal,vec3 lightLocation, vec4 lightDiffuse){
           //表面点与光源方向的向量
           //(normalize(x)标准化向量，返回一个方向和X相同但长度为1的向量 )
           vec3 vp=normalize(lightLocation-(vMatrix*vec4(vPosition,1)).xyz);
           //变换后的法向量
           vec3 newTarget=normalize((vMatrix*vec4(normal+vPosition,1)).xyz
                           - (vMatrix*vec4(vPosition,1)).xyz);
           //散射光光照效果=材质反射系数（1）*环境光强度* max（cos（入射角），0）
          return lightDiffuse* max(0.0,dot(newTarget,vp));
       }


void main(){
    //根据总变换矩阵计算此次绘制此顶点位置
   gl_Position = vMatrix * vec4(vPosition,1);
   vec3 pos=vec3(0.0,80.0,0.0);//光照位置
   vec4 at=vec4(1.0,1.0,1.0,1.0);//光照强度
   vDiffuse=pointLight(normalize(vPosition),pos,at);
   vTextureCoord = aTexCoor;

}