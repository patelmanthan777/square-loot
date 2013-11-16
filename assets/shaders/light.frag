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

void main(){
	int cpt = 0;
	vec4 color = gl_Color;
	gl_FragColor = vec4(0,0,0,0);
	while(cpt < nbLights){
		vec2 vertexToLight = light[cpt].position - vertexPosition.xy; //vertexPosition;
		float attenuation = 1.0 / (length(vertexToLight) / light[cpt].radius);
		vertexToLight = normalize(vertexToLight);
		attenuation = max(0.0,attenuation);
		gl_FragColor += color * vec4(attenuation*light[cpt].color,1.0);
		cpt++;
	}
}