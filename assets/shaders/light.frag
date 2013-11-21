#version 120

struct Light{
	vec2 position;
	vec3 color;
	float radius;
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
	float attenuation = 1.0/dst;
	vec4 color = vec4(attenuation, attenuation, attenuation, pow(attenuation, 3)) * vec4(light.color, 1) * light.radius;
	gl_FragColor = color;
	
}