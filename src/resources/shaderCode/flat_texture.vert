#version 430

layout(location = 0) in vec2 position;
layout(location = 2) in vec2 textureCoordinates;

out vec2 uv_coordinates;

void main(void){

	gl_Position = vec4(position, 0.0 ,1.0);
	uv_coordinates = textureCoordinates;
}