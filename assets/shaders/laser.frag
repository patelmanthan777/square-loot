#version 120

struct Laser{
	vec2 position;
	vec3 color;
	vec2 direction;
 }; 

uniform Laser laser;

varying vec3 vertexPosition;
varying vec3 vertexNormal;

float a = 1;
float b = 0.5;
float c = 0.25;

void main(){
	vec2 vertexToLight = laser.position - vertexPosition.xy;
	float dst = length(vertexToLight);
		
	float dot = dot(normalize(vertexToLight), laser.direction);
	//vec4 color = vec4(attenuation, attenuation, attenuation, pow(attenuation, 3)) * vec4(laser.color, 1);
	if (dot > 0.999999){
		gl_FragColor = gl_Color * vec4(laser.color,1.0);
	}else{
		gl_FragColor = vec4(0,0,0,0);
	}
	
}