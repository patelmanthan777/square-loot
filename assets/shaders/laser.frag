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
	float a = laser.direction.y / laser.direction.y;
	float b = laser.position.y - laser.position.x * a; 
	vec2 vertexToLight = vertexPosition.xy - laser.position;
	float dst1 = length(vertexToLight);
	float dst2 = dot(vertexToLight,laser.direction);
	float dst = dst1*dst1 - dst2*dst2;
	float dot = dot(normalize(vertexToLight), laser.direction);
	float attenuation = 1.0/dst;
	vec4 color = vec4(attenuation, attenuation, attenuation, pow(attenuation, 10)) * vec4(laser.color, 1);
	if (dst < 10 && dot < 0){
		gl_FragColor = gl_Color * color;
	}else{
		gl_FragColor = vec4(0,0,0,0);
	}
	
}