#120

struct Bullet{
	vec2 position;
	vec2 direction;
	float radius;
	float length;
};

uniform Bullet bullet;

varying vec3 vertexPosition;

void main(){
	vec2 vertexToBullet = bullet.position - vertexPosition.xy;
	
	float line_dst = abs((vertexToBullet.x * bullet.direction.y - vertexToBullet.y* bullet.direction.x) / length(bullet.direction));
	
	float point_dst = length(vertexToBullet);
	
	float factor = 1/(line_dst*point_dst);
	
	if (line_dst < bullet.radius && point_dst < bullet.length){
		gl_FragColor =  gl_Color * factor;
	}else{
		gl_FragColor = vec4(0,0,0,0);
	}	
}