#version 120

struct Light{
	vec2 position;
	vec3 color;
	float radius;
	float maxDst;
 }; 

uniform Light light;
uniform int nbLights;

varying vec3 vertexPosition;
varying vec3 vertexNormal;

float a = 1;
float b = 0.5;
float c = 0.25;

void main(){
	vec2 vertexToLight = light.position - vertexPosition.xy;
	float dst = length(vertexToLight);	
	float attenuation = 1.0/(dst);
	float dstShade = light.maxDst/2.0;
	if(dst > light.maxDst - dstShade){
		attenuation *= -(1.0/dstShade)*(dst-dstShade)+1.0;
	}
	vec4 color = vec4(attenuation, attenuation, attenuation, pow(attenuation, 3)) * vec4(light.color, 1.0) * light.radius;
	gl_FragColor = gl_Color * color;
}