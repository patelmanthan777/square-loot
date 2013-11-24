#version 120

struct Laser{
	vec2 position;
	vec3 color;
	vec2 direction;
 }; 

uniform Laser laser;

varying vec3 vertexPosition;


void main(){
	vec2 vertexToLight = vertexPosition.xy - laser.position;
	float dst1 = length(vertexToLight);
	float dst2 = dot(vertexToLight,laser.direction);
	float dst = dst1*dst1 - dst2*dst2;
	float dot = dot(normalize(vertexToLight), laser.direction);
	float attenuation1 = 1.0/pow(dst+1.0,1.0);
	float attenuation2 = 1.0/pow(dst+1.0,3.0);
	if (dst < 100 && dot < 0){
		gl_FragColor = gl_Color * (attenuation1 * vec4(laser.color, 1.0) + attenuation2 * vec4(0.5));
	}else{
		gl_FragColor = vec4(0,0,0,0);
	}
	
}