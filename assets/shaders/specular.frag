#version 120

struct Light{
	vec2 position;
	vec3 color;
	float radius;
}; 

uniform vec2 cameraPosition;
uniform Light light[128];
uniform int nbLights;
varying vec3 vertexPosition;
//varying vec3 vertexNormal;

void main(){
	int cpt = 0;
	while(cpt < nbLights){
		vec3 pixelToLight = light[cpt].position - gl_FragCoord.xy;
		float attenuation = 1.0 / (length(pixelToLight));
		
		//vertexToLight = normalize(vertexToLight);
		//attenuation *= dot(vertexToLight,vertexNormal);
		//attenuation = max(0.0,attenuation);
		gl_FragColor = 0;//gl_Color * vec4(attenuation*light[cpt].color,1.0);
		cpt++;
	}
}