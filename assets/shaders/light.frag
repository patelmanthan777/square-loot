#version 120

struct Light{
	vec2 position;
	vec3 color;
	float radius;
	float maxDst;
 }; 

uniform Light light;
uniform int nbLights;
uniform sampler2D texture;

void main(){
	float dst = length(light.position - gl_FragCoord.xy);
	float attenuation = 1.0/dst;
	float dstShade = light.maxDst/2.0;
	if(dst > light.maxDst - dstShade){
		attenuation *= -(1.0/dstShade)*(dst-dstShade) + 1.0;
	}
	vec4 color = vec4(attenuation, attenuation, attenuation, pow(attenuation, 6)) * vec4(light.color, 1.0) * light.radius;
	gl_FragColor =  gl_Color * color; //texture2D(texture, gl_TexCoord[0].st);
}