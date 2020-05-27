#version 430

layout(location = 0) in vec3 position;

uniform mat4 projectionMatrix;
uniform mat4 modelMatrix;

void main(void){

    mat4 mvpMatrix = modelMatrix * projectionMatrix;

    gl_Position = vec4(position, 1.0) * mvpMatrix;
}