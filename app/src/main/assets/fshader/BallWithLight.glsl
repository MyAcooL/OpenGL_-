

precision mediump float;
varying vec4 vDiffuse;//要传递的散射光变量
varying vec2 vTextureCoord;
uniform sampler2D sTexture;//采样器
void main(){
vec4 finalColor=vec4(1.0);


  gl_FragColor=finalColor*vDiffuse+finalColor*texture2D(sTexture,vTextureCoord);

}
