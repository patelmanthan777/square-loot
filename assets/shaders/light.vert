#version 120

varying vec3 vertexPosition;
varying vec3 vertexNormal;

void main(){
	gl_Position = ftransform();
	vertexPosition = gl_Vertex.xyz;
	vertexNormal = normalize(gl_Normal.xyz);
	gl_FrontColor = gl_Color;
}