# 120

varying vec3 vertexPosition;

void main(){
	gl_Position = ftransform();
	vertexPosition = gl_Vertex.xyz;
	gl_FrontColor = gl_Color;
}