#version 430

layout(location = 0) in vec3 position;

uniform mat4 projectionMatrix;
uniform mat4 modelMatrix;

void main(void){

    mat4 mvpMatrix = projectionMatrix * modelMatrix ;

    gl_Position = mvpMatrix * vec4(position, 1.0);
}