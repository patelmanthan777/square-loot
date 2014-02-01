#version 120

uniform vec3 color;
uniform float power;
uniform sampler2D texture;

void main(){
	gl_FragColor.rgb = texture2D(texture, gl_TexCoord[0].st).rgb * color.rgb * power;
	gl_FragColor.a = texture2D(texture, gl_TexCoord[0].st).a;
}