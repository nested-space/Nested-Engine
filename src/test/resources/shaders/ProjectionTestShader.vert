#version 430

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 normal;

uniform vec3 lightPosition;
uniform mat4 projectionMatrix;
uniform mat4 modelMatrix;

out vec3 surfaceNormal;
out vec3 toLight;

void main(void){

    vec4 worldPosition = modelMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * worldPosition;

    surfaceNormal = (modelMatrix * vec4(normal, 0.0)).xyz;
    toLight = (lightPosition - worldPosition.xyz);
}