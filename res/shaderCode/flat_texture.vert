#version 430

layout(location = 0) in vec3 position;
layout(location = 2) in vec2 textureCoordinates;

out vec2 pass_texCoords;

void main(void){

	gl_Position = vec4(position,1.0);
	pass_texCoords = textureCoordinates;
}