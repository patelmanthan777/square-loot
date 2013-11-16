#version 120

struct Light{
	vec2 position;
	vec3 color;
	float radius;
 }; 

uniform Light light[128];
uniform int nbLights;
varying vec3 vertexPosition;
varying vec3 vertexNormal;

float a = 1;
float b = 0.5;
float c = 0.25;

void main(){
	int cpt = 0;
	vec4 color = gl_Color;
	gl_FragColor = vec4(0,0,0,0);
	while(cpt < nbLights){
		vec2 vertexToLight = light[cpt].position - vertexPosition.xy; //vertexPosition;
		float dst = length(vertexToLight);	
	float attenuation = light[cpt].radius / (a + b*dst + c * dst * dst) ;
		vertexToLight = normalize(vertexToLight);
		attenuation = max(0.0,attenuation);
		gl_FragColor += color * vec4(attenuation*light[cpt].color,1.0);
		cpt++;
	}
}