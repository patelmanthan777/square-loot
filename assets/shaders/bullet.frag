#120

struct Bullet{
	vec2 position;
	float radius;
};

uniform Bullet bullet;

varying vec3 vertexPosition;

void main(){
	vec2 vertexToBullet = bullet.position - vertexPosition.xy;
	float dst = length(vertexToBullet);
	//float attenuation = 1 - dst*dst/(bullet.radius*bullet.radius);
	float attenuation = 1/(dst*dst+1);
	if (dst < bullet.radius){
		gl_FragColor = vec4(attenuation*4.,attenuation,attenuation,0);
	}else{
		gl_FragColor = vec4(0,0,0,0);
	}	
}